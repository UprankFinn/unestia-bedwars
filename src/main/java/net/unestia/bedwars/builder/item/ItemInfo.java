package net.unestia.bedwars.builder.item;

import org.bukkit.Material;

public class ItemInfo {

    private final Material material;
    private final Integer price;

    public ItemInfo(Material material, Integer price) {
        this.material = material;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "{" +
                "\"material\":\"" + material.name().toUpperCase() +
                "\",\"price\":" + price +
                '}';
    }
}
