package builder.entities.npc.spawners;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;
import engine.timing.RepeatingTimer;

import java.util.List;


/**
 * Spawns {@link builder.entities.npc.enemies.Pigeon} enemies on a set interval once
 * the world contains cabbage tile.
 *
 * <p>The pigeon spawner searches for the nearest cabbage tile and releases
 * a pigeon toward it once its internal timer expires.
 */
public class PigeonSpawner extends Spawner {

    /**
     * Creates a new pigeon spawner with the given position and spawn interval.
     *
     * @param x the horizontal spawn position
     * @param y the vertical spawn position
     * @param duration the timer duration, in ticks, between spawns
     */
    public PigeonSpawner(int x, int y, int duration) {
        super(x, y, new RepeatingTimer(duration));
    }


    /**
     * Determines whether a given tile contains a cabbage.
     *
     * @param tile the tile to check
     * @return {@code true} if a cabbage entity exists on this tile, otherwise {@code false}
     */
    private static boolean hasCabbage(Tile tile) {
        for (Entity entity : tile.getStackedEntities()) {
            if (entity instanceof Cabbage) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the distance between this spawner and a given position.
     *
     * @param position the position to measure distance from
     * @return the integer distance between this spawner and the position
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Updates the spawner each frame, checking for cabbages and spawning pigeons.
     *
     * @param state the current engine state
     * @param game the active game context containing world and enemy references
     */
    @Override
    public void tick(EngineState state, GameState game) {
        tickTimer();
        List<Tile> tiles = game.getWorld().tileSelector(PigeonSpawner::hasCabbage);

        if (!tiles.isEmpty()) {
            int distance = this.distanceFrom(tiles.getFirst());
            Tile closest = tiles.getFirst();
            for (Tile tile : tiles) {
                if (this.distanceFrom(tile) < distance) {
                    closest = tile;
                }
            }

            if (isTimerFinished()) {
                setSpawnPosition(game);
                game.getEnemies().addEnemy(game.getEnemies().createPigeon(closest));
            }
        }
    }


}
