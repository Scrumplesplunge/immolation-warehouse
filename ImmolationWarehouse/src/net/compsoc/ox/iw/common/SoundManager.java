package net.compsoc.ox.iw.common;

import java.util.HashMap;
import java.util.Map;

import net.compsoc.ox.iw.MainGameListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * A class to manage the sound effects being used in the game.
 * 
 * @author CSD Elliot
 * 
 */
public class SoundManager implements MainGameListener {
    
    /**
     * A string of the class' name, used in error logging.
     */
    private static final String loggerTag = "SoundManager";
    
    /**
     * The list of sounds the sound manager knows about.
     */
    protected Map<String, Sound> soundMap;
    
    /**
     * Default constructor.
     */
    public SoundManager() {
        this.soundMap = new HashMap<String, Sound>();
    }
    
    /**
     * Add a sound to the manager.
     * 
     * @param identifier
     *            The identifier used to reference the sound in future.
     * @param handle
     *            The handle of the file where the sound is stored.
     */
    public void add(String identifier, FileHandle handle) {
        if (handle.exists()) {
            if (this.soundMap.containsKey(identifier)) {
                Gdx.app.error(loggerTag, "Sound " + identifier
                    + " forcefully overwritten. Please remove the old sound"
                    + " before adding a new one with the same identifier.");
                Gdx.app.exit();
            }
            Sound sound = Gdx.audio.newSound(handle);
            this.soundMap.put(identifier, sound);
        } else {
            Gdx.app.error(loggerTag, "No such sound " + handle.name()
                + " exists.");
            Gdx.app.exit();
        }
    }
    
    /**
     * Add a sound to the manager.
     * 
     * @param identifier
     *            The identifier used to reference the sound in future.
     * @param filename
     *            The name of the file where the sound is stored.
     */
    public void add(String identifier, String filename) {
        FileHandle fh = Gdx.files.internal("audio/" + filename);
        this.add(identifier, fh);
    }
    
    /**
     * Remove a sound to free memory once it is done with.
     * 
     * @param identifier
     *            The identifier of the sound to remove.
     */
    public void remove(String identifier) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.remove(identifier);
        } else {
            Gdx.app.error(loggerTag, "Tried to remove nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to play.
     */
    public void play(String identifier) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).play();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to play.
     * @param volume
     *            The volume of the sound, between 0 and 1.
     */
    public void play(String identifier, float volume) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).play(volume);
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to play.
     * @param volume
     *            The volume of the sound, between 0 and 1.
     * @param pitch
     *            The pitch of the sound, between 0.5 and 2.0.
     * @param pan
     *            The pan of the sound, between 1 (left) and -1 (right).
     */
    public void play(String identifier, float volume, float pitch, float pan) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).play(volume, pitch, pan);
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound on a loop.
     * 
     * @param identifier
     *            The identifier of the sound to pause.
     */
    public void loop(String identifier) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).loop();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to play.
     * @param volume
     *            The volume of the sound, between 0 and 1.
     */
    public void loop(String identifier, float volume) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).loop(volume);
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Play the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to play.
     * @param volume
     *            The volume of the sound, between 0 and 1.
     * @param pitch
     *            The pitch of the sound, between 0.5 and 2.0.
     * @param pan
     *            The pan of the sound, between 1 (left) and -1 (right).
     */
    public void loop(String identifier, float volume, float pitch, float pan) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).loop(volume, pitch, pan);
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Stop all instances of the specified sound.
     * 
     * @param identifier
     *            The identifier of the sound to stop.
     */
    public void stop(String identifier) {
        if (this.soundMap.containsKey(identifier)) {
            this.soundMap.get(identifier).stop();
        } else {
            Gdx.app.error(loggerTag, "Tried to access nonexistent sound "
                + identifier + ".");
            Gdx.app.exit();
        }
    }
    
    /**
     * Stop all playing sounds.
     */
    public void stopAll() {
        for (String s : this.soundMap.keySet()) {
            this.stop(s);
        }
    }
    
    @Override
    public void onCreate() {
        // Nothing to do here.
    }
    
    @Override
    public void onDispose() {
        // Here we have to dispose of the music objects that we're using.
        for (Sound s : this.soundMap.values()) {
            s.dispose();
        }
        this.soundMap = null;
        
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
