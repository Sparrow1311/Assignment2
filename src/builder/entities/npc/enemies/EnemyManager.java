package builder.entities.npc.enemies;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.entities.npc.spawners.Spawner;
import builder.player.Player;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * The EnemyManager class is responsible for creating, updating, and maintaining
 * all enemy entities in the game. It coordinates enemy spawning through {@link Spawner}
 * objects, handles cleanup of defeated enemies, and ensures that all active enemies
 * are updated and rendered each tick.
 */
public class EnemyManager implements Tickable, RenderableGroup, Interactable {

    private final ArrayList<Spawner> spawnersList = new ArrayList<>();
    private final ArrayList<Enemy> enemiesList = new ArrayList<>();
    private int spawnX;
    private int spawnY;

    /**
     * Constructs a new EnemyManager instance with the given display dimensions.
     *
     * @param dimensions the dimensions of the game screen.
     */
    public EnemyManager(Dimensions dimensions) {}

    /**
     * Retrieves the x-coordinate of this spawner.
     *
     * @return the current x position.
     */
    public int getX() {
        return spawnX;
    }

    /**
     * Retrieves the y-coordinate of this spawner.
     *
     * @return the current y position.
     */
    public int getY() {
        return spawnY;
    }

    /**
     * Sets the x-coordinate of the spawner.
     *
     * @param newX intended new x-coordinate.
     */
    public void setX(int newX) {
        spawnX = newX;
    }

    /**
     * Sets the y-coordinate of the spawner.
     *
     * @param newY intended new y-coordinate.
     */
    public void setY(int newY) {
        spawnY = newY;
    }

    /**
     * Removes any enemies that have been marked for removal from the active list.
     */
    public void cleanUp() {
        this.enemiesList.removeIf(Enemy::isMarkedForRemoval);
    }

    /**
     * Retrieves the list of spawners field.
     *
     * @return spawners
     */
    public ArrayList<Spawner> getSpawner() {
        return this.spawnersList;
    }

    /**
     * Adds a new enemy spawner to the manager.
     *
     * @param spawner the spawner to add.
     */
    public void addSpawner(Spawner spawner) {
        this.getSpawner().add(spawner);
    }

    /**
     * Adds a newly created enemy to the active enemy list.
     *
     * @param enemy the enemy to add.
     */
    public void addEnemy(Enemy enemy) {
        this.enemiesList.add(enemy);
    }

    /**
     * Creates and registers a new {@link Magpie} enemy.
     *
     * @param player the player used to determine the magpie’s target or behavior.
     * @return the newly created {@link Magpie} instance.
     */
    public Magpie createMagpie(Player player) {
        final Magpie magpie = new Magpie(this.spawnX, this.spawnY, player);
        this.enemiesList.add(magpie);
        return magpie;
    }

    /**
     * Creates and registers a new {@link Pigeon} enemy.
     *
     * @param position the position near which the pigeon will be spawned.
     * @return the newly created {@link Pigeon} instance.
     */
    public Pigeon createPigeon(HasPosition position) {
        final Pigeon pigeon = new Pigeon(this.spawnX, this.spawnY, position);
        this.enemiesList.add(pigeon);
        return pigeon;
    }

    /**
     * Creates and registers a new {@link Eagle} enemy.
     *
     * @param player the player associated with this eagle’s targeting behavior.
     * @return the newly created {@link Eagle} instance.
     */
    public Eagle createEagle(Player player) {
        return new Eagle(this.spawnX, this.spawnY, player);
    }

    /**
     * Updates all spawners and active enemies once per game tick.
     *
     * @param state the engine state (keyboard, mouse, and other runtime data).
     * @param game  the current game state containing world and player information.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanUp();

        for (Spawner spawner : this.getSpawner()) {
            spawner.tick(state, game);
        }

        for (Enemy enemy : enemiesList) {
            enemy.tick(state, game);
        }
    }

    /**
     * Returns all active enemies currently being managed.
     *
     * @return a list of all active {@link Enemy} objects.
     */
    public ArrayList<Enemy> getAllEnemies() {
        return this.enemiesList;
    }

    /**
     * Provides all active enemies as renderable objects for the game’s renderer.
     *
     * @return a list of all {@link Renderable} enemies.
     */
    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.enemiesList);
    }

    @Override
    public void interact(EngineState engine, GameState game){}
}
