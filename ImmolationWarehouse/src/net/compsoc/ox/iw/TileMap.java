package net.compsoc.ox.iw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * Datatype representing a 2D grid of Tiles
 * By James
 */
public class TileMap {
	// Grid of tiles
	private int gridWidth, gridHeight;
	private Tile[][] grid;		// grid[row][column]
	
	// Constructor
	//Create the tile map from a text description.
	public TileMap(String[] lines) {
		//Well formed level files will have at least one row, and
        //each row will have the same number of columns.
        gridWidth = lines[0].length();
        gridHeight = lines.length;
        grid = new Tile[gridHeight][gridWidth];
        GameTileType tileType;
        for (int r = 0; r < gridHeight; r++) {
        	for (int c = 0; c < gridWidth; c++) {
        		switch (lines[gridHeight-r-1].charAt(c)) {
        		case 'w':
        			tileType = GameTileType.Wall;
        			break;
        		case 'f':
        			tileType = GameTileType.Floor;
        			break;
        		case 't':
        			tileType = GameTileType.Table;
        			break;
        		case 's':
        			tileType = GameTileType.Start;
        			break;
        		case 'e':
        			tileType = GameTileType.End;
        			break;
        		default:
        			tileType = GameTileType.Wall;
        			break;
        		}
        		//Pass X and Y coords to Tile.
        		grid[r][c] = new Tile(tileType, c, r);
        	}
        }
	}
	
	// Update the tilemap
	public void update(float delta) {
		// Attempt to spread fire
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				if (grid[r][c].shouldSpreadFire()) {
					if (r > 0) grid[r-1][c].attemptToLight();
					if (r < gridHeight-1) grid[r+1][c].attemptToLight();
					if (c > 0) grid[r][c-1].attemptToLight();
					if (c < gridHeight-1) grid[r][c+1].attemptToLight();
				}
			}
		}
		
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
	
	// Get list of AABBs in tilemap that the given AABB intersects with
	public AABB[] getIntersectingAABBs(AABB aabb) {
		// Calculate size of array
		int n = 0;
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				if(grid[r][c].isSolid() && aabb.collidesWith(grid[r][c].getAABB()))
					n++;
			}
		}
		
		// Build array
		int i = 0;
		AABB[] result = new AABB[n];
		for(int r = 0; r < gridHeight; r++) {
			for(int c = 0; c < gridWidth; c++) {
				if(grid[r][c].isSolid() && aabb.collidesWith(grid[r][c].getAABB()))
					result[i++] = grid[r][c].getAABB();
			}
		}
		
		// Return array
		// God that was dumb code, wasn't it?
		return result;
	}
}