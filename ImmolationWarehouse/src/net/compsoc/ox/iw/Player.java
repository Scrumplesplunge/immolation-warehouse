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
    private static final float speed = 256.0f;
    private static final float overheatTime = 20.0f;
    private static final float armCraziness = 0.0f;
    private static final float width = 64.0f, height = 64.0f, armWidth = 22.0f, armHeight = 44.0f;
    private static final float inertia = 0.01f, armInertia = 0.001f;

    // Position and velocity.
    private float x, y, vx, vy;
    private float overheat = 0.0f;
    private float delta;

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
        
        setPosition(x, y);
    }
    
    public void setPosition(float x, float y) {
    	this.x = x;
    	this.y = y;
    	sprite.setPosition(x - 0.5f * width, y - 0.5f * height);
    	fire.setPosition(x, y);
    }
    
    private void updateSprites() {
    	float degrees = 180.0f / (float)Math.PI;
        float nvx = vx / speed, nvy = vy / speed;
        bodyAngle = vx < 0 ? (float)Math.acos(nvy) : -(float)Math.acos(nvy);
        sprite.setRotation(bodyAngle * degrees);
        leftArm.setRotation(leftArmAngle * degrees);
        leftArm.setPosition(x - nvy * 0.35f * width - 0.5f * armWidth, y + nvx * 0.35f * height);
        rightArm.setRotation(rightArmAngle * degrees);
        rightArm.setPosition(x + nvy * 0.35f * width - 0.5f * armWidth, y - nvx * 0.35f * height);
    }
    
    private float angNorm(float ang) {
    	float pi = (float)Math.PI;
    	return ((ang + pi) % (2 * pi) + 2 * pi) % 2 * pi - pi;
    }

    // Update our position.
    public void update(float delta) {
    	this.delta = delta;
    	
    	float lerp = armInertia;
    	
        x += vx * delta;
        y += vy * delta;
    	leftArmAngVel = (leftArmAngVel * lerp + (1 - lerp) * angNorm(bodyAngle - leftArmAngle)) + (float)(Math.random() - 0.5) * armCraziness;
    	rightArmAngVel = (rightArmAngVel * lerp + (1 - lerp) * angNorm(bodyAngle - rightArmAngle)) + (float)(Math.random() - 0.5) * armCraziness;
    	leftArmAngle += leftArmAngVel * delta;
    	rightArmAngle += rightArmAngVel * delta;
        
        overheat += delta / overheatTime;
        
        setPosition(x, y);
        
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
    	// Scale this velocity to match the speed.
        float mul = (float) (speed / Math.sqrt(vx * vx + vy * vy));
        vx *= mul;
        vy *= mul;
        
    	float lerp = (float)Math.pow(inertia, delta);
        this.vx = lerp * this.vx + (1 - lerp) * vx;
        this.vy = lerp * this.vy + (1 - lerp) * vy;
        
    	// Scale the final velocity to match the speed.
        mul = (float) (speed / Math.sqrt(this.vx * this.vx + this.vy * this.vy));
        this.vx *= mul;
        this.vy *= mul;
    }

    // Disposal function.
    public void dispose() {
        texture.dispose();
        fire.dispose();
    }
}

