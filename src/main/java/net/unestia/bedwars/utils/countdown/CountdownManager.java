package net.unestia.bedwars.utils.countdown;

import lombok.Getter;
import net.unestia.bedwars.BedWars;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CountdownManager {

    private final BedWars plugin;

    @Getter
    private final Map<String, Countdown> countdowns;

    public CountdownManager(BedWars plugin) {
        this.plugin = plugin;

        this.countdowns = new HashMap<>();
    }

    public void createCountdown(Countdown countdown) {
        this.countdowns.put(countdown.getName(), countdown);
    }

    public Countdown getCountdown(String name) {
        if (this.countdowns.containsKey(name)) return this.countdowns.get(name);
        return null;
    }

}
