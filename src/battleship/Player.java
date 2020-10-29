package battleship;

import java.util.ArrayList;
import java.util.Scanner;

import static battleship.Battleship.isSunk;
import static battleship.Main.printField;

public class Player {

    /* Name of the player. */
    private final String name;

    /* Minimum and maximum indexes on the field. */
    final int MIN_INDEX = 0;
    final int MAX_INDEX = 9;

    /* The player's actual field. */
    final char[][] field;

    /* Fog of war field to be shown to the opponent to take a shot blindly. */
    final char[][] fogOfWarField;

    /* List of player's battleships that will be placed on the battle field. */
    ArrayList<Battleship> battleships;

    /**
     * Create a player by specifying his name.
     *
     * @param name the name of the player.
     */
    public Player(String name) {
        this.name = name;

        /* Initialize and fill both fields. */
        field = new char[10][10];
        fogOfWarField = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = '~';
                fogOfWarField[i][j] = '~';
            }
        }

    }

    /**
     * Ask this player to place his ships on the battle field.
     */
    void takePosition() {
        System.out.println(this.name + ", place your ships on the game field");
        printField(field);

        /* Each player has 5 types of ships:
         * 1- Aircraft Carrier: 5 cells.
         * 2- Battleship: 4 cells.
         * 3- Submarine: 3 cells.
         * 4- Cruiser: 3 cells.
         * 5- Destroyer: 2 cells.
         * */
        battleships = new ArrayList<>();
        battleships.add(new Battleship("Aircraft Carrier", 5));
        battleships.add(new Battleship("Battleship", 4));
        battleships.add(new Battleship("Submarine", 3));
        battleships.add(new Battleship("Cruiser", 3));
        battleships.add(new Battleship("Destroyer", 2));

        for (Battleship battleship : battleships) {
            getCoordinatesOf(battleship);
        }
    }

    /**
     * Get the coordinates of battleship.
     *
     * @param battleship the battleship to get its coordinates.
     */
    void getCoordinatesOf(Battleship battleship) {
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", battleship.getType(), battleship.getSize());

        Scanner sc = new Scanner(System.in);
        String cord1, cord2;

        // How the battleship will be placed in the battle field (horizontally or vertically).
        String orientation;
        do {
            cord1 = sc.next();
            cord2 = sc.next();

            if (!isValidCoordinate(cord1) || !isValidCoordinate(cord2)) {
                System.out.print("Improper format of one or both of the coordinates! Try again:\n");
                continue;
            }

            /* Get and check the orientation of the entered coordinates. */
            orientation = getOrientationOf(cord1, cord2);
            if (orientation.equals("unknown")) {
                System.out.print("Error! Wrong ship orientation! Try again:\n");
                continue;
            }

            /* A ship cannot cross or be too close to another ship.
             * Two ships are too close to each other if there were no empty cells between them.
             * */
            boolean intersection = false;
            boolean tooCloseToAnotherShip = false;
            if (orientation.equals("horizontal")) {
                int cord1Col = extractColIndex(cord1.substring(1));
                int cord2Col = extractColIndex(cord2.substring(1));

                /* Make sure that the battleship's length (represented by coordinates) are equal to the particular
                 * type of the battleship. */
                if (Math.abs(cord1Col - cord2Col) + 1 != battleship.getSize()) {
                    System.out.printf("Error! Wrong length of %s! Try again:\n", battleship.getType());
                    continue;
                }

                /* In horizontal mode, the ship is placed on the same row.
                 * For example: A1 A4
                 * */
                int row = extractRowIndex(cord1.charAt(0));
                if (row == -1) {
                    throw new IllegalArgumentException("Invalid row index -1");
                }

                int startColIndex = Math.min(cord1Col, cord2Col);
                int endColIndex = Math.max(cord1Col, cord2Col);

                /* Walk the cells and check if they are empty -> For placing the ship itself.
                 * And check the neighbouring cells -> Two ships cannot cross or be placed close to each other. */
                Cell[] neighbours = new Cell[2];
                for (int i = startColIndex; i <= endColIndex; i++) {
                    if (!isCellEmpty(field, row, i)) {
                        intersection = true;
                        break;
                    }

                    /* For each cell, check the tow cells close to it. One above it and the other below it. */
                    neighbours[0] = new Cell(row + 1, i);
                    neighbours[1] = new Cell(row - 1, i);
                    for (Cell neighbour : neighbours) {
                        /* Don't go outside the field. */
                        if (neighbour.row <= MAX_INDEX && neighbour.row >= MIN_INDEX) {
                            if (!isCellEmpty(field, neighbour)) {
                                tooCloseToAnotherShip = true;
                                break;
                            }
                        }
                    }

                    if (tooCloseToAnotherShip) {
                        break;
                    }
                }

                /* Check the two cells at the two ends (i.e. one cell before the head of the ship and the other
                 * after the tail of it). */
                neighbours[0] = new Cell(row, startColIndex - 1);
                neighbours[1] = new Cell(row, endColIndex + 1);
                for (Cell terminalCell : neighbours) {
                    if (terminalCell.col <= MAX_INDEX && terminalCell.col >= MIN_INDEX) {
                        if (!isCellEmpty(field, terminalCell)) {
                            tooCloseToAnotherShip = true;
                            break;
                        }
                    }
                }

            } else if (orientation.equals("vertical")) {
                int cord1Row = extractRowIndex(cord1.charAt(0));
                int cord2Row = extractRowIndex(cord2.charAt(0));

                /* Make sure that the battleship's length (represented by coordinates) are equal to the particular
                 * type of the battleship. */
                if (Math.abs(cord1Row - cord2Row) + 1 != battleship.getSize()) {
                    System.out.printf("Error! Wrong length of %s! Try again:\n", battleship);
                    continue;
                }

                /* In vertical mode, the ship is placed on the same column.
                 * For example: A1 D1
                 * */
                int col = extractColIndex(cord1.substring(1));
                if (col == -1) {
                    throw new IllegalArgumentException("Invalid column index -1");
                }

                int startRowIndex = Math.min(cord1Row, cord2Row);
                int endRowIndex = Math.max(cord1Row, cord2Row);

                /* Walk the cells and check if they are empty -> For placing the ship itself.
                 * And check the neighbouring cells -> Two ships cannot cross or be placed close to each other. */
                Cell[] neighbours = new Cell[2];
                for (int i = startRowIndex; i <= endRowIndex; i++) {
                    if (!isCellEmpty(field, i, col)) {
                        intersection = true;
                        break;
                    }

                    /* For each cell, check the tow cells close to it. One to the right and the other to the left. */
                    neighbours[0] = new Cell(i, col + 1);
                    neighbours[1] = new Cell(i, col - 1);
                    for (Cell neighbour : neighbours) {
                        /* Don't go outside the field. */
                        if (neighbour.col <= MAX_INDEX && neighbour.col >= MIN_INDEX) {
                            if (!isCellEmpty(field, neighbour)) {
                                tooCloseToAnotherShip = true;
                                break;
                            }
                        }
                    }

                    if (tooCloseToAnotherShip) {
                        break;
                    }
                }

                /* Check the two cells at the two ends (i.e. one cell before the head of the ship and the other
                 * after the tail of it). */
                neighbours[0] = new Cell(startRowIndex - 1, col);
                neighbours[1] = new Cell(endRowIndex + 1, col);
                for (Cell terminalCell : neighbours) {
                    if (terminalCell.row <= MAX_INDEX && terminalCell.row >= MIN_INDEX) {
                        if (!isCellEmpty(field, terminalCell)) {
                            tooCloseToAnotherShip = true;
                            break;
                        }
                    }
                }
            }

            if (intersection || tooCloseToAnotherShip) {
                System.out.print("Error! Your ship intercepts or too close to another one. Try again:\n");
                continue;
            }

            /* If the execution reached here (i.e. was not intercepted by a continue statement), then the coordinates
             * are valid. */
            break;
        } while (true);

        /*
         * At this point, we guarantee the validity of the coordinates entered.
         * Store them and fill them in the field.
         * */
        battleship.setCord1(cord1);
        battleship.setCord2(cord2);
        battleship.setOrientation(orientation);

        placeShip(cord1, cord2, orientation);
        printField(field);
    }

    /**
     * Checks the validation of the passed coordinate, in terms of length, possible columns and rows.
     *
     * @param cord the coordinate to check. Such as A10, B9, D5...etc.
     * @return true if the format of the coordinate is valid, otherwise false.
     */
    static boolean isValidCoordinate(String cord) {
        /* The maximum length of a coordinate is 3 (A10) and the minimum is 2 (A1). */
        if (cord.length() > 3 || cord.length() < 2) {
            return false;
        }

        /* Check the row part of the coordinate, it should be in range [A-J]. */
        if (!String.valueOf(cord.charAt(0)).matches("[A-J]")) {
            return false;
        }

        /* Check the column part of the coordinate, it should be in range [1-10]. */
        if (!(cord.substring(1).matches("[1-9]") ||
                cord.substring(1).matches("10"))) {
            return false;
        }

        return true;
    }

    /**
     * Check the orientation of the passed coordinates.
     * Battleships can be placed only horizontally or vertically, not diagonally.
     * <p>
     * The orientation is horizontal if and only if the row is equal in both coordinates and columns are different.
     * For example: A1 A5.
     * <p>
     * The orientation is vertical if and only if the column is equal in both coordinates and rows are different.
     * * For example: A1 D1.
     *
     * @param cord1 the head of the battleship.
     * @param cord2 the tail of the battleship.
     * @return the orientation represented by those coordinates. If the orientation is not horizontal or vertical,
     * it returns unknown.
     */
    private String getOrientationOf(String cord1, String cord2) {
        if (cord1.charAt(0) == cord2.charAt(0) && cord1.charAt(1) != cord2.charAt(1)) {
            return "horizontal";
        } else if (cord1.charAt(1) == cord2.charAt(1) && cord1.charAt(0) != cord2.charAt(0)) {
            return "vertical";
        }

        return "unknown";
    }

    /**
     * Get the corresponding index (in the field) of the passed string.
     * For example: if the passed string is 10, it would return the index 9,
     * which represents the last column index in the field.
     *
     * @param colCord the column part of a coordinate.
     * @return the corresponding index of the string, otherwise -1.
     */
    static int extractColIndex(String colCord) {
        switch (colCord) {
            case "1":
                return 0;

            case "2":
                return 1;

            case "3":
                return 2;

            case "4":
                return 3;

            case "5":
                return 4;

            case "6":
                return 5;

            case "7":
                return 6;

            case "8":
                return 7;

            case "9":
                return 8;

            case "10":
                return 9;

            default:
                return -1;
        }
    }

    /**
     * Get the row index (in the field) of the passed character.
     * For example: if the symbol passed is A, then it would return the index 0,
     * which represents the first row in the field.
     *
     * @param symbol the character symbol representing the row.
     * @return the corresponding index in the field, otherwise -1.
     */
    static int extractRowIndex(char symbol) {
        switch (symbol) {
            case 'A':
                return 0;

            case 'B':
                return 1;

            case 'C':
                return 2;

            case 'D':
                return 3;

            case 'E':
                return 4;

            case 'F':
                return 5;

            case 'G':
                return 6;

            case 'H':
                return 7;

            case 'I':
                return 8;

            case 'J':
                return 9;

            default:
                return -1;
        }
    }


    /**
     * Returns whether this cell is empty in the field.
     *
     * @param cell the cell to check, each cell object has a row and column for the cell coordinates.
     * @return true if the specified cell is empty, otherwise false.
     */
    static boolean isCellEmpty(char[][] field, Cell cell) {
        return field[cell.row][cell.col] == '~';
    }

    /**
     * Returns whether this cell is empty in the field.
     *
     * @param row the row of which the cell is located.
     * @param col the column of which the cell is located.
     * @return true if the specified cell is empty, otherwise false.
     */
    static boolean isCellEmpty(char[][] field, int row, int col) {
        return field[row][col] == '~';
    }

    static boolean isCellEmpty(char[][] field, String cord) {
        return isCellEmpty(
                field,
                extractRowIndex(cord.charAt(0)),
                extractColIndex(cord.substring(1)));
    }

    /**
     * Place the ship represented by cord1 & cord2 on the field.
     * Fill the cells from cord1 -> cord2 with 'O'.
     *
     * @param cord1       the head of the battleship.
     * @param cord2       the tail of the battleship.
     * @param orientation of the battleship.
     */
    private void placeShip(String cord1, String cord2, String orientation) {
        switch (orientation) {
            case "horizontal":
                /* In horizontal mode, the difference is in the columns. */
                int row = extractRowIndex(cord1.charAt(0));
                int cord1Col = extractColIndex(cord1.substring(1));
                int cord2Col = extractColIndex(cord2.substring(1));

                int starColIndex = Math.min(cord1Col, cord2Col);
                int endColIndex = Math.max(cord1Col, cord2Col);
                for (int i = starColIndex; i <= endColIndex; i++) {
                    field[row][i] = 'O';
                }

            case "vertical":
                /* In vertical mode, the difference is in the rows. */
                int col = extractColIndex(cord1.substring(1));
                int cord1Row = extractRowIndex(cord1.charAt(0));
                int cord2Row = extractRowIndex(cord2.charAt(0));

                int startRowIndex = Math.min(cord1Row, cord2Row);
                int endRowIndex = Math.max(cord1Row, cord2Row);
                for (int i = startRowIndex; i <= endRowIndex; i++) {
                    field[i][col] = 'O';
                }
        }
    }

    /**
     * Take a shot and hit a point on the opponent's field and report the result.
     *
     * @param opponent of this player.
     * @return the report of the shot.
     */
    String takeShotAndReport(Player opponent) {
        Scanner sc = new Scanner(System.in);

        printField(opponent.fogOfWarField);
        System.out.println("---------------------");
        printField(this.field);
        System.out.println(this.name + ", it's your turn:");

        String shot = sc.nextLine().trim();
        while (!isValidCoordinate(shot)) {
            System.out.print("Improper format of the target! Try again:\n");
            shot = sc.nextLine().trim();
        }

        int row = extractRowIndex(shot.charAt(0));
        int col = extractColIndex(shot.substring(1));

        String report;
        if (isCellEmpty(opponent.field, row, col)) {
            report = "You missed!";
            opponent.field[row][col] = 'M';
            opponent.fogOfWarField[row][col] = 'M';
        } else {
            report = "You hit a ship!";
            opponent.field[row][col] = 'X';
            opponent.fogOfWarField[row][col] = 'X';

            // Check if a ship was sunk due to the hit.
            for (Battleship battleship : opponent.battleships) {
                if (isSunk(opponent.field, battleship)) {
                    opponent.battleships.remove(battleship);
                    report = "You sank a ship! Specify a new target";

                    if (opponent.battleships.size() == 0) {
                        report = "You sank the last ship. You won. Congratulations!\n" +
                                "The winner is " + this.name;
                    }

                    break;
                }
            }
        }

        return report;
    }

}

























