package TicTacToe.src.tictactoe;

import java.util.Scanner;

public class TicTacToe {

    private static String[][] gameMap = new String[3][3];
    private static Scanner scanner = new Scanner(System.in);
    private static String userName;
    private static Bot bot;

    public static void main(String[] args) {
        Scanner username = new Scanner(System.in);
        System.out.print("Введите ваше имя: ");
        userName = username.nextLine();
        System.out.println("Привет, " + userName);
        bot = new Bot(gameMap, "O", "X");
        System.out.println("Добро пожаловать в игру Крестики-нолики!\n");
        printGameMap();
        while (true) {
            // Ход пользователя
            while (!userMove()) {
                System.out.println("Ввод не корректен, попробуйте еще раз.");
            }
            printGameMap();

            if (checkWinner("X")) {
                System.out.println("\nПоздравляем! Вы победили!");
                break;
            }
            if (checkDraw()) {
                System.out.println("\nНичья! На поле не осталось свободных клеток.");
                break;
            }

            // Ход бота
            System.out.println("\nХод бота...");
            botMove();
            printGameMap();

            if (checkWinner("O")) {
                System.out.println("\nБот победил! Не расстраивайтесь, повезет в другой раз.");
                break;
            }
            if (checkDraw()) {
                System.out.println("\nНичья! На поле не осталось свободных клеток.");
                break;
            }
        }
        scanner.close();
    }

    /**
     * Выводит текущее состояние игрового поля с нумерацией строк и столбцов.
     */
    private static void printGameMap() {
        System.out.println(" x  0   1   2");
        System.out.println("y  ------------");
        for (int y = 0; y < 3; y++) {
            System.out.print(y + " | ");
            for (int x = 0; x < 3; x++) {
                String cell = gameMap[y][x] == null ? "_" : gameMap[y][x];
                System.out.print(cell + " | ");
            }
            System.out.println();
            System.out.println("   ------------");
        }
    }

    /**
     * Позволяет пользователю сделать ход, вводя координаты клетки.
     * Возвращает true, если ход успешен, иначе false.
     */
    private static boolean userMove() {
        while (true) {
            try {
                System.out.print(userName + ", Ваш ход.\nВедите координаты X Y (от 1 до 3): \n");
                String input = scanner.nextLine();
                String[] coordinates = input.split(" ");
                if (coordinates.length != 2) {
                    System.out.println("Ошибка: Некорректный ввод. Введите два целых числа через пробел.");
                    continue;
                }
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                if (!(x >= 0 && x < 3 && y >= 0 && y < 3)) {
                    System.out.println("Ошибка: Координаты должны быть целыми числами от 0 до 2.");
                    continue;
                }
                if (gameMap[y][x] != null) {
                    System.out.println("Ошибка: Эта клетка уже занята. Попробуйте другую.");
                    continue;
                }
                gameMap[y][x] = "X";
                return true;

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Некорректный ввод. Введите два целых числа через пробел.");
            }
        }
    }

    /**
     * Реализует ход бота, используя минимакс.
     */
    private static void botMove() {
        int[] bestMove = bot.findBestMove();
        if (bestMove[0] == -1) {
            return; // Ничья обрабатывается в основном цикле
        }
        gameMap[bestMove[1]][bestMove[0]] = "O";
    }

    /**
     * Проверяет, есть ли победитель на поле для заданного игрока.
     * Возвращает true, если есть победа, иначе false.
     */
    private static boolean checkWinner(String player) {
        // Проверка горизонталей
        for (int y = 0; y < 3; y++) {
            if (player.equals(gameMap[y][0])
                    && player.equals(gameMap[y][1])
                    && player.equals(gameMap[y][2])) {
                return true;
            }
        }

        // Проверка вертикалей
        for (int x = 0; x < 3; x++) {
            if (player.equals(gameMap[0][x])
                    && player.equals(gameMap[1][x])
                    && player.equals(gameMap[2][x])) {
                return true;
            }
        }

        // Проверка диагоналей
        if (player.equals(gameMap[0][0])
                && player.equals(gameMap[1][1])
                && player.equals(gameMap[2][2])) {
            return true;
        }

        if (player.equals(gameMap[0][2])
                && player.equals(gameMap[1][1])
                && player.equals(gameMap[2][0])) {
            return true;
        }
        return false;
    }

    /**
     * Проверяет, есть ли ничья на поле (нет свободных клеток).
     */
    private static boolean checkDraw() {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (gameMap[y][x] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Класс Bot для реализации минимакса.
     */
    static class Bot {
        private String[][] board;
        private String botSymbol;
        private String playerSymbol;

        public Bot(String[][] board, String botSymbol, String playerSymbol) {
            this.board = board;
            this.botSymbol = botSymbol;
            this.playerSymbol = playerSymbol;
        }

        public int[] findBestMove() {
            int[] bestMove = new int[]{-1, -1};
            int bestScore = Integer.MIN_VALUE;

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (board[y][x] == null) {
                        board[y][x] = botSymbol;
                        int score = minimax(board, 0, false);
                        board[y][x] = null;
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[]{x, y};
                        }
                    }
                }
            }
            return bestMove;
        }

        private int minimax(String[][] currentBoard, int depth, boolean isMaximizing) {
            if (checkWin(currentBoard, botSymbol)) return 1;
            if (checkWin(currentBoard, playerSymbol)) return -1;
            if (isDraw(currentBoard)) return 0;

            if (isMaximizing) {
                int bestScore = Integer.MIN_VALUE;
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        if (currentBoard[y][x] == null) {
                            currentBoard[y][x] = botSymbol;
                            int score = minimax(currentBoard, depth + 1, false);
                            currentBoard[y][x] = null;
                            bestScore = Math.max(score, bestScore);
                        }
                    }
                }
                return bestScore;
            } else {
                int bestScore = Integer.MAX_VALUE;
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        if (currentBoard[y][x] == null) {
                            currentBoard[y][x] = playerSymbol;
                            int score = minimax(currentBoard, depth + 1, true);
                            currentBoard[y][x] = null;
                            bestScore = Math.min(score, bestScore);
                        }
                    }
                }
                return bestScore;
            }
        }

        // Проверка победы игрока
        private boolean checkWin(String[][] currentBoard, String player) {
            // Горизонтали
            for (int y = 0; y < 3; y++) {
                if (currentBoard[y][0] != null && currentBoard[y][0].equals(player)
                        && currentBoard[y][1] != null && currentBoard[y][1].equals(player)
                        && currentBoard[y][2] != null && currentBoard[y][2].equals(player)) {
                    return true;
                }
            }

            // Вертикали
            for (int x = 0; x < 3; x++) {
                if (currentBoard[0][x] != null && currentBoard[0][x].equals(player)
                        && currentBoard[1][x] != null && currentBoard[1][x].equals(player)
                        && currentBoard[2][x] != null && currentBoard[2][x].equals(player)) {
                    return true;
                }
            }

            // Диагонали
            if (currentBoard[0][0] != null && currentBoard[0][0].equals(player)
                    && currentBoard[1][1] != null && currentBoard[1][1].equals(player)
                    && currentBoard[2][2] != null && currentBoard[2][2].equals(player)) {
                return true;
            }

            if (currentBoard[0][2] != null && currentBoard[0][2].equals(player)
                    && currentBoard[1][1] != null && currentBoard[1][1].equals(player)
                    && currentBoard[2][0] != null && currentBoard[2][0].equals(player)) {
                return true;
            }

            return false;
        }

        // Проверка ничьи
        private boolean isDraw(String[][] currentBoard) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (currentBoard[y][x] == null) return false;
                }
            }
            return true;
        }
    }
}