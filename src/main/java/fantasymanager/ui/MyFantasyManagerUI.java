package fantasymanager.ui;

import java.util.Locale;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.ui.event.FantasyManagerEvent.BrowserResizeEvent;
import fantasymanager.ui.event.FantasyManagerEventBus;
import fantasymanager.ui.menu.FantasyManagerMenu;

@Theme("dashboard")
@Title("Welcome to FantasyManager")
@Widgetset("AppWidgetset")
@SpringUI
@SpringViewDisplay
@SuppressWarnings("serial")
public class MyFantasyManagerUI extends UI implements ViewDisplay {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Panel springViewDisplay;

	private final FantasyManagerEventBus dashboardEventbus = new FantasyManagerEventBus();

	// @Autowired
	// private SpringViewProvider viewProvider;

	@Override
	protected void init(final VaadinRequest request) {

		setLocale(Locale.US);

		FantasyManagerEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		// updateContent();

		initLayout();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				FantasyManagerEventBus.post(new BrowserResizeEvent());
			}
		});

	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	// private void updateContent() {
	// setContent(new MainView());
	// getNavigator().navigateTo(getNavigator().getState());
	// }

	private void initLayout() {
		final HorizontalLayout root = new HorizontalLayout();
		root.setSizeFull();
		root.setSpacing(false);
		setContent(root);

		root.addComponent(new FantasyManagerMenu());

		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);

	}

	@Override
	public void showView(final View view) {
		springViewDisplay.setContent((Component) view);
	}

	public static FantasyManagerEventBus getDashboardEventbus() {
		return ((MyFantasyManagerUI) getCurrent()).dashboardEventbus;
	}
}