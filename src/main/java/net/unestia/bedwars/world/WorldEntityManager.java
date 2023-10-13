package net.unestia.bedwars.world;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import net.unestia.bedwars.BedWars;
import org.bukkit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldEntityManager {

    private final Gson gson = new Gson();
    private final Map<String, WorldEntity> worlds = new HashMap();

    public WorldEntityManager(BedWars plugin) {
        Arrays.asList(Objects.requireNonNull(plugin.getServer().getWorldContainer().listFiles())).forEach((world) -> {
            if (world.isDirectory()) {
                File file = new File(world + "/config.json");
                if (file.exists()) {
                    World bukkitWorld = Bukkit.createWorld(new WorldCreator(world.getName()));
                    Objects.requireNonNull(bukkitWorld).setAutoSave(false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_MOB_LOOT, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_MOB_SPAWNING, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_TRADER_SPAWNING, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
                    Objects.requireNonNull(bukkitWorld).setGameRule(GameRule.SPAWN_RADIUS, 0);
                    Objects.requireNonNull(bukkitWorld).setTime(6000L);

                    try {
                        JsonObject jsonObject = this.gson.fromJson(new FileReader(file), JsonObject.class);
                        Map<String, Location> locations = new HashMap<>();
                        Map<String, LinkedTreeMap<String, Object>> objects = this.gson.fromJson(this.gson.toJson(jsonObject.get("locations")), Map.class);
                        objects.forEach((name, location) -> {
                            if (location.containsKey("yaw") && location.containsKey("pitch")) {
                                locations.put(name, new Location(Bukkit.getWorld((String)location.get("world")), (Double)location.get("x"), (Double)location.get("y"), (Double)location.get("z"), BigDecimal.valueOf((Double)location.get("yaw")).floatValue(), BigDecimal.valueOf((Double)location.get("pitch")).floatValue()));
                            } else {
                                locations.put(name, new Location(Bukkit.getWorld((String)location.get("world")), (Double)location.get("x"), (Double)location.get("y"), (Double)location.get("z")));
                            }

                        });
                        this.worlds.put(world.getName(), new WorldEntity(jsonObject.get("name").getAsString(), this.gson.fromJson(jsonObject.get("builder"), String[].class), locations));
                    } catch (FileNotFoundException var7) {
                        Logger.getLogger(WorldEntityManager.class.getName()).log(Level.SEVERE, null, var7);
                    }

                }
            }
        });
    }

    public WorldEntity getWorld(String name) {
        return this.worlds.get(name);
    }

    public List<WorldEntity> getWorlds() {
        return new ArrayList(this.worlds.values());
    }

}
