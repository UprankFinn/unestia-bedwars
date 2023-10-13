package net.unestia.bedwars.phase.ending.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class EndingChatListener {

    private final BedWars plugin;

    Consumer<AsyncPlayerChatEvent> asyncPlayerChatEvent;

    public EndingChatListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        asyncPlayerChatEvent = (AsyncPlayerChatEvent event) -> {
            //TODO: RankEntity rankEntity = UnestiaAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getRank();

            //TODO: event.setFormat(rankEntity.getPrefix() + event.getPlayer().getName() + " §7» §f" + event.getMessage().replace("%", "%%"));

        };

        this.plugin.getEventManager().registerEvent(AsyncPlayerChatEvent.class, asyncPlayerChatEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(AsyncPlayerChatEvent.class, asyncPlayerChatEvent);

    }

}
