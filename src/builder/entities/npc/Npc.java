package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;

import engine.EngineState;
import engine.game.Entity;
import engine.game.HasPosition;

/**
 * The {@code Npc} class represents a basic non-player character within the game world.
 * <p>
 * NPCs maintain a position, movement direction, and speed. They can interact with the game
 * environment and move each tick based on their direction and speed.
 * </p>
 */
public class Npc extends Entity implements Interactable, Tickable, Directable {

    private int direction = 0;
    private double speed = 1;

    /**
     * Constructs a new {@link Npc} instance at the specified coordinates.
     *
     * @param x the initial x-coordinate.
     * @param y the initial y-coordinate.
     */
    public Npc(int x, int y) {
        super(x, y);
    }

    /**
     * Retrieves the current movement speed of this NPC.
     *
     * @return the movement speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the movement speed of this NPC.
     *
     * @param speed the new movement speed.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }


    /**
     * Retrieves the direction of this NPC.
     *
     * @return the current direction in degrees.
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Sets the facing direction of this NPC.
     *
     * @param direction the new direction in degrees.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Moves this NPC by adjusting its X and Y coordinates according to
     * its current direction and speed.
     */
    public void move() {
        final int deltaX = (int) Math.round(Math.cos(Math.toRadians(getDirection())) * getSpeed());
        final int deltaY = (int) Math.round(Math.sin(Math.toRadians(getDirection())) * getSpeed());
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    /**
     * Updates this NPC once per tick.
     * <p>
     * Called each frame to move the NPC based on its speed and direction.
     * </p>
     *
     * @param state the current engine state.
     */
    @Override
    public void tick(EngineState state) {
        this.move();
    }

    /**
     * Updates this NPC once per tick, providing access to both the engine
     * and game state. Different signature from tick
     *
     * @param state the current engine state.
     * @param game  the current game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        this.move();
    }

    @Override
    public void interact(EngineState state, GameState game) {}

    /**
     * Return how far away this npc is from the given position
     *
     * @param position the position we are measuring to from this npcs position!
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(HasPosition position) {
        int deltaX = position.getX() - this.getX();
        int deltaY = position.getY() - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Return how far away this npc is from the given position
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return integer representation for how far apart they are
     */
    public int distanceFrom(int x, int y) {
        int deltaX = x - this.getX();
        int deltaY = y - this.getY();
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
