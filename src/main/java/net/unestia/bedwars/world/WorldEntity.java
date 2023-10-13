package net.unestia.bedwars.world;

import org.bukkit.Location;

import java.util.Map;

public class WorldEntity {

    private final String name;
    private final String[] builder;
    private final Map<String, Location> locations;

    public WorldEntity(String name, String[] builder, Map<String, Location> locations) {
        this.name = name;
        this.builder = builder;
        this.locations = locations;
    }

    public String getName() {
        return this.name;
    }

    public String[] getBuilder() {
        return this.builder;
    }

    public Map<String, Location> getLocations() {
        return this.locations;
    }

}
