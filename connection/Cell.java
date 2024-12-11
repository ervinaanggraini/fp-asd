import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
   // Define named constants for drawing
   public static final int SIZE = 120; // cell width/height (square)
   // Symbols (cross/nought) are displayed inside a cell, with padding from border
   public static final int PADDING = SIZE / 5;
   public static final int SEED_SIZE = SIZE - PADDING * 2;
   public static final int SEED_STROKE_WIDTH = 8; // pen's stroke width
   public static final int IMAGE_SIZE = SIZE - PADDING * 2; // Image size

   // Define properties (package-visible)
   /** Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT) */
   Seed content;
   /** Row and column of this cell */
   int row, col;

   // Image references
   private static BufferedImage crossImage;
   private static BufferedImage noughtImage;

   static {
      try {
         crossImage = ImageIO.read(new File("image/x.png"));
         noughtImage = ImageIO.read(new File("image/o.png"));
      } catch (IOException e) {
         e.printStackTrace(); // Handle error if the image files aren't found
      }
   }

   /** Constructor to initialize this cell with the specified row and col */
   public Cell(int row, int col) {
      this.row = row;
      this.col = col;
      content = Seed.NO_SEED;
   }

   /** Reset this cell's content to EMPTY, ready for new game */
   public void newGame() {
      content = Seed.NO_SEED;
   }

   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      // Use Graphics2D which allows us to set the pen's stroke
      Graphics2D g2d = (Graphics2D)g;
      g2d.setStroke(new BasicStroke(SEED_STROKE_WIDTH,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      // Draw the Seed if it is not empty
      int x1 = col * SIZE + PADDING;
      int y1 = row * SIZE + PADDING;
      // if (content == Seed.CROSS) {
      //    g2d.setColor(GameMain.COLOR_CROSS);  // draw a 2-line cross
      //    int x2 = (col + 1) * SIZE - PADDING;
      //    int y2 = (row + 1) * SIZE - PADDING;
      //    g2d.drawLine(x1, y1, x2, y2);
      //    g2d.drawLine(x2, y1, x1, y2);
      // } else if (content == Seed.NOUGHT) {  // draw a circle
      //    g2d.setColor(GameMain.COLOR_NOUGHT);
      //    g2d.drawOval(x1, y1, SEED_SIZE, SEED_SIZE);
      // }

      if (content == Seed.CROSS && crossImage != null) {
         // Draw the cross image in the cell
         g2d.drawImage(crossImage, x1, y1, IMAGE_SIZE, IMAGE_SIZE, null);
      } else if (content == Seed.NOUGHT && noughtImage != null) {
         // Draw the nought image in the cell
         g2d.drawImage(noughtImage, x1, y1, IMAGE_SIZE, IMAGE_SIZE, null);
      }
   }
}