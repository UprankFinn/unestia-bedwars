package net.unestia.bedwars.builder.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BedWarsItem extends ItemStack {

    private Integer amount;
    private ItemStack type;

    /**
     * An item stack with the specified damage / durability
     *
     * @param type   item material
     * @param amount stack size
     * @param damage durability / damage
     * @deprecated see {@link #setDurability(short)}
     */
    public BedWarsItem(Material type, int amount, short damage) {
        super(type, amount, damage);
    }

    /**
     * @param type   the type
     * @param amount the amount in the stack
     * @param damage the damage value of the item
     * @param data   the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    public BedWarsItem(Material type, int amount, short damage, Byte data) {
        super(type, amount, damage, data);
    }

    public BedWarsItem() {
    }

    /**
     * Defaults stack size to 1, with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     */
    public BedWarsItem(Material type) {
        super(type);
    }

    /**
     * An item stack with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type   item material
     * @param amount stack size
     */
    public BedWarsItem(Material type, int amount) {
        super(type, amount);
    }

    public Integer getShopAmount() {
        return amount;
    }

    public ItemStack getShopType() {
        return type;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setType(ItemStack type) {
        this.type = type;
    }

}
