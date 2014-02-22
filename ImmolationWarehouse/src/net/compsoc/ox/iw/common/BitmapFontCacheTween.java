package net.compsoc.ox.iw.common;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;

/**
 * A class to allow BitmapFontCaches to be tweened.
 * 
 * @author CSD Elliot
 * 
 */
public class BitmapFontCacheTween implements TweenAccessor<BitmapFontCache> {
    
    /**
     * Tween the Alpha of the sprite's colour.
     */
    public static final int Alpha = 0;
    
    /**
     * The instance of this class.
     */
    private static BitmapFontCacheTween Instance;
    
    /**
     * Used for error logging.
     */
    private static final String loggerTag = "BitmapFontCacheTween";
    
    /**
     * Register the class with the Tween engine.
     */
    public BitmapFontCacheTween() {
        if (BitmapFontCacheTween.Instance == null) {
            BitmapFontCacheTween.Instance = this;
            Tween.registerAccessor(BitmapFontCache.class,
                BitmapFontCacheTween.Instance);
        }
        else {
            Gdx.app.error(BitmapFontCacheTween.loggerTag,
                "SpriteTween has already been initialised.");
            Gdx.app.exit();
        }
    }
    
    @Override
    public int getValues(final BitmapFontCache target, final int tweenType,
        final float[] returnValues) {
        switch (tweenType) {
            case Alpha:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                Gdx.app.error(BitmapFontCacheTween.loggerTag,
                    "Nonexistent tween type requested"
                        + "for this BitmapFontCache.");
                Gdx.app.exit();
                break;
        }
        return 0;
    }
    
    @Override
    public void setValues(final BitmapFontCache target, final int tweenType,
        final float[] newValues) {
        switch (tweenType) {
            case Alpha:
                final Color c = target.getColor();
                target.setColor(c.r, c.g, c.b, newValues[0]);
                break;
            default:
                Gdx.app.error(BitmapFontCacheTween.loggerTag,
                    "Nonexistent tween type requested"
                        + "for this BitmapFontCache.");
                Gdx.app.exit();
                break;
        }
    }
    
}
