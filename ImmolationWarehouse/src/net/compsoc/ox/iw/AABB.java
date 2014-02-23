package net.compsoc.ox.iw;

import com.badlogic.gdx.math.Vector2;

/*
 * Datatype representing an AABB
 * By James
 */
public class AABB {
	// Member variables
	public float x, y, w, h;
	
	// Constructor
	public AABB(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	// Does this AABB collide with the given AABB?
	public boolean collidesWith(AABB other) {
		return x <= other.x + other.w && x + w >= other.x
			&& y <= other.y + other.h && y + h >= other.y;
	}
	
	// Calculate the intersect-separation vector between this AABB and the given AABB
	// Note that zero is returned if the two AABBs do not intersect
	public Vector2 getSeparationVector(AABB other) {
		// Return zero if AABBs do not intersect
		if (!collidesWith(other)) return new Vector2(0.0f, 0.0f);
		
		// Calculate separation distance
		float hw1 = w/2;
		float hh1 = h/2;
		float cx1 = x + hw1;
		float cy1 = y + hh1;
		float hw2 = other.w/2;
		float hh2 = other.h/2;
		float cx2 = other.x + hw2;
		float cy2 = other.y + hh2;
		return new Vector2(Math.abs(cx1+cx2) - (hw1+hw2), Math.abs(cy1+cy2) - (hh1+hh2));
	}
}