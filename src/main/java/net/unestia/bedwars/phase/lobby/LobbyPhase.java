package net.unestia.bedwars.phase.lobby;

import lombok.Getter;
import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.phase.lobby.listener.*;
import net.unestia.bedwars.phase.lobby.runnable.LobbyRunnable;
import net.unestia.bedwars.utils.phase.GamePhase;

import java.util.HashMap;
import java.util.Map;

public class LobbyPhase extends GamePhase {

    private Map<Integer, String> scores;

    private final LobbyBlockListener lobbyBlockListener;
    private final LobbyChatListener lobbyChatListener;
    private final LobbyEntityListener lobbyEntityListener;
    private final LobbyFoodListener lobbyFoodListener;
    @Getter
    private final LobbyInventoryListener lobbyInventoryListener;
    private final LobbyPlayerListener lobbyPlayerListener;
    private final ServerListener serverListener;

    private LobbyRunnable lobbyRunnable;

    public LobbyPhase(String name) {
        super(BedWars.getInstance(), name);

        this.scores = new HashMap<>();

        this.lobbyBlockListener = new LobbyBlockListener(this.getPlugin());
        this.lobbyChatListener = new LobbyChatListener(this.getPlugin());
        this.lobbyEntityListener = new LobbyEntityListener(this.getPlugin());
        this.lobbyFoodListener = new LobbyFoodListener(this.getPlugin());
        this.lobbyInventoryListener = new LobbyInventoryListener(this.getPlugin());
        this.lobbyPlayerListener = new LobbyPlayerListener(this.getPlugin(), this);
        this.serverListener = new ServerListener(this.getPlugin());

    }

    @Override
    public void onEnable() {

        this.lobbyRunnable = new LobbyRunnable(this.getPlugin());

        this.lobbyBlockListener.registerEvents();
        this.lobbyChatListener.registerEvents();
        this.lobbyEntityListener.registerEvents();
        this.lobbyFoodListener.registerEvents();
        this.lobbyInventoryListener.registerEvents();
        this.lobbyPlayerListener.registerEvents();
        this.serverListener.registerEvents();

    }

    @Override
    public void onDisable() {

        this.lobbyRunnable.stopRunnable();

        this.lobbyBlockListener.unregisterEvents();
        this.lobbyChatListener.unregisterEvents();
        this.lobbyEntityListener.unregisterEvents();
        this.lobbyFoodListener.unregisterEvents();
        this.lobbyInventoryListener.unregisterEvents();
        this.lobbyPlayerListener.unregisterEvents();
        this.serverListener.unregisterEvents();

        this.getPlugin().setGamePhase(this.getPlugin().getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase"));
        this.getPlugin().getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase").onEnable();
    }

    @Override
    public void setScores(Map<Integer, String> scores) {
        this.scores = scores;
    }

    @Override
    public Map<Integer, String> getScores() {
        return this.scores;
    }

}
