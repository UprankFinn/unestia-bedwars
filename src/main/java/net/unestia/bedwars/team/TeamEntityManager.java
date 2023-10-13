package net.unestia.bedwars.team;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamEntityManager {

    private final Map<String, TeamEntity> abstractTeams = new HashMap();

    public TeamEntityManager() {
    }

    public TeamEntity getTeam(String name) {
        return this.abstractTeams.get(name);
    }

    public List<TeamEntity> getTeams() {
        return new ArrayList<>(this.abstractTeams.values());
    }

    public void createTeam(String name, ChatColor color) {
        this.abstractTeams.put(name, new TeamEntity(name, color));
    }

    public void deleteTeam(String name) {
        this.abstractTeams.remove(name);
    }

}
