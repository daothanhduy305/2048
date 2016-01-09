package ebolo;

import ebolo.handlers.BoardControlPanel;
import ebolo.handlers.BoardKeysHandler;
import ebolo.parts.PuzzleBoardGroup;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

/**
 * Created by Ebolo on 02-Jan-16.
 * This is the main class of the Application
 */

public class Main extends Application {
    private static Dimension2D boardSize;
    private static BoardControlPanel boardControlPanel;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("2048 - Puzzle Game");
        boardSize = new Dimension2D(600, 600);
        PuzzleBoardGroup puzzleBoardGroup = new PuzzleBoardGroup(boardSize);
        Scene demo = new Scene(puzzleBoardGroup, boardSize.getWidth(), boardSize.getHeight(), false, SceneAntialiasing.BALANCED);
        demo.setOnKeyPressed(new BoardKeysHandler(boardControlPanel = new BoardControlPanel(puzzleBoardGroup.getPuzzleBoard())));
        primaryStage.setScene(demo);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static BoardControlPanel getBoardControlPanel() {
        return boardControlPanel;
    }
}
