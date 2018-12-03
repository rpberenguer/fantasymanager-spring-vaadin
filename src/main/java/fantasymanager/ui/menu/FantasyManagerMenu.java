package fantasymanager.ui.menu;

import com.google.common.eventbus.Subscribe;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import fantasymanager.ui.event.FantasyManagerEvent.PostViewChangeEvent;
import fantasymanager.ui.event.FantasyManagerEventBus;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial" })
public final class FantasyManagerMenu extends CustomComponent {

	public static final String ID = "dashboard-menu";

	public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";

	public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";

	private static final String STYLE_VISIBLE = "valo-menu-visible";
	// private Label notificationsBadge;
	// private Label reportsBadge;

	public FantasyManagerMenu() {
		setPrimaryStyleName("valo-menu");
		setId(ID);
		setSizeUndefined();

		// There's only one DashboardMenu per UI so this doesn't need to be
		// unregistered from the UI-scoped DashboardEventBus.
		FantasyManagerEventBus.register(this);

		setCompositionRoot(buildContent());
	}

	private Component buildContent() {
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");

		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildMenuItems());

		return menuContent;
	}

	private Component buildTitle() {
		final Label logo = new Label("FantasyManager <strong>Dashboard</strong>", ContentMode.HTML);
		logo.setSizeUndefined();
		final HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		logoWrapper.addStyleName("valo-menu-title");
		logoWrapper.setSpacing(false);
		return logoWrapper;
	}

	private Component buildMenuItems() {
		final CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");

		for (final FantasyManagerViewType view : FantasyManagerViewType.values()) {
			final Component menuItemComponent = new ValoMenuItemButton(view);

			menuItemsLayout.addComponent(menuItemComponent);
		}
		return menuItemsLayout;

	}

	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}

	public final class ValoMenuItemButton extends Button {

		private static final String STYLE_SELECTED = "selected";

		private final FantasyManagerViewType view;

		public ValoMenuItemButton(final FantasyManagerViewType view) {
			this.view = view;
			setPrimaryStyleName("valo-menu-item");
			setIcon(view.getIcon());
			if (view == FantasyManagerViewType.DEFAULT) {
				setCaption("Main");
			}
			else {
				setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
			}
			FantasyManagerEventBus.register(this);
			addClickListener(new ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					UI.getCurrent().getNavigator().navigateTo(view.getViewName());
				}
			});

		}

		@Subscribe
		public void postViewChange(final PostViewChangeEvent event) {
			removeStyleName(STYLE_SELECTED);
			if (event.getView() == view) {
				addStyleName(STYLE_SELECTED);
			}
		}
	}
}
