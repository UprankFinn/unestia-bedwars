package net.unestia.bedwars.phase.lobby.inventories;

import lombok.Getter;
import net.unestia.bedwars.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class PlayerInventory {

    @Getter
    private final Inventory inventory;

    public PlayerInventory() {
        this.inventory = Bukkit.createInventory(null, 36);
        this.inventory.setItem(0, new ItemBuilder(Material.RED_BED, 1, (byte) 0).setDisplayName("§6Team auswählen").build());
        this.inventory.setItem(8, new ItemBuilder(Material.MAGMA_CREAM, 1, (byte) 0).setDisplayName("§6Spiel verlassen").build());
    }

}
