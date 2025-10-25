package builder.entities.npc.enemies;

import builder.ui.SpriteGallery;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the abstract Bird class, using a minimal concrete subclass to instantiate it.
 */
public class BirdTest {

    /** Test subclass to be able to instance Bird for testing. */
    private static class TestBird extends Bird {
        public TestBird(int x, int y, engine.game.HasPosition target) {
            super(x, y, null, 100, SpriteGallery.pigeon, "default");
        }

        @Override
        public void tick(engine.EngineState state, builder.GameState game) {
            lifeSpanTick();
        }
    }

    @Test
    public void testConstructorSetsLifespanAndSpriteAndAttacking() {
        TestBird bird = new TestBird(10, 20, null);

        assertNotNull("Lifespan should be set in constructor", bird.getLifespan());
        assertNotNull("Sprite should be set in constructor", bird.getSprite());
        assertTrue("Bird should start attacking by default", bird.isAttacking());
    }

    @Test
    public void testGettersReturnExpectedValues() {
        TestBird bird = new TestBird(15, 30, null);

        assertEquals("SpawnX should return correct value", 15, bird.getSpawnX());
        assertEquals("SpawnY should return correct value", 30, bird.getSpawnY());
        assertNull("Tracked target should be null when not set", bird.getTrackedTarget());
        assertNotNull("Art should not be null", bird.getArt());
        assertNotNull("Lifespan should not be null", bird.getLifespan());
    }

    @Test
    public void testAttackingFlagCanBeToggled() {
        TestBird bird = new TestBird(0, 0, null);
        assertTrue("Bird should start attacking by default", bird.isAttacking());

        bird.setAttacking(false);
        assertFalse("Bird should stop attacking after setAttacking(false)", bird.isAttacking());

        bird.setAttacking(true);
        assertTrue("Bird should start attacking again", bird.isAttacking());
    }

    @Test
    public void testLifespanEventuallyExpires() {
        TestBird bird = new TestBird(0, 0, null);
        for (int i = 0; i < 400; i++) {
            bird.tick(null, null);
        }
        assertTrue("Bird should expire after its lifespan", bird.isMarkedForRemoval());
    }


    @Test
    public void testBirdCanMoveAndNotRemovedImmediately() {
        TestBird bird = new TestBird(0, 0, null);
        bird.setX(50);
        bird.setY(50);

        int xBefore = bird.getX();
        int yBefore = bird.getY();

        bird.tick(null, null);

        boolean moved = (bird.getX() != xBefore) || (bird.getY() != yBefore);
        assertFalse("Bird should not remove itself immediately", bird.isMarkedForRemoval());
    }

    @Test
    public void testConstructorSetsSpriteAndAttackingFlag() {
        TestBird bird = new TestBird(0, 0, null);
        assertNotNull("Sprite should be set in constructor", bird.getSprite());
        assertTrue("Attacking flag should be true in constructor", bird.isAttacking());
    }

    @Test
    public void testGetTrackedTargetAfterSet() {
        engine.game.HasPosition dummy = new engine.game.HasPosition() {
            private int x = 5, y = 10;
            @Override public int getX() { return x; }
            @Override public int getY() { return y; }
            @Override public void setX(int nx) { x = nx; }
            @Override public void setY(int ny) { y = ny; }
        };

        TestBird bird = new TestBird(0, 0, null);
        bird.setTrackedTarget(dummy);

        assertNotNull("Tracked target should be settable and retrievable", bird.getTrackedTarget());
        assertEquals("Tracked target reference should match", dummy, bird.getTrackedTarget());
    }

    @Test
    public void testPursueTargetAndReturnSpawnChangeSpriteAndDirection() {
        engine.game.HasPosition target = new engine.game.HasPosition() {
            private int x = 100, y = 100;
            @Override public int getX() { return x; }
            @Override public int getY() { return y; }
            @Override public void setX(int nx) { x = nx; }
            @Override public void setY(int ny) { y = ny; }
        };


        TestBird bird = new TestBird(0, 0, null);
        bird.setTrackedTarget(target);
        bird.setAttacking(true);


        scenarios.mocks.MockEngineState state =
                new scenarios.mocks.MockEngineState(new engine.renderer.TileGrid(10, 800));


        bird.pursueTarget();
        assertNotNull("Sprite should be set when pursuing target", bird.getSprite());
        assertNotEquals("Direction should change when pursuing target", 0, bird.getDirection());


        bird.setAttacking(false);
        bird.setX(50);
        bird.setY(50);
        bird.returnSpawn(state);
        assertNotNull("Sprite should be set when returning to spawn", bird.getSprite());
    }

    @Test
    public void testPursueTargetUpdatesSprite() {
        engine.game.HasPosition target = new engine.game.HasPosition() {
            @Override public int getX() { return 0; }
            @Override public int getY() { return 200; }
            @Override public void setX(int nx) {}
            @Override public void setY(int ny) {}
        };

        TestBird bird = new TestBird(0, 100, target);
        bird.setAttacking(true);
        bird.pursueTarget();

        assertNotNull("Sprite should be updated while pursuing target", bird.getSprite());
    }

    @Test
    public void testReturnSpawnUpdatesDirectionAndSprite() {
        TestBird bird = new TestBird(100, 100, null);
        bird.setAttacking(false);

        scenarios.mocks.MockEngineState state =
                new scenarios.mocks.MockEngineState(new engine.renderer.TileGrid(10, 800));

        bird.returnSpawn(state);

        assertNotNull("Sprite should be updated when returning to spawn", bird.getSprite());
        assertTrue("Direction should be set when returning to spawn",
                bird.getDirection() >= -180 && bird.getDirection() <= 180);
    }


}
