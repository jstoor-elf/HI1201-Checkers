package GUI;

import Model.theGame;
import Model.Cell;
import Model.Piece;
import Model.Board;
import Exceptions.LoadException;
import Controller.Gamecontroller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

/**
 *
 * @author joakimstoor
 */
public class GameGUI extends Application {

    private Stage window;
    private Scene startScene;
    private Button newGameButton, howToPlayButton, loadOldGameButton, historyButton, vsPlayer, vsComputer, back1, back2, back3, back4;
    private int width, height;
    private Image background;

    private MenuBar menubar1, menubar2;
    private Menu draughtMenu1, draughtMenu2, fileMenu, viewMenu, gameMenu, newGameItem1, newGameItem2;
    private MenuItem exitItem, player2Item1, compPlayerItem1, loadGame1, saveGameItem, quitGameItem,
            historyItem, player2Item2, compPlayerItem2, loadGame2;

    private FileChooser filechooser;

    private theGame game;

    private Gamecontroller gc;

    private NewgameView nw;
    private BorderPane startPane, windowPane, histPane;

    private TextArea history;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        File initDir = new File(System.getProperty("user.home"), "/Desktop/Dam/SavedGames");
        if (!initDir.exists()) {
            initDir.mkdirs();
        }

        newGameButton = new Button("New Game");
        loadOldGameButton = new Button("Load Game");
        howToPlayButton = new Button("How To Play");
        historyButton = new Button("History");
        vsPlayer = new Button("2-Player");
        vsComputer = new Button("Vs. Computer");
        back1 = new Button("Back");
        back2 = new Button("Back");
        back3 = new Button("Back");
        back4 = new Button("Back");
        filechooser = new FileChooser();
        filechooser.setInitialDirectory(initDir);
        filechooser.setTitle("Select Saved Game");
        history = new TextArea();
        history.setPrefWidth(300);

        // Text for game rules
        Text gameRules = new Text("Spelarna turas om att göra drag, "
                + "den som har svarta brickor börjar.\n"
                + "\n"
                + "Brickorna flyttas diagonalt över de svarta "
                + "rutorna på brädet.\n"
                + "\n"
                + "En bricka kan gå antingen ett steg (till tom ruta) eller "
                + "två steg till en tom ruta bortom en ruta där en "
                + "motståndarbricka står.\n"
                + "En bricka som hoppats över tas ur spel\n"
                + "\n"
                + "Når en bricka når andra sidan av brädet förvandlas den till "
                + "en Dam. Damen kan röra sig på samma sätt som en "
                + "löpare i schack.\n"
                + "\n"
                + "En Dam kan endast hoppa över motståndarens brickor om de "
                + "ligger intill Damen när draget görs");
        //Events

        // Creating menu for startpage
        // Loading BackgroundImage and creting ImageView
        background = new Image(this.getClass().getResource("/resources/background.jpg").toString());
        ImageView backgroundView = getBackground(background);

        // Setting width and Heigth for table
        width = (int) (background.getWidth());
        height = (int) (background.getHeight());

        // Initializing game
        initializeMenus();

