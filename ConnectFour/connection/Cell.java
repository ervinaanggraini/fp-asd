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