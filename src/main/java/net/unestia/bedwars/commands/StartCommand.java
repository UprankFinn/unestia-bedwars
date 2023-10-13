package net.unestia.bedwars.commands;

import net.unestia.bedwars.BedWars;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final BedWars plugin;

    public StartCommand(BedWars plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("start").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        if (this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("LobbyPhase"))) {
            this.plugin.getBedWarsUtils().getCountdownManager().getCountdown("LobbyCountdown").forceStart(player);
        } else {
            player.sendMessage(BedWars.PREFIX + "§cDer Server ist bereits Ingame!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }

        return false;
    }
}
