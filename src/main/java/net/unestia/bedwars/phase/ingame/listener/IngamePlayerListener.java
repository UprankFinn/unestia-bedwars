package net.unestia.bedwars.phase.ingame.listener;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.phase.ingame.IngamePhase;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import net.unestia.bedwars.team.TeamEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class IngamePlayerListener {

    private final BedWars plugin;
    private final IngamePhase ingamePhase;

    Consumer<PlayerDeathEvent> playerDeathEvent;
    Consumer<PlayerRespawnEvent> playerRespawnEvent;
    Consumer<PlayerJoinEvent> playerJoinEvent;
    Consumer<PlayerDropItemEvent> playerDropItemEvent;
    Consumer<PlayerInteractEvent> playerInteractEvent;
    Consumer<PlayerInteractAtEntityEvent> playerInteractAtEntityEvent;
    Consumer<PlayerPickupItemEvent> playerPickupItemEvent;
    Consumer<PlayerSwapHandItemsEvent> playerSwapHandItemsEvent;
    Consumer<PlayerQuitEvent> playerQuitEvent;

    public IngamePlayerListener(BedWars plugin, IngamePhase ingamePhase) {
        this.plugin = plugin;
        this.ingamePhase = ingamePhase;
    }

    public void registerEvents() {

        /*
        PlayerDeathEvent
         */

        playerDeathEvent = (PlayerDeathEvent event) -> {
            Player player = event.getEntity();
            Player killer = event.getEntity().getKiller();

            if (this.plugin.getTeamUtils().getTeam(player) == null) return;
            if (this.plugin.getTeamUtils().getTeam(killer) == null) return;

            TeamEntity teamEntity = this.plugin.getTeamUtils().getTeam(player);
            TeamEntity teamEntityDamager = this.plugin.getTeamUtils().getTeam(killer);

            event.getDrops().clear();
            event.setDroppedExp(0);

            if (killer != null) {
                Bukkit.broadcastMessage(BedWars.PREFIX + teamEntity.getColor() + player.getName() + " §7wurde von " + teamEntityDamager.getColor() + killer.getName() + " §7getötet.");
                //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(player.getUniqueId()).addDeath("statistics_alltime");
                //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(player.getUniqueId()).addDeath("statistics_monthly");

                //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(killer.getUniqueId()).addKill("statistics_alltime");
                //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(killer.getUniqueId()).addKill("statistics_monthly");
            } else {
                Bukkit.broadcastMessage(BedWars.PREFIX + teamEntity.getColor() + player.getName() + " §7ist gestorben.");
                //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(player.getUniqueId()).addDeath("statistics_alltime");
                //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(player.getUniqueId()).addDeath("statistics_monthly");
            }

            if (!(((AtomicBoolean) teamEntity.getSettings().get("bed")).get())) {
                //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(event.getEntity().getUniqueId()).addLoose("statistics_alltime");
                //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(event.getEntity().getUniqueId()).addLoose("statistics_monthly");

                this.plugin.getPlayers().remove(player);
                player.sendMessage(BedWars.PREFIX + "§cDu bist aus dem Spiel ausgeschieden.");
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);

                teamEntity.getPlayers().remove(player);

                this.plugin.getTeamManager().getTeams().forEach((team) -> {
                    Iterables.removeIf(team.getPlayers(), Predicates.isNull());
                });

                if (teamEntity.getPlayers().isEmpty()) {
                    this.plugin.getTeamManager().deleteTeam(teamEntity.getName());
                }

                Bukkit.getOnlinePlayers().forEach((players) -> {
                    players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                    ScoreboardUtil.updateScoreboard(players, IngamePhase.getTEAMS().get(teamEntity), teamEntity.getColor() + "§m" + teamEntity.getName() + "§r §7(" + teamEntity.getPlayers().size() + ")");
                });

                if (this.plugin.getTeamManager().getTeams().size() >= 2) {
                    Bukkit.broadcastMessage(BedWars.PREFIX + "§7Es verbleiben " + this.plugin.getTeamManager().getTeams().size() + " §7weitere Teams.");
                }

            }

            this.plugin.getBedWarsUtils().handleEnding();


        };

        /*
        PlayerRespawnEvent
         */

        playerRespawnEvent = (PlayerRespawnEvent event) -> {

            if (this.plugin.getPlayers().contains(event.getPlayer())) {
                TeamEntity teamEntity = this.plugin.getTeamUtils().getTeam(event.getPlayer());

                if (((AtomicBoolean) teamEntity.getSettings().get("bed")).get()) {
                    event.setRespawnLocation(this.plugin.getWorldEntity().getLocations().get(this.plugin.getTeamUtils().getTeam(event.getPlayer()).getName().toLowerCase() + "_spawn"));
                } else {
                    this.ingamePhase.setSpectator(event.getPlayer());
                    event.getPlayer().teleport(Bukkit.getWorld(this.plugin.getWorldEntity().getName()).getSpawnLocation());
                    event.getPlayer().sendMessage(BedWars.PREFIX + "§7Du schaust nun zu!");
                }
            } else {
                this.ingamePhase.setSpectator(event.getPlayer());
                event.getPlayer().teleport(Bukkit.getWorld(this.plugin.getWorldEntity().getName()).getSpawnLocation());
                event.getPlayer().sendMessage(BedWars.PREFIX + "§7Du schaust nun zu!");
            }

        };

        /*
        PlayerJoinEvent
         */

        playerJoinEvent = (PlayerJoinEvent event) -> {
            event.setJoinMessage(null);
            event.getPlayer().setLevel(0);
            event.getPlayer().setHealth(20);
            event.getPlayer().setHealthScale(20);

            this.ingamePhase.setSpectator(event.getPlayer());
            ScoreboardUtil.createScoreboard(event.getPlayer(), this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase").getScores());

            /*this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 1));
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 0));
                });
            });*/

            this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().c;
                this.plugin.getScoreboard().g().forEach((teams) -> {
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(teams));
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(teams, true));
                });
            });

            event.getPlayer().teleport(Bukkit.getWorld(this.plugin.getWorldEntity().getName()).getSpawnLocation());
            event.getPlayer().sendMessage(BedWars.PREFIX + "§7Du schaust nun zu!");

        };

        /*
        PlayerDropItemEvent
         */

        playerDropItemEvent = (PlayerDropItemEvent event) -> {
            if (!(this.plugin.getPlayers().contains(event.getPlayer()))) event.setCancelled(true);
        };

        /*
        PlayerInteractEvent
         */

        playerInteractEvent = (PlayerInteractEvent event) -> {
            if (event.getItem() == null) return;
            if (!(event.getItem().hasItemMeta())) return;
            if (event.getItem().getItemMeta().getDisplayName() == null) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;


            if (!(this.plugin.getPlayers().contains(event.getPlayer()))) {

                if (event.getItem().getType().equals(Material.COMPASS)) {
                    Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Lebende Spieler");

                    BedWars.getInstance().getPlayers().forEach((players) -> inventory.addItem(new ItemBuilder(Material.PLAYER_HEAD, 1, (byte) 0).setSkullOwner(players.getName()).setDisplayName("§e" + players.getName()).build()));

                    event.getPlayer().openInventory(inventory);
                }

                if (event.getItem().getType().equals(Material.MAGMA_CREAM)) {
                    event.getPlayer().kickPlayer(BedWars.PREFIX + "§cDu hast das Spiel verlassen.");
                }

                event.setCancelled(true);
            }

        };

        /*
        PlayerInteractAtEntityEvent
         */

        playerInteractAtEntityEvent = (PlayerInteractAtEntityEvent event) -> {
            if (!(BedWars.getInstance().getPlayers().contains(event.getPlayer()))) event.setCancelled(true);

            if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
                event.setCancelled(true);
                this.plugin.getBedWarsUtils().getItemStoreManager().getItemStore("BLOCK").openInventory(event.getPlayer());
            }
        };

        /*
        PlayerPickupItemEvent
         */

        playerPickupItemEvent = (PlayerPickupItemEvent event) -> {
            if (!(this.plugin.getPlayers().contains(event.getPlayer()))) event.setCancelled(true);
        };

        /*
        PlayerSwapItemsEvent
         */

        playerSwapHandItemsEvent = (PlayerSwapHandItemsEvent event) -> {
            if (!(this.plugin.getPlayers().contains(event.getPlayer()))) event.setCancelled(true);
        };

        /*
        PlayerQuitEvent
         */

        playerQuitEvent = (PlayerQuitEvent event) -> {
            event.setQuitMessage(null);

            if (!(this.plugin.getPlayers().contains(event.getPlayer()))) return;
            if (this.plugin.getTeamUtils().getTeam(event.getPlayer()) == null) return;

            //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(event.getPlayer().getUniqueId()).addLoose("statistics_alltime");
            //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(event.getPlayer().getUniqueId()).addLoose("statistics_monthly");

            TeamEntity teamEntity = this.plugin.getTeamUtils().getTeam(event.getPlayer());
            teamEntity.getPlayers().remove(event.getPlayer());

            if (teamEntity.getPlayers().isEmpty())
                this.plugin.getTeamManager().deleteTeam(teamEntity.getName());

            Bukkit.getOnlinePlayers().forEach((players) -> {
                players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                ScoreboardUtil.updateScoreboard(players, IngamePhase.getTEAMS().get(teamEntity), teamEntity.getColor() + "§m" + teamEntity.getName() + "§r §7(" + teamEntity.getPlayers().size() + ")");
            });

            this.plugin.getPlayers().remove(event.getPlayer());
            this.plugin.getTeamManager().getTeams().forEach((teams) -> {
                if (teams.getPlayers().isEmpty()) this.plugin.getTeamManager().deleteTeam(teams.getName());
            });

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

            this.plugin.getTeamManager().getTeams().forEach((teams) -> {
                if (!(teams.getPlayers().isEmpty())) return;
                this.plugin.getTeamManager().deleteTeam(teams.getName());
            });

            this.plugin.getBedWarsUtils().handleEnding();

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
