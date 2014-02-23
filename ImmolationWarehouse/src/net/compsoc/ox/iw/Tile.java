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
	
	// Position in world (do not change, it won't do anything)
	float posX, posY;
	
	// Graphics for this tile
	private Texture texture;		// Texture for the sprite
	private Sprite sprite;			// Sprite for this tile
	private ParticleEffect fire;	// FIRE
	
	// AABB for this tile (regardless of solidity)
	private AABB aabb;
	
	// State of this tile
	private String imagename = "";			// WITHOUT extension (png assumed)
	private boolean flammable = true;
	private float flammability = 0.005f;
	private boolean onFire = false;
	private boolean solid = false;
	private boolean destructable = false;
	private int hitpoints = 0;
	private float firedamagetick = 0.0f;
	private boolean attemptfirespread = false;
	
	// Accessors
	public AABB getAABB() { return aabb; }
	public boolean isFlammable() { return flammable; }
	public float getFlammability() { return flammability; }
	public boolean isOnFire() { return onFire; }
	public boolean isSolid() { return solid; }
	public boolean isDestructable() { return destructable; }
	public int getHitPoints() { return hitpoints; }
	
	// Constructor
	public Tile(GameTileType type, int tileX, int tileY) {
		// Load settings for this tile type
		switch(type) {
		case Wall:
			imagename = "wall";
			flammable = true;
			flammability = 0.001f;
			solid = true;
			destructable = false;
			hitpoints = 0;
			break;
		case Floor:
			imagename = "floor";
			flammable = true;
			flammability = 0.01f;
			solid = false;
			destructable = false;
			hitpoints = 0;
			break;
		case Table:
			imagename = "table";
			flammable = true;
			flammability = 0.2f;
			solid = true;
			destructable = true;
			hitpoints = 100;
			break;
		case Barrel:
			imagename = "barrel";
			flammable = true;
			flammability = 0.2f;
			destructable = false;
			hitpoints = 50;
			break;
		case Start:
			imagename = "start";
			flammable = false;
			flammability = 0.0f;
			destructable = false;
			hitpoints = 0;
			break;
		case End:
			imagename = "end";
			flammable = false;
			flammability = 0.0f;
			destructable = false;
			hitpoints = 0;
			break;
		default:
			break;
		}
		
		// Calculate pixel position of tile
		posX = (float)tileX * tileWidth;
		posY = (float)tileY * tileHeight;
		
		// Build AABB
		aabb = new AABB(posX, posY, tileWidth, tileHeight);
		
		// Prepare tile graphic (this is the only case we DO NOT dispose the texture first)
		setImage(imagename + ".png");
		
		// Prepare fire particle effect
		fire = new ParticleEffect(MainGame.fire);
		fire.setPosition(posX + (tileWidth*0.5f), posY + (tileHeight*0.5f));
	}
	
	// Set the tile image to the given filename
	// DO NOT FORGET to dispose the previous texture first
	private void setImage(String filename) {
		texture = new Texture(Gdx.files.internal(filename));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, (int)tileWidth, (int)tileHeight);
		sprite = new Sprite(region);
		sprite.setScale(1.0f, 1.0f);
		sprite.setOrigin(0.0f, 0.0f);
		sprite.setPosition(posX, posY);
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		texture.dispose();
	}
	
	// Attempt (with this tile's flammability) to make this tile on fire (if it is flammable)
	public void attemptToLight() {
		if (Math.random() <= flammability) setFire(true);
	}
	
	// Make this tile on fire / not on fire (if it is flammable)
	public void setFire(boolean onFire) {
		if(flammable) {
			this.onFire = onFire;
			if (onFire) {
				fire.start();
			} else {
				fire.reset();
			}
		}
	}
	
	// Apply given damage to this tile (if it is destructable)
	public void damage(int dmg) {
		// Apply damage
		if (destructable) hitpoints = Math.max(0, hitpoints-dmg);
		
		// Destroy tile if hitpoints drop to zero
		if (destructable && hitpoints == 0) destroy();
	}
	
	// Break this tile (if it is destructable)
	private void destroy() {
		if (destructable) {
			solid = false;
			destructable = false;
			setImage(imagename + "_broken.png");
		}
	}
	
	// Ask tile if it wants to spread fire - if it does, reset back to false
	public boolean shouldSpreadFire() {
		if (attemptfirespread) {
			attemptfirespread = false;
			return true;
		} else {
			return false;
		}
	}
	
	// Update the tile
	public void update(float delta) {
		// Update fire damage tick
		if (onFire) {
			firedamagetick += delta;
			if (firedamagetick > 0.1f) {
				firedamagetick -= 0.1f;
				attemptfirespread = true;
				damage(1);
			}
		}
		
		// Update particle systems
		if (onFire) fire.update(delta);
	}
	
	// Render the tile only
	public void renderTile(SpriteBatch batch) {
		// Draw the tile
		sprite.draw(batch);
	}
	
	// Render the particle effects only
	public void renderParticles(SpriteBatch batch) {
		// Draw the particles
		if (onFire) fire.draw(batch);
	}
}