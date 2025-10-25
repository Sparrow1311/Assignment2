package builder.entities.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.WorldBuilder;
import builder.world.WorldLoadException;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BeeHiveTest {

    private static final Dimensions DIMENSIONS = new TileGrid(10, 800);

    @Test
    public void constructor_setsSpriteAndSpeed() {
        BeeHive hive = new BeeHive(10, 20);
        assertNotNull(hive.getSprite());
        assertEquals(0.0, hive.getSpeed(), 0.0);
    }

    @Test
    public void checkAndSpawnBee_returnsGuardBeeWhenEnemyNearby() {
        BeeHive hive = new BeeHive(0, 0);
        Enemy dummy = new Enemy(50, 0) {};
        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(dummy);
        assertNotNull(hive.checkAndSpawnBee(enemies));
    }

    @Test
    public void interact_addsBeeToGameWhenEnemyNearby() throws IOException, WorldLoadException {
        BeeHive hive = new BeeHive(0, 0);
        MockEngineState state = new MockEngineState(DIMENSIONS);
        var world = WorldBuilder.empty();
        var player = new ChickenFarmer(0, 0);
        var inventory = new TinyInventory(5);
        var npcs = new NpcManager();
        var enemies = new EnemyManager(DIMENSIONS);
        var game = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        Enemy near = new Enemy(200, 0) {};
        game.getEnemies().getAllEnemies().add(near);
        hive.interact(state, game);
        assertFalse(game.getNpcs().getNpcs().isEmpty());
    }
}
