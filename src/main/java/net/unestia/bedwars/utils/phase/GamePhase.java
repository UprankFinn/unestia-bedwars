package net.unestia.bedwars.utils.phase;

import lombok.Getter;
import net.unestia.bedwars.BedWars;

import java.util.Map;

@Getter
public abstract class GamePhase {

    private final BedWars plugin;

    private final String name;

    public abstract void setScores(Map<Integer, String> scores);

    public abstract Map<Integer, String> getScores();
    public GamePhase(BedWars plugin, String name) {
        this.plugin = plugin;

        this.name = name;
    }

    public abstract void onEnable();

    public abstract void onDisable();

}
