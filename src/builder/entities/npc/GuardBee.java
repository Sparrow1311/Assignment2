package builder.entities.npc;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;

import engine.EngineState;
import engine.art.sprites.SpriteGroup;
import engine.game.HasPosition;
import engine.timing.FixedTimer;

/**
 * A highly trained Guard Bee... don't think about that too much. This is our projectile class,
 * basically a bullet.
 */
public class GuardBee extends Npc implements Expirable {

    private final int spawnX;
    private final int spawnY;
    private static final double SPEED = 2;
    private static final SpriteGroup art = SpriteGallery.bee;
    private FixedTimer lifespan = new FixedTimer(300);
    private final HasPosition trackedTarget;

    /**
     * Creates a guard bee that pursues a target from the given spawn position.
     *
     * @param x horizontal spawning position
     * @param y vertical spawning position
     * @param trackedTarget target with a position we want this to track
     */
    public GuardBee(int x, int y, HasPosition trackedTarget) {
        super(x, y);
        this.setSprite(art.getSprite("default"));
        this.trackedTarget = trackedTarget;
        this.spawnX = x;
        this.spawnY = y;
    }

    @Override
    public double getSpeed(){
        return SPEED;
    }
    @Override
    public FixedTimer getLifespan() {
        return lifespan;
    }

    @Override
    public void setLifespan(FixedTimer timer) {
        this.lifespan = timer;
    }

    /**
     * Updates the bee’s visual orientation to match its current facing direction.
     */
    public void updateArtBasedOnDirection() {
        int directionFacing = this.getDirection();
        String direction;

        if (directionFacing >= 230 && directionFacing < 310) {
            direction = "up";
        } else if (directionFacing >= 40 && directionFacing < 140) {
            direction = "down";
        } else if (directionFacing >= 310 || directionFacing < 40) {
            direction = "right";
        } else {
            direction = "left";
        }

        switch (direction) {
            case "up" -> this.setSprite(art.getSprite("up"));
            case "down" -> this.setSprite(art.getSprite("down"));
            case "right" -> this.setSprite(art.getSprite("right"));
            case "left" -> this.setSprite(art.getSprite("left"));
            default -> this.setSprite(art.getSprite("default"));
        }
    }

    /**
     * Determines the coordinates the bee should currently move toward.
     *
     * @return an array containing {x, y} target coordinates
     */
    public double[] getTargetCoordinates() {
        if (this.trackedTarget != null) {
            return new double[]{this.trackedTarget.getX(), this.trackedTarget.getY()};
        }
        return new double[]{this.spawnX, this.spawnY};
    }

    /**
     * Adjusts the bee’s facing direction to point toward its current target.
     */
    public void updateDirectionToTarget(double targetX, double targetY) {
        double deltaX = targetX - this.getX();
        double deltaY = targetY - this.getY();
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (angle % 90 == 0) {
            this.setDirection((int) angle + 1);
        } else {
            this.setDirection((int) angle);
        }
    }

    /**
     * Checks for collisions with enemies or target destination and handles expiration.
     *
     * @return true if the bee was removed (either collided or expired)
     */
    public boolean checkCollisionsAndExpire(EngineState state, GameState game,
                                             double targetX, double targetY) {
        if (this.distanceFrom((int) targetX, (int) targetY) <= SPEED) {
            this.markForRemoval();
            return true;
        }

        for (Enemy enemy : game.getEnemies().getAllEnemies()) {
            if (this.distanceFrom(enemy) < state.getDimensions().tileSize()) {
                enemy.markForRemoval();
                this.markForRemoval();
                return true;
            }
        }
        return false;
    }

    /**
     * Advances the bee every tick. chooses a target position, moves toward it,
     * resolves collisions, and updates expiration.
     *
     * @param state the current engine state
     * @param game  the current game state
     */
    @Override
    public void tick(EngineState state, GameState game) {
        double[] target = getTargetCoordinates();
        double targetX = target[0];
        double targetY = target[1];

        updateDirectionToTarget(targetX, targetY);
        super.tick(state);
        this.setSpeed(SPEED);

        if (checkCollisionsAndExpire(state, game, targetX, targetY)) {
            return;
        }

        this.updateArtBasedOnDirection();
        lifespan.tick();
        if (lifespan.isFinished()) {
            this.markForRemoval();
        }
        this.move();
    }
}
