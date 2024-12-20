/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231078 - Hilman Mumtaz Sya`bani
 * 2 - 5026231101 - Muhammad Akmal Rafiansyah
 * 3 - 5026231042 - Ervina Anggraini
 */
package connection;

public class Cell {
  private Seed seed;
  private int row;
  private int col;
  public Seed content = Seed.EMPTY;
  

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    this.seed = Seed.EMPTY;
  }

  public Seed getSeed() {
    return seed;
  }

  public void setSeed(Seed seed) {
    this.seed = seed;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}