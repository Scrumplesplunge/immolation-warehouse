package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * Datatype representing a single Tile
 * By James
 */
public class Tile {
	// Static information about all tiles
	private static final float tileWidth = 64.0f, tileHeight = 64.0f;	// Tile dimensions (pixels)
	private static String getTileImageName(GameTileType type) {
		// Get image name for given tile
		switch(type) {
		case Wall: return "wall.png";
		case Floor: return "floor.png";
		default: return "";
		}
	}
	
	// State of this tile
	private Sprite sprite;			// Sprite for this tile
	private Texture texture;
	
	// Constructor
	public Tile(GameTileType type, int tileX, int tileY) {
		// Initialise tile
		texture = new Texture(Gdx.files.internal(getTileImageName(type)));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, (int)tileWidth, (int)tileHeight);
		sprite = new Sprite(region);
		sprite.setScale(1.0f, 1.0f);
		sprite.setOrigin(0.0f, 0.0f);
		sprite.setPosition((float)tileX * tileWidth, (float)tileY * tileHeight);
	}
	
	// Render the tile
	public void render(SpriteBatch batch) {
		// Draw the tile
		sprite.draw(batch);
	}
}