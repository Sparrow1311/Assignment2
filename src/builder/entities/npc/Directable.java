package builder.entities.npc;

/**
 * Represents an entity capable of having and changing a direction.
 *
 * <p>This interface is typically used for movable entities that must
 * store a direction in degrees and respond to movement updates
 * based on that direction.
 */
public interface Directable {

    /**
     * Returns the current direction of this entity, in degrees.
     *
     * @return the current direction.
     */
    public int getDirection();

    /**
     * Sets the direction of this entity, in degrees.
     *
     * @param direction the new direction angle to face in degrees.
     */
    public void setDirection(int direction);

    /**
     * Moves this entity according to its current direction and speed.
     */
    public void move();
}
