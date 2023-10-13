package net.unestia.bedwars.phase.lobby.listener;


import net.unestia.bedwars.BedWars;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.function.Consumer;

public class LobbyFoodListener {

    private final BedWars plugin;

    Consumer<FoodLevelChangeEvent> foodLevelChangeEvent;

    public LobbyFoodListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        foodLevelChangeEvent = (FoodLevelChangeEvent event) -> {
            event.setCancelled(true);
        };

        this.plugin.getEventManager().registerEvent(FoodLevelChangeEvent.class, foodLevelChangeEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(FoodLevelChangeEvent.class, foodLevelChangeEvent);

    }

}
