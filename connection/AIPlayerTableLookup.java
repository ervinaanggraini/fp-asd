/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231078 - Hilman Mumtaz Sya`bani
 * 2 - 5026231101 - Muhammad Akmal Rafiansyah
 * 3 - 5026231042 - Ervina Anggraini
 */
public class AIPlayerTableLookup extends AIPlayer {
 
   // Moves {row, col} in order of preferences. {0, 0} at top-left corner
   private int[][] preferredMoves = {
         {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
         {0, 1}, {1, 0}, {1, 2}, {2, 1}};
 
   /** constructor */
   public AIPlayerTableLookup(Board board) {
      super(board);
   }
 
   /** Search for the first empty cell, according to the preferences
    *  Assume that next move is available, i.e., not gameover
    *  @return int[2] of {row, col}
    */
   @Override
   public int[] move() {
      for (int[] move : preferredMoves) {
         if (cells[move[0]][move[1]].content == Seed.NO_SEED) {
            return move;
         }
      }
      assert false : "No empty cell?!";
      return null;
   }
}  