package GUI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Model.Cell;
import Model.Board;
import Model.theGame;
import Controller.Gamecontroller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Alex
 */
public class NewgameView extends Pane {

    private final theGame game;
    private final Cell[][] gameboard;
    private final int width, height;
    private double boardLength;
    private ArrayList<ImageView> pieces;
    private final TextArea gameInfo;
    private final GridPane pane1;
    private final VBox pane2;
    private final Label redplayer, yellowplayer, howtoplay;
    private int redcounter, yellowcounter;
    private Button b;
    private final Image RedQueen, YellowQueen;
    private HBox yellowScoreHbox, redScoreHbox;
    private double gridCellWidth, gridCellHeight;

    public NewgameView(theGame g, int width, int height) {
        redcounter = yellowcounter = 0;
        this.game = g;
        gameboard = game.getBoard().getBoard();
        this.width = width;
        this.height = height - 30;
        boardLength = 0;
        gameInfo = new TextArea();
        redplayer = new Label("Red Player");
        yellowplayer = new Label("Yellow Player");
        howtoplay = new Label("HOOVER OVER PIECES TO SEE POSSIBLE MOVES\n"
                + "MOVE PIECE BY CLICKING DOWN ON A PIECE "
                + "\nAND DRAG TO DESIRED LOCATION");
        pane1 = new GridPane();
        pane2 = new VBox(15);
        yellowScoreHbox = new HBox(3);
        redScoreHbox = new HBox(3);
        pane1.setPrefSize(width / 2, height);
        pane2.setPrefSize(width / 2, height);
        b = new Button("Save Game");
        pane2.getChildren().add(b);
        this.getChildren().addAll(pane1, pane2);
        RedQueen = new Image(("resources/RedDame.png"),
                40, 40, false, false);
        YellowQueen = new Image(("resources/YellowDame.png"),
                40, 40, false, false);
        initializeGame(this.width, this.height);
    }

