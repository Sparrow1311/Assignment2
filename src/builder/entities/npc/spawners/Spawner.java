package builder.entities.npc.spawners;

import builder.GameState;
import builder.Tickable;
import engine.EngineState;
import engine.game.HasPosition;
import engine.timing.TickTimer;

/**
 * The {@code Spawner} class provides a common base for all enemy spawners.
 * <p>
 * Each subclass defines its own conditions for spawning different enemy types.
 * A spawner maintains a spawn position and an internal timer that controls
 * when a new enemy should appear in the game.
 * </p>
 */
public abstract class Spawner implements HasPosition, Tickable {

    private int spawnX;
    private int spawnY;
    private final TickTimer timer;

    /**
     * Creates a new {@code Spawner} with an associated {@link TickTimer}.
     *
     * @param timer the timer that manages how frequently the spawner is triggered.
     */
    public Spawner(int x, int y, TickTimer timer) {
        this.spawnX = x;
        this.spawnY = y;
        this.timer = timer;
    }

    /**
     * Retrieves the x-coordinate of this spawner.
     *
     * @return the current x position.
     */
    public int getX() {
        return spawnX;
    }

    /**
     * Sets the x-coordinate of the spawner.
     *
     * @param newX intended new x-coordinate.
     */
    public void setX(int newX) {
        this.spawnX = newX;
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
     * Sets the y-coordinate of the spawner.
     *
     * @param newY intended new y-coordinate.
     */
    public void setY(int newY) {
        this.spawnY = newY;
    }

    /**
     * Retrieves the internal {@link TickTimer} used to control spawning.
     *
     * @return the timer associated with this spawner.
     */
    private TickTimer getTimer() {
        return timer;
    }

    /**
     * Helper method for repeated logic, sets the spawn position for enemies based on the current
     * {@link GameState}.
     *
     * @param game the current {@link GameState} of the game world.
     */
    protected void setSpawnPosition(GameState game) {
        game.getEnemies().setX(this.getX());
        game.getEnemies().setY(this.getY());
    }

    /**
     * Updates the timer by one tick.
     * <p>
     * Checks timer is not null.
     * </p>
     */
    protected void tickTimer() {
        if (timer != null) {
            timer.tick();
        }
    }

    protected boolean isTimerFinished() {
        return timer != null && getTimer().isFinished();
    }

    /**
     * Updates this spawner each tick.
     * <p>
     * Each subclass must implement its own logic for when and how to spawn
     * entities, depending on the timer and game state.
     * </p>
     *
     * @param state the current {@link EngineState}.
     * @param game  the current {@link GameState}.
     */
    public abstract void tick(EngineState state, GameState game);


}
