package net.unestia.bedwars.phase.ending;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.phase.ending.listener.*;
import net.unestia.bedwars.utils.phase.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class EndingPhase extends GamePhase {

    private Map<Integer, String> scores;

    private EndingBlockListener endingBlockListener;
    private EndingChatListener endingChatListener;
    private EndingEntityListener endingEntityListener;
    private EndingInventoryListener endingInventoryListener;
    private EndingPlayerListener endingPlayerListener;

    public EndingPhase(String name) {
        super(BedWars.getInstance(), name);

        this.scores = new HashMap<>();
    }

    @Override
    public void setScores(Map<Integer, String> scores) {
        this.scores = scores;
    }

    @Override
    public Map<Integer, String> getScores() {
        return this.scores;
    }

    @Override
    public void onEnable() {
        this.endingBlockListener = new EndingBlockListener(this.getPlugin());
        this.endingChatListener = new EndingChatListener(this.getPlugin());
        this.endingEntityListener = new EndingEntityListener(this.getPlugin());
        this.endingInventoryListener = new EndingInventoryListener(this.getPlugin());
        this.endingPlayerListener = new EndingPlayerListener(this.getPlugin());

        this.endingBlockListener.registerEvents();
        this.endingChatListener.registerEvents();
        this.endingEntityListener.registerEvents();
        this.endingInventoryListener.registerEvents();
        this.endingPlayerListener.registerEvents();

        Bukkit.getOnlinePlayers().forEach((players) -> {
            players.showPlayer(this.getPlugin(), players);
            players.setGameMode(GameMode.SURVIVAL);
            players.getInventory().clear();
            players.setHealth(20);
            players.setHealthScale(20);
            players.setFoodLevel(20);
            players.teleport(new Location(Bukkit.getWorld("world"), -41.5, 72, -110.5, -180, 0));
        });

    }

    @Override
    public void onDisable() {
        new EndingBlockListener(this.getPlugin()).unregisterEvents();
        new EndingChatListener(this.getPlugin()).unregisterEvents();
        new EndingEntityListener(this.getPlugin()).unregisterEvents();
        new EndingInventoryListener(this.getPlugin()).unregisterEvents();
        new EndingPlayerListener(this.getPlugin()).unregisterEvents();
    }
}
