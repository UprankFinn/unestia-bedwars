package net.unestia.bedwars.event;

import net.unestia.bedwars.BedWars;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager {

    private final BedWars plugin;
    private final Map<Consumer<? extends Event>, List<ListenerExecutor<? extends Event>>> executors;

    public EventManager(final BedWars plugin) {
        this.plugin = plugin;
        this.executors = new HashMap<Consumer<? extends Event>, List<ListenerExecutor<? extends Event>>>();
    }

    public <T extends Event> void registerEvent(final Class<T> eventClass, final Consumer<T> event) {
        this.registerEvent(eventClass, event, EventPriority.NORMAL);
    }

    public <T extends Event> void registerEvent(final Class<T> eventClass, final Consumer<T> event, final EventPriority eventPriority) {
        final ListenerExecutor<T> executor = new ListenerExecutor<T>(eventClass, event);
        this.plugin.getServer().getPluginManager().registerEvent((Class)eventClass, (Listener) new Listener() {}, eventPriority, (EventExecutor) executor, (Plugin) this.plugin);
        if (!this.executors.containsKey(event)) {
            this.executors.put(event, new ArrayList<ListenerExecutor<? extends Event>>());
        }
        this.executors.get(event).add(executor);
    }

    public <T extends Event> void unregisterEvent(final Class<T> eventClass, final Consumer<T> event) {
        if (!this.executors.containsKey(event)) {
            return;
        }
        this.executors.get(event).stream().filter(executor -> executor.getEventConsumer().equals(event)).forEach(executor -> executor.setDisable(true));
    }

}
