package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Expirable;
import builder.player.Player;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;
import engine.timing.RepeatingTimer;

/**
 * A Magpie enemy that steals coins from the player.
 * <p>
 * The Magpie initially attacks the player to steal a single coin. After stealing,
 * it retreats to its spawn position. Once it reaches its spawn location or its
 * lifespan expires, it is removed from the game.
 * </p>
 * <p>
 * This class extends {@link Bird} and inherits shared functionality such as
 * movement, targeting, lifespan ticking, and attacking state logic.
 * </p>
 */
public class Magpie extends Bird {

    private int coins = 0;

    /**
     * Constructs a new Magpie at the given coordinates targeting an initial position with a given lifespan,
     * SpriteGroup and sprite.
     *
     * @param x             initial X-coordinate of the Magpie.
     * @param y             initial Y-coordinate of the Magpie.
     * @param trackedTarget initial target the Magpie should pursue (typically the player).
     *
     * Magpie instances are constructed with a lifespan of 10000, SpriteGallery.magpie and sprite set "down".
     */
    public Magpie(int x, int y, HasPosition trackedTarget) {
        super(x, y, trackedTarget, 10000, SpriteGallery.magpie, "down");
        this.setSpeed(1);
    }

    /**
     * Handles the logic for stealing a coin from the player.
     * <p>
     * If the Magpie is attacking and close enough to the player, one coin is removed
     * from the player's inventory, added to this Magpie, and the Magpie switches to
     * a retreating state. After stealing, it flies back to its spawn.
     * </p>
     * <p>
     * Once the Magpie reaches its spawn point, it is marked for removal.
     * If it is removed while still attacking, the stolen coins are returned to
     * the player's inventory.
     * </p>
     *
     * @param engine the current {@link EngineState}, used for tile size and movement.
     * @param game   the current {@link GameState}, providing access to the player and inventory.
     */
    public void stealCoin(EngineState engine, GameState game) {
        final boolean hasHitPlayer =
                this.distanceFrom(game.getPlayer().getX(),
                        game.getPlayer().getY()) < engine.getDimensions().tileSize();
        if (hasHitPlayer && game.getInventory().getCoins() > 0 && isAttacking()) {
            game.getInventory().addCoins(-1);
            this.coins += 1;
            setAttacking(false);
            this.setSpeed(2);
        }
        if (!isAttacking()) {
            if (this.distanceFrom(getSpawnX(), getSpawnY()) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
        }
        if (this.isMarkedForRemoval() && isAttacking()) {
            game.getInventory().addCoins(this.coins);
        }
    }

    /**
     * Updates the Magpie's state each tick.
     * <p>
     * This includes handling its lifespan, movement and coin stealing behaviour
     * </p>
     *
     * @param engine the current {@link EngineState} for world and movement information.
     * @param game   the current {@link GameState}, including player and inventory data.
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
        stealCoin(engine, game);
    }
}
