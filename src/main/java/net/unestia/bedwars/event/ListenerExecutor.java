package net.unestia.bedwars.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.function.Consumer;

public class ListenerExecutor<T extends Event> implements EventExecutor {

    private final Class<T> eventClass;
    private final Consumer<T> eventConsumer;
    private boolean disable;

    public ListenerExecutor(final Class<T> eventClass, final Consumer<T> eventConsumer) {
        this.disable = false;
        this.eventClass = eventClass;
        this.eventConsumer = eventConsumer;
    }

    public void execute(final Listener listener, final Event event) throws EventException {
        if (this.disable) {
            event.getHandlers().unregister(listener);
            return;
        }
        if (this.eventClass.equals(event.getClass())) {
            this.eventConsumer.accept((T) event);
        }
    }

    public Class<T> getEventClass() {
        return this.eventClass;
    }

    public Consumer<T> getEventConsumer() {
        return this.eventConsumer;
    }

    public void setDisable(final boolean disable) {
        this.disable = disable;
    }
}
