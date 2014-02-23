package net.compsoc.ox.iw;

/*
 * Experimental procedural level generator
 * By James
 */
public class ProceduralLevelGenerator {
	public static String[] generateLevel(int width, int height) {
		// Prepare design map
		char[][] map = new char[height][width];
		for (int r = 0; r < height; r++) {
        	for (int c = 0; c < width; c++) {
        		map[r][c] = 'w';
        	}
		}
		
		// Build and output level
		String[] result = new String[height];
		for (int r = 0; r < height; r++) {
			result[r] = "";
        	for (int c = 0; c < width; c++) {
        		result[r] += map[r][c];
        	}
		}
		return result;
	}
}