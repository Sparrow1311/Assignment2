package builder.entities.npc.spawners;

import builder.GameState;

import engine.EngineState;
import engine.timing.RepeatingTimer;

/**
 * Spawns {@link builder.entities.npc.enemies.Eagle} enemies on a set interval.
 *
 * <p>The eagle spawner activates once its internal timer expires, placing
 * a new eagle near its assigned position and targeting the player.
 */
public class EagleSpawner extends Spawner {

    /**
     * Creates a new eagle spawner with the given position and spawn interval.
     *
     * @param x the horizontal spawn position
     * @param y the vertical spawn position
     * @param duration the timer duration, in ticks, between spawns
     */
    public EagleSpawner(int x, int y, int duration) {
        super(x, y, new RepeatingTimer(duration));
    }

    /**
     * Updates the spawner each frame, checking whether it is time to spawn a new eagle.
     *
     * @param state the current engine state
     * @param game the active game context containing enemies and player
     */
    @Override
    public void tick(EngineState state, GameState game) {
        tickTimer();
        if (isTimerFinished()) {
            setSpawnPosition(game);
            game.getEnemies().addEnemy(game.getEnemies().createEagle(game.getPlayer()));
        }
    }

}
