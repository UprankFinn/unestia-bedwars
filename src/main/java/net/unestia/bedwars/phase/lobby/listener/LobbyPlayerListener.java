package net.unestia.bedwars.phase.lobby.listener;

import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.phase.lobby.LobbyPhase;
import net.unestia.bedwars.phase.lobby.countdown.LobbyCountdown;
import net.unestia.bedwars.phase.lobby.inventories.PlayerInventory;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LobbyPlayerListener {

    private final BedWars plugin;
    private final LobbyPhase lobbyPhase;

    Consumer<PlayerDeathEvent> playerDeathEvent;
    Consumer<PlayerLoginEvent> playerLoginEvent;
    Consumer<PlayerJoinEvent> playerJoinEvent;
    Consumer<PlayerDropItemEvent> playerDropItemEvent;
    Consumer<PlayerInteractEvent> playerInteractEvent;
    Consumer<PlayerPickupItemEvent> playerPickupItemEvent;
    Consumer<PlayerSwapHandItemsEvent> playerSwapHandItemsEvent;
    Consumer<PlayerQuitEvent> playerQuitEvent;

    public static final Inventory inventory = Bukkit.createInventory(null, 9, "Team auswählen");


    public LobbyPlayerListener(BedWars plugin, LobbyPhase lobbyPhase) {
        this.plugin = plugin;
        this.lobbyPhase = lobbyPhase;
        this.plugin.getTeamManager().getTeams().forEach((teams) -> inventory.addItem(new ItemBuilder((Material) teams.getSettings().get("bedType"), 1, (byte) 0).setDisplayName(teams.getColor() + teams.getName()).build()));
    }

    public void registerEvents() {

        /*
        PlayerDeathEvent
         */

        playerDeathEvent = (PlayerDeathEvent event) -> {
            event.setKeepInventory(false);
        };

        /*
        PlayerLoginEvent
         */

        playerLoginEvent = (PlayerLoginEvent event) -> {
            if (Bukkit.getOnlinePlayers().size() == this.plugin.getBedWarsUtils().getMaxPlayers()) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, BedWars.PREFIX + "§cDer Server ist breits voll.");
            }
        };

        /*
        PlayerJoinEvent
         */

        playerJoinEvent = (PlayerJoinEvent event) -> {
            event.setJoinMessage("§8» §6" + event.getPlayer().getName() + " §7hat das Spiel betreten.");

            //TODO: this.plugin.getPlayerManager().createPlayer(new Player(event.getPlayer().getUniqueId()));

            Map<Integer, String> scores;
            scores = new HashMap<>();

            scores.put(7, "§a");
            scores.put(6, "§f§lMap:");
            scores.put(5, "§e" + this.plugin.getWorldEntity().getName());
            scores.put(4, "§b");
            scores.put(3, "§f§lTeam:");
            scores.put(2, "§e" + "Kein Team");
            scores.put(1, "§c");

            ScoreboardUtil.createScoreboard(event.getPlayer(), scores);
            event.getPlayer().getInventory().setContents(new PlayerInventory().getInventory().getContents());

            event.getPlayer().setGameMode(GameMode.SURVIVAL);
            event.getPlayer().setHealthScale(20);
            event.getPlayer().setHealth(20);
            event.getPlayer().setFoodLevel(20);
            event.getPlayer().getActivePotionEffects().forEach(potionEffect -> event.getPlayer().removePotionEffect(potionEffect.getType()));

            LobbyCountdown lobbyCountdown = (LobbyCountdown) this.plugin.getBedWarsUtils().getCountdownManager().getCountdown("LobbyCountdown");
            lobbyCountdown.checkPlayers(Bukkit.getOnlinePlayers().size());

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
                PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().c;c
                this.plugin.getScoreboard().g().forEach((team) -> {
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team));
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team, true));
                });
            });

            event.getPlayer().setLevel(lobbyCountdown.getCountdown());

            event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -41.5, 72, -110.5, -180, 0));

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

            if (event.getItem().getType().equals(Material.RED_BED)) {
                event.getPlayer().openInventory(inventory);
            } else if (event.getItem().getType().equals(Material.MAGMA_CREAM)) {
                event.getPlayer().kickPlayer(BedWars.PREFIX + "§cDu hast das Spiel verlassen.");
            }

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
            event.setQuitMessage("§8» §6" + event.getPlayer().getName() + " §7hat das Spiel verlassen.");

            //TODO: this.plugin.getPlayerManager().removeFromCache(event.getPlayer().getUniqueId());

            this.plugin.getTeamManager().getTeams().forEach((teamEntities) -> teamEntities.getPlayers().remove(event.getPlayer()));

            LobbyCountdown lobbyCountdown = (LobbyCountdown) this.plugin.getBedWarsUtils().getCountdownManager().getCountdown("LobbyCountdown");
            lobbyCountdown.checkPlayers((Bukkit.getOnlinePlayers().size() - 1));

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
        this.plugin.getEventManager().registerEvent(PlayerLoginEvent.class, playerLoginEvent);
        this.plugin.getEventManager().registerEvent(PlayerJoinEvent.class, playerJoinEvent);
        this.plugin.getEventManager().registerEvent(PlayerDropItemEvent.class, playerDropItemEvent);
        this.plugin.getEventManager().registerEvent(PlayerInteractEvent.class, playerInteractEvent);
        this.plugin.getEventManager().registerEvent(PlayerPickupItemEvent.class, playerPickupItemEvent);
        this.plugin.getEventManager().registerEvent(PlayerSwapHandItemsEvent.class, playerSwapHandItemsEvent);
        this.plugin.getEventManager().registerEvent(PlayerQuitEvent.class, playerQuitEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(PlayerDeathEvent.class, playerDeathEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerLoginEvent.class, playerLoginEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerJoinEvent.class, playerJoinEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerDropItemEvent.class, playerDropItemEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerInteractEvent.class, playerInteractEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerPickupItemEvent.class, playerPickupItemEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerSwapHandItemsEvent.class, playerSwapHandItemsEvent);
        this.plugin.getEventManager().unregisterEvent(PlayerQuitEvent.class, playerQuitEvent);
    }

}
