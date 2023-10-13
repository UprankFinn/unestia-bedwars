package net.unestia.bedwars.builder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryUtil {

    public static void remove(Inventory inventory, ItemStack material, Integer amount) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == material.getType()) {
                int newamount = itemStack.getAmount() - amount;
                if (newamount > 0) {
                    itemStack.setAmount(newamount);
                    break;
                }
                inventory.remove(itemStack);
                amount = -newamount;
                if (amount == 0) {
                    break;
                }
            }
        }
    }

    public void remove(Inventory inventory, ItemStack material, String localizedName, Integer amount) {
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == material.getType()) {
                if (Objects.requireNonNull(itemStack.getItemMeta()).getLocalizedName().equals(localizedName)) {
                    int newamount = itemStack.getAmount() - amount;
                    if (newamount > 0) {
                        itemStack.setAmount(newamount);
                        break;
                    }
                    inventory.remove(itemStack);
                    amount = -newamount;
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }

    public static Integer getAmount(Player player, Material material) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Arrays.stream(player.getInventory().getContents()).forEach((items) -> {
            if (items != null && items.getType() != null) {
                if (items.getType().equals(material)) {
                    atomicInteger.getAndAdd(items.getAmount());
                }
            }
        });
        return atomicInteger.get() > 64 ? 64 : atomicInteger.get();
    }

}
