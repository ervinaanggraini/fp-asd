import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = new Color(0, 0, 139);
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("Poppins", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private Seed humanSeed;      // human player's seed choice
    private JLabel statusBar;    // for displaying status message
    private AIPlayer aiPlayer;   // AI player object
    private int xWins = 0;   // Skor untuk X
   private int oWins = 0;   // Skor untuk O
   private int draws = 0;   // Skor untuk Draw


    /** Constructor to setup the UI and game components */
    public GameMain() {
        // Ask the player to choose between "X" or "O" at the start of the game
        Object[] options = { "X", "O" };
        int choice = JOptionPane.showOptionDialog(null,
            "Choose your symbol", "Player Choice",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);

        // Set the human player's seed and initialize the AI player accordingly
        if (choice == 0) {
            humanSeed = Seed.CROSS;
        } else {
            humanSeed = Seed.NOUGHT;
        }

        // Initialize the game board and AI player
        initGame();

        // Initialize AI player with the opposite seed
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(humanSeed == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS); // AI plays as the opposite seed

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                  // Check if clicked position is valid and the cell is empty
                  if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                      && board.cells[row][col].content == Seed.NO_SEED) {
                      // Player makes a move
                      currentState = board.stepGame(currentPlayer, row, col);
          
                      // Switch to the next player if the game is still in progress
                      if (currentState == State.PLAYING) {
                          currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
          
                          // AI will play only if game is still ongoing
                          if (currentState == State.PLAYING) {
                              // Call aiMove() to make AI move after human player's move
                              aiMove();
                          }
                      }
                  }
                } else {        // game over
                    newGame();  // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Now that everything is initialized, start a new game
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();  // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
      // Reset game board
      for (int row = 0; row < Board.ROWS; ++row) {
          for (int col = 0; col < Board.COLS; ++col) {
              board.cells[row][col].content = Seed.NO_SEED; // all cells empty
          }
      }
      
      currentPlayer = humanSeed;    // Human player starts
      currentState = State.PLAYING;  // Ready to play
      repaint();  // Update display
  }
  

    /** Method for AI to automatically make a move */
    public void aiMove() {
      if (currentState == State.PLAYING) {
          // Get the AI's move
          int[] aiMove = aiPlayer.move();
          int aiRow = aiMove[0];
          int aiCol = aiMove[1];
  
          // Update the game board with the AI's move
          currentState = board.stepGame(currentPlayer, aiRow, aiCol);
  
          // If the game is over, update the score
          if (currentState != State.PLAYING) {
              updateScore();
          }
  
          // Switch player after AI's move
          if (currentState == State.PLAYING) {
              currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
          }
      }
  
      repaint();  // Callback paintComponent().
  }
  private void updateScore() {
      if (currentState == State.CROSS_WON) {
       xWins++;
       } else if (currentState == State.NOUGHT_WON) {
       oWins++;
      } else if (currentState == State.DRAW) {
       draws++;
      }
   }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG); // Set its background color
        board.paint(g);  // Ask the game board to paint itself
    
        // Print status-bar message with the score
        String statusMessage = "X " + xWins + " | Draw " + draws + " | O " + oWins;
        statusBar.setText(statusMessage);  // Display the score in the status bar
    }
    

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Set the content-pane of the JFrame to an instance of main JPanel
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }
}
