package net.compsoc.ox.iw.common;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Auxiliary functions for graphics.
 * 
 * @author CSD Elliot
 * 
 */
public class GraphicsFunctions {
    
    /**
     * Sprite fade in.
     * 
     * @param manager
     *            The tween manager.
     * @param duration
     *            The duration of the fade.
     * @param delay
     *            The delay before the fade starts.
     * @param sprites
     *            The sprites to perform the fade on.
     */
    public static void fadeIn(TweenManager manager, float duration,
        float delay, Sprite... sprites) {
        for (Sprite s : sprites) {
            if (duration != 0) {
                Tween.to(s, SpriteTween.Alpha, 0).target(0f).delay(delay)
                    .start(manager);
            }
            Tween.to(s, SpriteTween.Alpha, duration).target(1f).delay(delay)
                .start(manager);
        }
    }
    
    /**
     * Sprite fade out.
     * 
     * @param manager
     *            The tween manager.
     * @param duration
     *            The duration of the fade.
     * @param delay
     *            The delay before the fade starts.
     * @param sprites
     *            The sprites to perform the fade on.
     */
    public static void fadeOut(TweenManager manager, float duration,
        float delay, Sprite... sprites) {
        for (Sprite s : sprites) {
            if (duration != 0) {
                Tween.to(s, SpriteTween.Alpha, 0).target(1f).delay(delay)
                    .start(manager);
            }
            Tween.to(s, SpriteTween.Alpha, duration).target(0f).delay(delay)
                .start(manager);
        }
    }
    
    /**
     * Centre a sprite on the screen.
     * 
     * @param sprite
     *            The sprite to centre.
     * @param camera
     *            The camera to centre the sprite in.
     */
    public static void centre(Sprite sprite, OrthographicCamera camera) {
        sprite.setPosition(
            (camera.viewportWidth / 2) - (sprite.getWidth() / 2),
            (camera.viewportHeight / 2) - (sprite.getHeight() / 2));
    }
    
    /**
     * Compute the coordinate of an inner object given its dimension and the two
     * points of the outer object so that it will be centred when placed there.
     * 
     * @param outerFirst
     *            The first point of the outer object.
     * @param outerLast
     *            The last point of the outer object.
     * @param innerSize
     *            The size of the inner object.
     * @return The coordinate to place the inner object at for it to be centred.
     */
    public static float relativeCentre(float outerFirst, float outerLast,
        float innerSize) {
        return outerFirst + (Math.abs(outerLast - outerFirst - innerSize) / 2f);
    }
}
