package net.unestia.bedwars.team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamEntity {

    private final String name;
    private final ChatColor color;
    private final List<Player> players;
    private final Map<String, Object> settings;

    public TeamEntity(String name, ChatColor color) {
        this.name = name;
        this.color = color;
        this.players = new ArrayList<>();
        this.settings = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Map<String, Object> getSettings() {
        return this.settings;
    }

}
