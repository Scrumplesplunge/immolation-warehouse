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
        for (int i = 0; i < gridHeight; i++) {
        	for (int j = 0; j < gridWidth; j++) {
        		switch (lines[i].charAt(j)) {
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
        		grid[i][j] = new Tile(tileType, j, i);
        		
        		if(i+j%5==0) grid[i][j].setFire(true);
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