package net.unestia.bedwars.phase.ending.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.function.Consumer;

public class EndingBlockListener {

    private final BedWars plugin;

    Consumer<BlockBreakEvent> blockBreakEvent;
    Consumer<BlockBurnEvent> blockBurnEvent;
    Consumer<BlockPlaceEvent> blockPlaceEvent;

    public EndingBlockListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        blockBreakEvent = (BlockBreakEvent event) -> event.setCancelled(true);

        blockBurnEvent = (BlockBurnEvent event) -> {
            event.setCancelled(true);
        };

        blockPlaceEvent = (BlockPlaceEvent event) -> event.setCancelled(true);

        this.plugin.getEventManager().registerEvent(BlockBreakEvent.class, blockBreakEvent);
        this.plugin.getEventManager().registerEvent(BlockBurnEvent.class, blockBurnEvent);
        this.plugin.getEventManager().registerEvent(BlockPlaceEvent.class, blockPlaceEvent);

    }

    public void unregisterEvents() {
        this.plugin.getEventManager().unregisterEvent(BlockBreakEvent.class, blockBreakEvent);
        this.plugin.getEventManager().unregisterEvent(BlockBurnEvent.class, blockBurnEvent);
        this.plugin.getEventManager().unregisterEvent(BlockPlaceEvent.class, blockPlaceEvent);
    }

}
