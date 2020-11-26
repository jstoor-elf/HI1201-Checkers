package Model;
import java.util.ArrayList;
import java.util.Random;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexander
 */
public class theGame {

    private final Board theBoard;
    private Cell[][] gameBoard;
    private final int width, height;
    private boolean player;
    private int redScore, yellowScore;
    private boolean isAIactive;
    private AI artificialIntelligence;

    /**
     * Creates an instance of the board game wtih height and width representing
     * the size of the board
     *
     * @param width The width of the board
     * @param height The height of the board
     */
    public theGame(int width, int height) {
        this.width = width;
        this.height = height;
        theBoard = new Board(width, height);
        gameBoard = theBoard.getBoard();
        player = true;
        redScore = yellowScore = 0;
        artificialIntelligence = null;
        placePieces();
    }

    /**
     * Creates an instance of this the game with an already existing board
     *
     * @param loadedBoard The already existing board
     */
    public theGame(Board loadedBoard) {
        this.width = loadedBoard.getBoardLength();
        this.height = loadedBoard.getBoardLength();
        this.theBoard = loadedBoard;
        player = getPlayer();
        redScore = getRedScore();
        yellowScore = getYellowScore();
        gameBoard = loadedBoard.getBoard();
    }

    /* Private method calles by the constructor, places pieces on board when the 
    game is initialized */
    private void placePieces() { //should be done in view
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (gameBoard[i][j].isCellBlack() && j < 4) {
                    gameBoard[i][j].addPiece(new Piece(true));
                } else if (gameBoard[i][j].isCellBlack() && j > height - 5) {
                    gameBoard[i][j].addPiece(new Piece(false));
                }
            }
        }
    }

    /**
     * Moves the piece in the cell @current to the one in @target if possible If
     * the move is possible it's done and a boolean true is returnes, if not
     * nothing is done and false is returned
     *
     * @param current
     * @param target
     * @return boolean
     */
    public boolean move(Cell current, Cell target) {
        ArrayList<Cell> possibleMoves = calculateMoves(current);
        if (possibleMoves.contains(target)) {
            Piece p = current.removePiece();
            target.addPiece(p);
            if (target.getPiece().isPieceWhite() && target.getX() == width - 1) {
                target.getPiece().setPieceDame(true);
            } else if (!target.getPiece().isPieceWhite() && target.getX() == 0) {
                target.getPiece().setPieceDame(true);
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the possible moves for the piece in theTarget Cell and Returns
     * an ArrayList of Cells representing the possible moves
     *
     * @param theTarget
     * @return theMoves
     */
    public ArrayList<Cell> calculateMoves(Cell theTarget) {
        ArrayList<Cell> theMoves = new ArrayList<>();
        if (theTarget.cellHasPiece() && theTarget.getPiece().isPieceDame()) {
            theMoves = calculateDameMoves(theTarget);
        } else if (theTarget.cellHasPiece() && theTarget.getPiece().isPieceWhite() && !player) {
            for (int i = 0; i < theTarget.returnNeighbours().size(); i++) {
                Cell temp = theTarget.returnNeighbours().get(i);
                if (temp != null && temp.isCellBlack() && temp.getX() > theTarget.getX()) {
                    if (temp.cellHasPiece()) {
                        if (temp.getPiece().isPieceWhite() != theTarget.getPiece().isPieceWhite()) {
                            Cell temp2 = temp.returnNeighbours().get(i);
                            if (temp2 != null && temp.getX() != temp2.getX()
                                    && temp.getY() != temp2.getY() && !temp2.cellHasPiece()) {
                                theMoves.add(temp);
                                theMoves.add(temp2);
                            }
                        }

                    } else {
                        theMoves.add(temp);
                    }
                }
            }
        } else if (theTarget.cellHasPiece() && !theTarget.getPiece().isPieceWhite() && player) {
            for (int i = 0; i < theTarget.returnNeighbours().size(); i++) {
                Cell temp = theTarget.returnNeighbours().get(i);
                if (temp != null && temp.isCellBlack() && temp.getX() < theTarget.getX()) {
                    if (temp.cellHasPiece()) {
                        if (temp.getPiece().isPieceWhite() != theTarget.getPiece().isPieceWhite()) {
                            Cell temp2 = temp.returnNeighbours().get(i);
                            if (temp2 != null && temp.getX() != temp2.getX()
                                    && temp.getY() != temp2.getY() && !temp2.cellHasPiece()) {
                                theMoves.add(temp);
                                theMoves.add(temp2);
                            }
                        }

                    } else {
                        theMoves.add(temp);
                    }
                }
            }
        }
        return theMoves;
    }

    /* Private method which is called by the calculateMoves if the piece is a,
    * dame, e.g other logic for a dame*/
    private ArrayList<Cell> calculateDameMoves(Cell cell) {
        Board board = getBoard();
        ArrayList<Cell> possiblemoves = new ArrayList();
        ArrayList<Integer> list = new ArrayList();
        list.add(1);
        list.add(-1);
        list.add(1);
        list.add(-1);
        list.add(-1);
        list.add(-1);
        list.add(1);
        list.add(1);

        for (int i = 0; i < 4; i++) {
            if (!cell.getPiece().isPieceWhite() == player) {
                if (cell.getY() + list.get(i) >= 0 && cell.getX() + list.get(i + 4) >= 0
                        && cell.getY() + list.get(i) <= board.getBoardLength() - 1
                        && cell.getX() + list.get(i + 4) <= board.getBoardLength() - 1) {
                    Cell c1 = board.getCell(cell.getY() + list.get(i), cell.getX() + list.get(i + 4));
                    while (c1 != null && !c1.cellHasPiece()) {
                        possiblemoves.add(c1);
                        if (c1.getY() + list.get(i) >= 0 && c1.getX() + list.get(i + 4) >= 0
                                && c1.getY() + list.get(i) <= board.getBoardLength() - 1
                                && c1.getX() + list.get(i + 4) <= board.getBoardLength() - 1) {
                            c1 = board.getCell(c1.getY() + list.get(i), c1.getX() + list.get(i + 4));
                        } else {
                            c1 = null;
                        }
                    }
                    if (c1 != null && c1.cellHasPiece() && c1.getPiece().isPieceWhite() != cell.getPiece().isPieceWhite()
                            && Math.abs(c1.getX() - cell.getX()) == 1) {
                        if (c1.getY() + list.get(i) >= 0 && c1.getX() + list.get(i + 4) >= 0
                                && c1.getY() + list.get(i) <= board.getBoardLength() - 1
                                && c1.getX() + list.get(i + 4) <= board.getBoardLength() - 1) {
                            Cell c2 = board.getCell(c1.getY() + list.get(i), c1.getX() + list.get(i + 4));
                            if (c2 != null && !c2.cellHasPiece()) {
                                possiblemoves.add(c1);
                                possiblemoves.add(c2);
                            }
                        }
                    }
                }
            }
        }
        return possiblemoves;
    }

    /**
     * Moves AI if AI is active, the logic is handled in the AI class
     *
     * @return ArrayList of the cell moved from an moved to
     */
    public ArrayList<Cell> moveAI() {
        if (isAIactive && artificialIntelligence != null) {
            return artificialIntelligence.move();
        }
        return null;
    }

    /**
     * Activates an instance of the nestled AI class if boolean in paramater
     * value is true
     *
     * @param b Boolean value
     */
    public void setAI(boolean b) {
        if (b) {
            isAIactive = true;
            artificialIntelligence = new AI();
        } else if (!b) {
            isAIactive = false;
            artificialIntelligence = null;
        }
    }

    /**
     * Returns the status of the AI, is acitve if true
     *
     * @return isAIcctive
     */
    public boolean isAIactive() {
        return isAIactive;
    }

    /**
     * Returns boolean value representing game status, if all pieces of one
     * player is taken gameOver returns true, otherwise false
     *
     * @return boolean
     */
    public boolean gameOver() {
        if (getRedScore() >= 20 || getYellowScore() >= 20) {
            return true;
        }
        return false;
    }

    /**
     * Set status for player, true if active, false if not
     *
     * @param b Boolean value for status
     */
    public void setPlayer(boolean b) {
        player = b;
    }

    /**
     * Returns status for player (true for yellow player, false for red player)
     *
     * @return player
     */
    public boolean getPlayer() {
        return player;
    }

    /**
     * Returns the board instance
     *
     * @return theBoard
     */
    public Board getBoard() {
        return theBoard;
    }

    /**
     * Returns player score for red player
     *
     * @return redScore
     */
    public int getRedScore() {
        return redScore;
    }

    /**
     * Return player score for yellow player
     *
     * @return yellowScore
     */
    public int getYellowScore() {
        return yellowScore;
    }

    /**
     * Sets score for redScore, increments with one
     *
     * @param score Inparameter which is incremented
     */
    public void setRedScore(int score) {
        redScore += score;
    }

    /**
     * Sets score for yellowScore, increments with one,
     *
     * @param score Inparameter which is incremented
     */
    public void setYellowScore(int score) {
        yellowScore += score;
    }

    private class AI {

        public ArrayList<Cell> move() {
            ArrayList<Cell[]> possiblemoves1 = new ArrayList();
            ArrayList<Cell[]> possiblemoves2 = new ArrayList();
            ArrayList<Cell[]> possiblemoves3 = new ArrayList();
            ArrayList<Cell[]> possiblemoves4 = new ArrayList();
            int weight = 0;

            for (int i = 0; i < theBoard.getBoardLength(); i++) {
                for (int j = 0; j < theBoard.getBoardLength(); j++) {
                    if (theBoard.getCell(j, i).cellHasPiece()) {
                        if (theBoard.getCell(j, i).getPiece().isPieceWhite()) {
                            for (Cell c : theGame.this.calculateMoves(theBoard.getCell(j, i))) {
                                Cell[] move = new Cell[2];
                                weight = quantifyMove(theBoard.getCell(j, i), c);
                                move[0] = theBoard.getCell(j, i);
                                move[1] = c;
                                if (weight == 1) {
                                    possiblemoves1.add(move);
                                } else if (weight == 2) {
                                    possiblemoves2.add(move);
                                } else if (weight == 3) {
                                    possiblemoves3.add(move);
                                } else if (weight == 4) {
                                    possiblemoves4.add(move);
                                }
                            }
                        }
                    }
                }
            }

            //
            ArrayList<Cell> finalCells = new ArrayList();
            int k = 0;
            Random random = new Random();
            if (!possiblemoves4.isEmpty()) {
                k = random.nextInt(possiblemoves4.size());
                finalCells.add(possiblemoves4.get(k)[0]);
                finalCells.add(possiblemoves4.get(k)[1]);
            } else if (!possiblemoves3.isEmpty()) {
                k = random.nextInt(possiblemoves3.size());
                finalCells.add(possiblemoves3.get(k)[0]);
                finalCells.add(possiblemoves3.get(k)[1]);
            } else if (!possiblemoves2.isEmpty()) {
                k = random.nextInt(possiblemoves2.size());
                finalCells.add(possiblemoves2.get(k)[0]);
                finalCells.add(possiblemoves2.get(k)[1]);
            } else if ((!possiblemoves1.isEmpty())) {
                k = random.nextInt(possiblemoves1.size());
                finalCells.add(possiblemoves1.get(k)[0]);
                finalCells.add(possiblemoves1.get(k)[1]);
            }
            return finalCells;
        }

        private int quantifyMove(Cell c1, Cell c2) {
            int k = 0;

            // if the move is over two cells, one yellow is taken = best move 
            if (c2.getX() == width - 1 && c1.cellHasPiece() && !c1.getPiece().isPieceDame()) {
                k = 4;
            } else if (Math.abs(c1.getX() - c2.getX()) == 2) {
                k = 3;
            } else if (c1.cellHasPiece() && c1.getPiece().isPieceDame()) {

                k = 2;
            } else if (c2.returnNeighbours().get(5) != null && c2.returnNeighbours().get(7) != null) {
                if ((c2.returnNeighbours().get(5).cellHasPiece() && c2.returnNeighbours().get(5).getPiece().isPieceWhite())
                        || (c2.returnNeighbours().get(7).cellHasPiece()
                        && c2.returnNeighbours().get(7).getPiece().isPieceWhite())) {
                    k = 1;
                } else {
                    k = 2;
                }
            } else if (c2.returnNeighbours().get(5) != null) {
                if (c2.returnNeighbours().get(5).cellHasPiece()) {
                    if (c2.returnNeighbours().get(5).getPiece().isPieceWhite()) {
                        k = 1;
                    }
                } else {
                    k = 2;
                }
            } else {
                if (c2.returnNeighbours().get(7).cellHasPiece()) {
                    if (c2.returnNeighbours().get(7).getPiece().isPieceWhite()) {
                        // Not a good move
                        k = 1;
                    }
                } else {
                    k = 2;
                }
            }
            return k;
        }
    }
}
