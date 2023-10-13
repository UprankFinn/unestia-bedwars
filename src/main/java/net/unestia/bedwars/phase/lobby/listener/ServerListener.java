package net.unestia.bedwars.phase.lobby.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.function.Consumer;

public class ServerListener {

    private final BedWars plugin;

    Consumer<ServerListPingEvent> serverListPingEvent;

    public ServerListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        serverListPingEvent = (ServerListPingEvent event) -> {
        };

        this.plugin.getEventManager().registerEvent(ServerListPingEvent.class, serverListPingEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(ServerListPingEvent.class, serverListPingEvent);
    }

}
