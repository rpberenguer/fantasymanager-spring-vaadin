package fantasymanager.ui.parser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import fantasymanager.data.Partido;
import fantasymanager.data.PartidoRepository;
import fantasymanager.exceptions.FantasyManagerParserException;
import fantasymanager.services.ServicioParserEspnImpl;
import lombok.extern.slf4j.Slf4j;

@SpringView(name = ParserView.VIEW_NAME)
@UIScope
@SuppressWarnings("serial")
@Slf4j
public class ParserView extends VerticalLayout implements View {

	private static final long serialVersionUID = -785973470246010003L;

	public static final String VIEW_NAME = "parser";

	@Autowired
	private ServicioParserEspnImpl service;

	@Autowired
	private PartidoRepository repository;

	@PostConstruct
	void init() {
		initLayout();
	}

	private void initLayout() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();

		final List<Partido> gameList = repository.findAllByOrderByFechaDesc();
		final Partido lastGame = gameList.get(0);

		final DateField dateFieldIni = new DateField("Fecha Inicio Parseo");
		dateFieldIni.setDateFormat("dd/MM/yyyy");
		final Date initDate = lastGame.getFecha();
		final LocalDate initDateLocal = Instant.ofEpochMilli(initDate.getTime()).atZone(ZoneId.systemDefault())
				.toLocalDate();
		dateFieldIni.setValue(initDateLocal.plusDays(1));
		dateFieldIni.setLocale(new Locale("es", "ES"));

		final DateField dateFieldFin = new DateField("Fecha Fin Parseo");
		dateFieldFin.setDateFormat("dd/MM/yyyy");
		dateFieldFin.setValue(LocalDate.now().minusDays(1));
		dateFieldFin.setLocale(new Locale("es", "ES"));

		final HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponent(dateFieldIni);
		hLayout.addComponent(dateFieldFin);

		final Button.ClickListener myClickListener = new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				try {
					log.debug("***** Inicioooo del TEST *****");
					service.getStatistics(dateFieldIni.getValue(), dateFieldFin.getValue());
					log.debug("***** Fin del TEST *****");

				} catch (final FantasyManagerParserException e) {
					e.printStackTrace();
				}
			}
		};

		final Button parserButton = new Button("Parsea!", myClickListener);
		parserButton.setDescription("Parseo de estadísticas");

		// Add components to view
		addComponent(hLayout);
		addComponent(parserButton);

		setExpandRatio(parserButton, 1.0f);

	}

}
