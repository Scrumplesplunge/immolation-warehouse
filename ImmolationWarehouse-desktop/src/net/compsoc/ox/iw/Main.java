package net.compsoc.ox.iw;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Immolation Warehouse";
		cfg.useGL20 = false;
		cfg.width = 512;
		cfg.height = 512;
		
		new LwjglApplication(new MainGame(), cfg);
	}
}