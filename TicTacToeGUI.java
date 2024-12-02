import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeGUI extends JFrame {

	private static final long serialVersionUID = -5034718198692604252L;
	JButton[] boardButtons = new JButton[9];
	JButton resetButton = new JButton("Reset");
	JFrame frame = new JFrame("Mitch's Tic Tac Toe");

	Board board; // board now will be initialized based on the player's choice
	Opponent opponent = new Opponent();

	char playerSymbol = 'X'; // Default symbol for the player
	char computerSymbol = 'O'; // Default symbol for the computer

	public TicTacToeGUI() {
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	private void initialise() {
		// Ask the player to choose X or O
		chooseSymbol();

		board = new Board(playerSymbol, computerSymbol); // Initialize the board with the player's choice

		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel gameBoard = new JPanel(new GridLayout(3, 3));

		frame.add(mainPanel);

		gameBoard.setPreferredSize(new Dimension(500, 500));

		mainPanel.add(gameBoard, BorderLayout.NORTH);
		// Initialize all the buttons
		for (int i = 0; i < 9; i++) {
			boardButtons[i] = new JButton();
			boardButtons[i].setBackground(Color.BLACK);
			boardButtons[i].setText("");
			boardButtons[i].setVisible(true);

			gameBoard.add(boardButtons[i]);
			boardButtons[i].addActionListener(new myActionListener());
			boardButtons[i].setFont(new Font("Tahoma", Font.BOLD, 100));
		}
	}

	// Method to prompt the player to choose X or O
	private void chooseSymbol() {
		String[] options = { "X", "O" };
		int choice = JOptionPane.showOptionDialog(frame, "Choose your symbol", "Select Symbol",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		if (choice == 0) { // Player chooses X
			playerSymbol = 'X';
			computerSymbol = 'O';
		} else if (choice == 1) { // Player chooses O
			playerSymbol = 'O';
			computerSymbol = 'X';
		}
	}

	// Listen for when the buttons are clicked
	private class myActionListener implements ActionListener {
		public void actionPerformed(ActionEvent action) {
			int computerMove;

			// Display the player's piece on the buttons
			for (int i = 0; i < 9; i++) {
				if (action.getSource() == boardButtons[i] &&
						board.spotAvailable(i)) {

					// Player turn
					board.newPiece(playerSymbol, i);
					boardButtons[i].setText(Character.toString(playerSymbol));
					boardButtons[i].setForeground(Color.GREEN);

					// If player has won, end game
					if (board.isWinner(playerSymbol)) {
						gameOver();
					}

					// Computer turn
					computerMove = opponent.getMove(board);

					// If computer has a legal move
					if (computerMove != -1) {
						board.newPiece(computerSymbol, computerMove);
						boardButtons[computerMove].setText(Character.toString(computerSymbol));
						boardButtons[computerMove].setForeground(Color.YELLOW);

						// If computer has won, end game
						if (board.isWinner(computerSymbol)) {
							gameOver();
						}
					} else {
						// If no more moves are available, it's a draw
						gameOver();
					}
				}
			}
		}
	}

	// When the game is over, display any winners in the title
	public void gameOver() {
		for (int i = 0; i < 9; i++) {
			boardButtons[i].setEnabled(false);
		}

		// Show the result in the title
		if (board.isWinner(playerSymbol)) {
			JOptionPane.showMessageDialog(frame, "CONGRATULATIONS! YOU WIN!");
			frame.setTitle("Mitch's Tic Tac Toe: ***** PLAYER WINS *****");
		} else if (board.isWinner(computerSymbol)) {
			frame.setTitle("Mitch's Tic Tac Toe: ***** COMPUTER WINS *****");
			JOptionPane.showMessageDialog(frame, "Bad luck. You Lose.");
		} else {
			JOptionPane.showMessageDialog(frame, "Draw.");
			frame.setTitle("Mitch's Tic Tac Toe: ***** IT'S A DRAW *****");
		}

		// Ask the player if they want to play again
		int playAgain = JOptionPane.showConfirmDialog(frame, "Do you want to play again?", "Game Over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (playAgain == JOptionPane.YES_OPTION) {
			// Ask the player to choose symbol again
			chooseSymbol();
			resetGame(); // Restart the game
		} else {
			frame.dispose(); // Close the game window
		}
	}

	// Reset the game for a new round
	private void resetGame() {
		for (int i = 0; i < 9; i++) {
			boardButtons[i].setText("");
			boardButtons[i].setEnabled(true);
		}
		board.reset(); // Reset the board state
		frame.setTitle("Mitch's Tic Tac Toe");
	}

	public static void main(String[] args) {
		TicTacToeGUI game = new TicTacToeGUI();
		game.initialise();
	}
}
