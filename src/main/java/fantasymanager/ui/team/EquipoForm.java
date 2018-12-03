package fantasymanager.ui.team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import fantasymanager.data.Equipo;
import fantasymanager.data.EquipoRepository;

@Component
@Scope("prototype")
public class EquipoForm extends FormLayout {

	private final Logger log = LoggerFactory.getLogger(EquipoForm.class);

	@Autowired
	private EquipoRepository equipoService;

	// @Autowired
	// private EventSystem eventSystem;

	private Integer id = null;
	private final TextField nombre = new TextField("Nombre:");
	private final TextField codigoCorto = new TextField("Codigo corto:");
	private final TextField codigoLargo = new TextField("Codigo largo:");

	public EquipoForm() {
		initForm();
	}

	public void setData(Equipo equipo) {
		id = equipo.getPkid();
		nombre.setValue(equipo.getNombre());
		codigoCorto.setValue(equipo.getCodigoCorto());
		codigoLargo.setValue(equipo.getCodigoLargo());
	}

	private void initForm() {
		addComponent(nombre);
		addComponent(codigoCorto);
		addComponent(codigoLargo);

		final Button commit = new Button("Commit");
		final Button cancel = new Button("Cancel");

		cancel.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				clearAndHide();
			}
		});
		commit.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				commitForm();
				fireCommitEvent();
				clearAndHide();
			}
		});

		final HorizontalLayout buttonBar = new HorizontalLayout();

		buttonBar.addComponent(commit);
		buttonBar.addComponent(cancel);

		addComponent(buttonBar);
	}

	private void commitForm() {
		if (id != null) {
			log.info("Update user with id {}, name {} and address {}", id, codigoCorto.getValue(),
					codigoLargo.getValue());

			final Equipo equipo = equipoService.findOne(id);
			equipo.setNombre(nombre.getValue());
			equipo.setCodigoCorto(codigoCorto.getValue());
			equipo.setCodigoLargo(codigoLargo.getValue());
			equipoService.save(equipo);
		} else {
			log.info("Creating user with name {} and address {}", codigoCorto.getValue(), codigoLargo.getValue());
			final Equipo equipo = new Equipo();
			equipo.setNombre(nombre.getValue());
			equipo.setCodigoCorto(codigoCorto.getValue());
			equipo.setCodigoLargo(codigoLargo.getValue());
			equipoService.save(equipo);
		}
	}

	private void clearAndHide() {
		codigoCorto.setValue("");
		codigoLargo.setValue("");
		id = null;
		setVisible(false);
	}

	private void fireCommitEvent() {
		log.info("Fire commit event!");
		// eventSystem.fire(new ReloadEntriesEvent());
	}
}