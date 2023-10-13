package net.unestia.bedwars.utils.phase;

import lombok.Getter;
import net.unestia.bedwars.BedWars;

import java.util.HashMap;
import java.util.Map;

public class GamePhaseManager {

    private final BedWars plugin;

    @Getter
    private final Map<String, GamePhase> gamePhases;

    public GamePhaseManager(BedWars plugin) {
        this.plugin = plugin;

        this.gamePhases = new HashMap<>();
    }

    public void createGamePhase(GamePhase gamePhase) {
        this.gamePhases.put(gamePhase.getName(), gamePhase);
    }

    public GamePhase getGamePhase(String name) {
        if (this.gamePhases.containsKey(name)) return this.gamePhases.get(name);
        return null;
    }

}
