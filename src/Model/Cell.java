package Model;
import java.util.ArrayList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alex
 */
public class Cell {

    private final int x, y;
    private boolean isBlack, hasPiece;
    private Cell top, right, bottom, left, topLeft, topRight,
            bottomLeft, bottomRight;
    private Piece thePiece;
    private ArrayList<Cell> neighbours;
    private boolean isHighLight;

    /**
     * Creates a Cell instance with designated x and y values
     *
     * @param x The x value represented by the cell
     * @param y The y value represented by the cell
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        hasPiece = false;
        thePiece = null;
        isHighLight = false;
    }

    /**
     * Returns boolean value representing the color of the cell
     * @return isBlack The boolean value
     *
     */
    public boolean isCellBlack() {
        return isBlack;
    }

    /**
     * HighLight marker that marks the cell, boolean value for marked
     *
     * @return isHighLight The boolean value
     */
    public boolean getHighLight() {
        return isHighLight;
    }

    /**
     * Sets HighLight boolean value
     *
     * @param b
     */
    public void setHightLight(boolean b) {
        isHighLight = false;
    }

    /**
     * Sets boolean value representing the color of the cell
     *
     * @param b The boolean value
     */
    public void setBlack(boolean b) {
        isBlack = b;
    }

    /**
     * Returns true if the cell has a piece, false if not
     *
     * @return hasPiece The boolean value
     */
    public boolean cellHasPiece() {
        return hasPiece;
    }

    /**
     * Adds a new piece to the cell and sets boolean value hasPiece to true
     *
     * @param newPiece The boolean value
     */
    public void addPiece(Piece newPiece) {
        thePiece = newPiece;
        hasPiece = true;
    }

    /**
     * Removes piece from board and returns the piece, boolean value hasPiece
     * becomes false
     *
     * @return thePiece The piece that is returned
     */
    public Piece removePiece() {
        Piece deadPiece = thePiece;
        thePiece = null;
        hasPiece = false;
        return deadPiece;
    }

    /**
     * Returns the piece in the cell, null if there is none
     *
     * @return thePiece
     */
    public Piece getPiece() {
        return thePiece;
    }

    /**
     * Returns the x value for this instance
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y value for this instance
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Returns an ArrayList of cells for the neighbours of the cell
     *
     * @return neighbours ArrayList of neighbouring cells
     */
    public ArrayList<Cell> returnNeighbours() {
        return (ArrayList<Cell>) neighbours.clone();
    }

    /**
     * Sets values for the neighbours of this cell instance
     *
     * @param b The Board
     */
    public void getNeighbours(Board b) {
        neighbours = new ArrayList();

        top = neighbourCell(0, b);
        right = neighbourCell(1, b);
        bottom = neighbourCell(2, b);
        left = neighbourCell(3, b);
        topLeft = neighbourCell(4, b);
        topRight = neighbourCell(5, b);
        bottomRight = neighbourCell(6, b);
        bottomLeft = neighbourCell(7, b);

        neighbours.add(topLeft);
        neighbours.add(top);
        neighbours.add(topRight);
        neighbours.add(left);
        neighbours.add(right);
        neighbours.add(bottomLeft);
        neighbours.add(bottom);
        neighbours.add(bottomRight);
    }

    private Cell neighbourCell(int i, Board b) {
        //TODO dimensions of board
        Cell[][] theB = b.getBoard();
        if (i == 0 && this.x - 1 >= 0) {
            return theB[this.y][this.x - 1];
        } else if (i == 1 && this.y + 1 < 10) {
            return theB[this.y + 1][this.x];
        } else if (i == 2 && this.x + 1 < 10) {
            return theB[this.y][this.x + 1];
        } else if (i == 3 && this.y - 1 >= 0) {
            return theB[this.y - 1][this.x];
        } else if (i == 4 && top != null && left != null && this.x - 1 >= 0
                && this.y - 1 >= 0) {
            return theB[this.y - 1][this.x - 1];
        } else if (i == 5 && top != null && right != null && this.x - 1 >= 0
                && this.y + 1 < 10) {
            return theB[this.y + 1][this.x - 1];
        } else if (i == 6 && bottom != null && right != null && this.y + 1 < 10
                && this.x + 1 < 10) {
            return theB[this.y + 1][this.x + 1];
        } else if (i == 7 && bottom != null && left != null && this.y - 1 >= 0
                && this.x + 1 < 10) {
            return theB[this.y - 1][this.x + 1];
        }
        return null;
    }
}
