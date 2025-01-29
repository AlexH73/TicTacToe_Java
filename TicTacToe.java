import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    private static String[][] gameMap = new String[3][3];
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static Bot bot;

    public static void main(String[] args) {
        bot = new Bot(gameMap, "O", "X");
        System.out.println("Добро пожаловать в игру Крестики-нолики!\n");
        printGameMap();
        while (true) {
            // Ход пользователя
            while (!userMove()) {
                // повторяем ход пользователя, пока ввод не корректен
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

            if (checkWinner("0")) {
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
                System.out.print("Введите координаты (x y) через пробел (например, 0 1): ");
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
     * Реализует ход бота, выбирая случайную свободную клетку.
     */
    private static void botMove() {
        int[] bestMove = bot.findBestMove();
        if (bestMove[0] != -1) {
            gameMap[bestMove[1]][bestMove[0]] = "O";
        }
    }

    /**
     * Проверяет, есть ли победитель на поле для заданного игрока.
     * Возвращает true, если есть победа, иначе false.
     */
    private static boolean checkWinner(String player) {
        // Проверка горизонталей
        for (int y = 0; y < 3; y++) {
            if (gameMap[y][0] != null &&
                    gameMap[y][1] != null &&
                    gameMap[y][2] != null &&
                    gameMap[y][0].equals(player) &&
                    gameMap[y][1].equals(player) &&
                    gameMap[y][2].equals(player)) {
                return true;
            }
        }

        // Проверка вертикалей
        for (int x = 0; x < 3; x++) {
            if (gameMap[0][x] != null &&
                    gameMap[1][x] != null &&
                    gameMap[2][x] != null &&
                    gameMap[0][x].equals(player) &&
                    gameMap[1][x].equals(player) &&
                    gameMap[2][x].equals(player)) {
                return true;
            }
        }

        // Проверка диагоналей
        if (gameMap[0][0] != null &&
                gameMap[1][1] != null &&
                gameMap[2][2] != null &&
                gameMap[0][0].equals(player) &&
                gameMap[1][1].equals(player) &&
                gameMap[2][2].equals(player)) {
            return true;
        }

        if (gameMap[0][2] != null &&
                gameMap[1][1] != null &&
                gameMap[2][0] != null &&
                gameMap[0][2].equals(player) &&
                gameMap[1][1].equals(player) &&
                gameMap[2][0].equals(player)) {
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
        return false;
    }

}

