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

import javax.swing.*;

import audio.SoundEffect;

import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame {
  private static final long serialVersionUID = 1L;

  public static final int BOARD_ROWS = 6;
  public static final int BOARD_COLS = 7;
  public static final int CELL_SIZE = 120;

  private Board board;
  private State gameState;
  private Seed currentPlayer;
  private int[][] winningCells; // To store the coordinates of the winning cells

  private BoardPanel gameBoardPanel;
  private JLabel gameStatusBar;

  private boolean isSinglePlayer;

  public GameMain(boolean isSinglePlayer) {
    this.isSinglePlayer = isSinglePlayer; // Set the game mode
    board = new Board(BOARD_ROWS, BOARD_COLS);
    gameState = State.IN_PROGRESS;
    currentPlayer = Seed.PLAYER_ONE;
    winningCells = new int[BOARD_ROWS][BOARD_COLS]; // Initialize with empty cells

    gameBoardPanel = new BoardPanel();
    gameBoardPanel.setPreferredSize(new Dimension(CELL_SIZE * BOARD_COLS, CELL_SIZE * BOARD_ROWS));
    gameBoardPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (gameState == State.IN_PROGRESS) {
          int col = e.getX() / CELL_SIZE;
          if (board.placeSeed(col, currentPlayer)) {
            repaint();
            SoundEffect.playClickedSound();

            if (board.checkWin(currentPlayer)) {
              gameState = (currentPlayer == Seed.PLAYER_ONE) ? State.PLAYER_ONE_WINS : State.PLAYER_TWO_WINS;
              SoundEffect.playWinSound();

              // Get winning cells (coordinates)
              winningCells = board.getWinningCells(currentPlayer);

              // Display win notification
              JOptionPane.showMessageDialog(GameMain.this,
                  (currentPlayer == Seed.PLAYER_ONE ? "Player 1 Wins!" : "Player 2 Wins!") +
                      " Click OK to restart.",
                  "Game Over", JOptionPane.INFORMATION_MESSAGE);
              resetGame();
            } else if (board.isFull()) {
              gameState = State.DRAW;
              SoundEffect.playDrawSound();

              // Display draw notification
              JOptionPane.showMessageDialog(GameMain.this,
                  "It's a draw! Click OK to restart.",
                  "Game Over", JOptionPane.INFORMATION_MESSAGE);
              resetGame();
            } else {
              currentPlayer = (currentPlayer == Seed.PLAYER_ONE) ? Seed.PLAYER_TWO : Seed.PLAYER_ONE;

              // If it's a single-player game and it's Player 2's (AI's) turn
              if (isSinglePlayer && currentPlayer == Seed.PLAYER_TWO && gameState == State.IN_PROGRESS) {
                aiTurn(); // Let AI make its move
              }
            }
          }
        } else {
          resetGame();
        }
      }
    });

    gameStatusBar = new JLabel("       ");
    gameStatusBar.setFont(new Font("Arial", Font.PLAIN, 14));
    gameStatusBar.setOpaque(true);

    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(gameBoardPanel, BorderLayout.CENTER);
    cp.add(gameStatusBar, BorderLayout.PAGE_END);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setTitle("Connect Four");
    setVisible(true);

    SoundEffect.playBackSound();
    resetGame();
  }

  private void aiTurn() {
    // AI logic for a single-player game (example: random move)
    new Thread(() -> {
      try {
        Thread.sleep(1000); // Simulate AI thinking time
        int col = (int) (Math.random() * BOARD_COLS); // Simple random AI
        while (!board.placeSeed(col, Seed.PLAYER_TWO)) {
          col = (int) (Math.random() * BOARD_COLS);
        }
        repaint();

        if (board.checkWin(Seed.PLAYER_TWO)) {
          gameState = State.PLAYER_TWO_WINS;
          SoundEffect.playWinSound();
          winningCells = board.getWinningCells(Seed.PLAYER_TWO);
          JOptionPane.showMessageDialog(GameMain.this,
              "Player 2 (AI) Wins! Click OK to restart.",
              "Game Over", JOptionPane.INFORMATION_MESSAGE);
          resetGame();
        } else if (board.isFull()) {
          gameState = State.DRAW;
          SoundEffect.playDrawSound();
          JOptionPane.showMessageDialog(GameMain.this,
              "It's a draw! Click OK to restart.",
              "Game Over", JOptionPane.INFORMATION_MESSAGE);
          resetGame();
        } else {
          currentPlayer = Seed.PLAYER_ONE; // Switch back to Player 1
        }
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  private void showMainMenu() {
    JPanel mainMenuPanel = new JPanel();
    mainMenuPanel.setLayout(new BorderLayout());
    JLabel welcomeLabel = new JLabel("Welcome to Connect Four!", JLabel.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
    mainMenuPanel.add(welcomeLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

    JButton playButton = new JButton("Play");
    playButton.setFont(new Font("Arial", Font.PLAIN, 18));
    playButton.addActionListener(e -> showPlayerSelectionMenu());

    JButton exitButton = new JButton("Exit");
    exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
    exitButton.addActionListener(e -> System.exit(0));

    buttonPanel.add(playButton);
    buttonPanel.add(exitButton);
    mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

    getContentPane().removeAll();
    getContentPane().add(mainMenuPanel);
    revalidate();
    repaint();
  }

  private void showPlayerSelectionMenu() {
    String[] options = { "1 Player", "2 Players" };
    int choice = JOptionPane.showOptionDialog(this, "Select number of players", "Game Mode",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

    if (choice == 0) {
      new GameMain(true); // Start game in single player mode
    } else {
      new GameMain(false); // Start game in two player mode
    }
    dispose(); // Close the menu window
  }

  public void resetGame() {
    board = new Board(BOARD_ROWS, BOARD_COLS);
    currentPlayer = Seed.PLAYER_ONE;
    gameState = State.IN_PROGRESS;
    winningCells = new int[BOARD_ROWS][BOARD_COLS]; // Reset winning cells
    repaint();
  }

  class BoardPanel extends JPanel {
    private Image demonImage;
    private Image angelImage;

    public BoardPanel() {
      demonImage = new ImageIcon(getClass().getResource("/image/demon.png")).getImage();
      angelImage = new ImageIcon(getClass().getResource("/image/angel.png")).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      setBackground(Color.WHITE);

      for (int row = 0; row < BOARD_ROWS; row++) {
        for (int col = 0; col < BOARD_COLS; col++) {
          int x = col * CELL_SIZE;
          int y = row * CELL_SIZE;

          // If the cell is part of the winning line, change the color
          if (winningCells[row][col] == 1) {
            g.setColor(Color.RED); // Red for winning cells
          } else {
            g.setColor(Color.black); // Default color
          }

          g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
          g.setColor(Color.white);
          g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

          Cell cell = board.getCells()[row][col];
          if (cell.getSeed() == Seed.PLAYER_ONE) {
            g.drawImage(demonImage, x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20, this);
          } else if (cell.getSeed() == Seed.PLAYER_TWO) {
            g.drawImage(angelImage, x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20, this);
          }
        }
      }

      if (gameState == State.IN_PROGRESS) {
        gameStatusBar.setText((currentPlayer == Seed.PLAYER_ONE) ? "Player 1's Turn" : "Player 2's Turn");
      } else if (gameState == State.DRAW) {
        gameStatusBar.setText("Draw! Click to restart.");
      } else if (gameState == State.PLAYER_ONE_WINS) {
        gameStatusBar.setText("Player 1 Wins! Click to restart.");
      } else if (gameState == State.PLAYER_TWO_WINS) {
        gameStatusBar.setText("Player 2 Wins! Click to restart.");
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      GameMain gameMain = new GameMain(false);
      gameMain.showMainMenu(); // Show the main menu
    });
  }
}