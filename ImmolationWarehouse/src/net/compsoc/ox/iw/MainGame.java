package net.compsoc.ox.iw;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.compsoc.ox.iw.common.MusicManager;
import aurelienribon.tweenengine.TweenManager;

public class MainGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public static MusicManager music = new MusicManager();
	public static TweenManager tweenManager = new TweenManager();
	
	private Level demoLevel;
	
	@Override
	public void create() {
		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(512, 512);
		batch = new SpriteBatch();
		
		demoLevel = new Level();
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
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		demoLevel.render(batch);
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
}
