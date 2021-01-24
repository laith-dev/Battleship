package battleship;

import static battleship.Player.*;

public class Battleship {

    /* Type of the ship. An Aircraft Carrier, Battleship, Submarine...etc. */
    private final String type;

    /* Size of the ship in cells. */
    private final int size;

    /* Coordinates of the battleship. */
    private String cord1, cord2;

    /* Physical orientation of the battleship, horizontal or vertical. */
    private String orientation;

    /**
     * Create a battleship by specifying its type and size.
     *
     * @param type of the ship.
     * @param size of the ship.
     */
    public Battleship(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getCord1() {
        return cord1;
    }

    public void setCord1(String cord1) {
        this.cord1 = cord1;
    }

    public String getCord2() {
        return cord2;
    }

    public void setCord2(String cord2) {
        this.cord2 = cord2;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * Check if this battleship has sunk. A battleship is sunk if all of its cells were hit.
     *
     * @param battleship the battleship to check.
     * @param field      the field in which to check the battleship still exists or not.
     * @return true if the battleship is sunk, otherwise false.
     */
    public static boolean isSunk(char[][] field, Battleship battleship) {
        switch (battleship.orientation) {
            case "horizontal":
                /* In horizontal mode, the difference is in the columns. */
                int row = extractRowIndex(battleship.getCord1().charAt(0));
                int cord1Col = extractColIndex(battleship.getCord1().substring(1));
                int cord2Col = extractColIndex(battleship.getCord2().substring(1));

                int startColIndex = Math.min(cord1Col, cord2Col);
                int endColIndex = Math.max(cord1Col, cord2Col);
                /* If a single O found, then ship is still alive and not sunk yet. */
                for (int i = startColIndex; i <= endColIndex; i++) {
                    if (field[row][i] == 'O') {
                        return false;
                    }
                }

                return true;

            case "vertical":
                /* In vertical mode, the difference is in the rows. */
                int col = extractColIndex(battleship.getCord1().substring(1));
                int cord1Row = extractRowIndex(battleship.getCord1().charAt(0));
                int cord2Row = extractRowIndex(battleship.getCord2().charAt(0));

                int startRowIndex = Math.min(cord1Row, cord2Row);
                int endRowIndex = Math.max(cord1Row, cord2Row);
                /* If a single O found, the ship is still alive and not sunk yet. */
                for (int i = startRowIndex; i <= endRowIndex; i++) {
                    if (field[i][col] == 'O') {
                        return false;
                    }
                }

                return true;

            default:
                throw new IllegalStateException("Unknown orientation of battleship " + battleship.orientation);
        }
    }
}














