package builder.entities.npc;

import builder.GameState;
import builder.Tickable;
import builder.entities.Interactable;
import builder.ui.RenderableGroup;

import engine.EngineState;
import engine.renderer.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code NpcManager} class is responsible for managing all active {@link Npc}
 * instances within the game world.
 * <p>
 * It tracks, updates, and renders NPCs each tick, and ensures that any NPCs marked
 * for removal are cleaned up appropriately. This class also delegates interaction
 * handling to all managed NPCs.
 * </p>
 */
public class NpcManager implements Interactable, Tickable, RenderableGroup {
    private final ArrayList<Npc> npcList = new ArrayList<>();

    /** Constructs a new, empty {@link NpcManager}.
     * */
    public NpcManager() {}

    /**
     * Removes all NPCs that have been marked for removal from the game world.
     */
    public void cleanup() {
        for (int i = this.getNpcs().size() - 1; i >= 0; i -= 1) {
            if (this.getNpcs().get(i).isMarkedForRemoval()) {
                this.getNpcs().remove(i);
            }
        }
    }


    /**
     * Retrieves the list of all active {@link Npc} instances currently managed.
     *
     * @return a list of all NPCs managed by this manager.
     */
    public List<Npc> getNpcs() {
        return npcList;
    }

    /**
     * Adds a new {@link Npc} to the manager for tracking and updates.
     *
     * @param npc the NPC to be added to this manager.
     */
    public void addNpc(Npc npc) {
        this.getNpcs().add(npc);
    }


    /**
     * Updates all NPCs once per tick.
     * <p>
     * Calls cleanup, then calls {@link Npc#tick(EngineState, GameState)} for each
     * active NPC.
     * </p>
     *
     * @param state the current engine state.
     * @param game  the current game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        this.cleanup();
        for (Npc npc : getNpcs()) {
            npc.tick(state, game);
        }
    }

    /**
     * Handles interactions for all managed NPCs.
     *
     * @param state the current engine state.
     * @param game  the current game state.
     */
    @Override
    public void interact(EngineState state, GameState game) {
        for (Npc npc : new ArrayList<>(getNpcs())) {
            npc.interact(state, game);
        }
    }

    /**
     * Provides all NPCs as {@link Renderable} objects for rendering each frame.
     *
     * @return a list of renderable NPCs.
     */
    @Override
    public List<Renderable> render() {
        return new ArrayList<>(this.getNpcs());
    }
}
