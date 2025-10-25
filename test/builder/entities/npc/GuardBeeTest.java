package builder.entities.npc;

import builder.GameState;
import builder.JavaBeanFarm;
import builder.entities.npc.enemies.Enemy;
import builder.ui.SpriteGallery;
import builder.world.WorldLoadException;
import engine.Engine;
import engine.EngineState;
import engine.game.Game;
import engine.game.HasPosition;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Before;
import org.junit.Test;
import scenarios.analysers.AnalyserManager;
import scenarios.details.ScenarioDetails;
import scenarios.mocks.MockCore;
import scenarios.mocks.MockEngineState;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class GuardBeeTest {
    private static final int SIZE = 800;
    private static final int TILES_PER_ROW = 25;

    @Test
    public void testSpriteIsSetToDefaultInConstructor() {
        GuardBee bee = new GuardBee(10, 20, null);
        assertNotNull("GuardBee should set its sprite in the constructor", bee.getSprite());
        assertEquals("GuardBee sprite should be default", bee.getSprite(), SpriteGallery.bee.getSprite("default"));
    }

    @Test
    public void testGetLifespanIsNotNull() {
        GuardBee bee = new GuardBee(0, 0, null);
        assertNotNull("getLifespan() should never return null", bee.getLifespan());
    }

    @Test
    public void testGetTargetCoordinatesReturnsTwoValues() throws Exception {
        GuardBee bee = new GuardBee(5, 10, null);

        double[] coordinates = bee.getTargetCoordinates();
        assertNotNull("Target coordinates array must not be null", coordinates);
        assertEquals("Array should have exactly 2 elements", 2, coordinates.length);
    }

    @Test
    public void testUpdateDirectionToTargetChangesDirection() throws Exception {
        GuardBee bee = new GuardBee(0, 0, null);
        int initialDirection = bee.getDirection();
        bee.updateDirectionToTarget(10.0, 10.0);
        assertNotEquals("Direction should change when target moves", initialDirection, bee.getDirection());
    }

    @Test
    public void testUpdateArtBasedOnDirectionSetsSprite() {
        GuardBee bee = new GuardBee(0, 0, null);
        int[] directions = {0, 90, 180, 270, 320};

        for (int dir : directions) {
            bee.setDirection(dir);
            bee.updateArtBasedOnDirection();
            assertNotNull("Sprite must not be null for direction " + dir, bee.getSprite());
        }
    }

    @Test
    public void testTickEventuallyMarksBeeForRemoval() {
        GuardBee bee = new GuardBee(0, 0, null);


        for (int i = 0; i < 350; i++) {
            bee.tick(null, null);
        }

        assertTrue("Bee should be marked for removal after lifespan expires", bee.isMarkedForRemoval());
    }

    @Test
    public void testBeeRemovesItselfWhenAtTarget() {
        GuardBee bee = new GuardBee(0, 0, null);

        bee.tick(null, null);

        assertTrue("Bee should remove itself when already at target", bee.isMarkedForRemoval());
    }

    @Test
    public void testBeeEventuallyExpiresAfterManyTicks() {
        GuardBee bee = new GuardBee(0, 0, null);

        for (int i = 0; i < 400; i++) {
            bee.tick(null, null);
        }

        assertTrue("Bee should expire after its lifespan ends", bee.isMarkedForRemoval());
    }

    @Test
    public void testUpdateArtBasedOnDirection_AllDirectionsCovered() {
        GuardBee bee = new GuardBee(0, 0, null);
        int[] dirs = {250, 90, 0, 180}; // up, down, right, left

        for (int dir : dirs) {
            bee.setDirection(dir);
            bee.updateArtBasedOnDirection();
            assertNotNull("Sprite must be set for direction " + dir, bee.getSprite());
        }
    }

//    @Test
//    public void testCheckCollisionsAndExpireRemovesWhenClose() {
//
//        Enemy target = new Enemy(2, 0);
//        GuardBee bee = new GuardBee(0, 0, target);
//
//        int beeDistance = bee.distanceFrom((int) bee.getTargetCoordinates()[0],
//                (int) bee.getTargetCoordinates()[1]);
//
//        boolean isMarkedForRemoval = bee.isMarkedForRemoval();
//
//        assertTrue("Bee should be marked for removal when one move from target",
//                    bee.checkCollisionsAndExpire(state, game, target.getX(), target.getY()));
//
//        assertTrue("Enemy should be marked for removal when at target", bee.isMarkedForRemoval());
//    }




}
