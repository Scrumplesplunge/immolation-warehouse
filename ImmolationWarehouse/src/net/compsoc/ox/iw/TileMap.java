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
	//Create the tile map from a text description.
	public TileMap(String[] lines) {
		//Well formed level files will have at least one row, and
        //each row will have the same number of columns.
        gridWidth = lines[0].length();
        gridHeight = lines.length;
        grid = new Tile[gridWidth][gridHeight];
        GameTileType tileType;
        for (int r = 0; r < gridHeight; r++) {
        	for (int c = 0; c < gridWidth; c++) {
        		switch (lines[r].charAt(c)) {
        		case 'w':
        			tileType = GameTileType.Wall;
        			break;
        		case 'f':
        			tileType = GameTileType.Floor;
        			break;
        		default:
        			tileType = GameTileType.Wall;
        			break;
        		}
        		//Pass X and Y coords to Tile.
        		grid[r][c] = new Tile(tileType, c, r);
        		
        		if((r+c)%10==0) {
        			grid[r][c].setFire(true);
        		}
        	}
        }
	}
	
	// Update the tilemap
	public void update(float delta) {
		// Update all the tiles
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				grid[r][c].update(delta);
			}
		}
	}
	
	// Render the tilemap
	public void render(SpriteBatch batch) {
		// Render all the tiles
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				grid[r][c].renderTile(batch);
			}
		}
		// Render all tile particle effects
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				grid[r][c].renderParticles(batch);
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
	
	// Does the given AABB intersect with the tilemap?
	public boolean collidesWith(AABB aabb) {
		// Collide with AABBs of all solid tiles
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				if(grid[r][c].isSolid() && aabb.collidesWith(grid[r][c].getAABB()))
					return true;
			}
		}
		return false;
	}
}