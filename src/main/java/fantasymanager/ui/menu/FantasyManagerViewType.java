package fantasymanager.ui.menu;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import fantasymanager.ui.game.GameView;
import fantasymanager.ui.main.DefaultView;
import fantasymanager.ui.parser.ParserView;
import fantasymanager.ui.statistic.StatisticDBUIView;
import fantasymanager.ui.team.TeamDBUIView;

public enum FantasyManagerViewType {
	DEFAULT(DefaultView.VIEW_NAME, DefaultView.class, FontAwesome.HOME, true), TEAMS(TeamDBUIView.VIEW_NAME,
			TeamDBUIView.class, FontAwesome.BAR_CHART_O,
			false), STATISTICS(StatisticDBUIView.VIEW_NAME, StatisticDBUIView.class, FontAwesome.TABLE, false), PARSER(
					ParserView.VIEW_NAME, ParserView.class, FontAwesome.TABLE,
					false), GAMES(GameView.VIEW_NAME, GameView.class, FontAwesome.FUTBOL_O, false);

	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private FantasyManagerViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon,
			final boolean stateful) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
	}

	public boolean isStateful() {
		return stateful;
	}

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public static FantasyManagerViewType getByViewName(final String viewName) {
		FantasyManagerViewType result = null;
		for (final FantasyManagerViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

}
