package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/*
 * Datatype representing a single Tile
 * By James
 */
public class Tile {
	// Static information about all tiles
	private static final float tileWidth = 64.0f, tileHeight = 64.0f;	// Tile dimensions (pixels)
	
	// Graphics for this tile
	private Texture texture;		// Texture for the sprite
	private Sprite sprite;			// Sprite for this tile
	private ParticleEffect fire;	// FIRE
	
	// AABB for this tile (regardless of solidity)
	private AABB aabb;
	
	// State of this tile
	private boolean onFire = false;
	private boolean solid = false;
	
	// Accessors
	public AABB getAABB() { return aabb; }
	public boolean isOnFire() { return onFire; }
	public boolean isSolid() { return solid; }
	
	// Constructor
	public Tile(GameTileType type, int tileX, int tileY) {
		// Load settings for this tile type
		String imageFilename = "";
		switch(type) {
		case Wall:
			imageFilename = "wall.png";
			solid = true;
			break;
		case Floor:
			imageFilename = "floor.png";
			solid = false;
			break;
		default:
			break;
		}
		
		// Calculate pixel position of tile
		float posX = (float)tileX * tileWidth;
		float posY = (float)tileY * tileHeight;
		
		// Build AABB
		aabb = new AABB(posX, posY, tileWidth, tileHeight);
		
		// Prepare tile graphic
		texture = new Texture(Gdx.files.internal(imageFilename));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, (int)tileWidth, (int)tileHeight);
		sprite = new Sprite(region);
		sprite.setScale(1.0f, 1.0f);
		sprite.setOrigin(0.0f, 0.0f);
		sprite.setPosition(posX, posY);
		
		// Prepare fire particle effect
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
		// Update particle systems
		if(onFire) fire.update(delta);
	}
	
	// Render the tile only
	public void renderTile(SpriteBatch batch) {
		// Draw the tile
		sprite.draw(batch);
	}
	
	// Render the particle effects only
	public void renderParticles(SpriteBatch batch) {
		// Draw the particles
		if(onFire) fire.draw(batch);
	}
}