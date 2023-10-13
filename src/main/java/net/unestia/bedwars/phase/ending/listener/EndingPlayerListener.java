package net.unestia.bedwars.phase.ending.listener;

import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.function.Consumer;

public class EndingPlayerListener {

    private final BedWars plugin;

    Consumer<PlayerDeathEvent> playerDeathEvent;
    Consumer<PlayerRespawnEvent> playerRespawnEvent;
    Consumer<PlayerJoinEvent> playerJoinEvent;
    Consumer<PlayerDropItemEvent> playerDropItemEvent;
    Consumer<PlayerInteractEvent> playerInteractEvent;
    Consumer<PlayerInteractAtEntityEvent> playerInteractAtEntityEvent;
    Consumer<PlayerPickupItemEvent> playerPickupItemEvent;
    Consumer<PlayerSwapHandItemsEvent> playerSwapHandItemsEvent;
    Consumer<PlayerQuitEvent> playerQuitEvent;

    public EndingPlayerListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        /*
        PlayerDeathEvent
         */

        playerDeathEvent = (PlayerDeathEvent event) -> {
            event.setDeathMessage(null);
        };

        /*
        PlayerRespawnEvent
         */

        playerRespawnEvent = (PlayerRespawnEvent event) -> {
        };

        /*
        PlayerJoinEvent
         */

        playerJoinEvent = (PlayerJoinEvent event) -> {
            event.setJoinMessage(null);
            ScoreboardUtil.createScoreboard(event.getPlayer(), this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase").getScores());

            //TODO: RankEntity rankEntity = UnestiaAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getRank();

            //TODO: this.plugin.getScoreboard().getTeam("0" + rankEntity.getId() + rankEntity.getName()).getPlayerNameSet().add(event.getPlayer().getName());
            /*this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                this.plugin.getScoreboard().getTeams().forEach((team) -> {
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                });
            });*/

            this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().c;
                this.plugin.getScoreboard().g().forEach((team) -> {
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team));
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team, true));
                });
            });

        };

        /*
        PlayerDropItemEvent
         */

        playerDropItemEvent = (PlayerDropItemEvent event) -> {
            event.setCancelled(true);
        };

        /*
        PlayerInteractEvent
         */

        playerInteractEvent = (PlayerInteractEvent event) -> {
            if (event.getItem() == null) return;
            if (!(event.getItem().hasItemMeta())) return;
            if (event.getItem().getItemMeta().getDisplayName() == null) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

            event.setCancelled(true);

        };

        /*
        PlayerInteractAtEntityEvent
         */

        playerInteractAtEntityEvent = (PlayerInteractAtEntityEvent event) -> {
            event.setCancelled(true);
        };

        /*
        PlayerPickupItemEvent
         */

        playerPickupItemEvent = (PlayerPickupItemEvent event) -> {
            event.setCancelled(true);
        };

        /*
        PlayerSwapItemsEvent
         */

        playerSwapHandItemsEvent = (PlayerSwapHandItemsEvent event) -> {
            event.setCancelled(true);
        };

        /*
        PlayerQuitEvent
         */

        playerQuitEvent = (PlayerQuitEvent event) -> {
            event.setQuitMessage(null);
            /*this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                if (teams.getPlayerNameSet().contains(event.getPlayer().getName())) {
                    teams.getPlayerNameSet().remove(event.getPlayer().getName());
                }
            });*/

            this.plugin.getScoreboard().g().forEach((teams) -> {
                //if (teams.g().contains(event.getPlayer().getName())) {
                teams.g().remove(event.getPlayer().getName());
                //}
            });
        };

        this.plugin.getEventManager().registerEvent(PlayerDeathEvent.class, playerDeathEvent);
        this.plugin.getEventManager().registerEvent(PlayerRespawnEvent.class, playerRespawnEvent);
        this.plugin.getEventManager().registerEvent(PlayerJoinEvent.class, playerJoinEvent);
        this.plugin.getEventManager().registerEvent(PlayerDropItemEvent.class, playerDropItemEvent);
        this.plugin.getEventManager().registerEvent(PlayerInteractEvent.class, playerInteractEvent);
        this.plugin.getEventManager().registerEvent(PlayerInteractAtEntityEvent.class, playerInteractAtEntityEvent);
        this.plugin.getEventManager().registerEvent(PlayerPickupItemEvent.class, playerPickupItemEvent);
        this.plugin.getEventManager().registerEvent(PlayerSwapHandItemsEvent.class, playerSwapHandItemsEvent);
        this.plugin.getEventManager().registerEvent(PlayerQuitEvent.class, playerQuitEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(PlayerDeathEvent.class, playerDeathEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerRespawnEvent.class, playerRespawnEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerJoinEvent.class, playerJoinEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerDropItemEvent.class, playerDropItemEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerInteractEvent.class, playerInteractEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerInteractAtEntityEvent.class, playerInteractAtEntityEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerPickupItemEvent.class, playerPickupItemEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerSwapHandItemsEvent.class, playerSwapHandItemsEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerQuitEvent.class, playerQuitEvent);
    }
}
