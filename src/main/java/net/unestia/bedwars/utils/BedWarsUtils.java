package net.unestia.bedwars.utils;

import com.google.common.collect.Iterables;
import lombok.Getter;

import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;
import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.team.TeamEntity;
import net.unestia.bedwars.utils.arena.ArenaManager;
import net.unestia.bedwars.utils.countdown.CountdownManager;
import net.unestia.bedwars.utils.phase.GamePhaseManager;
import net.unestia.bedwars.utils.store.ItemStoreManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;

public class BedWarsUtils {

    private final BedWars plugin;

    @Getter
    private String template = "2x1";
    @Getter
    private final Integer maxTeams = Integer.parseInt(this.template.split("x")[0]);
    @Getter
    private final Integer maxTeamPlayers = Integer.parseInt(this.template.split("x")[1]);
    @Getter
    private final Integer maxPlayers = maxTeams * maxTeamPlayers;
    @Getter
    private final Integer playersToForceStart = maxPlayers;


    @Getter
    private final ArenaManager arenaManager;
    @Getter
    private final CountdownManager countdownManager;
    @Getter
    private final GamePhaseManager gamePhaseManager;
    @Getter
    private final ItemStoreManager itemStoreManager;

    public BedWarsUtils() {
        this.plugin = BedWars.getInstance();

        this.arenaManager = new ArenaManager();
        this.countdownManager = new CountdownManager(this.plugin);
        this.gamePhaseManager = new GamePhaseManager(this.plugin);
        this.itemStoreManager = new ItemStoreManager(this.plugin);
    }

    public String getTime(Integer count) {
        int min = 0;
        int sec = 0;
        String minStr = "";
        String secStr = "";
        min = (int) Math.floor(count / 60);
        sec = count % 60;
        minStr = min < 10 ? "0" + (min) : String.valueOf(min);
        secStr = sec < 10 ? "0" + (sec) : String.valueOf(sec);
        return minStr + ":" + secStr;
    }

    public void handleUndecided() {

        StringBuilder winners = new StringBuilder();
        for (TeamEntity teams : this.plugin.getTeamManager().getTeams()) {
            winners.append(teams.getColor()).append(teams.getName());
            winners.append("§7, ");
        }

        Bukkit.broadcastMessage(BedWars.PREFIX + "§7Das Spiel wurde beendet, da die Zeit abgelaufen ist.");
        Bukkit.broadcastMessage(BedWars.PREFIX + "§7Folgende Teams haben gewonnen: " + winners);

        this.plugin.getPlayers().forEach((players) -> {
            //TODO: this.playerManager.getStatsAllPlayer(players.getUniqueId()).addWin("statistics_alltime");
            //TODO: this.playerManager.getStatsMonthlyPlayer(players.getUniqueId()).addWin("statistics_monthly");
        });

        this.getGamePhaseManager().getGamePhase("IngamePhase").onDisable();
        Bukkit.getOnlinePlayers().forEach((players) -> this.plugin.getTeamManager().getTeams().forEach((team) -> team.getPlayers().remove(players)));
        this.getCountdownManager().getCountdown("EndingCountdown").startCountdown();

        this.plugin.getServer().getOnlinePlayers().forEach((players) -> {

            players.showPlayer(this.plugin, players);

            /*this.plugin.getScoreboard().getTeams().forEach((scoreboardTeams) -> {
                if (scoreboardTeams.getPlayerNameSet().contains(players.getName())) {
                    scoreboardTeams.getPlayerNameSet().remove(players.getName());
                }
            });*/

            this.plugin.getScoreboard().g().forEach((scoreboardTeams) -> {
                //if (scoreboardTeams.g().contains(players.getName())) {
                scoreboardTeams.g().remove(players.getName());
                //}
            });

            //TODO: RankEntity rankEntity = UnestiaAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).getRank();

            //TODO: this.plugin.getScoreboard().getTeam("0" + rankEntity.getId() + rankEntity.getName()).getPlayerNameSet().add(player.getName());

            /*PlayerConnection playerConnection = ((CraftPlayer) players).getHandle().playerConnection;
            this.plugin.getScoreboard().getTeams().forEach((team) -> {
                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
            });*/

            PlayerConnection playerConnection = ((CraftPlayer) players).getHandle().c;
            this.plugin.getScoreboard().g().forEach((team) -> {
                playerConnection.a(PacketPlayOutScoreboardTeam.a(team));
                playerConnection.a(PacketPlayOutScoreboardTeam.a(team, true));
            });

        });

    }

    public void handleEnding() {

        if (this.plugin.getTeamManager().getTeams().size() == 1) {
            TeamEntity winnerTeam = Iterables.getLast(this.plugin.getTeamManager().getTeams());

            Bukkit.broadcastMessage(BedWars.PREFIX + "§7Das Team " + winnerTeam.getColor() +
                    winnerTeam.getName() + " §7hat das Spiel gewonnen!");

            winnerTeam.getPlayers().forEach((players) -> {
                //TODO: this.playerManager.getStatsAllPlayer(players.getUniqueId()).addWin("statistics_alltime");
                //TODO: this.playerManager.getStatsMonthlyPlayer(players.getUniqueId()).addWin("statistics_monthly");
            });

            this.getGamePhaseManager().getGamePhase("IngamePhase").onDisable();
            Bukkit.getOnlinePlayers().forEach((players) -> this.plugin.getTeamManager().getTeams().forEach((team) -> team.getPlayers().remove(players)));
            this.getCountdownManager().getCountdown("EndingCountdown").startCountdown();

            this.plugin.getServer().getOnlinePlayers().forEach((players) -> {

                players.showPlayer(this.plugin, players);

                /*this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                    if (teams.getPlayerNameSet().contains(players.getName())) {
                        teams.getPlayerNameSet().remove(players.getName());
                    }
                });*/

                this.plugin.getScoreboard().g().forEach((team) -> team.g().remove(players.getName()));


                //TODO: RankEntity rankEntity = UnestiaAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).getRank();

                //TODO: this.plugin.getScoreboard().getTeam("0" + rankEntity.getId() + rankEntity.getName()).getPlayerNameSet().add(player.getName());

                /*PlayerConnection playerConnection = ((CraftPlayer) players).getHandle().playerConnection;
                this.plugin.getScoreboard().getTeams().forEach((team) -> {
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 1));
                    playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
                });*/

                PlayerConnection playerConnection = ((CraftPlayer) players).getHandle().c;
                this.plugin.getScoreboard().g().forEach((team) -> {
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team));
                    playerConnection.a(PacketPlayOutScoreboardTeam.a(team, true));
                });

            });


        }

    }

}
