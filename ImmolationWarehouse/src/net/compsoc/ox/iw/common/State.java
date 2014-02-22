package net.compsoc.ox.iw.common;

/**
 * An interface to allow the game to be in discrete states that can be
 * transitioned between. When the state is active it should receive relevant
 * inputs, update and render to the screen. When it is inactive it should do
 * nothing.
 * 
 * States should start inactive and only become active when instructed to do so.
 * States are mutually exclusive, so only one should be active at a time. At
 * least one state must be active at all times.
 * 
 * @author CSD Elliot
 * 
 */
public interface State {
    
    /**
     * Event triggered when the state is activated.
     */
    public void onActivate();
    
    /**
     * Event triggered when the state is deactivated.
     */
    public void onDeactivate();
    
    /**
     * Event triggered when the state is destroyed.
     */
    public void onDispose();
    
    /**
     * Event triggered when the state is first created.
     */
    public void onInitialise();
    
    /**
     * Event triggered when a new frame is required. Update the state and render
     * to the screen.
     */
    public void onRender();
    
    /**
     * Event triggered when the application is resized.
     */
    public void onResize();
    
}
