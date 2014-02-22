package net.compsoc.ox.iw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Datatype representing a 2D grid of Tiles
 * By James
 */
public class TileMap {
	// Grid of tiles
	int gridWidth, gridHeight;
	private Tile[][] grid;		// grid[row][column]
	
	// Constructor
	public TileMap(int gridWidth, int gridHeight) {
		// Build grid
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		grid = new Tile[gridWidth][gridHeight];
		
		// Make a random level (this is debug)
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				if ((c + r) % 3 == 0) {
					grid[r][c] = new Tile(GameTileType.Wall, c, r);
				} else {
					grid[r][c] = new Tile(GameTileType.Floor, c, r);
				}
			}
		}
	}
	
	// Render the tilemap
	public void render(SpriteBatch batch) {
		// Render all the tiles
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				grid[r][c].render(batch);
			}
		}
	}
	
	// Dispose of stuff when finished
	public void dispose() {
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				grid[r][c].dispose();
			}
		}
	}
}