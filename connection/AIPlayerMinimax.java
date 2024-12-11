import java.util.*;

public class AIPlayerMinimax extends AIPlayer {

    public AIPlayerMinimax(Board board) {
        super(board);
    }

    @Override
    int[] move() {
        int[] result = minimax(2, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] {result[1], result[2]};   // Return row, col
    }

    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        List<int[]> nextMoves = generateMoves();  // Generate all possible moves
        int score;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            score = evaluate();  // Game over or depth reached, evaluate score
            return new int[] {score, bestRow, bestCol};
        } else {
            for (int[] move : nextMoves) {
                cells[move[0]][move[1]].content = player; // Try this move
                if (player == mySeed) {  // Maximizing for the AI
                    score = minimax(depth - 1, oppSeed, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // Minimizing for the opponent
                    score = minimax(depth - 1, mySeed, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                cells[move[0]][move[1]].content = Seed.NO_SEED;  // Undo the move
                if (alpha >= beta) break;  // Alpha-beta pruning
            }
            return new int[] {(player == mySeed) ? alpha : beta, bestRow, bestCol};
        }
    }

    private List<int[]> generateMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLS; j++) {
                if (cells[i][j].content == Seed.NO_SEED) {  // Empty spot
                    moves.add(new int[] {i, j});
                }
            }
        }
        return moves;
    }

    private int evaluate() {
        // A basic evaluation function (can be enhanced)
        if (hasWon(mySeed)) return 10;
        if (hasWon(oppSeed)) return -10;
        return 0;
    }

    private boolean hasWon(Seed seed) {
        // Check for win in rows, columns, and diagonals
        for (int i = 0; i < Board.ROWS; i++) {
            if (cells[i][0].content == seed && cells[i][1].content == seed && cells[i][2].content == seed) {
                return true;
            }
            if (cells[0][i].content == seed && cells[1][i].content == seed && cells[2][i].content == seed) {
                return true;
            }
        }
        if (cells[0][0].content == seed && cells[1][1].content == seed && cells[2][2].content == seed) {
            return true;
        }
        if (cells[0][2].content == seed && cells[1][1].content == seed && cells[2][0].content == seed) {
            return true;
        }
        return false;
    }
}
