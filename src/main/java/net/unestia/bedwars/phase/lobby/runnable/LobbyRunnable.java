package net.unestia.bedwars.phase.lobby.runnable;

import net.unestia.bedwars.BedWars;
import org.bukkit.Bukkit;

public class LobbyRunnable {

    private final BedWars plugin;

    private final Integer runnable;

    public LobbyRunnable(BedWars plugin) {
        this.plugin = plugin;

        this.runnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {

            if ((this.plugin.getBedWarsUtils().getMaxTeams() - Bukkit.getOnlinePlayers().size()) == (this.plugin.getBedWarsUtils().getMaxTeams() * this.plugin.getBedWarsUtils().getMaxTeamPlayers())) {
                Bukkit.broadcastMessage(BedWars.PREFIX + "§cEs fehlen " + (this.plugin.getBedWarsUtils().getMaxTeams() - Bukkit.getOnlinePlayers().size()) + " Spieler, damit das Spiel starten kann!");
            }

        }, 1200L, 1200L);

    }

    public void stopRunnable() {
        Bukkit.getScheduler().cancelTask(this.runnable);
    }

    public Integer getRunnable() {
        return runnable;
    }
}
