package net.unestia.bedwars.utils;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.team.TeamEntity;
import org.bukkit.entity.Player;

public class TeamUtils {

    private final BedWars plugin;

    public TeamUtils() {
        this.plugin = BedWars.getInstance();
    }

    public TeamEntity getTeam(Player player) {

        for (TeamEntity teamEntity : this.plugin.getTeamManager().getTeams()) {
            if (teamEntity.getPlayers().contains(player)) {
                return teamEntity;
            }
        }

        for (TeamEntity team : this.plugin.getTeamManager().getTeams()) {
            if (team.getPlayers().size() != this.plugin.getBedWarsUtils().getMaxPlayers()) {
                team.getPlayers().add(player);
                return team;
            }
        }
        return null;
    }

}
