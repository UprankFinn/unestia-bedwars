package net.unestia.bedwars.phase.ingame.listener;

import com.google.gson.Gson;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.builder.InventoryUtil;
import net.unestia.bedwars.builder.ItemBuilder;
import net.unestia.bedwars.builder.item.ItemInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class IngameInventoryListener {

    private final BedWars plugin;

    Consumer<InventoryClickEvent> inventoryClickEvent;

    public IngameInventoryListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        inventoryClickEvent = (InventoryClickEvent event) -> {

            if (!(this.plugin.getPlayers().contains(event.getWhoClicked()))) event.setCancelled(true);

            if (event.getCurrentItem() == null) return;
            if (event.getClickedInventory() == null) return;
            if (event.getCurrentItem().getItemMeta() == null) return;

            Player player = (Player) event.getWhoClicked();

            if (!(this.plugin.getPlayers().contains(player))) {
                event.setCancelled(true);
                if (event.getView().getTitle().equals("Lebende Spieler")) {

                    Player playerTeleport = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replace("§e", ""));

                    if (playerTeleport == null) {
                        event.getWhoClicked().sendMessage(BedWars.PREFIX + "§cDer Spieler ist nicht auf dem Server!");
                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        event.getWhoClicked().closeInventory();
                        return;
                    }

                    if (!(this.plugin.getPlayers().contains(playerTeleport))) {
                        event.getWhoClicked().sendMessage(BedWars.PREFIX + "§cDer Spieler lebt nicht mehr!");
                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        event.getWhoClicked().closeInventory();
                        return;
                    }

                    event.getWhoClicked().teleport(playerTeleport);
                    event.getWhoClicked().sendMessage(BedWars.PREFIX + "§7Du hast dich zu §e" + playerTeleport.getName() + " §7teleportiert!");
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_WOOD_PLACE, 1f, 1f);
                    event.getWhoClicked().closeInventory();

                }

            } else {
                if (event.getView().getTitle().startsWith("Shop")) {

                    if (event.getCurrentItem() == null) return;
                    if (event.getAction() == null) return;

                    event.setCancelled(true);
                    if (event.getSlot() == 0) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("BLOCK").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 1) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("WEAPON").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 2) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("TOOLS").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 3) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("DISTANCE").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 4) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("ARMOR").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 5) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("CHEST").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 6) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("POTION").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 7) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("ENCHANTMENT").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() == 8) {
                        BedWars.getInstance().getBedWarsUtils().getItemStoreManager().getItemStore("EFFECT").openInventory((Player) event.getWhoClicked());
                    } else if (event.getSlot() >= 18) {

                        ItemStack itemStack = event.getCurrentItem();

                        ItemInfo itemInfo = new Gson().fromJson(itemStack.getItemMeta().getLocalizedName(), ItemInfo.class);

                        if (itemStack == null) return;
                        if (itemInfo == null) return;

                        Integer integer = InventoryUtil.getAmount((Player) event.getWhoClicked(), Material.BRICK);
                        Bukkit.broadcastMessage(String.valueOf(integer));

                        if (!(event.getWhoClicked().getInventory().contains(itemInfo.getMaterial(), itemInfo.getPrice()))) {
                            event.getWhoClicked().sendMessage(BedWars.PREFIX + "§cDu hast nicht genügend Resourcen, um dieses Item zu kaufen!");
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            return;
                        }

                        if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {

                            if (!(player.getInventory().contains(itemInfo.getMaterial(), itemInfo.getPrice() * InventoryUtil.getAmount(player, itemInfo.getMaterial())))) {
                                event.getWhoClicked().sendMessage(BedWars.PREFIX + "§cDu hast nicht genügend Resourcen, um dieses Item zu kaufen!");
                                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                                return;
                            }

                            event.getWhoClicked().getInventory().addItem(new ItemStack(new ItemBuilder(event.getCurrentItem().getType(), event.getCurrentItem().getAmount() * InventoryUtil.getAmount(player, itemInfo.getMaterial())).setDisplayName(event.getCurrentItem().getItemMeta().getDisplayName()).build()));
                            InventoryUtil.remove(event.getWhoClicked().getInventory(), new ItemStack(itemInfo.getMaterial()), itemInfo.getPrice() * InventoryUtil.getAmount(player, itemInfo.getMaterial()));
                            event.getWhoClicked().sendMessage(BedWars.PREFIX + "§7Du hast §e" + event.getCurrentItem().getAmount() * InventoryUtil.getAmount(player, itemInfo.getMaterial()) + "x " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7gekauft.");
                        } else {
                            event.getWhoClicked().getInventory().addItem(new ItemStack(new ItemBuilder(event.getCurrentItem().getType(), event.getCurrentItem().getAmount()).setDisplayName(event.getCurrentItem().getItemMeta().getDisplayName()).build()));
                            InventoryUtil.remove(event.getWhoClicked().getInventory(), new ItemStack(itemInfo.getMaterial()), itemInfo.getPrice());
                            event.getWhoClicked().sendMessage(BedWars.PREFIX + "§7Du hast §e" + event.getCurrentItem().getAmount() + "x " + event.getCurrentItem().getItemMeta().getDisplayName() + " §7gekauft.");
                        }

                    }
                }
            }


        };

        this.plugin.getEventManager().registerEvent(InventoryClickEvent.class, inventoryClickEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(InventoryClickEvent.class, inventoryClickEvent);

    }


}

