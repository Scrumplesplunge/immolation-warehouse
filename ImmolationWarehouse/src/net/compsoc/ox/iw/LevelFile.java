package net.compsoc.ox.iw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelFile {
	
	public static final String loggerTag = "LevelFile";
	
	public static Level loadLevel(String fileName) {
        FileHandle levelFile = Gdx.files.internal(fileName);
        if (!levelFile.exists()) {
            Gdx.app.error(loggerTag, "Level file " + fileName
                + " was not found.");
        }
        String creditsString = levelFile.readString();
        String[] lines = creditsString.split("\\n");
        Level level = new Level(lines);
        return level;
	}
	
}
