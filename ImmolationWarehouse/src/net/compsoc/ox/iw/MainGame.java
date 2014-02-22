package net.compsoc.ox.iw;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.compsoc.ox.iw.common.MusicManager;
import aurelienribon.tweenengine.TweenManager;

public class MainGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private GameControls controls;
	public static MusicManager music = new MusicManager();
	public static TweenManager tweenManager = new TweenManager();

    // Global instances are literally the best of the things.
    public static ParticleEffect fire = new ParticleEffect();
	
	private Level demoLevel;
	private Player daPlayerMan;
	
	@Override
	public void create() {
		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();
		
		// IT'S A HACK
		fire.load(Gdx.files.internal("fire.p"), Gdx.files.internal("."));
		
		controls = new GameControls();
		
		camera = new OrthographicCamera(512, 512);
		batch = new SpriteBatch();
		
		demoLevel = new Level();
		daPlayerMan = new Player(demoLevel, 32.0f, 32.0f);
	}

	@Override
	public void dispose() {
		demoLevel.dispose();
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Make sure the controls update.
		controls.onRender();
		
		daPlayerMan.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		demoLevel.render(batch);
		daPlayerMan.render(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	// Reposition camera
	public void positionCamera(float x, float y, float rotation, float zoom) {
		camera.position.set(x, y, 0.0f);
		camera.up.set((float)Math.cos(rotation), (float)Math.sin(rotation), 0.0f);
		camera.zoom = zoom;
		camera.update();
	}
}
