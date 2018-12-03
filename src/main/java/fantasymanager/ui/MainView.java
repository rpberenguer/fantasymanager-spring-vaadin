package fantasymanager.ui;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import fantasymanager.ui.menu.FantasyManagerMenu;
import fantasymanager.ui.navigator.FantasyManagerNavigator;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

	public MainView() {
		setSizeFull();
		addStyleName("mainview");
		setSpacing(false);

		addComponent(new FantasyManagerMenu());

		final ComponentContainer content = new CssLayout();
		content.addStyleName("view-content");
		content.setSizeFull();
		addComponent(content);
		setExpandRatio(content, 1.0f);

		new FantasyManagerNavigator(content);
	}
}
