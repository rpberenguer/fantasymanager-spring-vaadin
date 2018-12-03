package fantasymanager.ui.team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.data.Equipo;
import fantasymanager.data.EquipoRepository;
import fantasymanager.ui.ConfirmDialog;
import fantasymanager.ui.ConfirmDialogListener;
import lombok.extern.slf4j.Slf4j;

@SpringView(name = TeamDBUIView.VIEW_NAME)
@UIScope
@Slf4j
public class TeamDBUIView extends VerticalLayout implements View {
	private static final long serialVersionUID = -785973470246010003L;
	public static final String VIEW_NAME = "teams";

	private Integer selectedId;
	private Equipo selectedTeam;

	@Autowired
	private TeamDBContainer teamDbContainer;

	@Autowired
	private EquipoForm editForm;

	@Autowired
	private EquipoRepository repository;

	// @Autowired
	// private EventSystem eventSystem;

	@PostConstruct
	void init() {
		initData();
		initLayout();
	}

	private void initLayout() {
		setMargin(true);
		setSpacing(true);

		// edit Form
		editForm.setVisible(false);

		addComponent(buildHeader());
		addComponent(buildGrid());
		addComponent(buildButtonBar());
		addComponent(editForm);
	}

	private Component buildHeader() {
		final HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		Responsive.makeResponsive(header);

		final Label titleLabel = new Label("Equipos");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponents(titleLabel);

		return header;
	}

	private Component buildGrid() {
		final Grid<Equipo> grid = new Grid<Equipo>();
		grid.setDataProvider(teamDbContainer);
		// set columns
		grid.addColumn(Equipo::getNombre).setCaption("Nombre");
		grid.addColumn(Equipo::getCodigoCorto).setCaption("Código corto");
		grid.addColumn(Equipo::getCodigoLargo).setCaption("Código Largo");

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setWidth("100%");
		grid.setHeight("100%");

		// table select listener
		grid.addSelectionListener(event -> {
			selectedTeam = event.getFirstSelectedItem().get();
			selectedId = selectedTeam.getPkid();

			log.info("Selected item id {" + selectedId + "}");
		});

		return grid;
	}

	private AbstractLayout buildButtonBar() {
		final HorizontalLayout buttonBar = new HorizontalLayout();

		buttonBar.setSpacing(true);
		buttonBar.setWidth("100%");

		final Button addButton = new Button("Add entry", event -> editForm.setVisible(true));
		final Button editButton = new Button("Edit Entry", event -> editSelectedEntry());
		final ConfirmDialogListener confirmDialogListener = new ConfirmDialogListener() {

			@Override
			public void yes() {
				removeSelectedEntry();
			}

			@Override
			public void no() {
				// do nothing
			}
		};

		final Button deleteButton = new Button("Delete entry", event -> {
			final ConfirmDialog confirmDialog = new ConfirmDialog("Delete Entry?", true, confirmDialogListener);
			UI.getCurrent().addWindow(confirmDialog);
		});

		buttonBar.addComponent(addButton);
		buttonBar.addComponent(editButton);
		buttonBar.addComponent(deleteButton);

		buttonBar.setComponentAlignment(addButton, Alignment.MIDDLE_LEFT);
		buttonBar.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
		buttonBar.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);

		return buttonBar;
	}

	private void editSelectedEntry() {
		if (selectedId != null) {
			log.info("Edit Team with id " + selectedId);
			editForm.setData(selectedTeam);
			editForm.setVisible(true);
		}
	}

	private void removeSelectedEntry() {
		if (selectedId != null) {
			log.info("Delete Team with id " + selectedId);
			repository.delete(selectedId);
			// eventSystem.fire(new ReloadEntriesEvent());
		}
	}

	private void initData() {
		// load data
		final List<Equipo> all = new ArrayList<Equipo>();
		repository.findAll().forEach(all::add);

		log.info(all.toString());

		// clear table
		final Collection<Equipo> items = teamDbContainer.getItems();
		items.clear();
		// set table data
		items.addAll(all);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

}
