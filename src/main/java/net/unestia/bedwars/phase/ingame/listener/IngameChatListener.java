package net.unestia.bedwars.phase.ingame.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class IngameChatListener {

    private final BedWars plugin;

    Consumer<AsyncPlayerChatEvent> asyncPlayerChatEvent;

    public IngameChatListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        asyncPlayerChatEvent = (AsyncPlayerChatEvent event) -> {

            if (this.plugin.getPlayers().contains(event.getPlayer())) {
                event.setFormat(this.plugin.getTeamUtils().getTeam(event.getPlayer()).getColor() +
                        this.plugin.getTeamUtils().getTeam(event.getPlayer()).getName() + " §7| " + this.plugin.getTeamUtils().getTeam(event.getPlayer()).getColor()
                        + event.getPlayer().getName() + " §7» §f" + event.getMessage().replace("%", "%%"));
            } else {
                event.setCancelled(true);
                Bukkit.getOnlinePlayers().forEach((players) -> {
                    if(!(this.plugin.getPlayers().contains(players))){
                        players.sendMessage("§cTot" + " §7| §7" + event.getPlayer().getName() + " §7» §f" + event.getMessage().replace("%", "%%"));
                    }
                });
            }


        };

        this.plugin.getEventManager().registerEvent(AsyncPlayerChatEvent.class, asyncPlayerChatEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(AsyncPlayerChatEvent.class, asyncPlayerChatEvent);

    }

}
