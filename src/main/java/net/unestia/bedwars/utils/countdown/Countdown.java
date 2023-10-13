package net.unestia.bedwars.utils.countdown;

import lombok.Getter;
import lombok.Setter;
import net.unestia.bedwars.utils.phase.GamePhase;
import org.bukkit.entity.Player;

@Getter
@Setter
public abstract class Countdown {

    public final String name;

    public final GamePhase gamePhase;

    public Integer countdown;
    public Boolean started;

    protected Countdown(String name, GamePhase gamePhase, Integer countdown, Boolean started) {
        this.name = name;

        this.gamePhase = gamePhase;

        this.countdown = countdown;
        this.started = started;
    }

    public abstract void startCountdown();

    public abstract void stopCountdown();

    public abstract void forceStart(Player player);

}
