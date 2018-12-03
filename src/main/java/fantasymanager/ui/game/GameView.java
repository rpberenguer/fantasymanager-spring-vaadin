package fantasymanager.ui.game;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.calendar.Calendar;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.data.EstadisticaRepository;
import fantasymanager.data.dto.StatisticDto;

@SpringView(name = GameView.VIEW_NAME)
@UIScope
public class GameView extends VerticalLayout implements View {

	private static final long serialVersionUID = -785973470246010003L;

	public static final String VIEW_NAME = "games";

	private static final NumberFormat nf = NumberFormat.getInstance(Locale.US);

	private static final DecimalFormat df = (DecimalFormat) nf;

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	static {
		df.applyPattern("0.00");
		df.setMaximumFractionDigits(1);
	}

	ListDataProvider<StatisticDto> dataProvider;

	@Autowired
	private EstadisticaRepository repository;

	@PostConstruct
	void init() {
		initLayout();
	}

	private void initLayout() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();

		// final List<StatisticDto> list =
		// service.findAvgAndSumStatisticsByPlayer();
		dataProvider = new ListDataProvider<>(new ArrayList<StatisticDto>());

		addComponent(buildHeader());
		addComponent(buildCalendarView());

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

		final Label titleLabel = new Label("Partidos");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponents(titleLabel);

		return header;
	}

	private Component buildCalendarView() {
		final VerticalLayout calendarLayout = new VerticalLayout();
		calendarLayout.setCaption("Calendar");
		calendarLayout.setSpacing(false);

		final Calendar calendar = new Calendar();
		calendar.setWidth(100.0f, Unit.PERCENTAGE);
		calendar.setHeight(1000.0f, Unit.PIXELS);

		final java.util.Calendar initialView = java.util.Calendar.getInstance();
		initialView.add(java.util.Calendar.DAY_OF_WEEK, -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
		// calendar.setStartDate(initialView.getTime());

		initialView.add(java.util.Calendar.DAY_OF_WEEK, 6);
		// calendar.setEndDate(initialView.getTime());

		calendar.setStartDate(ZonedDateTime.of(2017, 9, 10, 0, 0, 0, 0, calendar.getZoneId()));
		calendar.setEndDate(ZonedDateTime.of(2017, 9, 16, 0, 0, 0, 0, calendar.getZoneId()));

		// calendar.clearTimeBlocks(day);
		;

		calendarLayout.addComponent(calendar);
		return calendarLayout;
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

		return grid;
	}

	private void loadData(final String filter) {

		final List<StatisticDto> all = new ArrayList<StatisticDto>();

		// clear table
		final Collection<StatisticDto> items = dataProvider.getItems();
		items.clear();
		// set table data
		items.addAll(all);

	}

}
