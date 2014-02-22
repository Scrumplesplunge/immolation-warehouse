package net.compsoc.ox.iw.common;

import java.util.EnumMap;

import net.compsoc.ox.iw.GameTileType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * A cache of the sprites that can be extracted from the texture atlas.
 * 
 * @author CSD Elliot
 * 
 */
public class SpriteAtlas {
    
    /**
     * A map of sprites accessed by GameTileType. We're using this because Java
     * promises that for evenly loaded buckets, get is a constant time
     * operation.
     */
    private EnumMap<GameTileType, Sprite> sprites;
    
    /**
     * The texture atlas we will load our sprites from.
     */
    private TextureAtlas texAtlas;
    
    private String loggerTag = "SpriteCache";
    
    /**
     * Constructor for SpriteCache.
     * 
     * @param texAtlas
     */
    public SpriteAtlas(TextureAtlas texAtlas) {
        this.texAtlas = texAtlas;
        this.sprites = new EnumMap<GameTileType, Sprite>(GameTileType.class);
        
        this.populateHashMap();
    }
    
    /**
     * Dispose of the unmanaged resources when we're done with them.
     */
    public void dispose() {
        this.texAtlas.dispose();
    }
    
    /**
     * Get a new sprite of the given tile.
     * 
     * @param tile
     *            The tile to get a new sprite of.
     * @return A new sprite of the given tile.
     */
    public Sprite createSprite(GameTileType tile) {
        if (!this.sprites.containsKey(tile)) {
            Gdx.app.error(this.loggerTag, "A tile was requested but not found."
                + "The SpriteCache has not been initialised properly.");
            Gdx.app.exit();
        }
        if (this.sprites.get(tile) == null) {
            System.out.println(tile);
        }
        // return new Sprite(this.sprites.get(tile));
        return this.sprites.get(tile);
    }
    
    /**
     * This is used for accessing graphics that aren't in the GameTileType enum.
     * Don't use this unless you're absolutely sure you have to.
     * 
     * @param name
     *            The name of the graphic.
     * @return A sprite of the requested graphic.
     */
    public Sprite createSpriteFromString(String name) {
        return this.texAtlas.createSprite(name);
    }
    
    /**
     * This is really super important, guys - this method relies on the
     * filenames of the graphics being the same as the (lowercased) enum text,
     * so pretty please remember to make the enum text entry the same as the
     * filename!
     */
    private void populateHashMap() {
        for (GameTileType tile : GameTileType.values()) {
            this.sprites.put(tile,
                this.texAtlas.createSprite(tile.name().toLowerCase()));
        }
    }
}
