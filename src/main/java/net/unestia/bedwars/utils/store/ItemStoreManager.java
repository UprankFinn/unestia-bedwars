package net.unestia.bedwars.utils.store;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.utils.store.type.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;

public class ItemStoreManager {

    private final BedWars plugin;

    private final Map<String, ItemStore> itemStores;

    public ItemStoreManager(BedWars plugin) {
        this.plugin = plugin;

        this.itemStores = new HashMap<>();
        this.itemStores.put("ARMOR", new ArmorItemStore());
        this.itemStores.put("BLOCK", new BlockItemStore());
        this.itemStores.put("CHEST", new ChestItemStore());
        this.itemStores.put("DISTANCE", new DistanceItemStore());
        this.itemStores.put("EFFECT", new EffectItemStore());
        this.itemStores.put("ENCHANTMENT", new EnchantmentItemStore());
        this.itemStores.put("POTION", new PotionItemStore());
        this.itemStores.put("TOOLS", new ToolsItemStore());
        this.itemStores.put("WEAPON", new WeaponItemStore());
    }

    public void summon() {
        this.plugin.getWorldEntity().getLocations().forEach(((name, location) -> {
            if (name.endsWith("villager_shop")) {
                Villager villager = (Villager) Bukkit.getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.VILLAGER);
                villager.setProfession(Villager.Profession.NONE);
                villager.setAdult();
                villager.setCanPickupItems(false);
                villager.setSilent(true);
                villager.setAI(false);
                villager.setCollidable(false);
                villager.setArrowsInBody(0);
                villager.setGravity(true);
                villager.setCustomNameVisible(true);
                villager.setCustomName("§eShop");
            }
        }));
    }

    public ItemStore getItemStore(String name) {
        if (this.itemStores.containsKey(name)) return this.itemStores.get(name);
        return null;
    }

    public Map<String, ItemStore> getItemStores() {
        return itemStores;
    }
}
