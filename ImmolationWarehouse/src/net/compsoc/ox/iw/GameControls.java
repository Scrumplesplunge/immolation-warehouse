package net.compsoc.ox.iw;

import com.badlogic.gdx.*;

public class GameControls implements InputProcessor, MainGameListener {
	
	private int xDir = 0;
	private int yDir = 0;
	private int frameWindow = 0;
	private boolean musicToggle = false;
	private int cycle = 0;
	
	public static float stunTimeout = 0.0f;

	@Override
	/**
	 * Add to the direction, so that if two conflicting keys are pressed,
	 * the result will be a zero total movement.
	 */
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1:
			MainGame.runLevel("level1");
			break;
		case Input.Keys.NUM_2:
			MainGame.runLevel("level2");
			break;
		case Input.Keys.NUM_3:
			MainGame.runLevel("level3");
			break;
		case Input.Keys.NUM_4:
			MainGame.runLevel("level4");
			break;
		case Input.Keys.NUM_5:
			MainGame.runLevel("level5");
			break;
		case Input.Keys.NUM_6:
			MainGame.runLevel("level6");
			break;
		case Input.Keys.NUM_7:
			MainGame.runLevel("level7");
			break;
		case Input.Keys.NUM_8:
			MainGame.runLevel("level8");
			break;
		case Input.Keys.NUM_9:
			MainGame.runLevel("level9");
			break;
		case Input.Keys.NUM_0:
			String[] bonus = {
				"demolevel"
			};
			MainGame.runLevel(bonus[cycle]);
			cycle = (cycle + 1) % bonus.length;
			break;
		case Input.Keys.W:
			//Travel upwards.
			yDir += 1;
			return true;
		case Input.Keys.A:
			//Travel left.
			xDir -= 1;
			return true;
		case Input.Keys.S:
			//Travel down.
			yDir -= 1;
			return true;
		case Input.Keys.D:
			//Travel right.
			xDir += 1;
			return true;
		case Input.Keys.UP:
			MainGame.nudgeCamera(0, 64);
			return true;
		case Input.Keys.LEFT:
			MainGame.nudgeCamera(-64, 0);
			return true;
		case Input.Keys.DOWN:
			MainGame.nudgeCamera(0, -64);
			return true;
		case Input.Keys.RIGHT:
			MainGame.nudgeCamera(64, 0);
			return true;
		case Input.Keys.M:
			if (!musicToggle) {
				MainGame.music.transition("music2", 2.5f);
				musicToggle = true;
			}
			else {
				MainGame.music.transition("music", 2.5f);
				musicToggle = false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
			//Stop travel upwards.
			frameWindow = 2;
			yDir -= 1;
			return true;
		case Input.Keys.A:
			//Stop travel left.
			frameWindow = 2;
			xDir += 1;
			return true;
		case Input.Keys.S:
			//Stop travel down.
			frameWindow = 2;
			yDir += 1;
			return true;
		case Input.Keys.D:
			//Stop travel right.
			frameWindow = 2;
			xDir -= 1;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * Set the direction the player is travelling when the frame ticks.
	 */
	public void onRender() {
		//If the player is attempting to move directions, change their facing.
		if (stunTimeout <= 0 && (xDir != 0 || yDir != 0) && frameWindow == 0) {
			MainGame.player.setVelocity(xDir, yDir);
		}
		frameWindow = Math.min(0, frameWindow);
		float delta = Gdx.graphics.getDeltaTime();
		stunTimeout -= delta;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
