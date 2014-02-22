package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
	
	// Graphics for this tile
	private Texture texture;		// Texture for the sprite
	private Sprite sprite;			// Sprite for this tile
	private ParticleEffect fire;	// FIRE
	
	// State of this tile
	boolean onFire = false;
	
	// Constructor
	public Tile(GameTileType type, int tileX, int tileY) {
		// Initialise tile
		// - calculate pixel position
		float posX = (float)tileX * tileWidth;
		float posY = (float)tileY * tileHeight;
		// - create texture
		texture = new Texture(Gdx.files.internal(getTileImageName(type)));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, (int)tileWidth, (int)tileHeight);
		// - create sprite
		sprite = new Sprite(region);
		sprite.setScale(1.0f, 1.0f);
		sprite.setOrigin(0.0f, 0.0f);
		sprite.setPosition(posX, posY);
		// - create particle effect (turned off)
		fire = new ParticleEffect(MainGame.fire);
		fire.setPosition(posX + (tileWidth*0.5f), posY + (tileHeight*0.5f));
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		texture.dispose();
	}
	
	// Make this tile on fire / not on fire (but why would you want the latter?)
	public void setFire(boolean onFire) {
		this.onFire = onFire;
		if (onFire) {
			fire.start();
		} else {
			fire.reset();
		}
	}
	
	// Update the tile
	public void update(float delta) {
	}
	
	// Render the tile
	public void render(SpriteBatch batch) {
		// Draw the tile
		sprite.draw(batch);
		fire.draw(batch);
	}
}