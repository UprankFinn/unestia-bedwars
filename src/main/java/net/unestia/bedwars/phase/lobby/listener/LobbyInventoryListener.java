package net.unestia.bedwars.phase.lobby.listener;

import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import net.unestia.bedwars.team.TeamEntity;
import net.unestia.bedwars.world.WorldEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LobbyInventoryListener {

    private final BedWars plugin;

    Consumer<InventoryClickEvent> inventoryClickEvent;
    Consumer<InventoryCloseEvent> inventoryCloseEvent;

    public LobbyInventoryListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        inventoryClickEvent = (InventoryClickEvent event) -> {
            if (event.getCurrentItem() == null) return;
            if (event.getClickedInventory() == null) return;
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (!(event.getWhoClicked().getGameMode().equals(GameMode.SURVIVAL))) return;
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if (event.getView().getTitle().equals("Team auswählen")) {

                String team = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                TeamEntity teamEntity = this.plugin.getTeamManager().getTeam(team);

                if (teamEntity == null) return;

                if (!(teamEntity.getPlayers().size() == this.plugin.getBedWarsUtils().getMaxTeamPlayers())) {
                    if (!(teamEntity.getPlayers().contains(player))) {

                        this.plugin.getTeamManager().getTeams().forEach((teamEntities) -> teamEntities.getPlayers().remove(player));
                        teamEntity.getPlayers().add(player);

                        ScoreboardUtil.updateScoreboard(player, 2, "" + teamEntity.getColor() + teamEntity.getName());

                        player.sendMessage(BedWars.PREFIX + "§7Du hast das Team " + teamEntity.getColor() + teamEntity.getName() + " §7betreten.");
                        player.closeInventory();
                        player.playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                        /*this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                            if (teams.getPlayerNameSet().contains(player.getName())) {
                                teams.getPlayerNameSet().remove(player.getName());
                            }
                        });*/

                        this.plugin.getScoreboard().g().forEach((teams) -> {
                            //if (teams.g().contains(player.getName())) {
                            teams.g().remove(player.getName());
                            //}
                        });

                        this.plugin.getTeamManager().getTeams().forEach((teams) -> {
                            Arrays.stream(LobbyPlayerListener.inventory.getContents()).forEach((itemStack) -> {
                                if (itemStack == null) return;
                                if (!(itemStack.getItemMeta().getDisplayName().equals(teams.getColor() + teams.getName())))
                                    return;

                                ItemMeta itemMeta = itemStack.getItemMeta();
                                if (teams.getPlayers().isEmpty()) {
                                    itemMeta.setLore(new ArrayList<>());
                                } else {

                                    List<String> lines = new ArrayList<>();

                                    lines.add("§7§m--------------------");
                                    for (int i = 0; i < teamEntity.getPlayers().size(); i++) {
                                        lines.add("§7» " + teamEntity.getColor() + teamEntity.getPlayers().get(i).getName());
                                    }
                                    itemMeta.setLore(lines);
                                }
                                itemStack.setItemMeta(itemMeta);
                            });
                        });

                        /*this.plugin.getScoreboard().getTeam(teamEntity.getName()).getPlayerNameSet().add(player.getName());

                        this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                            PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                            this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 1));
                                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 0));
                            });
                        });*/

                        Objects.requireNonNull(this.plugin.getScoreboard().f(teamEntity.getName())).g().add(player.getName());

                        this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                            PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().c;
                            this.plugin.getScoreboard().g().forEach((teams) -> {
                                playerConnection.a(PacketPlayOutScoreboardTeam.a(teams));
                                playerConnection.a(PacketPlayOutScoreboardTeam.a(teams, true));
                            });
                        });


                    } else {
                        player.sendMessage(BedWars.PREFIX + "§cDu bist bereits in diesem Team!");
                        player.closeInventory();
                        player.playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    }
                } else {
                    player.sendMessage(BedWars.PREFIX + "§cDas Team ist bereits voll!");
                    player.closeInventory();
                    player.playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }

            } else if (event.getView().getTitle().equals("Wähle eine Map...")) {
                String world = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().replace(" ", "").toLowerCase());
                WorldEntity worldEntity = BedWars.getInstance().getWorldManager().getWorld(world);

                if (worldEntity != null) {
                    player.sendMessage(BedWars.PREFIX + "§7Du hast erfolgreich die Map §e" + worldEntity.getName() + " §7ausgewählt.");
                    this.plugin.setWorldEntity(worldEntity);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    Bukkit.getOnlinePlayers().forEach((players) -> ScoreboardUtil.updateScoreboard(players, 5, "§e" + this.plugin.getWorldEntity().getName()));
                } else {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    player.sendMessage(BedWars.PREFIX + "§7Diese Map ist derzeit nicht verfügbar!");
                }

            }

        };

        inventoryCloseEvent = (InventoryCloseEvent event) -> {
        };

        this.plugin.getEventManager().registerEvent(InventoryClickEvent.class, inventoryClickEvent);
        this.plugin.getEventManager().registerEvent(InventoryCloseEvent.class, inventoryCloseEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(InventoryClickEvent.class, inventoryClickEvent);
        this.plugin.getEventManager().unregisterEvent(InventoryCloseEvent.class, inventoryCloseEvent);
    }

}
