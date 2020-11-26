package Model;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alex
 */
public class Board {

    private int height, width;
    private Cell[][] theBoard;

    /**
     * Creates an instance of a Board with designated height and width
     *
     * @param width The width of the board
     * @param height The height of the board
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        theBoard = new Cell[this.width][this.height];
        fillBoard();
    }

    /**
     * Assign color to the cells by setting Cells datafield to either true(black)
     * or false(white). Also declare neighbours in the board for each cell
     */
    private void fillBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                theBoard[j][i] = new Cell(i, j);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 != 0 && j % 2 != 0) {
                    theBoard[j][i].setBlack(true);
                }
            }
        }
        for (Cell[] cArr : theBoard) {
            for (Cell cell : cArr) {
                cell.getNeighbours(this);
            }
        }
    }
    
    /**
     * At gameover, clear the board of pieces, rendering the board noninteractive
     */

    public void clearPieces() {
        for (Cell[] ca : theBoard) {
            for (Cell c : ca) {
                if (c.cellHasPiece()) {
                    c.removePiece();
                }
            }
        }
    }

    /**
     * Returns the width of the board
     *
     * @return width
     */
    public int getBoardLength() {
        return width;
    }

    /**
     * Returns the cell of the board with designated row and column values,
     * returns null if there is no cell for the row and column value
     *
     * @param row The row value
     * @param col The column value
     * @return cell
     */
    public Cell getCell(int row, int col) {
        return theBoard[row][col];
    }

    /**
     * Returns this board instance
     *
     * @return theBoard
     */
    public Cell[][] getBoard() {
        return theBoard;
    }
}
