package net.compsoc.ox.iw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Datatype representing a single level
 * By James
 */
public class Level {
	// Tilemap representing level layout
	private TileMap map;
	
	// Constructor
	public Level() {
		map = new TileMap(10, 10);
	}
	
	// Render the level
	public void render(SpriteBatch batch) {
		// Render the tilemap
		map.render(batch);
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		map.dispose();
	}
}