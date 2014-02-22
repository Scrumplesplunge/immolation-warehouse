package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * Datatype representing a single level
 * By James
 */
public class Level {
	// Tilemap representing level layout
	private TileMap map;
	
	// Background image
	private Texture background_tex;		// Texture for the background sprite
	private Sprite background_sprite;	// Background sprite
	
	// Constructor
	public Level(String[] lines) {
		// Load tilemap
        map = new TileMap(lines);
        
        // Prepare background image
        background_tex = new Texture(Gdx.files.internal("background.png"));
        background_tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
 		TextureRegion region = new TextureRegion(background_tex, 0, 0, 512, 512);
 		background_sprite = new Sprite(region);
 		background_sprite.setScale(64.0f, 64.0f);
 		background_sprite.setOrigin(256.0f, 256.0f);
 		background_sprite.setPosition(0.0f, 0.0f);
	}
	
	// Update the level
	public void update(float delta) {
		map.update(delta);
	}
	
	// Render the level
	public void render(SpriteBatch batch) {
		// Draw background
		background_sprite.draw(batch);
		
		// Render the tilemap
		map.render(batch);
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		map.dispose();
	}
}