package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


class Player {
    // Configuration.
    private static final float defaultSpeed = 256.0f;
    private static final float waterRefreshingness = 0.2f;
    private static final float width = 64.0f, height = 64.0f;
    private static final float overheatTime = 30.0f;
    private static final float armCraziness = 0.99f, armSpeed = 2000.0f;
    private static final float armWidth = 22.0f, armHeight = 44.0f;
    private static final float inertia = 0.01f, armInertia = 0.001f;
    private static final float stunTimeout = 0.1f;

    // Position and velocity.
    private float x, y, vx, vy, target_bodyAngle;
    public float overheat = 0.0f;
    private float delta = 1.0f;
    private float speed = defaultSpeed;
    
    // Collisions
    private AABB aabb;

    // Level reference.
    private Level level;

    // Texture/Sprite for the player.
    private Texture texture, arms;
    private Sprite sprite, leftArm, rightArm;
    
    // Various rotations.
    private float bodyAngle, leftArmAngle, rightArmAngle;
    private float leftArmAngVel, rightArmAngVel;
    
    // FIRE
    private ParticleEffect fire;

    // Constructor.
    public Player(Level level, float x, float y) {
        this.level = level;
        this.vx = 0;
        this.vy = speed;
        
        bodyAngle = leftArmAngle = rightArmAngle = 0.0f;
        leftArmAngVel = rightArmAngVel = 0.0f;
        
        // Build AABB
        aabb = new AABB(x - 24.0f, y - 24.0f, 48.0f, 48.0f);

        // Load the fire effect.
        fire = new ParticleEffect(MainGame.fire);
        fire.start();

        // Set up the player texture and sprite.
        texture = new Texture(Gdx.files.internal("character.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture, 0.0f, 0.0f, 1.0f, 1.0f);

        sprite = new Sprite(region);
        sprite.setScale(1.0f, 1.0f);
        sprite.setOrigin(0.5f * width, 0.5f * height);
        
        // Player arms.
        arms = new Texture(Gdx.files.internal("arms.png"));
        arms.setFilter(TextureFilter.Linear,  TextureFilter.Linear);
        TextureRegion left = new TextureRegion(arms, 0, 22, 22, 44);
        TextureRegion right = new TextureRegion(arms, 22, 22, 44, 44);
        
        leftArm = new Sprite(left);
        leftArm.setScale(1.0f, 1.0f);
        leftArm.setOrigin(0.5f * armWidth, 0.0f);
        
        rightArm = new Sprite(right);
        rightArm.setScale(1.0f, 1.0f);
        rightArm.setOrigin(0.5f * armWidth, 0.0f);
        
        setPosition(level.map.startX * 64 + 32, level.map.startY * 64 + 32);
    }
    
    public void setPosition(float x, float y) {
    	this.x = x;
    	this.y = y;
    	aabb.x = x - (aabb.w / 2);
    	aabb.y = y - (aabb.h / 2);
    	sprite.setPosition(x - 0.5f * width, y - 0.5f * height);
    	fire.setPosition(x, y);
    }
    
    private void updateSprites() {
    	float degrees = 180.0f / (float)Math.PI;
        float nvx = vx / speed, nvy = vy / speed;
        sprite.setRotation(bodyAngle * degrees);
        leftArm.setRotation(leftArmAngle * degrees);
        leftArm.setPosition(x - nvy * 0.35f * width - 0.5f * armWidth, y + nvx * 0.35f * height);
        rightArm.setRotation(rightArmAngle * degrees);
        rightArm.setPosition(x + nvy * 0.35f * width - 0.5f * armWidth, y - nvx * 0.35f * height);
    }
    
    private float angNorm(float ang) {
    	float pi = (float)Math.PI;
    	return ((ang + pi) % (2 * pi) + 2 * pi) % (2 * pi) - pi;
    }
    
    private float angFromVec(float x, float y) {
    	float ny = y / (float)Math.sqrt(x * x + y * y);
    	return x < 0 ? (float)Math.acos(ny) : -(float)Math.acos(ny);
    }
    
    private float horizontalIndent(boolean resolveLeft, AABB other) {
    	if (resolveLeft) {
    		return 0.001f + aabb.x + aabb.w - other.x;
    	} else {
    		return 0.001f + other.x + other.w - aabb.x;
    	}
    }
    
    private float verticalIndent(boolean resolveDown, AABB other) {
    	if (resolveDown) {
    		return 0.001f + aabb.y + aabb.h - other.y;
    	} else {
    		return 0.001f + other.y + other.h - aabb.y;
    	}
    }
    
    private void doHorizontal() {
    	if (GameControls.stunTimeout < 0.0f) {
    		vx = -vx;
    		target_bodyAngle = -target_bodyAngle;
    		bodyAngle = -bodyAngle;
    	} else {
    		if (Math.random() < 0.5) {
				target_bodyAngle = bodyAngle = 0;
			} else {
				target_bodyAngle = bodyAngle = (float)Math.PI;
			}
    	}
    }
    
    private void doVertical() {
    	if (GameControls.stunTimeout < 0.0f) {
    		vy = -vy;
    		target_bodyAngle = -angNorm(target_bodyAngle + (float)Math.PI);
    		bodyAngle = -angNorm(bodyAngle + (float)Math.PI);
    	} else {
    		if (Math.random() < 0.5) {
    			target_bodyAngle = bodyAngle = 0.5f * (float)Math.PI;
    		} else {
    			target_bodyAngle = bodyAngle = -0.5f * (float)Math.PI;
    		}
    	}
    }
    
    private boolean resolveHorizontal(boolean resolveLeft, AABB other) {
    	float indent = horizontalIndent(resolveLeft, other);
    	if (resolveLeft) {
			// Try to resolve by moving character to the left.
			AABB newAABB = new AABB(aabb.x - indent, aabb.y, aabb.w, aabb.h);
			if (!level.collidesWith(newAABB)) {
				setPosition(x - indent, y);
				doHorizontal();
				updateSprites();
				GameControls.stunTimeout = stunTimeout;
				return true;
			}
		} else {
			// Try to resolve by moving character to the right.
			AABB newAABB = new AABB(aabb.x + indent, aabb.y, aabb.w, aabb.h);
			if (!level.collidesWith(newAABB)) {
				setPosition(x + indent, y);
				doHorizontal();
				updateSprites();
				GameControls.stunTimeout = stunTimeout;
				return true;
			}
		}
    	return false;
    }
    
    private boolean resolveVertical(boolean resolveDown, AABB other) {
    	float indent = verticalIndent(resolveDown, other);
    	if (resolveDown) {
			// Try to resolve by moving character downwards.
			AABB newAABB = new AABB(aabb.x, aabb.y - indent, aabb.w, aabb.h);
			if (!level.collidesWith(newAABB)) {
				setPosition(x, y - indent);
				doVertical();
				updateSprites();
				GameControls.stunTimeout = stunTimeout;
				return true;
			}
		} else {
			// Try to resolve by moving character upwards.
			AABB newAABB = new AABB(aabb.x, aabb.y + indent, aabb.w, aabb.h);
			if (!level.collidesWith(newAABB)) {
				setPosition(x, y + indent);
				doVertical();
				updateSprites();
				GameControls.stunTimeout = stunTimeout;
				return true;
			}
		}
    	return false;
    }

    // Update our position.
    public void update(float delta) {
    	this.delta = delta;
    	
    	float lerp = (float)Math.pow(inertia, delta);
    	bodyAngle += 10 * angNorm(target_bodyAngle - bodyAngle) * delta;
    	vy = speed * (float)Math.cos(bodyAngle);
    	vx = -speed * (float)Math.sin(bodyAngle);
        x += vx * delta;
        y += vy * delta;
        float leftArmAccel = 50.0f * angNorm(bodyAngle - leftArmAngle - leftArmAngVel / 15.0f);
        float rightArmAccel = 50.0f * angNorm(bodyAngle - rightArmAngle - rightArmAngVel / 15.0f);
        
        this.speed = defaultSpeed * (1 + this.overheat);
        
        float prob = 1 - (float)Math.pow(1 - armCraziness, delta);
        if (Math.random() < prob) leftArmAccel += armSpeed * (float)(Math.random() - 0.5);
        if (Math.random() < prob) rightArmAccel += armSpeed * (float)(Math.random() - 0.5);
        leftArmAngVel += leftArmAccel * delta;
        rightArmAngVel += rightArmAccel * delta;
        
        // Cap the velocities.
        if (leftArmAngVel < -10.0f) leftArmAngVel = -10.0f;
        if (leftArmAngVel > 10.0f) leftArmAngVel = 10.0f;
        if (rightArmAngVel < -10.0f) rightArmAngVel = -10.0f;
        if (rightArmAngVel > 10.0f) rightArmAngVel = 10.0f;
        
        // Update the angle.
    	leftArmAngle += leftArmAngVel * delta;
    	rightArmAngle += rightArmAngVel * delta;
        
        overheat += delta / overheatTime;
        
        setPosition(x, y);
        
        if(level.collidesWith(aabb)) {
        	// Try resolving in all four directions.
        	AABB[] collisions = level.getIntersectingAABBs(aabb);
        	
        	boolean nudgeLeft = vx > 0;
        	boolean nudgeDown = vy > 0;
        	
        	for (int i = 0; i < collisions.length; i++) {
        		float h = horizontalIndent(nudgeLeft, collisions[i]);
        		float v = verticalIndent(nudgeDown, collisions[i]);
        		
        		if (h < v) {
        			// Attempt to resolve horizontally.
        			if (resolveHorizontal(nudgeLeft, collisions[i])) return;
        		
        			// Attempt to resolve vertically.
        			if (resolveVertical(nudgeDown, collisions[i])) return;
        		} else {
        			// Attempt to resolve vertically.
        			if (resolveVertical(nudgeDown, collisions[i])) return;
        			
        			// Attempt to resolve horizontally.
        			if (resolveHorizontal(nudgeLeft, collisions[i])) return;
        		}
        	}
        }
        
        Pickup[] pickups = level.getIntersectingPickups(aabb);
        for (int i = 0; i < pickups.length; i++) {
        	switch (pickups[i].type) {
        	case Water:
        		overheat = (float)Math.max(0, overheat - waterRefreshingness);
        		break;
        	default:
        		System.out.println("ERMAGERD");
        	}
        }
        level.removePickups(pickups);
        
        updateSprites();
        
        MainGame.positionCamera(x, y, 0.0f, 1.0f);
        fire.update(delta);
    }

    // Render the player.
    public void render(SpriteBatch batch) {
        // Draw the player sprite first.
    	leftArm.draw(batch);
    	rightArm.draw(batch);
        sprite.draw(batch);

        // Now draw the player fire.
        fire.draw(batch);
    }

    // Change our velocity.
    public void setVelocity(float vx, float vy) {
    	target_bodyAngle = angFromVec(vx, vy);
    }

    // Disposal function.
    public void dispose() {
        texture.dispose();
        fire.dispose();
    }
}

