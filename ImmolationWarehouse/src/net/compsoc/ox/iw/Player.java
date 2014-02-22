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
    private static final float width = 64.0f, height = 64.0f;

    // Position and velocity.
    private float x, y, vx, vy;
    private float overheat = 0.0f;
    private float delta = 1.0f;

    // Level reference.
    private Level level;

    // Texture/Sprite for the player.
    private Texture texture;
    private Sprite sprite;
    
    // FIRE
    private ParticleEffect fire;

    // Constructor.
    public Player(Level level, float x, float y) {
        this.level = level;
        this.vx = 0;
        this.vy = speed;

        // Load the fire effect.
        fire = new ParticleEffect(MainGame.fire);
        fire.start();

        // Set up the player texture and sprite.
        texture = new Texture(Gdx.files.internal("character.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture, 0.0f, 0.0f, 1.0f, 1.0f);

        sprite = new Sprite(region);
        sprite.setScale(1.0f, 1.0f);
        sprite.setOrigin(0.5f, 0.5f);
        
        setPosition(x, y);
    }
    
    public void setPosition(float x, float y) {
    	this.x = x;
    	this.y = y;
    	sprite.setPosition(x, y);
    	fire.setPosition(x + 0.5f * width, y + 0.5f * height);
    }

    // Update our position.
    public void update() {
        delta = Gdx.graphics.getDeltaTime();
        x += vx * delta;
        y += vy * delta;
        
        setPosition(x, y);
        
        MainGame.positionCamera(x, y, 0.0f, 1.0f);
    }

    // Render the player.
    public void render(SpriteBatch batch) {
        // Draw the player sprite first.
        sprite.draw(batch);

        // Now draw the player fire.
        fire.draw(batch, delta);
    }

    // Change our velocity.
    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;

        // Scale this velocity to match the speed.
        float mul = (float) (speed / Math.sqrt(this.vx * this.vx + this.vy * this.vy));
        this.vx *= mul;
        this.vy *= mul;
    }

    // Disposal function.
    public void dispose() {
        texture.dispose();
        fire.dispose();
    }
}

