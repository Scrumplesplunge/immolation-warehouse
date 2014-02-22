import net.compsoc.ox.iw.*;
import math;

class Player {
    // Configuration.
    private static final float speed = 1.0f;
    private static final float width = 64.0f, height = 64.0f;

    // Position and velocity.
    private float x, y, vx, vy;
    private float overheat = 0.0f;

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
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = speed;

        // Load the fire effect.
        fire = new ParticleEffect();

        // Set up the player texture and sprite.
        texture = new Texture(Gdx.files.internal("character.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        TextureRegion region = new TextureRegion(texture, 0.0f, 0.0f, 1.0f, 1.0f);

        sprite = new Sprite(region);
        sprite.setScale(1.0f, 1.0f);
        sprite.setOrigin(0.5f, 0.5f);
        sprite.setPosition(x, y);
    }

    // Update our position.
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        x += vx * delta;
        y += vy * delta;
    }

    // Render the player.
    public void render(SpriteBatch batch) {
        // Draw the player sprite first.
        sprite.draw(batch);

        // Now draw the player fire.
        
    }

    // Change our velocity.
    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;

        // Scale this velocity to match the speed.
        float mul = speed / math.sqrt(this.vx * this.vx + this.vy * this.vy);
        this.vx *= mul;
        this.vy *= mul;
    }

    // Disposal function.
    public void dispose() {
        texture.dispose();
    }
}

