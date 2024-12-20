/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231078 - Hilman Mumtaz Sya`bani
 * 2 - 5026231101 - Muhammad Akmal Rafiansyah
 * 3 - 5026231042 - Ervina Anggraini
 */

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
