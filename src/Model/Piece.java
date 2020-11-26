package Model;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexander
 */
public class Piece {

    private boolean pieceDead, isDame;
    private final boolean isWhite;

    /**
     * Cretae a piece with designated color represented by boolean value
     *
     * @param isWhite Boolean value for Color
     */
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        pieceDead = false;
        isDame = false;
    }
    
    /**
     * Sets value for the status of the piece
     *
     * @param b Boolean value for status
     */
    public void isDead(boolean b) {
        pieceDead = b;
    }

    /**
     * Gets value for the status of the piece
     *
     * @return pieceDead Boolean value for status
     */
     
    public boolean isDead() {
        return pieceDead;
    }

        /**
     * Returns value for the color of the piece represented by boolean value
     *
     * @return isWhite
     */
    public boolean isPieceWhite() {
        return isWhite;
    }

    /**
     * Returns value boolean value of Dame, true if dame, else false
     * @return is Dame
     */
    public boolean isPieceDame() {
        return isDame;
    }

    /**
     * Sets status for dame
     *
     * @param b Boolean value for isDame
     */

    public void setPieceDame(boolean b) {
        isDame = b;
    }

}
