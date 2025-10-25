package builder.entities.npc.enemies;

import builder.GameState;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.game.HasPosition;

/**
 * An Eagle enemy in the game.
 *
 * <p>The Eagle tries to steal food from the player.
 * Once it has stolen food, it retreats to its spawn position. If it
 * reaches home or its lifespan ends, it is removed from the game.</p>
 *
 * <p>
 * This class extends {@link Bird} and inherits shared functionality such as
 * movement, targeting, lifespan ticking, and attacking state logic.
 * </p>
 */
public class Eagle extends Bird {
    private int food = 0;

    /**
     * Creates a new Eagle at the specified coordinate with an initial target.
     *
     * @param x      initial x-coordinate of the Eagle
     * @param y      initial y-coordinate of the Eagle
     * @param target the entity the Eagle will track
     *
     * Eagle instances are constructed with a lifespan of 5000, SpriteGallery.eagle and sprite set "default".
     */
    public Eagle(int x, int y, HasPosition target) {
        super(x, y, target, 5000, SpriteGallery.eagle, "default");
        this.setSpeed(2);
    }

    /**
     * Handles the Eagle's behaviour for stealing food from the player and deciding
     * whether to return home or be removed.
     *
     * <p>Behaviour flow:</p>
     * <ul>
     *   <li>If close to the player and still attacking → steal food and retreat.</li>
     *   <li>If returning and reaches spawn → remove Eagle from game.</li>
     *   <li>If removed before reaching spawn while still carrying food → return food to inventory.</li>
     * </ul>
     *
     * @param engine the engine state for proximity checks
     * @param game   the game state to modify inventory or removal
     */
    private void stealFood(EngineState engine, GameState game) {
        boolean hasHitPlayer =
                this.distanceFrom(game.getPlayer()) < engine.getDimensions().tileSize();

        if (hasHitPlayer && isAttacking()) {
            if (this.food == 0) {
                game.getInventory().addFood(-3);
                this.food = 3;
            }
            setAttacking(false);
            this.setSpeed(4);
        }

        if (!isAttacking() && this.distanceFrom(getSpawnX(), getSpawnY())
                < engine.getDimensions().tileSize()) {
            this.markForRemoval();
        }

        if (this.isMarkedForRemoval() && this.distanceFrom(getSpawnX(), getSpawnY())
                > engine.getDimensions().tileSize()) {
            game.getInventory().addFood(this.food);
        }
    }

    /**
     * Updates the Eagle every game tick handling lifespan, attack and movement and targeting behaviour.
     *
     * @param engine the engine state for world information
     * @param game   the complete game state for accessing the player and inventory
     */
    @Override
    public void tick(EngineState engine, GameState game) {
        super.tick(engine, game);
        lifeSpanTick();
        if (isAttacking()) {
            pursueTarget();
        } else {
            returnSpawn(engine);
        }

        move();
        stealFood(engine, game);
    }

}
