package net.unestia.bedwars.utils.arena;


import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Random;

public class ArenaManager {

    private final BedWars plugin;

    private Integer bronzeSpawner;
    private Integer eisenSpawner;
    private Integer goldSpawner;

    public ArenaManager() {
        this.plugin = BedWars.getInstance();
    }

    public ArenaManager(BedWars plugin) {
        this.plugin = plugin;
    }

    public void startSpawner() {
        this.bronzeSpawner = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            this.plugin.getWorldEntity().getLocations().forEach((name, location) -> {
                if (location != null) {
                    if (name.startsWith("bronze")) {
                        Bukkit.getWorld(location.getWorld().getName()).dropItemNaturally(location, new ItemBuilder(Material.BRICK, 1, (byte) 0).setDisplayName("§eBronze").build());
                    }
                }
            });
        }, 20L, 20L);

        this.eisenSpawner = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            this.plugin.getWorldEntity().getLocations().forEach((name, location) -> {
                if (location != null) {
                    if (name.startsWith("iron")) {
                        Bukkit.getWorld(location.getWorld().getName()).dropItemNaturally(location, new ItemBuilder(Material.IRON_INGOT, 1, (byte) 0).setDisplayName("§eEisen").build());
                    }
                }
            });
        }, 300L, 300L);

        this.goldSpawner = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            this.plugin.getWorldEntity().getLocations().forEach((name, location) -> {
                if (location != null) {
                    if (name.startsWith("gold")) {
                        Bukkit.getWorld(location.getWorld().getName()).dropItemNaturally(location, new ItemBuilder(Material.GOLD_INGOT, 1, (byte) 0).setDisplayName("§eGold").build());
                    }
                }
            });
        }, 600L, 600L);

    }

    public void stopSpawners() {
        Bukkit.getScheduler().cancelTask(this.bronzeSpawner);
        Bukkit.getScheduler().cancelTask(this.eisenSpawner);
        Bukkit.getScheduler().cancelTask(this.goldSpawner);
    }

    public void handleAllPlayers() {
        Bukkit.getOnlinePlayers().forEach((players) -> {
            if (this.plugin.getTeamUtils().getTeam(players) == null) {
                this.plugin.getTeamManager().getTeams().forEach((teams) -> {
                    if (!(teams.getPlayers().size() <= this.plugin.getBedWarsUtils().getMaxPlayers())) {
                        Random random = new Random();
                        this.plugin.getTeamManager().getTeams().get(random.nextInt()).getPlayers().add(players);
                    }
                });
            }
        });
    }

    public void teleport() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.RED + "");
            player.sendMessage(BedWars.PREFIX + "§7Du bist im Team " + this.plugin.getTeamUtils().getTeam(player).getColor() + this.plugin.getTeamUtils().getTeam(player).getName());
            player.sendMessage(BedWars.PREFIX + "§7Es wird auf der Map §e" + this.plugin.getWorldEntity().getName() + " §7gespielt.");
            player.sendMessage(ChatColor.RED + "");
        });

    }

}
