package audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Enum untuk menangani semua efek suara dalam permainan.
 */
public enum SoundEffect {
   WIN("/audio/win.wav"),
   LOSE("/audio/lose.wav"),
   DRAW("/audio/draw.wav"),
   BACKSOUND("/audio/backsound.wav"),
   CLICKED("/audio/clicked.wav");

   private Clip clip;

   // Konstruktor untuk memuat file audio
   private SoundEffect(String soundFileName) {
      try {
         // Menggunakan getClass().getResource() untuk mengakses file audio dalam folder
         // yang sama
         URL url = this.getClass().getResource(soundFileName); // Mengakses file dalam folder yang sama dengan kelas ini

         if (url == null) {
            System.err.println("Error: File not found " + soundFileName);
         } else {
            System.out.println("File found: " + url);
         }

         // Memuat file audio dan menyiapkan clip
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         clip = AudioSystem.getClip();
         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
         e.printStackTrace();
      }
   }

   // Method untuk memainkan suara
   public void play() {
      if (clip != null) {
         if (clip.isRunning()) {
            clip.stop();
         }
         clip.setFramePosition(0); // Mulai dari awal
         clip.start();
      }
   }

   // Static method untuk memainkan backsound secara terus menerus
   public static void playBacksound() {
      BACKSOUND.play();
      BACKSOUND.clip.loop(Clip.LOOP_CONTINUOUSLY); // Memainkan backsound secara terus menerus
   }

   // Static method untuk menghentikan backsound
   public static void stopBacksound() {
      if (BACKSOUND.clip.isRunning()) {
         BACKSOUND.clip.stop();
      }
   }

   public static void playWinSound() {
      WIN.play();
   }

   public static void playLoseSound() {
      LOSE.play();
   }

   public static void playDrawSound() {
      DRAW.play();
   }

   public static void playBackSound() {
      BACKSOUND.play();
   }

   public static void playClickedSound() {
      CLICKED.play();
   }
}  