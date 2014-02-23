package net.compsoc.ox.iw;

/*
 * Experimental procedural level generator
 * By James
 */
public class ProceduralLevelGenerator {
	// Map in progress
	private static char[][] map;
	private static int width, height;
	
	// Generate a level of given size
	public static String[] generateLevel(int w, int h) {
		// Prepare design map
		width = w;
		height = h;
		map = new char[height][width];
		for (int r = 0; r < height; r++) {
        	for (int c = 0; c < width; c++) {
        		map[r][c] = 'w';
        	}
		}
		
		// Create some rooms
		int failsleft = 10;
		boolean repeat = true;
		while(repeat || failsleft > 0) {
			repeat = fitRoom((int)(Math.random() * 4.0d), 6 + (int)(Math.random() * 8.0d), 6 + (int)(Math.random() * 8.0d));
			if (!repeat) failsleft--;
		}
		
		// Place a start
		int mr = -1, mc = -1;
		for (int r = 0; r < height; r++) {
        	for (int c = 0; c < width; c++) {
        		if (mr < 0 || (map[r][c] == 'f' && r >= mr && Math.random() < 0.1f)) {
        			mr = r;
        			mc = c;
        		}
        	}
		}
		map[mr][mc] = 's';
		
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
	
	// Attempt to fit a room of the given size in the given direction
	// position; return sucess
	// Dir: 0: TL; 1: TR; 2: BL; 3: BR
	public static boolean fitRoom(int dir, int w, int h) {
		int r, c;
		for (int gr = 0; gr < height; gr++) {
			if (dir == 0 || dir == 1) r = gr; else r = height - gr - 1;
        	for (int gc = 0; gc < width; gc++) {
        		if (dir == 0 || dir == 2) c = gc; else c = width - gc - 1;
        		boolean workable = true;
        		for (int sr = r; sr < r + h; sr++) {
                	for (int sc = c; sc < c + w; sc++) {
                		if (sr >= height || sc >= width || map[sr][sc] != 'w') workable = false;
                	}
        		}
        		if (workable) {
        			createRoom(c, r, w, h);
        			return true;
        		}
        	}
		}
		return false;
	}
	
	// Create a room with the given top-left position and size
	public static void createRoom(int x, int y, int w, int h) {
		for (int r = y; r < y + h; r++) {
        	for (int c = x; c < x + w; c++) {
        		if (	c <= x + (int)(Math.random() * 1.2d)
        			||	c >= x + w - 1 - (int)(Math.random() * 1.2d)
        			||	r <= y + (int)(Math.random() * 1.2d)
        			||	r >= y + h - 1 - (int)(Math.random() * 1.2d)	) {
        			map[r][c] = 'w';
        		} else {
        			map[r][c] = 'f';
        		}
        	}
		}
	}
}