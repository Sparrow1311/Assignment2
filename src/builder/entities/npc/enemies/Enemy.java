package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.npc.Npc;

import engine.EngineState;

/**
 * Represents a non-player character (NPC) that acts as a hostile enemy.
 *
 * <p> Specific enemy types inherit from this class
 * to share common behaviour and interaction logic.
 */
public class Enemy extends Npc {

    /**
     * Constructs a new enemy instance at the specified coordinates.
     *
     * @param x the initial horizontal position of the enemy
     * @param y the initial vertical position of the enemy
     */
    public Enemy(int x, int y) {
        super(x, y);
    }

    /**
     * Updates the enemy’s state for one frame of the game loop.
     *
     * @param state the current engine state containing input and timing data
     * @param game  the current game state providing world and entity context
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }
}
