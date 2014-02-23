package net.compsoc.ox.iw;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelFile {
	
	public static final String loggerTag = "LevelFile";
	public static Random r = new Random();
	
	public static Level loadLevel(String fileName) {
		MainGame.scoreAtLevelStart = MainGame.score;
        FileHandle levelFile = Gdx.files.internal(fileName + ".iw");
        if (!levelFile.exists()) {
            Gdx.app.error(loggerTag, "Level file " + fileName
                + " was not found.");
        }
        FileHandle possibleSoundFile = Gdx.files.internal("audio/" + fileName + ".ogg");
        //Play the sound file if it exists, otherwise play a burnman scream.
        if (possibleSoundFile.exists()) {
            MainGame.sound.add(fileName, fileName + ".ogg");
            MainGame.sound.play(fileName);
        }
        else {
        	int soundClipNo = r.nextInt(6);
        	MainGame.sound.play(MainGame.burnManScreams[soundClipNo]);
        }
        String creditsString = levelFile.readString();
        String[] lines = creditsString.split("\\n");
        Level level = new Level(lines, fileName);
        return level;
	}
	
}
