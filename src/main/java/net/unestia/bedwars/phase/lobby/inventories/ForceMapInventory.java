package net.unestia.bedwars.phase.lobby.inventories;


import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ForceMapInventory {

    public static final Inventory inventory = Bukkit.createInventory(null, 6*9, "Wähle eine Map...");

    public ForceMapInventory() {

        BedWars.getInstance().getWorldManager().getWorlds().forEach((worlds) -> {
            inventory.addItem(new ItemBuilder(Material.MAP, 1, (byte) 0).setDisplayName("§e" + worlds.getName()).setLore(Arrays.stream(worlds.getBuilder()).toList()).build());
        });

    }

}
