package net.unestia.bedwars.utils.store.type;

import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.utils.store.ItemStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class EnchantmentItemStore extends ItemStore {

    private final Inventory inventory;

    public EnchantmentItemStore() {
        super("ENCHANTMENT");

        //===========================================================================================================================================================

        this.inventory = Bukkit.createInventory(null, 6 * 9, "Shop - Verzaubern");
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

        this.inventory.setItem(22, new ItemBuilder(Material.BARRIER, 1, (byte) 0).setDisplayName("§eKommt bald!").build());

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
