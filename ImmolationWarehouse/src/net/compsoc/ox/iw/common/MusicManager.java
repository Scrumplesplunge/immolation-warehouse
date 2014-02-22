package net.compsoc.ox.iw.common;

import java.util.HashMap;
import java.util.Map;

import net.compsoc.ox.iw.MainGame;
import net.compsoc.ox.iw.MainGameListener;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * A class to manage the music being used in the game.
 * 
 * @author CSD Elliot
 * 
 */
public class MusicManager implements MainGameListener {
    
    /**
     * A string of the class' name, used in error logging.
     */
    private static final String loggerTag = "MusicManager";
    
    /**
     * The list of music the music manager knows about.
     */
    protected Map<String, Music> musicMap;
    
    /**
     * The target volume of all music.
     */
    protected float volume;
    
    /**
     * Default constructor for MusicManager.
     */
    public MusicManager() {
        this.musicMap = new HashMap<String, Music>();
        this.setVolume(1);
    }
    
    /**
     * Add music to the manager.
     * 
     * @param identifier
     *            The identifier used to reference the music in future.
     * @param filename
     *            The name of the file where the music is stored.
     */
    public void add(String identifier, String filename) {
        FileHandle fh = Gdx.files.internal("audio/" + filename);
        if (fh.exists()) {
            if (this.musicMap.containsKey(identifier)) {
                Gdx.app.error(loggerTag, "Music " + identifier
                    + " forcefully overwritten. Please remove the old music"
                    + " before adding a new one with the same identifier.");
                Gdx.app.exit();
            }
            Music music = Gdx.audio.newMusic(fh);
            this.musicMap.put(identifier, music);
            music.setVolume(this.volume);
        } else {
            Gdx.app.error(loggerTag, "No such music track " + filename
                + " exists.");
            Gdx.app.exit();
        }
    }
    
    /*
     * public Music get(String identifier) { if
     * (this.musicMap.containsKey(identifier)) { return
     * this.musicMap.get(identifier); } Gdx.app.error(loggerTag,
     * "Tried to get nonexistent music " + identifier + "."); Gdx.app.exit();
     * return null; }
     */
    
    /**
     * Remove music to free memory once it is done with.
     * 
     * @param identifier
     *            The identifier of the music to remove.
     */
    public void remove(String identifier) {
        if (this.musicMap.containsKey(identifier)) {
            this.musicMap.remove(identifier);
        } else {
            Gdx.app.error(loggerTag, "Tried to remove nonexistent music "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified music.
     * 
     * @param identifier
     *            The identifier of the music to play.
     */
    public void play(String identifier) {
        if (this.musicMap.containsKey(identifier)) {
            this.musicMap.get(identifier).play();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent music "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Pause the specified music.
     * 
     * @param identifier
     *            The identifier of the music to pause.
     */
    public void pause(String identifier) {
        if (this.musicMap.containsKey(identifier)) {
            this.musicMap.get(identifier).pause();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent music "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Stop the specified music.
     * 
     * @param identifier
     *            The identifier of the music to stop.
     */
    public void stop(String identifier) {
        if (this.musicMap.containsKey(identifier)) {
            this.musicMap.get(identifier).stop();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent music "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Stop all playing music.
     */
    public void stopAll() {
        for (String s : this.musicMap.keySet()) {
            this.stop(s);
        }
    }
    
    /**
     * Stop all playing music after the given delay.
     * 
     * @param delay
     *            The delay to wait before stopping music.
     */
    public void stopAll(float delay) {
        Tween.call(new MusicTween.TransitionCallback(this, null)).delay(delay)
            .start(MainGame.tweenManager);
    }
    
    /**
     * Transition smoothly to a new track.
     * 
     * @param newTrack
     *            The track to transition to.
     * @param duration
     *            The total duration of the transition.
     * @param delay
     *            The delay before the transition starts.
     */
    public void transition(String newTrack, float duration, float delay) {
        Tween.to(this, MusicTween.Volume, duration / 2).target(0f).delay(delay)
            .start(MainGame.tweenManager);
        Tween.call(new MusicTween.TransitionCallback(this, newTrack))
            .delay(duration / 2 + delay).start(MainGame.tweenManager);
        Tween.to(this, MusicTween.Volume, duration / 2).target(this.volume)
            .delay(duration / 2 + delay).start(MainGame.tweenManager);
    }
    
    /**
     * Transition smoothly to a new track.
     * 
     * @param newTrack
     *            The track to transition to.
     * @param duration
     *            The total duration of the transition.
     */
    public void transition(String newTrack, float duration) {
        this.transition(newTrack, duration, 0);
    }
    
    /**
     * Fades the music smoothly from start to target.
     * 
     * @param duration
     *            The duration of the fade.
     * @param start
     *            The start volume.
     * @param target
     *            The target volume.
     * @param delay
     *            The delay before the fade starts.
     */
    public void fade(float duration, float start, float target, float delay) {
        this.setVolume(start);
        Tween.to(this, MusicTween.Volume, duration).target(target).delay(delay)
            .start(MainGame.tweenManager);
    }
    
    /**
     * Smoothly fades the track out.
     * 
     * @param duration
     *            The duration of the fade.
     * @param delay
     *            The delay before the fade begins.
     */
    public void fadeOut(float duration, float delay) {
        this.fade(duration, this.volume, 0f, delay);
    }
    
    /**
     * Smoothly fades the track in.
     * 
     * @param duration
     *            The duration of the fade.
     * @param delay
     *            The delay before the fade begins.
     */
    public void fadeIn(float duration, float delay) {
        this.fade(duration, 0f, this.volume, delay);
    }
    
    /**
     * Set the volume of all music.
     * 
     * @param volume
     *            The volume to set the music to.
     */
    public void setVolume(float volume) {
        if (!(0f <= volume && volume <= 1f)) {
            Gdx.app.error(loggerTag, "Volume set to illegal value. It must "
                + "be between 0 and 1.");
        } else {
            this.volume = volume;
            for (Music m : this.musicMap.values()) {
                m.setVolume(volume);
            }
        }
    }
    
    /**
     * Get the volume the music is playing at.
     * 
     * @return The current volume.
     */
    public float getVolume() {
        return this.volume;
    }
    
    /**
     * Set whether a given music track should loop.
     * 
     * @param identifier
     *            The identifier of the music.
     * @param looping
     *            Whether the music should loop.
     */
    public void setLooping(String identifier, boolean looping) {
        if (this.musicMap.containsKey(identifier)) {
            this.musicMap.get(identifier).setLooping(looping);
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent music "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    @Override
    public void onCreate() {
        // Nothing to do here.
    }
    
    @Override
    public void onDispose() {
        // Here we have to dispose of the music objects that we're using.
        for (Music m : this.musicMap.values()) {
            m.dispose();
        }
        this.musicMap = null;
        
    }
    
    @Override
    public void onRender() {
        // Nothing to do here.
    }
    
    @Override
    public void onResize(int width, int height) {
        // Nothing to do here.
    }
    
    @Override
    public void onPause() {
        // Nothing to do here.
    }
    
    @Override
    public void onResume() {
        // Nothing to do here.
    }
    
}
