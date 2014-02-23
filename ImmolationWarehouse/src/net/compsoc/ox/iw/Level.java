package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;
import java.util.ArrayList;

/*
 * Datatype representing a single level
 * By James
 */
public class Level {
	// Tilemap representing level layout
	private TileMap map;
	
	// Level start and end points
	private int startX, startY, endX, endY;
	
	// List of pickups
	private List<Pickup> pickups = new ArrayList<Pickup>();
	
	// Background image
	private Texture background_tex;		// Texture for the background sprite
	private Sprite background_sprite;	// Background sprite
	
	// Accessors
	public int getStartX() { return startX; }
	public int getStartY() { return startY; }
	public int getEndX() { return endX; }
	public int getEndY() { return endY; }
	
	// Constructor
	public Level(String[] lines) {
		// Load tilemap
        map = new TileMap(lines);
        
        // Load level data
		// Well formed level files will have at least one row, and
        // each row will have the same number of columns.
        int gridWidth = lines[0].length();
        int gridHeight = lines.length;
        for (int r = 0; r < gridHeight; r++) {
        	for (int c = 0; c < gridWidth; c++) {
        		switch (lines[gridHeight-r-1].charAt(c)) {
        		case 's':
        			startX = c;
        			startY = r;
        			break;
        		case 'e':
        			endX = c;
        			endY = r;
        			break;
        		default:
        			// Do nothing
        			break;
        		}
        	}
        }
        
        // Place test pickup
        pickups.add(new Pickup(GamePickupType.Water, 128.0f, 128.0f));
        
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
		for (Pickup p : pickups) {
			p.update(delta);
		}
	}
	
	// Render the level
	public void render(SpriteBatch batch) {
		// Draw background
		background_sprite.draw(batch);
		
		// Render the tilemap
		map.render(batch);
		
		// Render pickups
		for (Pickup p : pickups) {
			p.render(batch);
		}
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		map.dispose();
		for (Pickup p : pickups) {
			p.dispose();
		}
	}
	
	// Does the given AABB intersect with the level?
	public boolean collidesWith(AABB aabb) {
		// Collide AABB with tilemap and return result
		return map.collidesWith(aabb);
	}
	
	// Get list of AABBs in level that the given AABB intersects with
	public AABB[] getIntersectingAABBs(AABB aabb) {
		return map.getIntersectingAABBs(aabb);
	}
	
	// Get list of pickups in level that the given AABB intersects with
	public Pickup[] getIntersectingPickups(AABB aabb) {
		// Calculate size of array
		int n = 0;
		for (Pickup p : pickups) {
			if(aabb.collidesWith(p.getAABB())) n++;
		}
		
		// Build array
		int i = 0;
		Pickup[] result = new Pickup[n];
		for (Pickup p : pickups) {
			result[i++] = p;
		}
		
		// Return array
		// God that was dumb code, wasn't it?
		return result;
	}
	
	// Remove the given pickups from the level
	public void removePickups(Pickup[] ps) {
		for (Pickup p : pickups) {
			pickups.remove(p);
		}
	}
}