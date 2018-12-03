package fantasymanager.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fantasymanager.data.Equipo;
import fantasymanager.data.EquipoRepository;

//@SpringUI
@Theme("dashboard")
public class MyUI extends UI {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private EquipoRepository service;

	private final Grid<Equipo> grid = new Grid<>(Equipo.class);

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);

		final CssLayout topBar = new CssLayout();
		final Label title = new Label("FantasyManager");
		title.setStyleName("h1");

		// grid.setColumns("codigoCorto", "codigoLargo", "nombre");
		// grid.setSizeFull();
		// layout.addComponents(grid, topBar);

		topBar.addComponent(title);
		layout.addComponent(topBar);

		final HorizontalLayout menuAndContent = new HorizontalLayout();
		menuAndContent.setSizeFull();
		layout.addComponent(menuAndContent);

		final CssLayout menu = new CssLayout();
		menu.addStyleName("menu");

		menuAndContent.addComponent(menu);

		Button section = new Button("Start");
		menu.addComponent(section);

		section = new Button("Admin");
		menu.addComponent(section);

		section = new Button("Settings");
		menu.addComponent(section);

		final VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);

		menuAndContent.addComponent(content);

		menuAndContent.setExpandRatio(menu, 2);
		menuAndContent.setExpandRatio(content, 8);

		final Label header = new Label("Lorem Ipsummmm");
		header.setStyleName("h2");
		content.addComponent(header);

		final Label textt = new Label(
				"<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. <br>Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. "
						+ "<br>Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu eget nulla. "
						+ "<br>Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero.</p>"
						+ "<br><p>Sed dignissim lacinia nunc. Curabitur tortor. Pellentesque nibh. Aenean quam. In scelerisque sem at dolor. Maecenas mattis. "
						+ "<br>Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor. Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa. "
						+ "<br>Fusce ac turpis quis ligula lacinia aliquet. Mauris ipsum. Nulla metus metus, ullamcorper vel, tincidunt sed, euismod in, nibh. "
						+ "<br>Quisque volutpat condimentum velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. </p>"
						+ "<br><p>Nam nec ante. Sed lacinia, urna non tincidunt mattis, tortor neque adipiscing diam, a cursus ipsum ante quis turpis. Nulla facilisi. "
						+ "<br>Ut fringilla. Suspendisse potenti. Nunc feugiat mi a tellus consequat imperdiet. Vestibulum sapien. Proin quam. Etiam ultrices. "
						+ "<br>Suspendisse in justo eu magna luctus suscipit. Sed lectus. Integer euismod lacus luctus magna. Quisque cursus, metus vitae pharetra auctor, "
						+ "<br>sem massa mattis sem, at interdum magna augue eget diam. </p>",
				ContentMode.HTML);
		content.addComponent(textt);

		final FormLayout form = new FormLayout();
		form.setSpacing(true);
		content.addComponent(form);

		final TextField firstName = new TextField("First Name");
		form.addComponent(firstName);

		final TextField lastName = new TextField("Last Name");
		form.addComponent(lastName);

		final TextField email = new TextField("Email");
		form.addComponent(email);

		final Button submit = new Button("submit");
		form.addComponent(submit);

		// final List<Equipo> equipoList = new ArrayList<Equipo>();
		// final Iterable<Equipo> equiposIt = service.findAll();
		// equiposIt.forEach(equipoList::add);
		//
		// grid.setItems(equipoList);

	}
}