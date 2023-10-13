package net.unestia.bedwars.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScoreboardUtil {

    private static final List<String> alphabetic = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

    public ScoreboardUtil() {
    }

    public static void createScoreboard(Player player, Map<Integer, String> scores) {
        Scoreboard scoreboard = ((ScoreboardManager) Objects.requireNonNull(Bukkit.getScoreboardManager())).getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", "dummy", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lUNESTIA.NET");
        scores.forEach((line, text) -> {
            Team team = scoreboard.registerNewTeam("x" + line);
            team.setPrefix(text);
            team.addEntry("§" + alphabetic.get(line));
            objective.getScore("§" + alphabetic.get(line)).setScore(line);
        });
        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player, Map<Integer, String> scores) {
        scores.forEach((line, text) -> {
            updateScoreboard(player, line, text);
        });
    }

    public static void updateScoreboard(Player player, Integer line, String text) {
        (Objects.requireNonNull(player.getScoreboard().getTeam("x" + line))).setPrefix(text);
    }

}