    public void addEventHandlers(Gamecontroller controller) {
        EventHandler targetEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getX() > 0 && event.getX() < boardLength
                        && event.getY() > 0 && event.getY() < boardLength) {
                    double x = event.getX(), y = event.getY();
                    int row = (int) ((x * 10) / boardLength);
                    int col = (int) ((y * 10) / boardLength);
                    controller.storeOrigin(row, col);
                }
            }
        };

        EventHandler dropEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getX() > 0 && event.getX() < boardLength
                        && event.getY() > 0 && event.getY() < boardLength) {
                    double x = event.getX(), y = event.getY();
                    int row = (int) ((x * 10) / boardLength);
                    int col = (int) ((y * 10) / boardLength);
                    controller.storeTarget(row, col);
                    try {
                        controller.handleMove();
                    } catch (InterruptedException e) {

                    }
                }
            }

        };
        pane1.setOnMousePressed(targetEvent);
        pane1.setOnMouseReleased(dropEvent);
        EventHandler saveEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    controller.handleSaveEvent();
                } catch (IOException ex) {
                    Logger.getLogger(NewgameView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        b.setOnAction(saveEvent);
    }

    public void reDraw(ArrayList<Cell> redraw) {
        Cell origin = redraw.get(0);
        Cell target = redraw.get(1);
        ImageView movedIm = getImageView(origin.getX(), origin.getY());
        if (redraw.size() > 2) {
            Cell killedCell = redraw.get(2);
            addPlayerScore(target);
            pane1.getChildren().remove(getImageView(killedCell.getX(), killedCell.getY()));
            pieces.remove(getImageView(killedCell.getX(), killedCell.getY()));
        }
        animateMove(movedIm, target);
        movedIm.setX(target.getX());
        movedIm.setY(target.getY());
        if (target.getPiece().isPieceWhite() && target.getX() >= 9) {
            movedIm.setImage(RedQueen);
        } else if (!target.getPiece().isPieceWhite() && target.getX() == 0) {
            movedIm.setImage(YellowQueen);
        }
        movedIm.setOnMouseMoved((MouseEvent event) -> {
            highLightEvent(target, (Rectangle) getNodeFromGrid(target.getX(),
                    target.getY()));
        });
        movedIm.setOnMouseExited((MouseEvent event) -> {
            deHighLightEvent(target, (Rectangle) getNodeFromGrid(target.getX(),
                    target.getY()));
        });
        if (game.gameOver()) {
            game.getBoard().clearPieces();
        }
        if (!game.getPlayer()) {
            gameInfo.appendText("Yellow player move from: " + origin.getX() + "," + origin.getY()
                    + " to: " + target.getX() + "," + target.getY() + "\n");
        } else if (game.getPlayer() && !game.isAIactive()) {
            gameInfo.appendText("Red player move from: " + origin.getX() + "," + origin.getY()
                    + " to: " + target.getX() + "," + target.getY() + "\n");
        } else if (game.getPlayer() && game.isAIactive()) {
            gameInfo.appendText("Computer move from: " + origin.getX() + "," + origin.getY()
                    + " to: " + target.getX() + "," + target.getY() + "\n");
        }
    }

    private void addPlayerScore(Cell c) {
        Ellipse ep = new Ellipse(5, 5);
        ep.setStroke(Color.BLACK);
        ep.setStrokeWidth(2);
        if (!c.getPiece().isPieceWhite()) {
            ep.setFill(Color.RED);
            yellowScoreHbox.getChildren().add(ep);
        } else if (c.getPiece().isPieceWhite()) {
            ep.setFill(Color.YELLOW);
            redScoreHbox.getChildren().add(ep);
        }
    }

    public void reDrawPlayerScore(boolean red) {
        Ellipse ep = new Ellipse(5, 5);
        ep.setStroke(Color.BLACK);
        ep.setStrokeWidth(2);
        if (red) {
            ep.setFill(Color.YELLOW);
            redScoreHbox.getChildren().add(ep);
        } else {
            ep.setFill(Color.RED);
            yellowScoreHbox.getChildren().add(ep);
        }
    }

    private void animateMove(ImageView iv, Cell target) {
        double xD = target.getY() - iv.getY();
        double yD = target.getX() - iv.getX();

        PathTransition pt = new PathTransition();/*
        Line l = new Line((int)(50.8 / 2 + iv.getTranslateX()),(int) 50.8 / 2 + iv.getTranslateY(),
                (int)(xD * 50.8) + (50.8 / 2) + iv.getTranslateX(),
                (int)(yD * 50.8) + (50.8 / 2) + iv.getTranslateY()-5);*/
        Line l = new Line((int) (50 / 2 + iv.getTranslateX()), (int) ((50 / 2) - 5 + iv.getTranslateY()),
                (int) (xD * 51) + (51 / 2 + iv.getTranslateX()),
                (int) (yD * 51) + (51 / 2 + iv.getTranslateY()));
        l.setStroke(Color.BLUE);
        l.setStrokeWidth(2);
        pt.setNode(iv);
        pt.setPath(l);
        pt.setDuration(Duration.seconds(1));
        pt.play();
    }

    private void initializeGame(int w, int h) {
        int cellLength = 0;
        boolean count = false;
        int rectCounter = 0;
        pieces = new ArrayList<>();
        Image Yim = new Image(("resources/YellowPiece.png"),
                40, 40, false, false);
        Image Rim = new Image(("resources/RedPiece.png"),
                40, 40, false, false);
        imageV iv = null;
        for (Cell[] cellArr : gameboard) {
            for (Cell c : cellArr) {
                Rectangle r = new Rectangle((h / 10), (h / 10));
                if (c.isCellBlack()) {
                    r.setFill(Color.BLACK);
                } else {
                    r.setFill(Color.WHITE);
                }

                r.setStroke(Color.BLACK);
                r.setId("r" + rectCounter++);
                r.setY(c.getX());
                r.setX(c.getY());
                r.setLayoutX(r.getY() * height);
                r.setLayoutY(r.getX() * height);
                gridCellWidth = r.getWidth();
                gridCellHeight = r.getHeight();
                if (count == false) {
                    cellLength += r.getWidth();
                }
                if (c.cellHasPiece() && c.getPiece().isPieceWhite()) {
                    iv = new imageV(Rim);
                    if (c.getPiece().isPieceDame()) {
                        iv.setImage(RedQueen);
                    }
                    iv.setId("iv" + (rectCounter - 1));
                    iv.setTranslateX((height / 10 - iv.getImage().getWidth()) / 2);
                    iv.setX(c.getX());
                    iv.setY(c.getY());

                    iv.setOnMouseEntered((MouseEvent event) -> {
                        highLightEvent(c, r);
                    });
                    iv.setOnMouseExited((MouseEvent event) -> {
                        deHighLightEvent(c, r);
                    });
                    iv.setLayoutX(r.getLayoutX() + 50.8 / 2);
                    iv.setLayoutY(r.getLayoutY() + 50.8 / 2);
                    pieces.add(iv);
                } else if (c.cellHasPiece() && !c.getPiece().isPieceWhite()) {
                    iv = new imageV(Yim);
                    if (c.getPiece().isPieceDame()) {
                        iv.setImage(YellowQueen);
                    }
                    iv.setTranslateX((height / 10 - iv.getImage().getWidth()) / 2);
                    iv.setId("iv" + (rectCounter - 1));
                    iv.setX(c.getX());
                    iv.setY(c.getY());

                    iv.setLayoutX(r.getLayoutX() + 50.8);
                    iv.setLayoutY(r.getLayoutY() + 50.8);

                    iv.setOnMouseMoved((MouseEvent event) -> {
                        highLightEvent(c, r);
                    });
                    iv.setOnMouseExited((MouseEvent event) -> {
                        deHighLightEvent(c, r);
                    });
                    pieces.add(iv);
                }
                pane1.add(r, c.getY(), c.getX());
            }
            count = true;
        }
        for (ImageView imageV : pieces) {
            pane1.add(imageV, (int) imageV.getY(), (int) imageV.getX());
        }
        boardLength = cellLength;
        pane2.getChildren().addAll(howtoplay, gameInfo, redplayer, yellowplayer);
        pane2.setAlignment(Pos.CENTER);
        pane2.setPadding(new Insets(0, 15, 15, 15));
        pane2.setTranslateX(boardLength);
        redScoreHbox.getChildren().add(redplayer);
        yellowScoreHbox.getChildren().add(yellowplayer);
        yellowScoreHbox.setAlignment(Pos.CENTER_LEFT);
        redScoreHbox.setAlignment(Pos.CENTER_LEFT);
        pane2.getChildren().add(redScoreHbox);
        pane2.getChildren().add(yellowScoreHbox);
        gameInfo.setMaxHeight(height / 2);
        gameInfo.setMaxWidth(height - 100);
        gameInfo.setTranslateX(-10);
        gameInfo.setEditable(false);
        yellowScoreHbox.setTranslateX(10);
        yellowScoreHbox.setTranslateY(10);
        redScoreHbox.setTranslateY(20);
        redScoreHbox.setTranslateX(10);

    }

    public void gameOverAlert(boolean winner) {
        Alert goa;
        if (!winner) {
            goa = new Alert(Alert.AlertType.INFORMATION, "Red player won!\nStart new game or quit using menubar", ButtonType.OK);
            goa.showAndWait();
        } else if (winner) {
            goa = new Alert(Alert.AlertType.INFORMATION, "Yellow player won!\nStart new game or quit using menubar", ButtonType.OK);
            goa.showAndWait();
        }
    }

    private ImageView getImageView(int row, int col) {
        ImageView theView = null;
        for (ImageView iv : pieces) {
            if ((int) iv.getX() == row && (int) iv.getY() == col) {
                theView = iv;
                break;
            }
        }
        return theView;
    }

    private void highLightEvent(Cell c, Rectangle r) {
        if (c.cellHasPiece() && c.isCellBlack()
                && !c.getPiece().isPieceWhite() && game.getPlayer()) {
            highlightCell(Color.YELLOW, r, true);
            highLightMoves(c, NewgameView.this.game.calculateMoves(c), true);
        } else if (c.cellHasPiece() && c.isCellBlack()
                && c.getPiece().isPieceWhite() && !game.getPlayer()) {
            highlightCell(Color.YELLOW, r, true);
            highLightMoves(c, NewgameView.this.game.calculateMoves(c), true);

        }
    }

    private void deHighLightEvent(Cell c, Rectangle r) {
        if (c.cellHasPiece() && c.isCellBlack()) {
            highlightCell(Color.BLACK,
                    r, false);
            highLightMoves(c,
                    NewgameView.this.game.calculateMoves(c), false);
        }
    }

    private void highlightCell(Color co, Rectangle r,
            boolean highLight) {
        if (highLight) {
            r.setFill(co);
            r.setStroke(Color.BLUE);
            r.setOpacity(0.6);
        } else {
            r.setFill(co);
            r.setStroke(Color.BLACK);
            r.setOpacity(1);
        }
    }

    private ArrayList<Cell> highLightMoves(Cell theTarget, ArrayList<Cell> theMoves,
            boolean highLight) {
        ArrayList<Cell> moves = theMoves;
        if (moves.isEmpty() && highLight) {
            highlightCell(Color.RED,
                    (Rectangle) getNodeFromGrid(theTarget.getX(),
                            theTarget.getY()), true);
        } else if (moves.isEmpty() && !highLight) {
            highlightCell(Color.BLACK,
                    (Rectangle) getNodeFromGrid(theTarget.getX(),
                            theTarget.getY()), false);
        } else {
            for (Cell c : moves) {
                if (highLight) {
                    highlightCell(Color.GREEN,
                            (Rectangle) getNodeFromGrid(c.getX(), c.getY()), true);
                } else {
                    highlightCell(Color.BLACK,
                            (Rectangle) getNodeFromGrid(c.getX(), c.getY()), false);
                }
            }
        }
        return moves;
    }

    private Node getNodeFromGrid(final int row, final int col) {
        Node result = null;
        ObservableList<Node> children = pane1.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) == col) {
                result = node;
                break;
            }
        }
        return result;
    }
}

class imageV extends ImageView {

    private boolean target;

    public imageV(Image im) {
        this.setImage(im);
        target = false;
    }

    public void setTarget(boolean b) {
        target = b;
    }

    public boolean getTargeted() {
        return target;
    }
}
