package net.unestia.bedwars.phase.ingame.listener;

import net.unestia.bedwars.BedWars;
import net.unestia.bedwars.team.TeamEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.function.Consumer;

public class IngameEntityListener {

    private final BedWars plugin;

    Consumer<EntityDamageByBlockEvent> entityDamageByBlockEvent;
    Consumer<EntityDamageByEntityEvent> entityDamageByEntityEvent;
    Consumer<ProjectileHitEvent> projectileHitEvent;
    Consumer<EntityDamageEvent> entityDamageEvent;

    public IngameEntityListener(BedWars plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {

        entityDamageByBlockEvent = (EntityDamageByBlockEvent event) -> {
        };

        entityDamageByEntityEvent = (EntityDamageByEntityEvent event) -> {

            if (!this.plugin.getPlayers().contains(event.getEntity().getUniqueId())) event.setCancelled(true);
            if (event.getEntity().getType().equals(EntityType.VILLAGER)) event.setCancelled(true);

            Player target = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            try {
                if (this.plugin.getPlayers().contains(target) && this.plugin.getPlayers().contains(damager)) {
                    if (event.getEntity().getType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {

                        TeamEntity targetTeam = this.plugin.getTeamUtils().getTeam(target);
                        TeamEntity damagerTeam = this.plugin.getTeamUtils().getTeam(damager);

                        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                            if (damagerTeam.getPlayers().contains(target)) {
                                event.setCancelled(true);
                            }
                        } else if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                            if (damagerTeam.getPlayers().contains(target)) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        };

        projectileHitEvent = (ProjectileHitEvent event) -> {
            Player target = (Player) event.getHitEntity();
            Player damager = (Player) event.getEntity().getShooter();

            try {
                if (this.plugin.getPlayers().contains(target) && this.plugin.getPlayers().contains(damager)) {
                    TeamEntity targetTeam = this.plugin.getTeamUtils().getTeam(target);
                    TeamEntity damagerTeam = this.plugin.getTeamUtils().getTeam(damager);

                    if (damagerTeam.getPlayers().contains(target)) {
                        event.setCancelled(true);
                    }

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        };

        entityDamageEvent = (EntityDamageEvent event) -> {


        };

        this.plugin.getEventManager().registerEvent(EntityDamageByBlockEvent.class, entityDamageByBlockEvent);
        this.plugin.getEventManager().registerEvent(EntityDamageByEntityEvent.class, entityDamageByEntityEvent);
        this.plugin.getEventManager().registerEvent(ProjectileHitEvent.class, projectileHitEvent);
        this.plugin.getEventManager().registerEvent(EntityDamageEvent.class, entityDamageEvent);

    }

    public void unregisterEvents() {

        this.plugin.getEventManager().unregisterEvent(EntityDamageByBlockEvent.class, entityDamageByBlockEvent);
        this.plugin.getEventManager().unregisterEvent(EntityDamageByEntityEvent.class, entityDamageByEntityEvent);
        this.plugin.getEventManager().unregisterEvent(ProjectileHitEvent.class, projectileHitEvent);
        this.plugin.getEventManager().unregisterEvent(EntityDamageEvent.class, entityDamageEvent);

    }

}
