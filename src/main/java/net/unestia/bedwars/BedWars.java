package net.unestia.bedwars;

import lombok.Getter;
import lombok.Setter;

import net.minecraft.EnumChatFormat;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import net.unestia.bedwars.commands.ForceMapCommand;
import net.unestia.bedwars.commands.StartCommand;
import net.unestia.bedwars.event.EventManager;
import net.unestia.bedwars.phase.ending.EndingPhase;
import net.unestia.bedwars.phase.ending.countdown.EndingCountdown;
import net.unestia.bedwars.phase.ingame.IngamePhase;
import net.unestia.bedwars.phase.lobby.LobbyPhase;
import net.unestia.bedwars.phase.lobby.countdown.LobbyCountdown;
import net.unestia.bedwars.phase.lobby.inventories.ForceMapInventory;
import net.unestia.bedwars.team.TeamEntityManager;
import net.unestia.bedwars.utils.BedWarsUtils;
import net.unestia.bedwars.utils.TeamUtils;
import net.unestia.bedwars.utils.phase.GamePhase;
import net.unestia.bedwars.world.WorldEntity;
import net.unestia.bedwars.world.WorldEntityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class BedWars extends JavaPlugin {

    public static final String PREFIX = "§8[§6Bedwars§8] §r";

    @Getter
    private static BedWars instance;

    private EventManager eventManager;
    private TeamEntityManager teamManager;
    private WorldEntityManager worldManager;

    private final BedWarsUtils bedWarsUtils;
    private final TeamUtils teamUtils;

    private final List<Player> players;

    private WorldEntity worldEntity;

    private GamePhase gamePhase;

    private final Scoreboard scoreboard = new Scoreboard();

    public BedWars() {
        instance = this;
        this.bedWarsUtils = new BedWarsUtils();
        this.teamUtils = new TeamUtils();
        this.players = new ArrayList<>();
    }

    @Override
    public void onEnable() {

        this.eventManager = new EventManager(this);
        this.teamManager = new TeamEntityManager();
        this.worldManager = new WorldEntityManager(this);

        Random random = new Random();
        this.worldEntity = this.worldManager.getWorlds().get(random.nextInt(this.worldManager.getWorlds().size()));

        new ForceMapInventory();

        new ForceMapCommand(this);
        new StartCommand(this);

        if (this.bedWarsUtils.getMaxTeams() == 2) {
            this.teamManager.createTeam("Blau", ChatColor.BLUE);
            this.teamManager.createTeam("Rot", ChatColor.RED);
        } else if (this.bedWarsUtils.getMaxTeams() == 4) {
            this.teamManager.createTeam("Blau", ChatColor.BLUE);
            this.teamManager.createTeam("Rot", ChatColor.RED);
            this.teamManager.createTeam("Gelb", ChatColor.YELLOW);
            this.teamManager.createTeam("Grün", ChatColor.GREEN);
        } else if (this.bedWarsUtils.getMaxTeams() == 8) {
            this.teamManager.createTeam("Blau", ChatColor.BLUE);
            this.teamManager.createTeam("Rot", ChatColor.RED);
            this.teamManager.createTeam("Gelb", ChatColor.YELLOW);
            this.teamManager.createTeam("Grün", ChatColor.GREEN);
            this.teamManager.createTeam("Schwarz", ChatColor.BLACK);
            this.teamManager.createTeam("Pink", ChatColor.LIGHT_PURPLE);
            this.teamManager.createTeam("Orange", ChatColor.GOLD);
            this.teamManager.createTeam("Grau", ChatColor.GRAY);
        }

        /*
        Here build in your rank System! :)
        This foreach loop gets all ranks from the list and sets in the ScoreboardTeam the prefix for the ranks + the Color!

        UnestiaAPI.getInstance().getRankManager().getRanks().forEach((rank) -> {
            ScoreboardTeam scoreboardTeam = scoreboard.createTeam("0" + rank.getId() + rank.getName());
            scoreboardTeam.setPrefix(new ChatComponentText(rank.getPrefix()));
            scoreboardTeam.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.NEVER);
        });
        */

        this.teamManager.getTeams().forEach((teams) -> {
            teams.getSettings().put("bed", new AtomicBoolean(true));
            teams.getSettings().put("bedType", Material.valueOf(teams.getColor().name().toUpperCase() + "_BED"));

            ScoreboardTeam scoreboardTeam = scoreboard.g(teams.getName());
            //scoreboardTeam.b(IChatBaseComponent.b(teams.getColor() + teams.getName() + " §7| "));
            scoreboardTeam.a(EnumChatFormat.valueOf(teams.getColor().name().toUpperCase()));
            scoreboardTeam.a(ScoreboardTeamBase.EnumTeamPush.b);
        });

        Bukkit.getWorlds().forEach((worlds) -> {
            worlds.getEntities().forEach((entities) -> {
                if (entities.getType().equals(EntityType.PLAYER)) return;
                entities.remove();
            });
            worlds.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            worlds.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            worlds.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            worlds.setGameRule(GameRule.DISABLE_RAIDS, false);
            worlds.setGameRule(GameRule.MOB_GRIEFING, false);
        });

        this.bedWarsUtils.getGamePhaseManager().createGamePhase(new LobbyPhase("LobbyPhase"));
        this.bedWarsUtils.getGamePhaseManager().createGamePhase(new IngamePhase("IngamePhase"));
        this.bedWarsUtils.getGamePhaseManager().createGamePhase(new EndingPhase("EndingPhase"));

        this.bedWarsUtils.getCountdownManager().createCountdown(new LobbyCountdown(this));
        this.bedWarsUtils.getCountdownManager().createCountdown(new EndingCountdown(this));

        this.setGamePhase(this.bedWarsUtils.getGamePhaseManager().getGamePhase("LobbyPhase"));

        this.bedWarsUtils.getGamePhaseManager().getGamePhase("LobbyPhase").onEnable();

    }

    @Override
    public void onDisable() {
        this.getPlayers().forEach((players) -> {
            players.kickPlayer("");
        });
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public WorldEntityManager getWorldManager() {
        return worldManager;
    }
}
