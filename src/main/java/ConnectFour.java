package main.java;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConnectFour {

    // define characters for players (R for Red, Y for Yellow)
    private static final char[] PLAYERS = {'R', 'Y'};

    // define dimensions
    private final int width;
    private final int height;

    // grid
    private final char[][] grid;

    // store the last move
    private int lastCol = -1;
    private int lastTop = -1;

    private ConnectFour(int w, int h) {
        width = w;
        height = h;
        grid = new char[h][];
        for (int i = 0; i < h; i++) {
            Arrays.fill(grid[i] = new char[w], '.');
        }
    }

    // string representation of the board
    public String toString() {
        return IntStream.range(0, width).mapToObj(Integer::toString).collect(Collectors.joining()) + "\n" +
                Arrays.stream(grid).map(String::new).collect(Collectors.joining("\n"));
    }

    // string representation of the row containing the last play of the user
    private String horizontal() {
        return new String(grid[lastTop]);
    }

    // returns the string representation of the column containing the last play of the user
    private String vertical() {
        StringBuilder sb = new StringBuilder(height);
        for (int i = 0; i < height; i++) {
            sb.append(grid[i][lastCol]);
        }
        return sb.toString();
    }

    // string representation of a forward slash diagonal containing the last play of the user
    private String slashDiagonal() {
        StringBuilder sb = new StringBuilder(height);
        for (int i = 0; i < height; i++) {
            int w = lastCol + lastTop - i;
            if (0 <= w && w < width) {
                sb.append(grid[i][w]);
            }
        }
        return sb.toString();
    }

    // string representation of the backward slash diagonal containing the last play of the user
    private String backslashDiagonal() {
        StringBuilder sb = new StringBuilder(height);
        for (int i = 0; i < height; i++) {
            int w = lastCol - lastTop + i;
            if (0 <= w && w < width) {
                sb.append(grid[i][w]);
            }
        }
        return sb.toString();
    }

    // static method checking if a substring is in a string
    private static boolean contains(String str, String substr) {
        return str.contains(substr);
    }

    // check if the last play is a winning play
    private boolean isWinningPlay() {
        if (lastCol == -1) {
            System.err.println("no move has been made");
            return false;
        }

        char sym = grid[lastTop][lastCol];

        // winning streak with the last play symmbol
        String streak = String.format("%c%c%c%c", sym, sym, sym, sym);

        // check if streak is in row, col, diagonal, or backwards diagonal
        return contains(horizontal(), streak) || contains(vertical(), streak)
                || contains(slashDiagonal(), streak) || contains(backslashDiagonal(), streak);
    }

    // prompt the user for a column, repeating until a valid choice is made
    private void chooseAndDrop(char symbol, Scanner input) {
        do {
            System.out.println("\nPlayer " + symbol + " turn: ");
            int col = input.nextInt();

            if (!(0 <= col) && col <= width) {
                System.out.println("Column must be between 0 and " + width);
                continue;
            }

            // place the symbol in the first available row in the specified column
            for (int i = height - 1; i >= 0; i--) {
                if (grid[i][col] == '.') {
                    grid[lastTop = i][lastCol = col] = symbol;
                    return;
                }
            }

            // if column is full we need to ask for a new input
            System.out.println("Column " + col + " is full.");
        } while (true);
    }

    public static void main(String[] args) {
        // assemble all the pieces of the puzzle for building our game
        try (Scanner input = new Scanner(System.in)) {
            int height = 6; int width = 7; int moves = height * width;

            ConnectFour board = new ConnectFour(width, height);
            System.out.println("Use 0-" + (width - 1) + " to choose a column");

            // display the initial board
            System.out.println(board);

            // iterate until max moves have been reached
            for (int player = 0; moves-- > 0; player = 1 - player) {
                // symbols for current player
                char symbol = PLAYERS[player];

                // ask the user to choose a column
                board.chooseAndDrop(symbol, input);

                // display the board
                System.out.println(board);

                // check if a player won. if not, continue. else, display a message
                if (board.isWinningPlay()) {
                    System.out.println("\nPlayer " + symbol + " wins!");
                    return;
                }
            }

            System.out.println("Game over -- no winner");
        }
    }

}
