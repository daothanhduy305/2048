package ebolo.gui;

import ebolo.controller.PuzzleBoardController;
import ebolo.data.Settings;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by Ebolo on 15-Jan-16.
 * Main Board holder
 */
public class AppBoard extends GridPane {
    public final PuzzleBoardController boardController;
    private final Group boardGroup;
    private final ActionMenu actionMenu;

    public AppBoard() {
        Dimension2D puzzleBoardSize = (new Settings()).getBoardSize();
        this.setBackground(new Background(new BackgroundFill(Settings.background_color, null, null)));
        Pane puzzleBoardHolder = new Pane();
        puzzleBoardHolder.setPrefSize(Settings.getScreenHeight(), Settings.getScreenHeight());
        PuzzleBoard puzzleBoard;
        puzzleBoardHolder.getChildren().add(puzzleBoard = new PuzzleBoard(this));
        boardController = new PuzzleBoardController(puzzleBoard);
        double paddingValue = (Settings.getScreenHeight() - puzzleBoardSize.getHeight()) / 2f;
        puzzleBoard.setLayoutX(paddingValue);
        puzzleBoard.setLayoutY(paddingValue);
        boardGroup = new Group(puzzleBoardHolder);
        this.add(boardGroup, 0, 0, 1, 1);
        this.add(actionMenu = new ActionMenu(this), 1, 0, 1, 1);

    }

    public Group getBoardGroup() {
        return boardGroup;
    }

    public ActionMenu getActionMenu() {
        return actionMenu;
    }
}
