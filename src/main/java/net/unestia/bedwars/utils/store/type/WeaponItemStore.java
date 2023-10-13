package net.unestia.bedwars.utils.store.type;

import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.utils.store.ItemStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WeaponItemStore extends ItemStore {

    private final Inventory inventory;

    public WeaponItemStore() {
        super("WEAPON");

        //===========================================================================================================================================================

        this.inventory = Bukkit.createInventory(null, 6 * 9, "Shop - Waffen");
        this.inventory.setItem(0, new ItemBuilder(Material.SANDSTONE, 1, (byte) 0).setDisplayName("§eBlöcke").build());
        this.inventory.setItem(1, new ItemBuilder(Material.GOLDEN_SWORD, 1, (byte) 0).setDisplayName("§eSchwerter").build());
        this.inventory.setItem(2, new ItemBuilder(Material.GOLDEN_PICKAXE, 1, (byte) 0).setDisplayName("§eWerkzeuge").build());
        this.inventory.setItem(3, new ItemBuilder(Material.CROSSBOW, 1, (byte) 0).setDisplayName("§eFernkampf").build());
        this.inventory.setItem(4, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, 1, (byte) 0).setDisplayName("§eRüstung").build());
        this.inventory.setItem(5, new ItemBuilder(Material.CHEST, 1, (byte) 0).setDisplayName("§eKisten").build());
        this.inventory.setItem(6, new ItemBuilder(Material.POTION, 1, (byte) 0).setDisplayName("§eTränke").build());
        this.inventory.setItem(7, new ItemBuilder(Material.ENCHANTING_TABLE, 1, (byte) 0).setDisplayName("§eVerzauberungen").build());
        this.inventory.setItem(8, new ItemBuilder(Material.LECTERN, 1, (byte) 0).setDisplayName("§eEffekte").build());

        //===========================================================================================================================================================

        this.inventory.setItem(20, new ItemBuilder(Material.STONE_SWORD, 1, (byte) 0).setDisplayName("§eStein Schwert").build());
        this.inventory.setItem(21, new ItemBuilder(Material.GOLDEN_SWORD, 1, (byte) 0).setDisplayName("§eGold Schwert").build());
        this.inventory.setItem(22, new ItemBuilder(Material.IRON_SWORD, 1, (byte) 0).setDisplayName("§eEisen Schwert").build());
        this.inventory.setItem(24, new ItemBuilder(Material.GOLDEN_APPLE, 1, (byte) 0).setDisplayName("§eGold Apfel").build());

        this.inventory.setItem(29, new ItemBuilder(Material.SHIELD, 1, (byte) 0).setDisplayName("§eSchild").build());
        this.inventory.setItem(30, new ItemBuilder(Material.FISHING_ROD, 1, (byte) 0).setDisplayName("§eAngel").build());

    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void openInventory(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_PLACE, 1f, 1f);
        player.openInventory(this.inventory);
    }
}
