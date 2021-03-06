package net.compsoc.ox.iw;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.compsoc.ox.iw.common.MusicManager;
import net.compsoc.ox.iw.common.MusicTween;
import net.compsoc.ox.iw.common.SoundManager;
import aurelienribon.tweenengine.TweenManager;

public class MainGame implements ApplicationListener {
	
	public static final String loggerTag = "MainGame";
	
	private static OrthographicCamera camera;
	private static OrthographicCamera hudCam;
	private SpriteBatch batch;
	private SpriteBatch barBatch;
	private GameControls controls;
	public static MusicManager music = new MusicManager();
	public static SoundManager sound = new SoundManager();
	public static TweenManager tweenManager = new TweenManager();
	public static BitmapFont font;
	
	public static String[] burnManScreams;
	
	public static int score = 0;
	public static int scoreAtLevelStart = 0;
	public static int levelNo = 1;

    // Global instances are literally the best of the things.
	public static ParticleEffect fire = new ParticleEffect();
	public static ParticleEffect fire_reduced = new ParticleEffect();
    public static ParticleEffect barFire = new ParticleEffect();
    
    private static Sprite heat;
	
	private static Level currentLevel;
	public static Player player;
	
	private static boolean skipFrame = true;
	
	@Override
	public void create() {
		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();
		
		burnManScreams = new String[6];
		sound.add("scream1", "scream1.ogg");
		burnManScreams[0] = "scream1";
		sound.add("scream2", "scream2.ogg");
		burnManScreams[1] = "scream2";
		sound.add("scream3", "scream3.ogg");
		burnManScreams[2] = "scream3";
		sound.add("scream4", "scream4.ogg");
		burnManScreams[3] = "scream4";
		sound.add("scream5", "scream5.ogg");
		burnManScreams[4] = "scream5";
		sound.add("scream6", "scream6.ogg");
		burnManScreams[5] = "scream6";
		
		Texture texture = new Texture(Gdx.files.internal("heat.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion region = new TextureRegion(texture, 0, 0, 8, 8);
		heat = new Sprite(region);

		// IT'S A HACK
		fire.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));

		// IT'S A HACK
		fire_reduced.load(Gdx.files.internal("fire_reduced.p"), Gdx.files.internal(""));
		
		barFire.load(Gdx.files.internal("barfire.p"), Gdx.files.internal(""));
		
		controls = new GameControls();
		Gdx.input.setInputProcessor(controls);
		
		camera = new OrthographicCamera(512, 512);
		hudCam = new OrthographicCamera(512, 512);
		batch = new SpriteBatch();
		barBatch = new SpriteBatch();
		
		currentLevel = LevelFile.loadLevel("hub");
		//currentLevel = new Level(ProceduralLevelGenerator.generateLevel(40,30), "proclevel");
		player = new Player(currentLevel);
		
		//Font!
		final String fontFileName = "font/scp.fnt";
        final FileHandle fontFile = Gdx.files.internal(fontFileName);
        if (!fontFile.exists()) {
            Gdx.app.error(MainGame.loggerTag, "Font file " + fontFileName
                + " was not found.");
        }
        MainGame.font = new BitmapFont(fontFile, false);
        font.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		
		//Music!
        new MusicTween();
		music.add("music", "immolationwarehouse.ogg");
		music.add("music2", "slowburn.ogg");
		music.setVolume(0.45f);
		music.setLooping("music", true);
		music.play("music");
		sound.add("failure", "failure.ogg");
		sound.add("splosion", "splosion.ogg");
	}

	@Override
	public void dispose() {
		batch.dispose();
		barBatch.dispose();
		currentLevel.dispose();
		fire.dispose();
		music.onDispose();
		sound.onDispose();
	}

	@Override
	public void render() {
		if(skipFrame) {
			skipFrame = false;
			return;
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		// Update things
		float delta = Gdx.graphics.getDeltaTime();

		tweenManager.update(delta);
		controls.onRender();
		currentLevel.update(delta);
		player.update(delta);
		
		// Render things
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		currentLevel.render(batch);
		for (NPC n : currentLevel.npcs) n.render(batch);
		player.render(batch);
		batch.end();
		drawHUD();
		
		// Check if the player fell out of the map.
		if (currentLevel.getTileAt((int)(player.getX() / Tile.tileWidth), (int)(player.getY() / Tile.tileHeight)) == null)
			levelFailed();
		
		// Check for endgame.
		AABB end = currentLevel.getTileAt(currentLevel.getEndX(), currentLevel.getEndY()).getAABB();
		AABB ply = player.getAABB();
		if (end.collidesWith(ply)) {
			advanceLevel();
		} else if (currentLevel.isHub()) {
			AABB[] hubends = currentLevel.getHubEnds();
			String[] hublinks = currentLevel.getHubLinks();
			for(int k = 0; k < 9; k++) {
				if (hubends[k].collidesWith(ply)) {
					runLevel(hublinks[k]);
				}
			}
		}
	}
	

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		hudCam.setToOrtho(false, width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public static void levelFailed() {
		skipFrame = true;
		score = scoreAtLevelStart;
		String restartname = currentLevel.levelName;
		currentLevel.dispose();
		currentLevel = LevelFile.loadLevel(restartname);
		sound.play("failure");
		player = new Player(currentLevel);
	}
	
	public static void advanceLevel() {
		/*
		levelNo += 1;
		String title = "level" + levelNo;
		runLevel(title);
		*/
		skipFrame = true;
		runLevel("hub");
	}
	
	public static void runLevel(String title) {
		skipFrame = true;
		scoreAtLevelStart = score;
		currentLevel.dispose();
		currentLevel = LevelFile.loadLevel(title);
		player = new Player(currentLevel);
	}
	
	// Reposition camera
	public static void positionCamera(float x, float y, float rotation, float zoom) {
		camera.position.set(x, y, 0.0f);
		camera.up.set(-(float)Math.sin(rotation), (float)Math.cos(rotation), 0.0f);
		camera.zoom = zoom;
		camera.update();
	}
	
	public static void nudgeCamera(float x, float y) {
		camera.position.set(camera.position.x + x, camera.position.y + y, 0.0f);
		camera.update();
	}
	
	private void drawHUD() {
		barBatch.setProjectionMatrix(hudCam.combined);
		barBatch.begin();
		font.draw(barBatch, String.format("Score: %d", score), 0,
				Gdx.graphics.getHeight() - font.getCapHeight() - 10);
		font.draw(barBatch, String.format("Level: %s", currentLevel.levelName), 0,
				Gdx.graphics.getHeight() - 5);
		float heatWidth = (Gdx.graphics.getWidth() * player.overheat);
		heat.setPosition(0, 0);
		heat.setSize((int) heatWidth, 8);
		heat.draw(barBatch);
		barFire.setPosition(heatWidth - 48, 0);
		barFire.draw(barBatch, Gdx.graphics.getDeltaTime());
		barBatch.end();
	}
}
