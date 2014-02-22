package net.compsoc.ox.iw.common;

import net.compsoc.ox.iw.MainGame;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;

/**
 * A class allowing music objects to be tweened.
 * 
 * @author CSD Elliot
 * 
 */
public class MusicTween implements TweenAccessor<MusicManager> {
    
    /**
     * A callback for when the music transition finishes.
     * 
     * @author CSD Elliot
     * 
     */
    public static class TransitionCallback implements TweenCallback {
        
        protected MusicManager music;
        
        /**
         * The track to transition to.
         */
        protected String newTrack;
        
        /**
         * Constructor.
         * 
         * @param music
         *            The music manager associated with this callback.
         * @param newTrack
         *            The new track to start playing after the transition
         *            finishes.
         */
        public TransitionCallback(MusicManager music, String newTrack) {
            this.music = music;
            this.newTrack = newTrack;
        }
        
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            switch (type) {
                case TweenCallback.START:
                    this.music.stopAll();
                    if (this.newTrack != null) {
                        this.music.play(this.newTrack);
                    }
                    break;
                default:
                    break;
            }
        }
        
    }
    
    /**
     * Indicates that the transition is over volume.
     */
    public static final int Volume = 0;
    
    /**
     * The instance of this class.
     */
    private static MusicTween Instance;
    
    /**
     * Used for error logging.
     */
    private static final String loggerTag = "MusicTween";
    
    /**
     * Register the class with the Tween engine.
     */
    public MusicTween() {
        if (Instance == null) {
            Instance = this;
            Tween.registerAccessor(MusicManager.class, Instance);
        } else {
            Gdx.app
                .error(loggerTag, "MusicTween has already been initialised.");
            Gdx.app.exit();
        }
    }
    
    @Override
    public int getValues(MusicManager target, int tweenType,
        float[] returnValues) {
        switch (tweenType) {
            case Volume:
                returnValues[0] = MainGame.music.getVolume();
                return 1;
            default:
                Gdx.app.error(loggerTag, "Nonexistent tween type requested"
                    + "for this MusicManager.");
                Gdx.app.exit();
                break;
        }
        return 0;
    }
    
    @Override
    public void
        setValues(MusicManager target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case Volume:
                MainGame.music.setVolume(newValues[0]);
                break;
            default:
                Gdx.app.error(loggerTag, "Nonexistent tween type requested"
                    + "for this MusicManager.");
                Gdx.app.exit();
                break;
        }
    }
    
}