        // Layout primary Stage
        windowPane = new BorderPane();
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.BASELINE_LEFT);
        vbox.setPadding(new Insets(15, 15, 15, 15));
        vbox.getChildren().addAll(newGameButton, loadOldGameButton, howToPlayButton, historyButton);
        windowPane.getChildren().addAll(backgroundView);
        windowPane.setLeft(vbox);

        //----------//
        startPane = new BorderPane();
        startPane.setTop(menubar1);
        startPane.setCenter(windowPane);
        startScene = new Scene(startPane, background.getWidth(), background.getHeight());

        // NewGame Stage layout
        BorderPane newGpane = new BorderPane();
        VBox newGbox = new VBox(15);
        newGbox.setPadding(new Insets(15, 15, 15, 15));
        newGbox.getChildren().addAll(vsComputer, vsPlayer, back1);
        newGpane.getChildren().add(getBackground(background));
        newGpane.setLeft(newGbox);

        // How to play layout
        BorderPane howBoarderpane = new BorderPane();
        VBox howBox = new VBox(15);
        howBox.setAlignment(Pos.CENTER);
        howBox.getChildren().addAll(gameRules, back2);
        howBoarderpane.setCenter(howBox);

        //Layout history
        histPane = new BorderPane();
        VBox histBox = new VBox(15);
        histBox.setAlignment(Pos.CENTER);
        histBox.getChildren().addAll(history, back4);
        histPane.setCenter(histBox);

        loadOldGameButton.setOnAction(new LoadGameGandler());
        loadGame1.setOnAction(new LoadGameGandler());
        loadGame2.setOnAction(new LoadGameGandler());
        back1.setOnAction(new BackHandler());
        back2.setOnAction(new BackHandler());
        back3.setOnAction(new BackHandler());
        back4.setOnAction(new BackHandler());
        compPlayerItem1.setOnAction(new newComputerHandler());
        compPlayerItem2.setOnAction(new newComputerHandler());
        vsComputer.setOnAction(new newComputerHandler());
        vsPlayer.setOnAction(new new2PlayerHandler());
        player2Item1.setOnAction(new new2PlayerHandler());
        player2Item2.setOnAction(new new2PlayerHandler());
        historyItem.setOnAction(new HistoryHandler());
        historyButton.setOnAction(new HistoryHandler());
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startPane.setCenter(newGpane);
            }
        });
        howToPlayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startPane.setCenter(howBoarderpane);

            }
        });
        quitGameItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert a = getAlert();
                a.showAndWait();
                if (a.getResult() == ButtonType.NO) {

                } else if (a.getResult() == ButtonType.YES) {
                    startPane.setCenter(newGpane);
                    startPane.setTop(menubar1);
                }
            }
        });
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, 
                        "Are you sure you want to exit?\nAny unsaved games will be discarded");
                closeConfirmation.setHeaderText("");
                closeConfirmation.setTitle("Exit");
                Button closeButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                Optional<ButtonType> closeresp = closeConfirmation.showAndWait();
                if(ButtonType.OK.equals(closeresp.get())){
                    window.close();
                }
            }
        });

        // handle stage parameters
        window.setTitle("Lady");
        window.sizeToScene();
        window.setResizable(false);
        window.setScene(startScene);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?\nAny unsaved games will be discarded");
                closeConfirmation.setHeaderText("");
                closeConfirmation.setTitle("Exit");
                Button closeButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                Optional<ButtonType> closeresp = closeConfirmation.showAndWait();
                if(ButtonType.OK.equals(closeresp.get())){
                    event.consume();
                    window.close();
                }
            }

        });
        window.show();
    }

    private ImageView getBackground(Image background) {
        ImageView backgroundImageView = new ImageView();
        backgroundImageView.setImage(background);
        return backgroundImageView;
    }

    private theGame handleLoadEvent(File theFile) throws LoadException, IOException {
        BufferedReader br = null;
        String filePath = theFile.getPath();
        Board b = new Board(10, 10);
        theGame loadGame = new theGame(b);
        int hasPiece, yellowPiece, pieceDame, xCord, yCord, playerTurn, AI;

        br = new BufferedReader(new FileReader(filePath));
        String line = br.readLine();
        while (line != null) {
            String[] tokens = line.split(",");
            for (String s : tokens) {
                if (s.equals("") || s.chars().allMatch(Character::isLetter)) {
                    throw new LoadException();
                }
            }
            hasPiece = intFromToken(tokens[0]);
            yellowPiece = intFromToken(tokens[1]);
            pieceDame = intFromToken(tokens[2]);
            xCord = intFromToken(tokens[3]);
            yCord = intFromToken(tokens[4]);

            Cell temp = b.getCell(yCord, xCord);
            if (hasPiece == 1) {
                if (yellowPiece == 1) {
                    temp.addPiece(new Piece(true));
                } else {
                    temp.addPiece(new Piece(false));
                }
            }
            if (pieceDame == 1 && temp.cellHasPiece()) {
                temp.getPiece().setPieceDame(true);
            }
            if (tokens.length > 5) {
                playerTurn = intFromToken(tokens[5]);
                if (playerTurn == 1) {
                    loadGame.setPlayer(false);
                } else {
                    loadGame.setPlayer(true);
                }
                loadGame.setRedScore(intFromToken(tokens[6]));
                loadGame.setYellowScore(intFromToken(tokens[7]));
                AI = intFromToken(tokens[8]);
                if (AI == 1) {
                    loadGame.setPlayer(true);
                    loadGame.setAI(true);
                } else if (AI == 0) {
                    loadGame.setAI(false);
                }
            }
            line = br.readLine();
        }
        br.close();
        return loadGame;
    }

    private int intFromToken(String s) {
        return Integer.valueOf(s);
    }

    private void parseHistory() throws LoadException, IOException {
        File parseDir = new File(System.getProperty("user.home"), "/Desktop/Dam/History");
        if (parseDir.listFiles() == null) {
            throw new LoadException();
        }
        BufferedReader br = null;
        for (File f : parseDir.listFiles()) {
            br = new BufferedReader(new FileReader(f.getAbsolutePath()));
            String line = br.readLine();
            while (line != null) {
                history.appendText(line + "\n");
                line = br.readLine();
            }
        }
    }

    private void initializeMenus() {
        menubar1 = new MenuBar();
        menubar2 = new MenuBar();
        draughtMenu1 = new Menu("Draught");
        draughtMenu2 = new Menu("Draught");
        exitItem = new MenuItem("Exit");
        saveGameItem = new MenuItem("Save Game");
        quitGameItem = new MenuItem("Quit Game");
        loadGame1 = new MenuItem("Load Game");
        loadGame2 = new MenuItem("Load Game");
        fileMenu = new Menu("File");
        gameMenu = new Menu("Game");
        newGameItem1 = new Menu("New Game");
        newGameItem2 = new Menu("New Game");
        player2Item1 = new MenuItem("2-Player");
        player2Item2 = new MenuItem("2-Player");
        compPlayerItem1 = new MenuItem("Vs. Computer");
        compPlayerItem2 = new MenuItem("Vs. Computer");
        newGameItem1.getItems().addAll(player2Item1, compPlayerItem1);
        newGameItem2.getItems().addAll(player2Item2, compPlayerItem2);
        viewMenu = new Menu("View");
        historyItem = new MenuItem("History");
        fileMenu.getItems().addAll(newGameItem1, loadGame1);
        gameMenu.getItems().addAll(newGameItem2, loadGame2);
        draughtMenu1.getItems().add(exitItem);
        draughtMenu2.getItems().addAll(quitGameItem, saveGameItem);
        viewMenu.getItems().add(historyItem);
        menubar1.getMenus().addAll(draughtMenu1, fileMenu, viewMenu);
        menubar2.getMenus().addAll(draughtMenu2, gameMenu);
    }

    private Alert getAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to quit this game instance?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.setTitle("Alert");
        return alert;
    }

    private class new2PlayerHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            boolean newPlay = true;
            if (startPane.getCenter().equals(nw)) {
                Alert alert = getAlert();
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    newPlay = false;
                } else if (alert.getResult() == ButtonType.YES) {
                    newPlay = true;
                }
            }
            if (newPlay) {
                game = new theGame(10, 10);
                nw = new NewgameView(game, width, height);
                gc = new Gamecontroller(game, nw);
                nw.addEventHandlers(gc);
                startPane.setCenter(nw);
                startPane.setTop(menubar2);
            }
        }
    }

    private class newComputerHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            boolean newPlay = true;
            if (startPane.getCenter().equals(nw)) {
                Alert alert = getAlert();
                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    newPlay = false;
                } else if (alert.getResult() == ButtonType.YES) {
                    newPlay = true;
                }
            }
            if (newPlay) {
                game = new theGame(10, 10);
                game.setAI(true);
                nw = new NewgameView(game, width, height);
                gc = new Gamecontroller(game, nw);
                nw.addEventHandlers(gc);
                startPane.setCenter(nw);
                startPane.setTop(menubar2);
            }
        }
    }

    private class BackHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            startPane.setCenter(windowPane);
        }
    }

    private class LoadGameGandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            File openedFile = filechooser.showOpenDialog(window);
            if (openedFile == null) {
                return;
            }
            try {
                game = handleLoadEvent(openedFile);
                NewgameView nw = new NewgameView(game, width, height);
                for (int i = 0; i < game.getRedScore(); i++) {
                    nw.reDrawPlayerScore(true);
                }
                for (int j = 0; j < game.getYellowScore(); j++) {
                    nw.reDrawPlayerScore(false);
                }
                gc = new Gamecontroller(game, nw);
                nw.addEventHandlers(gc);
                startPane.setCenter(nw);
                startPane.setTop(menubar2);
            } catch (IOException e) {
            } catch (LoadException le) {

            }
        }
    }

    private class HistoryHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            try {
                parseHistory();
                startPane.setCenter(histPane);
            } catch (LoadException le) {

            } catch (IOException e) {

            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
