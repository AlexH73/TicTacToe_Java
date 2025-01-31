import java.util.ArrayList;
import java.util.List;

public class Bot {

    private String[][] gameMap;
    private String botPlayer;
    private String humanPlayer;

    // Константы для оценки
    private static final int WIN_SCORE = 10;
    private static final int LOSE_SCORE = -10;
    private static final int DRAW_SCORE = 0;

    public Bot(String[][] gameMap, String botPlayer, String humanPlayer) {
        this.gameMap = gameMap;
        this.botPlayer = botPlayer;
        this.humanPlayer = humanPlayer;
    }

    /**
     * Находит лучший ход для бота с использованием алгоритма минимакс.
     */
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

    /**
     * Реализует алгоритм минимакс для оценки ходов.
     */
    private int minimax(String[][] board, int depth, boolean isMaximizingPlayer) {
        if (checkWinner(board, botPlayer)) {
            return WIN_SCORE - depth; // Чем быстрее победа, тем выше оценка
        }
        if (checkWinner(board, humanPlayer)) {
            return depth + LOSE_SCORE; // Чем быстрее победа человека, тем ниже оценка
        }
        if (checkDraw(board)) {
            return DRAW_SCORE; // Ничья
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

    /**
     * Проверяет, выиграл ли указанный игрок.
     */
    private boolean checkWinner(String[][] board, String player) {
        // Проверка горизонталей
        for (int y = 0; y < 3; y++) {
            if (player.equals(board[y][0]) && player.equals(board[y][1]) && player.equals(board[y][2])) {
                return true;
            }
        }
        // Проверка вертикалей
        for (int x = 0; x < 3; x++) {
            if (player.equals(board[0][x]) && player.equals(board[1][x]) && player.equals(board[2][x])) {
                return true;
            }
        }
        // Проверка диагоналей
        if (player.equals(board[0][0]) && player.equals(board[1][1]) && player.equals(board[2][2])) {
            return true;
        }
        if (player.equals(board[0][2]) && player.equals(board[1][1]) && player.equals(board[2][0])) {
            return true;
        }
        return false;
    }

    /**
     * Проверяет, является ли текущее состояние игры ничьей.
     */
    private boolean checkDraw(String[][] board) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board[y][x] == null) {
                    return false; // Есть свободные клетки
                }
            }
        }
        return true; // Все клетки заняты
    }
}