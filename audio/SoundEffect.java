package audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This enum encapsulates all the sound effects of a game.
 */
public enum SoundEffect {
   EAT_FOOD("audio/eatfood.wav"),
   EXPLODE("audio/explode.wav"),
   DIE("audio/die.wav");

   public static enum Volume {
      MUTE, LOW, MEDIUM, HIGH
   }

   public static Volume volume = Volume.LOW;
   private Clip clip;

   private SoundEffect(String soundFileName) {
      try {
         URL url = this.getClass().getClassLoader().getResource(soundFileName);
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         clip = AudioSystem.getClip();
         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
         e.printStackTrace();
      }
   }

   public void play() {
      if (volume != Volume.MUTE) {
         if (clip.isRunning())
            clip.stop();
         clip.setFramePosition(0);
         clip.start();
      }
   }

   static void initGame() {
      values();
   }
}