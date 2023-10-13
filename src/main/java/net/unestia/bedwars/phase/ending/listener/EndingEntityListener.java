package net.unestia.bedwars.phase.ending.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.function.Consumer;

public class EndingEntityListener {

    private final BedWars plugin;

    Consumer<EntityDamageByBlockEvent> entityDamageByBlockEvent;
    Consumer<EntityDamageByEntityEvent> entityDamageByEntityEvent;
    Consumer<EntityDamageEvent> entityDamageEvent;

    public EndingEntityListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        entityDamageByBlockEvent = (EntityDamageByBlockEvent event) -> {
            event.setCancelled(true);
        };

        entityDamageByEntityEvent = (EntityDamageByEntityEvent event) -> {
            event.setCancelled(true);
        };

        entityDamageEvent = (EntityDamageEvent event) -> {
            event.setCancelled(true);
        };

        this.plugin.getEventManager().registerEvent(EntityDamageByBlockEvent.class, entityDamageByBlockEvent);
        this.plugin.getEventManager().registerEvent(EntityDamageByEntityEvent.class, entityDamageByEntityEvent);
        this.plugin.getEventManager().registerEvent(EntityDamageEvent.class, entityDamageEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(EntityDamageByBlockEvent.class, entityDamageByBlockEvent);
        this.plugin.getEventManager().unregisterEvent(EntityDamageByEntityEvent.class, entityDamageByEntityEvent);
        this.plugin.getEventManager().unregisterEvent(EntityDamageEvent.class, entityDamageEvent);

    }

}
