package builder.entities.npc.enemies;

import builder.GameState;
import builder.entities.resources.Cabbage;
import builder.entities.tiles.Tile;
import builder.ui.SpriteGallery;
import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;
import java.util.List;

/**
 * A Pigeon enemy that searches for and steals cabbages from the game world.
 * <p>
 * The Pigeon flies toward a target cabbage when attacking. If it successfully reaches
 * and steals a cabbage, it retreats toward its spawn location. Once it returns to its
 * spawn or its lifespan expires, it is removed from the game.
 * </p>
 * <p>
 * This class extends {@link Bird} and inherits shared functionality such as
 * movement, targeting, lifespan ticking, and attacking state logic.
 * </p>
 */
public class Pigeon extends Bird {

    /**
     * Creates a new Pigeon at the given coordinates targeting an initial position.
     *
     * @param x      initial X-coordinate of the pigeon.
     * @param y      initial Y-coordinate of the pigeon.
     * @param target initial target that the pigeon will pursue (e.g., a cabbage tile).
     *
     * Pigeon instances are constructed with a lifespan of 3000, SpriteGallery.pigeon and sprite set "default".
     */
    public Pigeon(int x, int y, HasPosition target) {
        super(x, y, target, 3000, SpriteGallery.pigeon, "default");
        this.setSpeed(1);
    }

    /**
     * Searches the game world for tiles that contain cabbages.
     *
     * @param game the current {@link GameState} used to access the game world.
     * @return a list of tiles that contain at least one {@link Cabbage} entity.
     */
    private List<Tile> findCabbageTiles(GameState game) {
        List<Tile> cabbageTiles = game.getWorld().tileSelector(tile -> {
            for (Entity entity : tile.getStackedEntities()) {
                if (entity instanceof Cabbage) {
                    return true;
                }
            }
            return false;
        });
        return cabbageTiles;
    }

    /**
     * Determines the closest tile in a given list relative to this pigeon.
     *
     * @param tiles the list of tiles to check.
     * @return closestTile the tile nearest to the pigeon.
     */
    private Tile findClosestTile(List<Tile> tiles) {
        Tile closestTile = tiles.getFirst();

        for (Tile tile : tiles) {
            if (this.distanceFrom(tile) < this.distanceFrom(closestTile)) {
                closestTile = tile;
            }
        }
        return closestTile;
    }


    /**
     * Attempts to steal a cabbage from the specified tile.
     * <p>
     * If the pigeon is attacking and within one tile size of the cabbage,
     * the cabbage is removed from the game and the pigeon switches to
     * retreating mode.
     * </p>
     *
     * @param tile   the tile containing the cabbage to steal.
     * @param engine the current {@link EngineState}, used to determine tile size.
     */
    private void stealCabbage(Tile tile, EngineState engine) {
        int tileSize = engine.getDimensions().tileSize();

        if (isAttacking() && this.distanceFrom(tile) < tileSize) {
            for (Entity entity : tile.getStackedEntities()) {
                if (entity instanceof Cabbage cabbage) {
                    cabbage.markForRemoval();
                    setAttacking(false);
                }
            }
        }
    }

    /**
     * Handles the process of locating, targeting, and stealing cabbages.
     * <p>
     * If no cabbages are found in the world, the pigeon stops attacking and returns to spawn.
     * If cabbages exist, the pigeon targets the closest one and attempts to steal it.
     * </p>
     *
     * @param engine the game engine state for movement and tile size information.
     * @param game   the current {@link GameState}, used to access world and entities.
     */
    private void cabbageSearchAndSteal(EngineState engine, GameState game) {
        List<Tile> cabbageTiles = findCabbageTiles(game);

        if (cabbageTiles.isEmpty()) {
            setAttacking(false);
            return;
        }

        Tile closestTile = findClosestTile(cabbageTiles);
        setTrackedTarget(closestTile);

        stealCabbage(closestTile, engine);
    }

    /**
     * Updates the pigeon each frame, handling movement, attacking/retreating logic,
     * cabbage interactions, and lifespan expiration.
     *
     * @param engine the current {@link EngineState} used for dimensions and timing.
     * @param game   the current {@link GameState} providing world and entity access.
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
        cabbageSearchAndSteal(engine, game);
    }
}
