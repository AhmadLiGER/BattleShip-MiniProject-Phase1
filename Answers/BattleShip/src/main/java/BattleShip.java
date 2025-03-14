import java.util.Scanner;
import java.util.Random;

/**
 * The BattleShip class manages the gameplay of the Battleship game between two players.
 * It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;
    static final char WATER = '~';
    static final char SHIP = 'S';
    static final char HIT = 'X';
    static final char MISS = '0';

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
     * The main method that runs the game loop.
     * It initializes the grids for both players, places ships randomly, and manages turns.
     * The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }
        System.out.println("Game Over!");
    }

    /**
     * Initializes the grid by filling it with water ('~').
     *
     * @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = WATER;
            }
        }
    }

    /**
     * Places ships randomly on the given grid.
     * This method is called for both players to place their ships on their respective grids.
     *
     * @param grid The grid where ships need to be placed.
     */

    static void placeShips(char[][] grid) {
        int[] shipSize = {2, 3, 4, 5};
        Random random = new Random();

        for (int size : shipSize) {
            boolean isTaken = false;

            while (!isTaken) {
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextBoolean();

                if (canPlaceShip(grid, row, col, size, horizontal)) {
                    placeShip(grid, row, col, size, horizontal);
                    isTaken = true;
                }
            }
        }
    }

    /**
     * Checks if a ship can be placed at the specified location on the grid.
     * This includes checking the size of the ship, its direction (horizontal or vertical),
     * and if there's enough space to place it.
     *
     * @param grid       The grid where the ship is to be placed.
     * @param row        The starting row for the ship.
     * @param col        The starting column for the ship.
     * @param size       The size of the ship.
     * @param horizontal The direction of the ship (horizontal or vertical).
     * @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != WATER) return false;
            }
        } else {
            if (row + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != WATER) return false;
            }
        }
        return true;
    }

    //If the canPlaceShips returns true the this function get called.
    static void placeShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) {
                throw new IllegalArgumentException("Ship goes out of bounds horizontally.");
            }
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != WATER) {
                    throw new IllegalArgumentException("Ship placement overlaps with another ship.");
                }
            }
            for (int i = 0; i < size; i++) {
                grid[row][col + i] = SHIP;
            }
        } else {
            if (row + size > GRID_SIZE) {
                throw new IllegalArgumentException("Ship goes out of bounds vertically.");
            }
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != WATER) {
                    throw new IllegalArgumentException("Ship placement overlaps with another ship.");
                }
            }
            for (int i = 0; i < size; i++) {
                grid[row + i][col] = SHIP;
            }
        }
    }


    /**
     * Manages a player's turn, allowing them to attack the opponent's grid
     * and updates their tracking grid with hits or misses.
     *
     * @param opponentGrid The opponent's grid to attack.
     * @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        Scanner sc = new Scanner(System.in);
        String input;
        int row, col;

        while (true) {
            System.out.print("Enter a position(e.g., B7): ");
            input = sc.next().toUpperCase();
            if (!isValidInput(input)) {
                System.out.println("Please Enter a valid input ,(0-9, A-J)");
                continue;
            }
            col = input.charAt(0) - 'A';
            row = input.charAt(1) - '0';


            if (trackingGrid[row][col] == HIT || trackingGrid[row][col] == MISS) {
                System.out.println("You already attacked this spot... , Choose ANOTHER");
                continue;
            }
            break;
        }

        if (opponentGrid[row][col] == WATER) {
            System.out.println("You Missed \uD83E\uDD23");
            opponentGrid[row][col] = MISS;
            trackingGrid[row][col] = MISS;
        }

        if (opponentGrid[row][col] == SHIP) {
            System.out.println("Hit!! \uD83D\uDD25");
            opponentGrid[row][col] = HIT;
            trackingGrid[row][col] = HIT;
        }
    }

    /**
     * Checks if the game is over by verifying if all ships are sunk.
     *
     * @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if (allShipsSunk(player1Grid)) {
            System.out.println("Player 2 wins!");
            return true;
        } else if (allShipsSunk(player2Grid)) {
            System.out.println("Player 1 wins!");
            return true;
        }
        return false;
    }


    /**
     * Checks if all ships have been destroyed on a given grid.
     *
     * @param grid The grid to check for destroyed ships.
     * @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        int sunkShips = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == HIT) sunkShips++;
            }
        }
        return sunkShips == 14;
    }

    /**
     * Validates if the user input is in the correct format (e.g., A5).
     *
     * @param input The input string to validate.
     * @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        input = input.trim();
        if (input.length() < 2) return false;

        char firstParam = input.charAt(0);
        if (firstParam < 'A' || firstParam > 'J') return false;

        try {
            int secondParam = Integer.parseInt(input.substring(1));
            if (secondParam < 0 || secondParam > 9) return false;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Prints the current state of the player's tracking grid.
     * This method displays the grid, showing hits, misses, and untried locations.
     *
     * @param grid The tracking grid to print.
     */

    static void printGrid(char[][] grid) {
        System.out.print("  ");
        for (char c = 'A'; c < 'K'; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((i) + "  ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
