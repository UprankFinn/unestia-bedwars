package net.unestia.bedwars.utils.store;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class ItemStore {

    private final String name;

    protected ItemStore(String name) {
        this.name = name;
    }

    public abstract Inventory getInventory();

    public abstract void openInventory(Player player);

}
