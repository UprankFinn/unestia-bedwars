package net.unestia.bedwars.phase.ending.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class EndingInventoryListener {

    private final BedWars plugin;

    Consumer<InventoryClickEvent> inventoryClickEvent;

    public EndingInventoryListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        inventoryClickEvent = (InventoryClickEvent event) -> {

            if (!(this.plugin.getPlayers().contains(event.getWhoClicked()))) event.setCancelled(true);

            if(event.getAction() == null) return;
            if(event.getInventory() == null) return;

            event.setCancelled(true);

        };

        this.plugin.getEventManager().registerEvent(InventoryClickEvent.class, inventoryClickEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(InventoryClickEvent.class, inventoryClickEvent);

    }

}
