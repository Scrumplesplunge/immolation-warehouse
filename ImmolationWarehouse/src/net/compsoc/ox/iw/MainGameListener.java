package net.compsoc.ox.iw;

/**
 * An interface for classes wishing to listen to core game events to implement.
 * 
 * @author CSD Elliot
 * 
 */
public interface MainGameListener {
    
    /**
     * Event triggered when the core game is first created.
     */
    public void onCreate();
    
    /**
     * Event triggered when the core game is disposed of.
     */
    public void onDispose();
    
    /**
     * Event triggered when the game is paused by Android.
     */
    public void onPause();
    
    /**
     * Event triggered when the game is being rendered.
     */
    public void onRender();
    
    /**
     * Event triggered when the game is resized.
     * 
     * @param width
     *            The new width of the game.
     * @param height
     *            The new height of the game.
     */
    public void onResize(int width, int height);
    
    /**
     * Event triggered when the game is resumed by Android.
     */
    public void onResume();
}
