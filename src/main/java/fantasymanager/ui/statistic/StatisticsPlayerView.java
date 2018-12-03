package fantasymanager.ui.statistic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.data.Estadistica;
import fantasymanager.data.EstadisticaRepository;
import fantasymanager.data.Jugador;
import fantasymanager.data.JugadorRepository;

@SpringView(name = StatisticsPlayerView.VIEW_NAME)
@UIScope
@SuppressWarnings("serial")
public class StatisticsPlayerView extends VerticalLayout implements View {

	private static final long serialVersionUID = -785973470246010003L;

	public static final String VIEW_NAME = "statisticsPlayer";

	private static final NumberFormat nf = NumberFormat.getInstance(Locale.US);

	private static final DecimalFormat df = (DecimalFormat) nf;

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	static {
		df.applyPattern("0.00");
		df.setMaximumFractionDigits(1);
	}

	private static final List<Button> yearsButton = new ArrayList<Button>();

	private ListDataProvider<Estadistica> dataProvider;

	private Jugador jugador;

	private Label titleLabel;

	@Autowired
	private EstadisticaRepository repository;

	@Autowired
	private JugadorRepository playerRepository;

	@PostConstruct
	void init() {
		initLayout();
	}

	private void initLayout() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();

		dataProvider = new ListDataProvider<>(new ArrayList<Estadistica>());

		addComponent(buildHeader());
		addComponent(buildButtonsYearBar());

		final Component grid = buildGrid();
		addComponent(grid);

		setExpandRatio(grid, 1.0f);

	}

	private Component buildHeader() {
		final HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		Responsive.makeResponsive(header);

		titleLabel = new Label();
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

				loadData();
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
	private Component buildGrid() {
		final Grid<Estadistica> grid = new Grid<Estadistica>();
		grid.setDataProvider(dataProvider);
		// set columns
		grid.addColumn(e -> e.getPartido().getFecha(), new DateRenderer(dateFormatter)).setCaption("Fecha");
		grid.addColumn(Estadistica::getValoracion, new NumberRenderer(df)).setCaption("Valoracion");
		grid.addColumn(Estadistica::getPuntos, new NumberRenderer(df)).setCaption("Puntos");
		grid.addColumn(Estadistica::getRebotes, new NumberRenderer(df)).setCaption("Rebotes");
		grid.addColumn(Estadistica::getAsistencias, new NumberRenderer(df)).setCaption("Asistencias");
		grid.addColumn(Estadistica::getRobos, new NumberRenderer(df)).setCaption("Robos");
		grid.addColumn(Estadistica::getTapones, new NumberRenderer(df)).setCaption("Tapones");
		grid.addColumn(Estadistica::getPerdidas, new NumberRenderer(df)).setCaption("Perdidas");

		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.setWidth("100%");
		grid.setHeight("100%");

		return grid;
	}

	private void loadData() {

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

		List<Estadistica> all = new ArrayList<Estadistica>();

		try {
			all = repository.findStatisticsByPlayer(jugador.getNombre(), dateFormatter.parse(fechaIni),
					dateFormatter.parse(fechaFin));

		} catch (final Exception e) {

		}

		// clear table
		final Collection<Estadistica> items = dataProvider.getItems();
		items.clear();
		// set table data
		items.addAll(all);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		final String playerName = event.getParameters();

		jugador = playerRepository.findJugadorByNombre(playerName);
		titleLabel.setValue(playerName);

		loadData();
	}

}
