// No need for ROWS and COLS here, use Board's constants instead

public abstract class AIPlayer {
   protected Cell[][] cells; // The game board's cells array
   protected Seed mySeed;    // Computer's seed
   protected Seed oppSeed;   // Opponent's seed

   /** Constructor with reference to game board */
   public AIPlayer(Board board) {
       cells = board.cells;  // Initialize cells with the board
   }

   /** Set/change the seed used by computer and opponent */
   public void setSeed(Seed seed) {
       this.mySeed = seed;
       oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
   }

   /** Abstract method to get next move. Return int[2] of {row, col} */
   abstract int[] move();  // To be implemented by subclasses
}
