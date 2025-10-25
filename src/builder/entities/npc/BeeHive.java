package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.timing.RepeatingTimer;

import java.util.ArrayList;

/**
 * Represents a stationary hive that periodically deploys defensive bees.
 *
 * <p>The hive monitors nearby enemies and releases {@link GuardBee} instances
 * when hostile entities approach within its detection range.
 * It consumes resources to operate and recharges over time between deployments.
 */
public class BeeHive extends Npc {

    public static final int DETECTION_DISTANCE = 350;
    public static final int TIMER = 240;
    public static final int FOOD_COST = 2;
    public static final int COIN_COST = 2;
    private static final SpriteGroup art = SpriteGallery.hive;
    private boolean loaded = true;
    private final RepeatingTimer timer = new RepeatingTimer(TIMER);

    /**
     * Creates a new bee hive positioned at the given coordinates.
     *
     * @param x the horizontal spawn position
     * @param y the vertical spawn position
     */
    public BeeHive(int x, int y) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.setSpeed(0);
    }

    /**
     * Updates the hive each frame of the game.
     *
     * <p>Progresses its internal timer and any other state-related behaviour.
     *
     * @param state the current engine state, including timing and input data
     * @param game the current game state containing entity references
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);
        this.timer.tick();
    }

    /**
     * Handles interaction behaviour for the hive.
     *
     * <p>Checks for nearby enemies and deploys bees when appropriate.
     * The hive recharges between deployments and will only spawn a new
     * bee once it is fully reloaded.
     *
     * @param state the current engine state
     * @param game the active game context containing NPCs and enemies
     */
    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);
        timer.tick();
        Npc npc = this.checkAndSpawnBee(game.getEnemies().getAllEnemies());
        if (npc != null) {
            game.getNpcs().addNpc(npc);
        }
        if (timer.isFinished()) {
            this.loaded = true;
        }
    }

    /**
     * Determines if a new bee should be deployed and creates one if conditions are met.
     *
     * <p>When an enemy enters the detection range and the hive is loaded,
     * a new {@link GuardBee} is spawned to pursue the intruder.
     *
     * @param targets a list of active enemies to evaluate
     * @return a new {@link GuardBee} instance if one is deployed, otherwise {@code null}
     */
    public Npc checkAndSpawnBee(ArrayList<Enemy> targets) {
        for (Enemy enemy : targets) {
            if (this.distanceFrom(enemy) < DETECTION_DISTANCE && this.loaded) {
                this.loaded = false;
                return new GuardBee(
                        this.getX(), this.getY(), enemy); // can only spawn one bee in a frame
            }
        }
        return null;
    }
}
