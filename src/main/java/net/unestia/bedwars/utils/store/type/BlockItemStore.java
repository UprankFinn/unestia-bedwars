package net.unestia.bedwars.utils.store.type;

import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.builder.item.ItemInfo;
import net.unestia.bedwars.utils.store.ItemStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class BlockItemStore extends ItemStore {

    private final Inventory inventory;

    public BlockItemStore() {
        super("BLOCK");

        //===========================================================================================================================================================

        this.inventory = Bukkit.createInventory(null, 6 * 9, "Shop - Blöcke");
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

        this.inventory.setItem(20, new ItemBuilder(Material.SANDSTONE, 2, (byte) 0).setDisplayName("§eSandstein").setMetadata(new ItemInfo(Material.BRICK, 1).toString()).build());
        this.inventory.setItem(21, new ItemBuilder(Material.OAK_PLANKS, 4, (byte) 0).setDisplayName("§eEichenholzbretter").setMetadata(new ItemInfo(Material.BRICK, 6).toString()).build());
        this.inventory.setItem(22, new ItemBuilder(Material.END_STONE_BRICKS, 8, (byte) 0).setDisplayName("§eEndsteinziegel").setMetadata(new ItemInfo(Material.IRON_INGOT, 8).toString()).build());
        this.inventory.setItem(23, new ItemBuilder(Material.CRYING_OBSIDIAN, 4, (byte) 0).setDisplayName("§eWeinender Obsidian").setMetadata(new ItemInfo(Material.GOLD_INGOT, 4).toString()).build());

        this.inventory.setItem(29, new ItemBuilder(Material.SCAFFOLDING, 8, (byte) 0).setDisplayName("§eGerüst").setMetadata(new ItemInfo(Material.BRICK, 8).toString()).build());
        this.inventory.setItem(30, new ItemBuilder(Material.COBWEB, 1, (byte) 0).setDisplayName("§eSpinnennetz").setMetadata(new ItemInfo(Material.BRICK, 16).toString()).build());
        this.inventory.setItem(31, new ItemBuilder(Material.BEACON, 1, (byte) 0).setDisplayName("§eMakierung").build());


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
