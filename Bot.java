import java.util.ArrayList;
import java.util.List;

public class Bot {

    private String[][] gameMap;
    private String botPlayer;
    private String humanPlayer;

    public Bot(String[][] gameMap, String botPlayer, String humanPlayer) {
        this.gameMap = gameMap;
        this.botPlayer = botPlayer;
        this.humanPlayer = humanPlayer;
    }

    public int[] findBestMove() {
        int[] bestMove = {-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (gameMap[y][x] == null) {
                    gameMap[y][x] = botPlayer;
                    int score = minimax(gameMap, 0, false);
                    gameMap[y][x] = null;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = x;
                        bestMove[1] = y;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(String[][] board, int depth, boolean isMaximizingPlayer) {
        if (checkWinner(board, botPlayer)) {
            return 10; // Выигрыш для бота
        }
        if (checkWinner(board, humanPlayer)) {
            return -10; // Выигрыш для человека
        }
        if (checkDraw(board)) {
            return 0; // Ничья
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (board[y][x] == null) {
                        board[y][x] = botPlayer;
                        int score = minimax(board, depth + 1, false);
                        board[y][x] = null;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (board[y][x] == null) {
                        board[y][x] = humanPlayer;
                        int score = minimax(board, depth + 1, true);
                        board[y][x] = null;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


    private boolean checkWinner(String[][] board, String player) {
        // Проверка горизонталей
        for (int y = 0; y < 3; y++) {
            if (board[y][0] != null &&
                    board[y][1] != null &&
                    board[y][2] != null &&
                    board[y][0].equals(player) &&
                    board[y][1].equals(player) &&
                    board[y][2].equals(player)) {
                return true;
            }
        }
        // Проверка вертикалей
        for (int x = 0; x < 3; x++) {
            if (board[0][x] != null &&
                    board[1][x] != null &&
                    board[2][x] != null &&
                    board[0][x].equals(player) &&
                    board[1][x].equals(player) &&
                    board[2][x].equals(player)) {
                return true;
            }
        }
        // Проверка диагоналей
        if (board[0][0] != null &&
                board[1][1] != null &&
                board[2][2] != null &&
                board[0][0].equals(player) &&
                board[1][1].equals(player) &&
                board[2][2].equals(player)) {
            return true;
        }

        if (board[0][2] != null &&
                board[1][1] != null &&
                board[2][0] != null &&
                board[0][2].equals(player) &&
                board[1][1].equals(player) &&
                board[2][0].equals(player)){
            return true;
        }
        return false;
    }

    private boolean checkDraw(String[][] board) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board[y][x] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}