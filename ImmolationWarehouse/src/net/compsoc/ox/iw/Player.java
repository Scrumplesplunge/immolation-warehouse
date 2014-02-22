import net.compsoc.ox.iw.*;
import math;

class Player {
    // Configuration.
    private static final float speed = 1.0f;

    // Position and velocity.
    private float x, y, vx, vy;
    private float overheat = 0.0f;
    private Level level;
    
    // FIRE
    private FireEmitter fire;

    // Constructor.
    public Player(Level level, float x, float y) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = speed;
        this.fire = new FireEmitter(this.x, this.y);
    }

    // Update our position.
    public void update() {

    }

    // Render the player.
    public void render(SpriteBatch batch) {

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
}

