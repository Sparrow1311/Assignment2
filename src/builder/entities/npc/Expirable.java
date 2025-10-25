package builder.entities.npc;

import engine.timing.FixedTimer;

/**
 * Represents an entity that has a finite lifespan within the game.
 *
 * <p>Implementing this interface allows the object to expire automatically
 * after a defined period, managed through a {@link FixedTimer} instance.
 */
public interface Expirable {
    /**
     * Sets the lifespan timer that determines how long this entity will exist.
     *
     * @param lifespan the {@link FixedTimer} controlling this entity’s lifetime
     */
    public void setLifespan(FixedTimer lifespan);

    /**
     * Retrieves the current lifespan timer associated with this entity.
     *
     * @return the {@link FixedTimer} tracking this entity’s remaining lifetime
     */
    public FixedTimer getLifespan();
}
