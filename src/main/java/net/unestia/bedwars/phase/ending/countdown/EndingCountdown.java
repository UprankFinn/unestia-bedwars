package net.unestia.bedwars.phase.ending.countdown;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.utils.countdown.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EndingCountdown extends Countdown {

    private final BedWars plugin;

    private Integer startCountdown;

    public EndingCountdown(BedWars plugin) {
        super("EndingCountdown", plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase"), 15, true);
        this.plugin = plugin;
    }

    @Override
    public void startCountdown() {
        if (this.plugin.getGamePhase().equals(this.gamePhase)) {
            this.startCountdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {

                if (this.started) {

                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));

                    if (this.countdown == 15 || this.countdown == 10) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Der Server stoppt in §e" + this.countdown + " §7Sekunden.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                        });
                    } else if (this.countdown == 5 || this.countdown == 4 || this.countdown == 3 || this.countdown == 2) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Der Server stoppt in §e" + this.countdown + " §7Sekunden.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                        });
                    } else if (this.countdown == 1) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Der Server stoppt in §e" + this.countdown + " §7Sekunde.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                        });
                    } else if (this.countdown == 0) {

                        Bukkit.getOnlinePlayers().forEach((players) -> players.kickPlayer(null));

                        this.stopCountdown();
                        Bukkit.getScheduler().cancelTask(startCountdown);
                        Bukkit.shutdown();
                    }
                    this.countdown--;
                } else {
                    this.setCountdown(60);
                    Bukkit.getScheduler().cancelTask(startCountdown);
                }

            }, 20L, 20L);
        }
    }

    @Override
    public void stopCountdown() {
        Bukkit.getScheduler().cancelTask(this.startCountdown);
    }

    @Override
    public void forceStart(Player player) {
    }
}
