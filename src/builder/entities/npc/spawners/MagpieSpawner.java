package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;

/**
 * Spawns {@link builder.entities.npc.enemies.Magpie} enemies on a set interval.
 *
 * <p>The magpie spawner activates once its internal timer expires, placing
 * a new magpie near its assigned position and targeting the player.
 */
public class MagpieSpawner extends Spawner {

    /**
     * Creates a new magpie spawner with the given position and spawn interval.
     *
     * @param x the horizontal spawn position
     * @param y the vertical spawn position
     * @param duration the timer duration, in ticks, between spawns
     */
    public MagpieSpawner(int x, int y, int duration) {
        super(x, y, new RepeatingTimer(duration));
    }

    /**
     * Updates the spawner each frame, checking whether it is time to spawn a new magpie.
     *
     * @param state the current engine state
     * @param game the active game context containing enemies and player
     */
    @Override
    public void tick(EngineState state, GameState game) {
        tickTimer();
        if (isTimerFinished()) {
            setSpawnPosition(game);
            game.getEnemies().addEnemy(game.getEnemies().createMagpie(game.getPlayer()));
        }
    }

}
