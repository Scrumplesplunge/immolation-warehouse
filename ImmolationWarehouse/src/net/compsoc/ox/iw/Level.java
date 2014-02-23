package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.*;

/*
 * Datatype representing a single level
 * By James
 */
public class Level {
	// Tilemap representing level layout
	private TileMap map;
	
	private String levelName;
	
	// Level start and end points
	private int startX, startY, endX, endY;
	
	// List of pickups
	private List<Pickup> pickups = new ArrayList<Pickup>();
	
	// List of npcs.
	public List<NPC> npcs = new ArrayList<NPC>();
	
	// List of explosion locations
	private List<Vector2> blasts = new ArrayList<Vector2>();
	
	// Explosion blast decal image
	private Texture blast_tex;			// Texture for the blast sprite
	private Sprite blast_sprite;		// Blast sprite
	
	// Background image
	private Texture background_tex;		// Texture for the background sprite
	private Sprite background_sprite;	// Background sprite
	
	// Accessors
	public int getStartX() { return startX; }
	public int getStartY() { return startY; }
	public int getEndX() { return endX; }
	public int getEndY() { return endY; }
	
	// Constructor
	public Level(String[] lines, String levelName) {
		// Load tilemap
        map = new TileMap(lines);
        this.levelName = levelName;
        // Load level data
		// Well formed level files will have at least one row, and
        // each row will have the same number of columns.
        int gridWidth = lines[0].length();
        int gridHeight = lines.length;
        for (int r = 0; r < gridHeight; r++) {
        	for (int c = 0; c < gridWidth; c++) {
        		switch (lines[gridHeight-r-1].charAt(c)) {
        		case 'l':
        			pickups.add(new Pickup(GamePickupType.Water, (int)c * Tile.tileWidth, (int)r * Tile.tileHeight));
        			break;
        		case 's':
        			startX = c;
        			startY = r;
        			break;
        		case 'e':
        			endX = c;
        			endY = r;
        			break;
        		case 'n':
        			// MAGIC HERE
        			npcs.add(new NPC(this, (c + 0.5f) * Tile.tileWidth, (r + 0.5f) * Tile.tileHeight));
        			break;
        		default:
        			// Do nothing
        			break;
        		}
        	}
        }
        
        // Prepare blast image
        blast_tex = new Texture(Gdx.files.internal("explosion.png"));
        blast_tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
 		TextureRegion region1 = new TextureRegion(blast_tex, 0, 0, 256, 256);
 		blast_sprite = new Sprite(region1);
 		blast_sprite.setScale(1.0f, 1.0f);
 		blast_sprite.setOrigin(0.0f, 0.0f);
        
        // Prepare background image
        background_tex = new Texture(Gdx.files.internal("background.png"));
        background_tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
 		TextureRegion region2 = new TextureRegion(background_tex, 0, 0, 512, 512);
 		background_sprite = new Sprite(region2);
 		background_sprite.setScale(64.0f, 64.0f);
 		background_sprite.setOrigin(256.0f, 256.0f);
 		background_sprite.setPosition(0.0f, 0.0f);
	}
	
	// Update the level
	public void update(float delta) {
		// Update map
		map.update(delta);
		
		// Update pickups
		for (Pickup p : pickups) {
			p.update(delta);
		}
		
		// Update explosions
		Vector2 cb = map.getExplosion();
		//Only one splosion sound pls.
		if (cb != null) {
			MainGame.sound.play("splosion");
		}
		while(cb != null) {
			for(int k = 0; k < 5; k++) applyPlayerFire(cb.x, cb.y);
			applyDamageArea(cb.x, cb.y);
			cb.x = cb.x - 96.0f;
			cb.y = cb.y - 96.0f;
			blasts.add(cb);
			cb = map.getExplosion();
		}
		
		// Update every NPC.
		for (NPC n : npcs) {
			n.update(delta);
		}
		
		// Check for player intersections.
		AABB ply = MainGame.player.getAABB();
		for (NPC n : npcs) {
			if (ply.collidesWith(n.getAABB())) n.burning = true;
		}
		
		// Now check for pairwise intersections.
		for (NPC n : npcs) {
			for (NPC m : npcs) {
				if (n == m) continue;
				if ((n.burning || m.burning) && n.getAABB().collidesWith(m.getAABB())) {
					n.burning = m.burning = true;
				}
			}
		}
	}
	
	// Render the level
	public void render(SpriteBatch batch) {
		// Draw background
		background_sprite.draw(batch);
		
		// Render the tilemap tiles
		map.renderTiles(batch);
		
		// Render explosion blasts
		for (Vector2 b : blasts) {
			blast_sprite.setPosition(b.x, b.y);
			blast_sprite.draw(batch);
		}
		
		// Render the tilemap particles
		map.renderParticles(batch);
		
		// Render pickups
		for (Pickup p : pickups) {
			p.render(batch);
		}
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		blast_tex.dispose();
		background_tex.dispose();
		map.dispose();
		for (Pickup p : pickups) {
			p.dispose();
		}
		FileHandle possibleSoundFile = Gdx.files.internal("audio/" + levelName + ".ogg");
        //Play the sound file if it exists, otherwise play a burnman scream.
        if (possibleSoundFile.exists()) {
            MainGame.sound.remove(levelName);
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
			if(aabb.collidesWith(p.getAABB())) result[i++] = p;
		}
		
		// Return array
		// God that was dumb code, wasn't it?
		return result;
	}
	
	// Remove the given pickups from the level
	public void removePickups(Pickup[] ps) {
		pickups.removeAll(Arrays.asList(ps));
	}
	
	// Return the tile that the given coordinates occupy
	public Tile getTileAt(float x, float y) {
		return map.getTileAt(x, y);
	}
	
	public Tile getTileAt(int x, int y) {
		return map.getTileAt(x, y);
	}
	
	// Apply player fire to the level at the given coordinates
	public void applyPlayerFire(float x, float y) {
		int n = (int)(Math.random() * 18.0d);
		for(int i = 0; i < n; i++) {
			float fx = x + (float)(Math.random() * 128.0d) - 64.0f;
			float fy = y + (float)(Math.random() * 128.0d) - 64.0f;
			Tile t = getTileAt(fx, fy);
			if (t != null) t.attemptToLight();
		}
	}
	
	// Apply a damage area to things in the level at the given coordinates
	public void applyDamageArea(float x, float y) {
		// Damage tiles
		for(int r = -2; r <= 2; r++) {
			for(int c = -2; c <= 2; c++) {
				Tile t = getTileAt(x + (c * Tile.tileWidth), y + (r * Tile.tileHeight));
				if (t != null) t.damage(100);
			}
		}
		
		// Damage player
		float pdiffX = MainGame.player.getX() - x;
		float pdiffY = MainGame.player.getY() - y;
		float pdist = (float)Math.sqrt(pdiffX*pdiffX + pdiffY*pdiffY);
		float dmg = (256.0f - pdist) / 256.0f;
		dmg = dmg * 0.3f;
		MainGame.player.overheat += Math.max(0, dmg);
	}
}