package net.compsoc.ox.iw.common;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;

/**
 * A class to manage the current states the game has access to.
 * 
 * @author CSD Elliot
 * 
 */
public class StateCache {
    
    /**
     * Here to prevent having to deal with null values.
     * 
     * @author CSD Elliot
     * 
     */
    private class DummyState implements State {
        
        public DummyState() {
            // Do nothing.
        }
        
        @Override
        public void onInitialise() {
            // Do nothing.
        }
        
        @Override
        public void onDispose() {
            // Do nothing.
        }
        
        @Override
        public void onRender() {
            // Do nothing.
        }
        
        @Override
        public void onActivate() {
            // Do nothing.
        }
        
        @Override
        public void onDeactivate() {
            // Do nothing.
        }
        
        @Override
        public void onResize() {
            // Do nothing.
        }
        
    }
    
    /**
     * A string of the class' name, used in error logging.
     */
    private static final String loggerTag = "StateCache";
    
    /**
     * The data structure storing the states.
     */
    protected HashMap<String, State> stateCache;
    
    /**
     * The currently active state.
     */
    protected State currentState;
    
    protected String currentStateName = null;
    
    /**
     * Constructor for StateCache.
     */
    public StateCache() {
        this.stateCache = new HashMap<String, State>();
        this.currentState = new DummyState();
    }
    
    /**
     * Add a non-null state to the cache.
     * 
     * @param state
     *            The state to add to the cache. Must be non-null.
     * @param identifier
     *            The identifier used to reference the state in future.
     */
    public void add(State state, String identifier) {
        if (state != null) {
            if (this.stateCache.containsKey(identifier)) {
                Gdx.app.error(loggerTag, "State " + identifier
                    + " forcefully overwritten. Please remove the old state"
                    + " before adding a new one with the same identifier.");
                Gdx.app.exit();
            }
            this.stateCache.put(identifier, state);
            // This is when the state gets initialised.
            state.onInitialise();
        } else {
            Gdx.app.error(loggerTag, "Null state added to cache.");
            Gdx.app.exit();
        }
    }
    
    /**
     * Get whether a state, identified by its name, is active.
     * 
     * @param identifier
     *            The state identifier.
     * @return Whether the given state is active.
     */
    public boolean active(String identifier) {
        return (identifier == this.currentStateName && this.currentStateName != null);
    }
    
    /**
     * Activate a non-active state in the cache.
     * 
     * @param identifier
     *            The identifier of the state to activate.
     */
    public void activate(String identifier) {
        if (this.stateCache.containsKey(identifier)) {
            if (this.stateCache.get(identifier).equals(this.currentState)) {
                Gdx.app.error(loggerTag, "Tried to activate already active"
                    + " state " + identifier + ".");
                Gdx.app.exit();
            }
            this.currentState.onDeactivate();
            this.currentState = this.stateCache.get(identifier);
            this.currentStateName = identifier;
            this.currentState.onActivate();
            this.currentState.onResize();
        } else {
            Gdx.app.error(loggerTag, "Tried to activate nonexistent state "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Remove a state from the cache.
     * 
     * @param identifier
     *            The identifier of the state to remove.
     */
    public void remove(String identifier) {
        if (this.stateCache.containsKey(identifier)) {
            // This is where the state gets destroyed.
            this.stateCache.get(identifier).onDispose();
            this.stateCache.remove(identifier);
        } else {
            Gdx.app.error(loggerTag, "Tried to remove nonexistent state "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * This will allow you access to other states in the cache. Make sure to use
     * this properly, or bad things will happen!
     * 
     * @param identifier
     *            The state's identifier.
     * @return The requested state.
     */
    public State get(String identifier) {
        if (this.stateCache.containsKey(identifier)) {
            // This is where the state gets destroyed.
            return this.stateCache.get(identifier);
        }
        Gdx.app.error(loggerTag, "Tried to get nonexistent state " + identifier
            + ".");
        Gdx.app.exit();
        return null;
    }
    
    /**
     * Dispose of the resources that might need explicitly freeing.
     */
    public void dispose() {
        for (State s : this.stateCache.values()) {
            // This should free the resources used by the game states.
            s.onDispose();
        }
    }
    
    /**
     * Instruct the currently active state to render.
     */
    public void render() {
        this.currentState.onRender();
    }
    
    /**
     * Resize the currently active state.
     * 
     * @param width
     *            The new width.
     * @param height
     *            The new height.
     */
    public void resize(int width, int height) {
        this.currentState.onResize();
    }
    
}
