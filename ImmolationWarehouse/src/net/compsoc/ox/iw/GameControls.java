package net.compsoc.ox.iw;

import com.badlogic.gdx.*;

public class GameControls implements InputProcessor, MainGameListener {
	
	private int xDir = 0;
	private int yDir = 0;

	@Override
	/**
	 * Add to the direction, so that if two conflicting keys are pressed,
	 * the result will be a zero total movement.
	 */
	public boolean keyDown(int keycode) {
		switch (keycode) {
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
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
			//Stop travel upwards.
			yDir -= 1;
			return true;
		case Input.Keys.A:
			//Stop travel left.
			xDir += 1;
			return true;
		case Input.Keys.S:
			//Stop travel down.
			yDir += 1;
			return true;
		case Input.Keys.D:
			//Stop travel right.
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
		if (xDir != 0 || yDir != 0) {
			MainGame.player.setVelocity(xDir, yDir);
		}
		
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
