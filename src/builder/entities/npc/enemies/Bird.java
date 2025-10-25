package builder.entities.npc.enemies;

import builder.entities.npc.Expirable;
import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * Represents a general bird-type enemy in the game.
 * <p>
 * This abstract class encapsulates shared behaviour and state across all birds
 * (e.g., Pigeon, Magpie, Eagle), such as lifespan, spawning position, target
 * tracking, sprite handling, and movement logic.
 * </p>
 *
 * <p>Subclasses must implement the <code>updateSprite</code> method to define
 * how their visual appearance changes based on direction or state.</p>
 */

public abstract class Bird extends Enemy implements Expirable {
    private final int spawnX;
    private final int spawnY;
    private HasPosition trackedTarget;
    private boolean attacking = true;
    private FixedTimer lifespan;
    private SpriteGroup art;

    /**
     * Constructs a new Bird instance at the given coordinates with a target, lifespan and set SpriteGroup.
     *
     * @param x              initial X-coordinate.
     * @param y              initial Y-coordinate.
     * @param trackedTarget  initial target the bird should pursue.
     * @param lifespanDuration lifespan in milliseconds before the bird expires.
     * @param artGroup       sprite group defining this bird's image assets.
     * @param sprite  the starting sprite key (e.g., "default", "down").
     */
    public Bird(int x, int y, HasPosition trackedTarget, int lifespanDuration, SpriteGroup artGroup,
                String sprite) {
        super(x, y);
        this.spawnX = x;
        this.spawnY = y;
        this.trackedTarget = trackedTarget;
        this.setLifespan(new FixedTimer(lifespanDuration));
        this.art = artGroup;
        this.setSprite(art.getSprite(sprite));
        setAttacking(true);

    }

    /**
     * Returns the X-coordinate of the bird's original spawn position.
     *
     * @return the spawn X position
     */
    public int getSpawnX() {
        return spawnX;
    }

    /**
     * Returns the Y-coordinate of the bird's original spawn position.
     *
     * @return the spawn Y position
     */
    public int getSpawnY() {
        return spawnY;
    }

    /**
     * Returns the current target this bird is pursuing.
     *
     * @return the tracked target, or {@code null} if none
     */
    public HasPosition getTrackedTarget() {
        return trackedTarget;
    }

    /**
     * Sets a new target for this bird to pursue.
     *
     * @param trackedTarget the new target position
     */
    public void setTrackedTarget(HasPosition trackedTarget) {
        this.trackedTarget = trackedTarget;
    }

    /**
     * Returns the sprite group defining this bird’s visual assets.
     *
     * @return the associated {@link SpriteGroup}
     */
    public SpriteGroup getArt() {
        return art;
    }

    /**
     * Sets a new sprite group for this bird.
     *
     * @param artGroup the {@link SpriteGroup} to assign
     */
    public void setArt(SpriteGroup artGroup) {
        this.art = artGroup;
    }

    /**
     * Returns whether the bird is currently in attacking mode.
     *
     * @return true if attacking, false if retreating.
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Updates whether the bird is actively attacking or retreating.
     *
     * @param attackChange <code>true</code> if the bird is attacking,
     *                     <code>false</code> if it should return to its spawn point.
     */
    public void setAttacking(boolean attackChange) {
        this.attacking = attackChange;
    }

    /**
     * Updates the bird's lifespan timer and checks if it has expired every tick.
     * <p>
     * This method should be called once per game tick. It advances the internal
     * {@link FixedTimer} tracking the bird’s lifespan. If the timer has completed,
     * the bird is marked for removal from the game world.
     * </p>
     */
    public void lifeSpanTick() {
        this.getLifespan().tick();
        if (this.getLifespan().isFinished()) {
            this.markForRemoval();
        }
    }

    /**
     * Adjusts this bird's direction and sprite to pursue its tracked target.
     * <p>
     * This method is only executed while the bird is in an attacking state
     * and a valid {@link HasPosition} target exists.
     * </p>
     */
    protected void pursueTarget() {
        if (isAttacking() && getTrackedTarget() != null) {
            double dx = getTrackedTarget().getX() - this.getX();
            double dy = getTrackedTarget().getY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(dy, dx)));
            this.setSprite(getTrackedTarget().getY() > this.getY()
                    ? getArt().getSprite("down")
                    : getArt().getSprite("up"));
        }
    }

    /**
     * Sends the bird back toward its spawn location when not attacking.
     * <p>
     * This method is triggered when the bird is not attacking. If the bird is close enough to its
     * spawn point—within one tile—it is marked for removal from the game.
     * </p>
     *
     * @param engine the current {@link EngineState}, used to determine tile size and proximity.
     */
    protected void returnSpawn(EngineState engine) {
        if (!isAttacking()) {
            double dx = getSpawnX() - this.getX();
            double dy = getSpawnY() - this.getY();
            this.setDirection((int) Math.toDegrees(Math.atan2(dy, dx)));
            this.setSprite(getSpawnY() < this.getY()
                    ? getArt().getSprite("up")
                    : getArt().getSprite("down"));

            // If we're close enough to home, mark for removal
            if (this.distanceFrom(getSpawnX(), getSpawnX()) < engine.getDimensions().tileSize()) {
                this.markForRemoval();
            }
        }
    }

    /**
     * Gets the lifespan timer for this bird.
     *
     * @return the current FixedTimer representing lifespan.
     */
    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    /**
     * Replaces the current lifespan timer.
     *
     * @param timer new FixedTimer to assign.
     */
    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

}
