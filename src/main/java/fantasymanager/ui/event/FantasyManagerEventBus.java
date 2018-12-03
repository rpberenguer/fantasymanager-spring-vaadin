package fantasymanager.ui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import fantasymanager.ui.MyFantasyManagerUI;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class FantasyManagerEventBus implements SubscriberExceptionHandler {

	private final EventBus eventBus = new EventBus(this);

	public static void post(final Object event) {
		MyFantasyManagerUI.getDashboardEventbus().eventBus.post(event);
	}

	public static void register(final Object object) {
		MyFantasyManagerUI.getDashboardEventbus().eventBus.register(object);
	}

	public static void unregister(final Object object) {
		MyFantasyManagerUI.getDashboardEventbus().eventBus.unregister(object);
	}

	@Override
	public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
		exception.printStackTrace();
	}
}
