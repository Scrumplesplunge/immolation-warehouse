package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * Datatype representing a single Pikcup
 * By James
 */
public class Pickup {
	// Static information about all pickups
	private static final float pickupWidth = 64.0f, pickupHeight = 64.0f;	// Pickup image dimensions (pixels)
	
	// Position in world
	float posX, posY;
	
	// Graphics for this tile
	private Texture texture;		// Texture for the sprite
	private Sprite sprite;			// Sprite for this tile
	
	// AABB for this pickup
	private AABB aabb;
	
	// State of this pickup
	String imagename = "";			// WITHOUT extension (png assumed)
	
	// Accessors
	public AABB getAABB() { return aabb; }
	
	// Constructor
	public Pickup(GamePickupType type, float posX, float posY) {
		// Load settings for this tile type
		switch(type) {
		case Water:
			imagename = "water";
			break;
		default:
			break;
		}
		
		// Note pickup position
		this.posX = posX;
		this.posY = posY;
		
		// Build AABB
		aabb = new AABB(posX, posY, pickupWidth, pickupHeight);
		
		// Prepare tile graphic (this is the only case we DO NOT dispose the texture first)
		setImage(imagename + ".png");
	}
	
	// Set the tile image to the given filename
	// DO NOT FORGET to dispose the previous texture first
	private void setImage(String filename) {
		texture = new Texture(Gdx.files.internal(filename));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, (int)pickupWidth, (int)pickupHeight);
		sprite = new Sprite(region);
		sprite.setScale(1.0f, 1.0f);
		sprite.setOrigin(0.0f, 0.0f);
		sprite.setPosition(posX, posY);
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		texture.dispose();
	}

	// Update the pickup
	public void update(float delta) {
	}
	
	// Render the pickup
	public void render(SpriteBatch batch) {
		// Draw the pickup
		sprite.draw(batch);
	}
}