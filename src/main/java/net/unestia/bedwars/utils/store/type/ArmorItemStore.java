package net.unestia.bedwars.utils.store.type;

import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.utils.store.ItemStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ArmorItemStore extends ItemStore {

    private final Inventory inventory;

    public ArmorItemStore() {
        super("ARMOR");

        //===========================================================================================================================================================

        this.inventory = Bukkit.createInventory(null, 6 * 9, "Shop - Rüstung");
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

        this.inventory.setItem(20, new ItemBuilder(Material.LEATHER_HELMET, 1, (byte) 0).setDisplayName("§eLederkappe").build());
        this.inventory.setItem(21, new ItemBuilder(Material.LEATHER_CHESTPLATE, 1, (byte) 0).setDisplayName("§eLederjacke").build());
        this.inventory.setItem(23, new ItemBuilder(Material.LEATHER_LEGGINGS, 1, (byte) 0).setDisplayName("§eLederhose").build());
        this.inventory.setItem(24, new ItemBuilder(Material.LEATHER_BOOTS, 1, (byte) 0).setDisplayName("§eLederstiefel").build());

        this.inventory.setItem(29, new ItemBuilder(Material.CHAINMAIL_HELMET, 1, (byte) 0).setDisplayName("§eKettenhaube").build());
        this.inventory.setItem(30, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, 1, (byte) 0).setDisplayName("§eKettenhemd").build());
        this.inventory.setItem(32, new ItemBuilder(Material.CHAINMAIL_LEGGINGS, 1, (byte) 0).setDisplayName("§eKettenhose").build());
        this.inventory.setItem(33, new ItemBuilder(Material.CHAINMAIL_BOOTS, 1, (byte) 0).setDisplayName("§eKettenstiefel").build());

        this.inventory.setItem(38, new ItemBuilder(Material.IRON_HELMET, 1, (byte) 0).setDisplayName("§eEisenhelm").build());
        this.inventory.setItem(39, new ItemBuilder(Material.GOLDEN_CHESTPLATE, 1, (byte) 0).setDisplayName("§eGoldharnisch").build());
        this.inventory.setItem(41, new ItemBuilder(Material.IRON_LEGGINGS, 1, (byte) 0).setDisplayName("§eEisenbeinschutz").build());
        this.inventory.setItem(42, new ItemBuilder(Material.GOLDEN_BOOTS, 1, (byte) 0).setDisplayName("§eGoldstiefel").build());

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
