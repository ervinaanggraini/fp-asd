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

public class Board {
  public static final int ROWS = 6;
  public static final int COLS = 7;
  private Cell[][] cells;
  private int lastRow;
  private int lastCol;

  public Board(int rows, int cols) {
    cells = new Cell[rows][cols];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        cells[row][col] = new Cell(row, col);
      }
    }
  }

  public boolean placeSeed(int col, Seed seed) {
    for (int row = cells.length - 1; row >= 0; row--) {
      if (cells[row][col].getSeed() == Seed.EMPTY) {
        cells[row][col].setSeed(seed);
        return true;
      }
    }
    return false;
  }

  public boolean checkWin(Seed seed) {
    // Check for horizontal, vertical, and diagonal wins
    return checkHorizontal(seed) || checkVertical(seed) || checkDiagonals(seed);
  }

  private boolean checkHorizontal(Seed seed) {
    for (int row = 0; row < cells.length; row++) {
      int count = 0;
      for (int col = 0; col < cells[0].length; col++) {
        if (cells[row][col].getSeed() == seed) {
          count++;
          if (count == 4)
            return true;
        } else {
          count = 0;
        }
      }
    }
    return false;
  }

  private boolean checkVertical(Seed seed) {
    for (int col = 0; col < cells[0].length; col++) {
      int count = 0;
      for (int row = 0; row < cells.length; row++) {
        if (cells[row][col].getSeed() == seed) {
          count++;
          if (count == 4)
            return true;
        } else {
          count = 0;
        }
      }
    }
    return false;
  }

  private boolean checkDiagonals(Seed seed) {
    // Check descending diagonal
    for (int row = 0; row < cells.length - 3; row++) {
      for (int col = 0; col < cells[0].length - 3; col++) {
        if (cells[row][col].getSeed() == seed &&
            cells[row + 1][col + 1].getSeed() == seed &&
            cells[row + 2][col + 2].getSeed() == seed &&
            cells[row + 3][col + 3].getSeed() == seed) {
          return true;
        }
      }
    }

    // Check ascending diagonal
    for (int row = 3; row < cells.length; row++) {
      for (int col = 0; col < cells[0].length - 3; col++) {
        if (cells[row][col].getSeed() == seed &&
            cells[row - 1][col + 1].getSeed() == seed &&
            cells[row - 2][col + 2].getSeed() == seed &&
            cells[row - 3][col + 3].getSeed() == seed) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean isFull() {
    for (int row = 0; row < cells.length; row++) {
      for (int col = 0; col < cells[0].length; col++) {
        if (cells[row][col].getSeed() == Seed.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  public int[][] getWinningCells(Seed currentPlayer) {
    int[][] winningCells = new int[ROWS][COLS];

    // Check horizontal, vertical, and diagonal directions for a win
    if (checkWinDirection(currentPlayer, winningCells)) {
      return winningCells;
    }

    return new int[ROWS][COLS]; // Return empty array if no win
  }

  private boolean checkWinDirection(Seed currentPlayer, int[][] winningCells) {
    // Horizontal check
    int count = 0;
    for (int col = lastCol - 3; col <= lastCol + 3; col++) {
      if (col >= 0 && col < COLS && cells[lastRow][col].getSeed() == currentPlayer) {
        winningCells[lastRow][col] = 1;
        count++;
        if (count == 4)
          return true; // If there are 4 consecutive cells, return true
      } else {
        count = 0; // Reset count if the sequence is broken
      }
    }

    // Vertical check
    count = 0;
    for (int row = lastRow - 3; row <= lastRow + 3; row++) {
      if (row >= 0 && row < ROWS && cells[row][lastCol].getSeed() == currentPlayer) {
        winningCells[row][lastCol] = 1;
        count++;
        if (count == 4)
          return true; // If there are 4 consecutive cells, return true
      } else {
        count = 0; // Reset count if the sequence is broken
      }
    }

    // Diagonal check (Top-left to Bottom-right)
    count = 0;
    for (int i = -3; i <= 3; i++) {
      int row = lastRow + i;
      int col = lastCol + i;
      if (row >= 0 && row < ROWS && col >= 0 && col < COLS &&
          cells[row][col].getSeed() == currentPlayer) {
        winningCells[row][col] = 1;
        count++;
        if (count == 4)
          return true; // If there are 4 consecutive cells, return true
      } else {
        count = 0; // Reset count if the sequence is broken
      }
    }

    // Diagonal check (Bottom-left to Top-right)
    count = 0;
    for (int i = -3; i <= 3; i++) {
      int row = lastRow - i;
      int col = lastCol + i;
      if (row >= 0 && row < ROWS && col >= 0 && col < COLS &&
          cells[row][col].getSeed() == currentPlayer) {
        winningCells[row][col] = 1;
        count++;
        if (count == 4)
          return true; // If there are 4 consecutive cells, return true
      } else {
        count = 0; // Reset count if the sequence is broken
      }
    }

    return false; // No win found
  }

  public void removeSeed(int col) {
    for (int row = 0; row < cells.length; row++) {
      if (cells[row][col].getSeed() != Seed.EMPTY) {
        cells[row][col].setSeed(Seed.EMPTY);
        break;
      }
    }
  }

  public Cell[][] getCells() {
    return cells;
  }
}