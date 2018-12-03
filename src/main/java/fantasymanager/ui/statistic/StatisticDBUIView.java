package fantasymanager.ui.statistic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.data.EstadisticaRepository;
import fantasymanager.data.dto.StatisticDto;

@SpringView(name = StatisticDBUIView.VIEW_NAME)
@UIScope
@SuppressWarnings("serial")
public class StatisticDBUIView extends VerticalLayout implements View {

	private static final long serialVersionUID = -785973470246010003L;

	public static final String VIEW_NAME = "statistics";

	private static final NumberFormat nf = NumberFormat.getInstance(Locale.US);

	private static final DecimalFormat df = (DecimalFormat) nf;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	static {
		df.applyPattern("0.00");
		df.setMaximumFractionDigits(1);
	}

	private static final List<Button> yearsButton = new ArrayList<Button>();

	private TextField filterText;

	ListDataProvider<StatisticDto> dataProvider;

	@Autowired
	private EstadisticaRepository service;

	@PostConstruct
	void init() {
		initLayout();
	}

	private void initLayout() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();

		filterText = new TextField();
		filterText.setPlaceholder("filter by name...");
		filterText.setValueChangeMode(ValueChangeMode.LAZY);

		final Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
		clearFilterTextBtn.setDescription("Clear the current filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		final CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		// final List<StatisticDto> list =
		// service.findAvgAndSumStatisticsByPlayer();
		dataProvider = new ListDataProvider<>(new ArrayList<StatisticDto>());

		filterText.addValueChangeListener(e -> {
			loadData(e.getValue());
			dataProvider.refreshAll();
		});

		addComponent(buildHeader());
		addComponent(filtering);
		addComponent(buildButtonsYearBar());
		final Component grid = buildGrid();
		addComponent(grid);

		// setExpandRatio(filtering, 1);
		setExpandRatio(grid, 1.0f);

		loadData(null);

	}

	private Component buildHeader() {
		final HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		Responsive.makeResponsive(header);

		final Label titleLabel = new Label("Estadísticas");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponents(titleLabel);

		return header;
	}

	private Component buildButtonsYearBar() {
		final HorizontalLayout buttonYears = new HorizontalLayout();
		buttonYears.addStyleName("toolbar");

		final Button.ClickListener myClickListener = new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				final Button buttonClicked = event.getButton();
				buttonClicked.addStyleName(ValoTheme.BUTTON_PRIMARY);
				buttonClicked.setEnabled(false);

				for (final Button button : yearsButton) {
					if (!buttonClicked.getId().equals(button.getId())) {
						button.removeStyleName(ValoTheme.BUTTON_PRIMARY);
						button.setEnabled(true);
					}
				}

				loadData(filterText.getValue());
				dataProvider.refreshAll();
			}
		};

		final Button previousSeasonButton = new Button("2017", myClickListener);
		previousSeasonButton.setId("2017");
		previousSeasonButton.setDescription("Temporada anterior");

		final Button currentSeasonButton = new Button("2018", myClickListener);
		currentSeasonButton.setId("2018");
		currentSeasonButton.setDescription("Temporada actual");
		currentSeasonButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		currentSeasonButton.setEnabled(false);

		yearsButton.add(previousSeasonButton);
		yearsButton.add(currentSeasonButton);

		buttonYears.addComponent(previousSeasonButton);
		buttonYears.addComponent(currentSeasonButton);

		return buttonYears;
	}

	/**
	 *
	 */
	private Grid<StatisticDto> buildGrid() {
		final Grid<StatisticDto> grid = new Grid<StatisticDto>();
		grid.setDataProvider(dataProvider);
		// set columns
		// grid.setColumnOrder(StatisticDBContainer.PROPERTIES);
		// grid.setColumns(StatisticDBContainer.HEADERS);
		grid.addColumn(StatisticDto::getNombre).setId("nombre").setCaption("Nombre");
		grid.addColumn(StatisticDto::getAvg, new NumberRenderer(df)).setCaption("Media");
		grid.addColumn(StatisticDto::getTot).setCaption("Total");
		grid.addColumn(StatisticDto::getAvgPuntos, new NumberRenderer(df)).setCaption("Puntos");
		grid.addColumn(StatisticDto::getAvgRebotes, new NumberRenderer(df)).setCaption("Rebotes");
		grid.addColumn(StatisticDto::getAvgAsistencias, new NumberRenderer(df)).setCaption("Asistencias");
		grid.addColumn(StatisticDto::getAvgRobos, new NumberRenderer(df)).setCaption("Robos");
		grid.addColumn(StatisticDto::getAvgTapones, new NumberRenderer(df)).setCaption("Tapones");
		grid.addColumn(StatisticDto::getAvgPerdidas, new NumberRenderer(df)).setCaption("Perdidas");

		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.setWidth("100%");
		grid.setHeight("100%");

		grid.addSelectionListener(e -> selectedStatisticsPlayer(e.getFirstSelectedItem().get()));

		return grid;
	}

	private void loadData(final String filter) {

		Button buttonSelected = null;

		for (final Button button : yearsButton) {
			if (!button.isEnabled()) {
				buttonSelected = button;
				break;
			}
		}

		String fechaIni, fechaFin = "";

		if ("2017".equals(buttonSelected.getCaption())) {
			fechaIni = "27/10/2016";
			fechaFin = "01/05/2017";
		} else {
			fechaIni = "17/10/2017";
			fechaFin = "01/05/2018";
		}

		List<StatisticDto> all = new ArrayList<StatisticDto>();

		try {
			if (StringUtils.isEmpty(filter)) {
				all = service.findAvgAndSumStatisticsByPlayer(formatter.parse(fechaIni), formatter.parse(fechaFin));
			} else {
				all = service.findAvgAndSumStatisticsByPlayer(filter, formatter.parse(fechaIni),
						formatter.parse(fechaFin));
			}
		} catch (final Exception e) {

		}

		// clear table
		final Collection<StatisticDto> items = dataProvider.getItems();
		items.clear();
		// set table data
		items.addAll(all);

	}

	public void selectedStatisticsPlayer(StatisticDto statisticDto) {
		UI.getCurrent().getNavigator().navigateTo(StatisticsPlayerView.VIEW_NAME + "/" + statisticDto.getNombre());
	}

}
