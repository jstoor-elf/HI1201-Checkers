package Controller;

import Model.theGame;
import Model.Cell;
import GUI.GameGUI;
import GUI.NewgameView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexander
 */
public class Gamecontroller {

    private final theGame game;
    private final NewgameView theView;
    private Cell origin, target;

    public Gamecontroller(theGame game, NewgameView theView) {
        this.game = game;
        this.theView = theView;
    }

    public void handleSaveEvent() throws IOException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String reportDate = dateFormat.format(date);
        PrintWriter writer = new PrintWriter(System.getProperty("user.home")
                + "/Desktop/Dam/SavedGames/" + reportDate + ".txt");
        Cell[][] cellarray = game.getBoard().getBoard();
        int counter = 0;
        // IsCellBlack, hasCellPiece, isPieceYellow, isPieceLady.
        for (Cell[] cArr : cellarray) {
            for (Cell c : cArr) {
                if (counter != 0) {
                    writer.println();
                }
                if (c.cellHasPiece()) {
                    writer.write("1,");
                } else {
                    writer.write("0,");
                }
                if (c.cellHasPiece() && c.getPiece().isPieceWhite()) {
                    writer.write("1,");
                } else {
                    writer.write("0,");
                }
                if (c.cellHasPiece() && c.getPiece().isPieceDame()) {
                    writer.write("1,");
                } else {
                    writer.write("0,");
                }
                writer.write(c.getX() + "," + c.getY() + ",");
                counter++;
            }
        }
        if (game.getPlayer()) {
            writer.write("1,");
        } else {
            writer.write("0,");
        }
        writer.write(game.getRedScore() + "," + game.getYellowScore());
        if (game.isAIactive()) {
            writer.write(",1");
        } else {
            writer.write(",0");
        }
        writer.close();
    }

    public void handleMove() throws InterruptedException {
        ArrayList<Cell> neighbours = origin.returnNeighbours();
        ArrayList<Cell> toReDraw = new ArrayList();
        int counter = 0;
        if (origin.equals(target) || !game.calculateMoves(origin).
                contains(target)) {
            return;
        } else if (target.cellHasPiece() && target.getPiece().isPieceWhite()
                != origin.getPiece().isPieceWhite()) {
            return;
        } else {
            if (game.calculateMoves(origin).contains(target)) {
                if (game.move(origin, target)) {
                    toReDraw.add(origin);
                    toReDraw.add(target);
                    if (Math.abs(origin.getX() - target.getX()) == 2) {
                        for (Cell theCell : neighbours) {
                            if (theCell != null && theCell.cellHasPiece()
                                    && (origin.getX() + target.getX()) / 2
                                    == theCell.getX()
                                    && (origin.getY() + target.getY()) / 2
                                    == theCell.getY()) {
                                if (target.getPiece().isPieceWhite()) {
                                    game.setRedScore(1);
                                } else if (!target.getPiece().isPieceWhite()) {
                                    game.setYellowScore(1);
                                }
                                if (game.gameOver()) {
                                    theView.gameOverAlert(game.getPlayer());
                                    try {
                                        saveToHistory();
                                    } catch (IOException e) {

                                    }
                                }
                                theCell.removePiece();
                                toReDraw.add(theCell);
                                break;
                            }
                        }
                    }
                }
            }
        }
        game.setPlayer(!game.getPlayer());
        theView.reDraw(toReDraw);
        if (game.isAIactive() && !game.getPlayer()) {
            ArrayList<Cell> moves = game.moveAI();
            storeOrigin(moves.get(0).getY(), moves.get(0).getX());
            storeTarget(moves.get(1).getY(), moves.get(1).getX());
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {

                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    try {
                        handleMove();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Gamecontroller.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }
            });
            new Thread(sleeper).start();
        }
    }

    public void storeOrigin(int row, int col) {
        origin = game.getBoard().getCell(row, col);
        //System.out.println(origin.getX() + "," + origin.getY());
    }

    public void storeTarget(int row, int col) {
        target = game.getBoard().getCell(row, col);
    }

    private void saveToHistory() throws IOException {
        File saveDir = new File(System.getProperty("user.home"), 
                "/Desktop/Dam/History");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String reportDate = dateFormat.format(date);
        if (saveDir.list().length == 0) {
            PrintWriter writer = new PrintWriter(System.getProperty("user.home") 
                    + "/Desktop/Dam/History/Gamehistory.txt");
            if (game.getPlayer()) {
                sb.append("Red player won, Score: " + game.getRedScore() + " - "
                        + game.getYellowScore() + " " + ", Game played : "
                        + reportDate);
            } else if (!game.getPlayer()) {
                sb.append("Yellow player won, Score: " + game.getYellowScore() + 
                        " - "
                        + game.getRedScore() + " " + ", Game played : "
                        + reportDate);
            }
            writer.write(sb.toString() + "\n");
            writer.close();
        } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    System.getProperty("user.home") + "/Desktop/Dam/History/Gamehistory.txt", true));
            if (!game.getPlayer()) {
                if (!game.isAIactive()) {
                    sb.append("Red player won, Score: " + game.getRedScore() + " - "
                            + game.getYellowScore() + " " + ", Game played : "
                            + reportDate);
                } else if (game.isAIactive()) {
                    sb.append("Computer won, Score: " + game.getRedScore() + " - "
                            + game.getYellowScore() + " " + ", Game played : "
                            + reportDate);
                }
            } else if (game.getPlayer()) {
                sb.append("Yellow player won, Score: " + game.getYellowScore() + " - "
                        + game.getRedScore() + " " + ", Game played : "
                        + reportDate);
            }
            writer.write(sb.toString() + "\n");
            writer.close();
        }

    }
}
