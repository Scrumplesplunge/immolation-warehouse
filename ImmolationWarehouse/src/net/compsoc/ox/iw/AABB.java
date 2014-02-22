package net.compsoc.ox.iw;

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
}