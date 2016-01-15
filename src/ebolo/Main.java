package ebolo;

import ebolo.controller.BoardKeysHandler;
import ebolo.controller.PuzzleBoardController;
import ebolo.data.Settings;
import ebolo.gui.AppBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Created by Ebolo on 02-Jan-16.
 * This is the main class of the Application
 */

public class Main extends Application {
    private PuzzleBoardController boardController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("2048 - Puzzle Game");
        AppBoard mainAppBoard;
        Scene mainScene = new Scene(mainAppBoard = new AppBoard(), Settings.getScreenWidth(), Settings.getScreenHeight(),
                false, SceneAntialiasing.BALANCED);
        mainScene.setOnKeyPressed(
                new BoardKeysHandler(mainAppBoard.boardController)
        );
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();
    }
}
