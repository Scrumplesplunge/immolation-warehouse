package net.compsoc.ox.iw.common;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A class to allow Sprites to be tweened and animated.
 * 
 * @author CSD Elliot
 * 
 */
public class SpriteTween implements TweenAccessor<Sprite> {
    
    /**
     * Tween the Alpha of the sprite's colour.
     */
    public static final int Alpha = 0;
    
    /**
     * The instance of this class.
     */
    private static SpriteTween Instance;
    
    /**
     * Used for error logging.
     */
    private static final String loggerTag = "SpriteTween";
    
    /**
     * Register the class with the Tween engine.
     */
    public SpriteTween() {
        if (SpriteTween.Instance == null) {
            SpriteTween.Instance = this;
            Tween.registerAccessor(Sprite.class, SpriteTween.Instance);
        }
        else {
            Gdx.app.error(SpriteTween.loggerTag,
                "SpriteTween has already been initialised.");
            Gdx.app.exit();
        }
    }
    
    @Override
    public int getValues(final Sprite target, final int tweenType,
        final float[] returnValues) {
        switch (tweenType) {
            case Alpha:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                Gdx.app.error(SpriteTween.loggerTag,
                    "Nonexistent tween type requested" + "for this Sprite.");
                Gdx.app.exit();
                break;
        }
        return 0;
    }
    
    @Override
    public void setValues(final Sprite target, final int tweenType,
        final float[] newValues) {
        switch (tweenType) {
            case Alpha:
                final Color c = target.getColor();
                target.setColor(c.r, c.g, c.b, newValues[0]);
                break;
            default:
                Gdx.app.error(SpriteTween.loggerTag,
                    "Nonexistent tween type requested" + "for this Sprite.");
                Gdx.app.exit();
                break;
        }
    }
    
}
