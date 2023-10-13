package net.unestia.bedwars.phase.lobby.countdown;

import lombok.Getter;
import lombok.Setter;

import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.network.PlayerConnection;
import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import net.unestia.bedwars.team.TeamEntity;
import net.unestia.bedwars.utils.countdown.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LobbyCountdown extends Countdown {

    private final BedWars plugin;

    @Getter
    @Setter
    private Integer startCountdown;

    public LobbyCountdown(BedWars plugin) {
        super("LobbyCountdown", plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("LobbyPhase"), 60, false);

        this.plugin = plugin;
    }

    public void checkPlayers(Integer onlinePlayers) {
        if (this.plugin.getBedWarsUtils().getTemplate().equalsIgnoreCase("2x1")) {
            if (onlinePlayers >= this.plugin.getBedWarsUtils().getMaxPlayers()) {
                if (started) {
                    return;
                }

                started = true;
                startCountdown();
            } else {
                if (this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase")) || this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase"))) {
                    stopCountdown();
                    started = false;
                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));
                }
                if (this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("LobbyPhase"))) {
                    stopCountdown();
                    started = false;
                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));
                }
            }
        } else {
            if (onlinePlayers >= this.plugin.getBedWarsUtils().getMaxPlayers() - 1) {
                if (started) {
                    return;
                }
                started = true;
                startCountdown();
            } else {
                if (this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase")) || this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase"))) {
                    stopCountdown();
                    started = false;
                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));
                }
                if (this.plugin.getGamePhase().equals(this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("LobbyPhase"))) {
                    stopCountdown();
                    started = false;
                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));
                }
            }
        }

    }

    @Override
    public void startCountdown() {
        if (this.plugin.getGamePhase().equals(this.gamePhase)) {
            this.startCountdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {

                if (this.started) {

                    Bukkit.getOnlinePlayers().forEach(players -> players.setLevel(this.countdown));

                    if (this.countdown == 60 || this.countdown == 50 || this.countdown == 40 || this.countdown == 30 || this.countdown == 20 || this.countdown == 15 || this.countdown == 10) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Das Spiel startet in §e" + this.countdown + " §7Sekunden.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                        });
                    } else if (this.countdown == 5 || this.countdown == 4 || this.countdown == 3 || this.countdown == 2) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Das Spiel startet in §e" + this.countdown + " §7Sekunden.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                        });
                    } else if (this.countdown == 1) {
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.sendMessage(BedWars.PREFIX + "§7Das Spiel startet in §e" + this.countdown + " §7Sekunde.");
                            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1f, 1f);
                        });
                    } else if (this.countdown == 0) {

                        /*if (UnestiaAPI.getInstance().getChannel() != null) {
                            if (UnestiaAPI.getInstance().getChannel().isActive() && UnestiaAPI.getInstance().getChannel().isOpen()) {
                                UnestiaAPI.getInstance().getChannel().writeAndFlush(new Packet(PacketType.CLOUD_SERVER_START_PACKET.name(), new CloudServerStartPacket(UnestiaAPI.getInstance().getGroup(), UnestiaAPI.getInstance().getTemplate())));
                            }
                        }*/

                        List<Player> player = new ArrayList<>();
                        this.plugin.getTeamManager().getTeams().forEach((team) -> player.addAll(team.getPlayers()));

                        Bukkit.getOnlinePlayers().forEach((players) -> {
                            if (!(player.contains(players))) {
                                for (TeamEntity teamEntity : this.plugin.getTeamManager().getTeams()) {
                                    if (teamEntity.getPlayers().size() != this.plugin.getBedWarsUtils().getMaxTeamPlayers()) {
                                        teamEntity.getPlayers().add(players);

                                        //TODO: RankEntity rankEntity = UnestiaAPI.getInstance().getPlayerManager().getPlayer(players.getUniqueId()).getRank();

                                        //TODO: this.plugin.getScoreboard().getTeam("0" + rankEntity.getId() + rankEntity.getName()).getPlayerNameSet().remove(players.getName());

                                        /*this.plugin.getScoreboard().getTeam(teamEntity.getName()).getPlayerNameSet().add(players.getName());

                                        this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                                            PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                                            this.plugin.getScoreboard().getTeams().forEach((teams) -> {
                                                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 1));
                                                playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(teams, 0));
                                            });
                                        });*/

                                        Objects.requireNonNull(this.plugin.getScoreboard().f(teamEntity.getName())).g().add(players.getName());

                                        this.plugin.getServer().getOnlinePlayers().forEach((onlinePlayer) -> {
                                            PlayerConnection playerConnection = ((CraftPlayer) onlinePlayer).getHandle().c;
                                            this.plugin.getScoreboard().g().forEach((teams) -> {
                                                playerConnection.a(PacketPlayOutScoreboardTeam.a(teams));
                                                playerConnection.a(PacketPlayOutScoreboardTeam.a(teams, true));
                                            });
                                        });

                                        return;
                                    }
                                }
                            }
                        });

                        this.plugin.getTeamManager().getTeams().forEach((teams) -> {
                            if (teams.getPlayers().isEmpty())
                                this.plugin.getTeamManager().deleteTeam(teams.getName());
                        });

                        this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("LobbyPhase").onDisable();

                        this.stopCountdown();

                        Bukkit.getOnlinePlayers().forEach((players) -> {
                            players.setLevel(0);
                            players.setExp(0);
                            players.getInventory().clear();
                            players.setGameMode(GameMode.SURVIVAL);
                            this.plugin.getPlayers().add(players);
                            ScoreboardUtil.createScoreboard(players, this.plugin.getBedWarsUtils().getGamePhaseManager().getGamePhase("IngamePhase").getScores());

                            if (this.plugin.getTeamUtils().getTeam(players) != null) {
                                players.teleport(this.plugin.getWorldEntity().getLocations().get(this.plugin.getTeamUtils().getTeam(players).getName().toLowerCase() + "_spawn"));
                            }

                        });

                        Bukkit.getScheduler().cancelTask(startCountdown);
                    }
                    this.countdown--;
                } else {
                    this.setCountdown(60);
                    Bukkit.getScheduler().cancelTask(startCountdown);
                }

            }, 20L, 20L);
        }
    }

    @Override
    public void stopCountdown() {
        if (this.started) {
            Bukkit.getScheduler().cancelTask(this.startCountdown);
            this.setCountdown(60);
        }
    }

    @Override
    public void forceStart(Player player) {
            if (this.started) {
                if (this.countdown >= 10) {
                    player.sendMessage(BedWars.PREFIX + "§7Du hast die Runde erfolgreich gestartet.");
                    this.countdown = 10;
                } else {
                    player.sendMessage(BedWars.PREFIX + "§cDer Countdown ist bereits unter 10 Sekunden!");
                }
            } else {
                player.sendMessage(BedWars.PREFIX + "§cDer Countdown wurde noch nicht gestartet!");
            }
    }
}
