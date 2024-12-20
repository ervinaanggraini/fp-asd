/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231078 - Hilman Mumtaz Sya`bani
 * 2 - 5026231101 - Muhammad Akmal Rafiansyah
 * 3 - 5026231042 - Ervina Anggraini
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import audio.SoundEffect;

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
    public static final Font FONT_DEFAULT = new Font("OCR A Extended", Font.PLAIN, 14);  // Ganti "Poppins" dengan font pilihan Anda


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
    private int difficultyLevel;
    private Timer aiTimer; // Timer for AI delay


    /** Constructor to setup the UI and game components */
    public GameMain() {
        // Ask the player to choose between "X" or "O" at the start of the game
        Object[] options = { "X", "O" };
        JLabel label = new JLabel("Choose your symbol", JLabel.CENTER);
        label.setFont(FONT_DEFAULT);  // Set font baru
        
        // Membuat panel untuk menampung label dengan font yang disesuaikan
        JPanel panel = new JPanel();
        panel.add(label);

        // Menampilkan dialog menggunakan panel kustom
        int choice = JOptionPane.showOptionDialog(null, panel,
            "Player Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);
            


        // Set the human player's seed and initialize the AI player accordingly
        if (choice == 0) {
            humanSeed = Seed.CROSS;
        } else {
            humanSeed = Seed.NOUGHT;
        }
        Object[] difficultyOptions = { "Easy", "Medium", "Hard" };
        JLabel difficultyLabel = new JLabel("Select Difficulty Level", JLabel.CENTER);
        difficultyLabel.setFont(FONT_DEFAULT);  // Set font baru
        
        // Membuat panel untuk menampung label
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BorderLayout());
        difficultyPanel.add(difficultyLabel, BorderLayout.CENTER);

        // Menampilkan dialog pilihan dengan font yang disesuaikan
        difficultyLevel = JOptionPane.showOptionDialog(null,
            difficultyPanel, "Difficulty", JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE, null, difficultyOptions, difficultyOptions[0]);
        // Initialize the game board and AI player
        initGame();
        setAIPlayer();

        // Initialize AI player with the opposite seed
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(humanSeed == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS); // AI plays as the opposite seed

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;
                

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                        && board.cells[row][col].content == Seed.NO_SEED) {
                        // Player makes a move
                        currentState = board.stepGame(currentPlayer, row, col);
                            
                        SoundEffect.CLICKED.play();
                
                        // Check if game has ended
                        if (currentState != State.PLAYING) {
                            updateScore(); // Update the score after the game ends
                        } else {
                            // Switch to AI if game is still ongoing
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                            aiTimer = new Timer(500, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    aiMove(); // Call AI's move after delay
                                }
                            });
                            aiTimer.setRepeats(false);  // Only run once
                            aiTimer.start();
                        }
                    }
                } else {
                    newGame(); // Restart the game
                }
            
                repaint(); // Refresh the drawing canvas
            }
        });


        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_DEFAULT);
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
        SoundEffect.playBacksound();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();
        currentState = State.PLAYING; // Set status permainan ke playing
        currentPlayer = humanSeed; // Tentukan pemain manusia yang pertama kali bergerak
        repaint(); // Refresh tampilan
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
                updateScore();  // Call to update score after the game ends
            }
    
            // Switch player after AI's move
            if (currentState == State.PLAYING) {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            }
        }
    
        repaint();  // Callback paintComponent().
    }
    
    private void updateScore() {
        String message = "";
        boolean playerLost = false;

        if (currentState == State.CROSS_WON) {
            if (humanSeed == Seed.CROSS) {
                xWins++;
                message = "X Wins! You Win!";
                SoundEffect.playWinSound();
            } else {
                oWins++;
                message = "X Wins! You Lose!";
                playerLost = true;
                SoundEffect.playLoseSound();
            }
        } else if (currentState == State.NOUGHT_WON) {
            if (humanSeed == Seed.NOUGHT) {
                oWins++;
                message = "O Wins! You Win!";
                SoundEffect.playWinSound();
            } else {
                xWins++;
                message = "O Wins! You Lose!";
                playerLost = true;
                SoundEffect.playLoseSound();
            }
        } else if (currentState == State.DRAW) {
            draws++;
            message = "It's a Draw!";
            SoundEffect.DRAW.play();
        }

        UIManager.put("OptionPane.messageFont", new Font("OCR A Extended", Font.PLAIN, 18));
        UIManager.put("OptionPane.buttonFont", new Font("OCR A Extended", Font.PLAIN, 16));

        // Tampilkan dialog pop-up
        if (!message.isEmpty()) {
            JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

            // Pilihan: Play Again, Back to Main Menu, Quit
            Object[] options = { "Play Again", "Back to Main Menu", "Quit" };
            int choice = JOptionPane.showOptionDialog(this,
                "What would you like to do?", "Game Options",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            // Handle pilihan pemain
            if (choice == JOptionPane.YES_OPTION) {
                newGame(); // Mulai game baru
            } else if (choice == JOptionPane.NO_OPTION) {
                returnToMainMenu(); // Kembali ke menu utama
            } else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                System.exit(0); // Keluar dari aplikasi
            }
        }

        if (playerLost) {
            System.out.println("Better luck next time!"); // Bisa diganti dengan aksi tambahan
        }

        repaint(); // Refresh display to show updated score
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG); // Set its background color
        board.paint(g);  // Ask the game board to paint itself
    
        // Print status-bar message with the score
        String statusMessage = "X = " + xWins + " | Draw = " + draws + " | O = " + oWins;
        statusBar.setText(statusMessage);  // Display the score in the status bar
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void setAIPlayer() {
        if (difficultyLevel == 0) { // Easy
            aiPlayer = new AIPlayerTableLookup(board); // Simple AI
        } else if (difficultyLevel == 1) { // Medium
            aiPlayer = new AIPlayerMinimax(board); // Minimax with limited depth
            ((AIPlayerMinimax) aiPlayer).setMaxDepth(2); // Set depth to 2 for medium difficulty
        } else if (difficultyLevel == 2) { // Hard
            aiPlayer = new AIPlayerMinimax(board); // Minimax with full depth
            ((AIPlayerMinimax) aiPlayer).setMaxDepth(Integer.MAX_VALUE); // Unlimited depth
        }
    
        // Set AI's seed
        aiPlayer.setSeed(humanSeed == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);
    }
    private void returnToMainMenu() {
        // Restart the main game menu
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new GameMain()); // Replace the current panel with a new GameMain
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** The entry "main" method */
}