package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;

import java.util.ArrayList;

/**
 * Represents a scarecrow entity that repels nearby birds such as magpies and pigeons.
 * <p>
 * Scarecrows can be placed on tilled dirt tiles for a cost of {@link #COIN_COST} coins.
 * When a magpie or pigeon comes within a 4-tile radius, the scarecrow causes them to
 * retreat to their spawn point by setting their attacking state to false.
 * </p>
 */
public class Scarecrow extends Npc {

    public static final int COIN_COST = 2;
    private static final SpriteGroup art = SpriteGallery.scarecrow;

    /**
     * Constructs a new {@link Scarecrow} entity at the specified coordinates.
     *
     * @param x the x-coordinate of the scarecrow's position.
     * @param y the y-coordinate of the scarecrow's position.
     */
    public Scarecrow(int x, int y) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.setSpeed(0);
    }

    /**
     * Compares if an enemy is an instance of Magpie or Pigeon and whether the enemy is within scare
     * radius
     *
     * @param enemy       the enemy to check
     * @param scareRadius the scare radius in pixels
     * @return true if within range and correct enemy type, false otherwise
     */
    private boolean isBirdWithinRadius(Enemy enemy, int scareRadius) {
        return (enemy instanceof Magpie || enemy instanceof Pigeon)
                && this.distanceFrom(enemy) < scareRadius;
    }

    /**
     * Stops the enemy from attacking if it is a bird affected by the scarecrow.
     *
     * @param enemy the enemy to disable attack for
     */
    private void disableAttack(Enemy enemy) {
        if (enemy instanceof Magpie magpie) {
            magpie.setAttacking(false);
        } else if (enemy instanceof Pigeon pigeon) {
            pigeon.setAttacking(false);
        }
    }

    /**
     * Interacts with nearby enemies, repelling birds within a 4-tile radius.
     * <p>
     * This method searches the world for magpies and pigeons. If any are within
     * the scare radius, their attacking behaviour is disabled to simulate being scared.
     * </p>
     *
     * @param state the current engine state.
     * @param game  the current game state used to access enemies.
     */
    @Override
    public void interact(EngineState state, GameState game) {
        super.interact(state, game);

        final EnemyManager enemies = game.getEnemies();
        final int scareRadius = state.getDimensions().tileSize() * 4;

        for (Enemy enemy : enemies.getAllEnemies()) {
            if (isBirdWithinRadius(enemy, scareRadius)) {
                disableAttack(enemy);
            }
        }
    }
}
