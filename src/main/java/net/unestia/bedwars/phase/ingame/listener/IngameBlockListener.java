package net.unestia.bedwars.phase.ingame.listener;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.phase.ingame.IngamePhase;
import net.unestia.bedwars.scoreboard.ScoreboardUtil;
import net.unestia.bedwars.team.TeamEntity;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class IngameBlockListener {

    private final BedWars plugin;

    private final List<Location> blockLocations;
    private final List<Material> canBuildBlocks;

    Consumer<BlockBreakEvent> blockBreakEvent;
    Consumer<BlockPlaceEvent> blockPlaceEvent;

    public IngameBlockListener(BedWars plugin) {
        this.plugin = plugin;

        this.blockLocations = new ArrayList<>();
        this.canBuildBlocks = new ArrayList<>();

        this.canBuildBlocks.add(Material.WHITE_WOOL);
        this.canBuildBlocks.add(Material.SANDSTONE);
        this.canBuildBlocks.add(Material.OAK_PLANKS);
        this.canBuildBlocks.add(Material.END_STONE_BRICKS);
        this.canBuildBlocks.add(Material.OBSIDIAN);
        this.canBuildBlocks.add(Material.CRYING_OBSIDIAN);
        this.canBuildBlocks.add(Material.SCAFFOLDING);
        this.canBuildBlocks.add(Material.TWISTING_VINES);
        this.canBuildBlocks.add(Material.COBWEB);
        this.canBuildBlocks.add(Material.CRAFTING_TABLE);
        this.canBuildBlocks.add(Material.CHEST);
        this.canBuildBlocks.add(Material.ENDER_CHEST);
        this.canBuildBlocks.add(Material.BEACON);
    }

    public void registerEvents() {

        blockBreakEvent = (BlockBreakEvent event) -> {

            if (event.getBlock().getType().toString().endsWith("_BED")) {

                event.getBlock().getDrops().forEach((drops) -> drops.setType(Material.AIR));
                event.getBlock().getDrops().clear();

                for (TeamEntity teamEntities : this.plugin.getTeamManager().getTeams()) {

                    if (event.getBlock().getType().toString().startsWith(teamEntities.getColor().name())) {

                        if (((AtomicBoolean) teamEntities.getSettings().get("bed")).get()) {
                            if (event.getBlock().getLocation().equals(this.plugin.getWorldEntity().getLocations().get(teamEntities.getColor().name().toLowerCase() + "_bed_up")) ||
                                    event.getBlock().getLocation().equals(this.plugin.getWorldEntity().getLocations().get(teamEntities.getColor().name().toLowerCase() + "_bed_down"))) {

                                if (!(teamEntities.getPlayers().contains(event.getPlayer()))) {

                                    this.plugin.getWorldEntity().getLocations().get(teamEntities.getColor().name().toLowerCase() + "_bed_up").getBlock().setType(Material.AIR);
                                    this.plugin.getWorldEntity().getLocations().get(teamEntities.getColor().name().toLowerCase() + "_bed_down").getBlock().setType(Material.AIR);

                                    ((AtomicBoolean) teamEntities.getSettings().get("bed")).set(false);
                                    Bukkit.broadcastMessage(BedWars.PREFIX + "§7Das Bett von Team " + teamEntities.getColor() + teamEntities.getName() + " §7wurde zerstört.");

                                    //TODO: this.plugin.getPlayerManager().getStatsAllPlayer(event.getPlayer().getUniqueId()).addDestroyedBeds("statistics_alltime");
                                    //TODO: this.plugin.getPlayerManager().getStatsMonthlyPlayer(event.getPlayer().getUniqueId()).addDestroyedBeds("statistics_monthly");

                                    Bukkit.getOnlinePlayers().forEach((players) -> {
                                        players.playSound(players.getLocation(), Sound.ENTITY_WITHER_DEATH, 1f, 1f);
                                        ScoreboardUtil.updateScoreboard(players, IngamePhase.getTEAMS().get(teamEntities), teamEntities.getColor() + "§m" + teamEntities.getName() + "§r §7(" + teamEntities.getPlayers().size() + ")");
                                    });

                                } else {
                                    event.setCancelled(true);
                                    event.getPlayer().sendMessage(BedWars.PREFIX + "§cDu kannst dein eigenes Bett nicht zerstören.");
                                    return;
                                }
                            }
                        } else {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(BedWars.PREFIX + "§cDas Bett wurde bereits zerstört!");
                            return;
                        }

                    }

                }

            }

            if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !(this.blockLocations.contains(event.getBlock().getLocation()))) {
                event.setCancelled(true);
            } else if (this.blockLocations.contains(event.getBlock().getLocation()) && event.getBlock().getType().equals(this.canBuildBlocks)) {
                this.blockLocations.remove(event.getBlock().getLocation());
            }

        };

        blockPlaceEvent = (BlockPlaceEvent event) -> {

            if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(false);
            } else if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && !(this.canBuildBlocks.contains(event.getBlock().getType()))) {
                event.setCancelled(true);
            } else if (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
                event.setCancelled(true);
            } else {
                this.blockLocations.add(event.getBlock().getLocation());
            }

            if (event.getBlock().getType().equals(Material.CRAFTING_TABLE)) {

                AtomicReference<Integer> counter = new AtomicReference<>(0);
                Integer breaker = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {

                    PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(new Random().nextInt(2000),
                            new BlockPosition(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()), counter.get());

                    Bukkit.getOnlinePlayers().forEach((player) -> ((CraftPlayer) player).getHandle().c.a(packet));

                    if (counter.get() == 9) {
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getDrops().clear();
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BRICK, 1));
                    }
                    counter.getAndSet(counter.get() + 1);
                }, 10L, 10L);

            }

        };

        this.plugin.getEventManager().registerEvent(BlockBreakEvent.class, blockBreakEvent);
        this.plugin.getEventManager().registerEvent(BlockPlaceEvent.class, blockPlaceEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(BlockBreakEvent.class, blockBreakEvent);
        this.plugin.getEventManager().unregisterEvent(BlockPlaceEvent.class, blockPlaceEvent);
    }

}
