package net.unestia.bedwars.phase.ingame;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.phase.ingame.listener.*;
import net.unestia.bedwars.team.TeamEntity;
import net.unestia.bedwars.utils.phase.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class IngamePhase<IngameTeamListener> extends GamePhase {

    private Map<Integer, String> scores;

    private static final Map<TeamEntity, Integer> TEAMS = new HashMap<>();

    private IngameBlockListener ingameBlockListener;
    private IngameChatListener ingameChatListener;
    private IngameEntityListener ingameEntityListener;
    private IngameInventoryListener ingameInventoryListener;
    private IngamePlayerListener ingamePlayerListener;

    public Integer count;
    private Integer actionBar;

    public IngamePhase(String name) {
        super(BedWars.getInstance(), name);

        this.scores = new HashMap<>();

    }

    @Override
    public void onEnable() {

        this.count = 3600;

        Integer size = this.getPlugin().getTeamManager().getTeams().size() + 1;

        this.scores.put(1, "§c");
        this.scores.put(size + 2, "§a");
        this.scores.put(size + 1, "§f§lTeams:");

        for (TeamEntity teamEntity : this.getPlugin().getTeamManager().getTeams()) {
            TEAMS.put(teamEntity, size);
            this.scores.put(size, teamEntity.getColor() + teamEntity.getName() + " §7(" + teamEntity.getPlayers().size() + ")");

            size--;
        }


        this.actionBar = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach((players) -> players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(BedWars.PREFIX + "§eVerbleibende Zeit: " + this.getPlugin().getBedWarsUtils().getTime(this.count))));
            this.count--;

            if (this.count == 0) {
                this.getPlugin().getBedWarsUtils().handleUndecided();
            }

        }, 20L, 20L);

        this.getPlugin().getBedWarsUtils().getArenaManager().teleport();
        this.getPlugin().getBedWarsUtils().getArenaManager().startSpawner();
        this.getPlugin().getBedWarsUtils().getItemStoreManager().summon();

        this.ingameBlockListener = new IngameBlockListener(this.getPlugin());
        this.ingameChatListener = new IngameChatListener(this.getPlugin());
        this.ingameEntityListener = new IngameEntityListener(this.getPlugin());
        this.ingameInventoryListener = new IngameInventoryListener(this.getPlugin());
        this.ingamePlayerListener = new IngamePlayerListener(this.getPlugin(), this);
        this.ingameBlockListener.registerEvents();
        this.ingameChatListener.registerEvents();
        this.ingameEntityListener.registerEvents();
        this.ingameInventoryListener.registerEvents();
        this.ingamePlayerListener.registerEvents();

    }

    @Override
    public void onDisable() {

        Bukkit.getScheduler().cancelTask(this.actionBar);

        this.ingameBlockListener.unregisterEvents();
        this.ingameChatListener.unregisterEvents();
        this.ingameEntityListener.unregisterEvents();
        this.ingameInventoryListener.unregisterEvents();
        this.ingamePlayerListener.unregisterEvents();

        this.getPlugin().setGamePhase(this.getPlugin().getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase"));
        this.getPlugin().getBedWarsUtils().getGamePhaseManager().getGamePhase("EndingPhase").onEnable();
    }

    @Override
    public void setScores(Map<Integer, String> scores) {
        this.scores = scores;
    }

    @Override
    public Map<Integer, String> getScores() {
        return this.scores;
    }

    public void setSpectator(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        Bukkit.getOnlinePlayers().forEach((players) -> players.hidePlayer(this.getPlugin(), player));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealthScale(20);
        player.setHealth(20);

        //player.spigot().setCollidesWithEntities(false);
        player.setCollidable(false);

        player.getInventory().setArmorContents(null);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
            player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS, 1, (byte) 0).setDisplayName("§eTeleporter").build());
            player.getInventory().setItem(4, new ItemBuilder(Material.COMPARATOR, 1, (byte) 0).setDisplayName("§eSoon :)").build());
            player.getInventory().setItem(8, new ItemBuilder(Material.MAGMA_CREAM, 1, (byte) 0).setDisplayName("§eSpiel verlassen").build());
        }, 5L);

        player.updateInventory();

    }

    public static Map<TeamEntity, Integer> getTEAMS() {
        return TEAMS;
    }
}
