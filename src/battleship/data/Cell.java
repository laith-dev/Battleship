package battleship.data;

public class Cell {
    int row;
    int col;

    /**
     * Create a cell by specifying its row and column indexes.
     *
     * @param row index of the cell.
     * @param col index of the cell.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
}