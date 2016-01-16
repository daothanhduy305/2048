package ebolo.gui;

import ebolo.data.Settings;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Ebolo on 11-Jan-16.
 * Action menu
 */
public class ActionMenu extends VBox {
    private final ScoreBox scoreBox;
    private final ScoreBox bestBox;
    private final AppBoard mainBoard;

    public ActionMenu(AppBoard appBoard) {
        //Preparation
        mainBoard = appBoard;
        Dimension2D menuSize = (new Settings()).getMenuSize();
        this.setPrefSize(menuSize.getWidth(), menuSize.getHeight());
        this.setBackground(new Background(new BackgroundFill(Settings.background_color, null, null)));

        //The below block of code will be used for further effects in game - Under constructing
        /*Pane boardWhiteCover = new Pane();
        boardWhiteCover.setPrefSize(boardSize.getWidth() + 20f,
                boardSize.getHeight() + 20f);
        boardWhiteCover.setBackground(new Background(new BackgroundFill(Color.rgb(253, 254, 230, 0.8f), null, null)));*/

        //Logo Creator
        ImageView logo = new ImageView(Settings.imagesFolder + "logo.png");
        logo.setFitWidth(menuSize.getWidth());
        logo.setSmooth(true);
        logo.setPreserveRatio(true);

        //Actions Area
        GridPane actionBoxes = new GridPane();
        actionBoxes.setBackground(new Background(new BackgroundFill(Settings.background_color, null, null)));
        actionBoxes.setPadding(new Insets(0, 30f, 0f, 30f));
        actionBoxes.setVgap(35f);
        double scoreBoxScaleRatio = 1.25;
        actionBoxes.add((scoreBox = new ScoreBox(
                new Dimension2D(
                        menuSize.getWidth() / (2f * scoreBoxScaleRatio),
                        (menuSize.getHeight() - logo.getBoundsInLocal().getHeight()) / (2f * scoreBoxScaleRatio))
        )).boxGroup, 0, 0, 1, 1);
        actionBoxes.add((bestBox = new ScoreBox(
                new Dimension2D(
                        menuSize.getWidth() / (2f * 1.25),
                        (menuSize.getHeight() - logo.getBoundsInLocal().getHeight()) / (2f * scoreBoxScaleRatio))
        )).boxGroup, 0, 1, 1, 1);

        //Create buttons
        GridPane buttonsBox = new GridPane();
        ActionButton undoBut = new ActionButton("undoBut");
        double buttonsScaleRatio = 1.55;
        undoBut.setFitWidth(menuSize.getWidth() / (2f * buttonsScaleRatio));
        undoBut.setOnMouseClicked(event -> {
            int savedMoveSets;
            if ((savedMoveSets = mainBoard.boardController.getMoveBackup().size()) > 0) {
                for (int counter = 0; counter < 16; counter++) {
                    mainBoard.boardController.getPieceAtPos(counter).setValue(
                            mainBoard.boardController.getMoveBackup().get(savedMoveSets - 1)[counter], true);
                }
                scoreBox.updateScore(0 - mainBoard.boardController.getMoveBackup().get(savedMoveSets - 1)
                        [Settings.totalTileNumber]);
                mainBoard.boardController.getMoveBackup().remove(
                        savedMoveSets - 1
                );
            }
        });
        buttonsBox.add(undoBut, 0, 0, 1, 1);

        ActionButton restartBut = new ActionButton("restartBut");
        restartBut.setFitWidth(menuSize.getWidth() / (2f * buttonsScaleRatio));
        restartBut.setOnMouseClicked(event -> {
            scoreBox.resetScore();
            mainBoard.boardController.resetBoard();
        });
        buttonsBox.add(restartBut, 0, 1, 1, 1);

        ActionButton moreBut = new ActionButton("moreBut");
        moreBut.setFitWidth(menuSize.getWidth() / (2f * buttonsScaleRatio));
        buttonsBox.add(moreBut, 0, 2, 1, 1);

        ActionButton exitBut = new ActionButton("exitBut");
        exitBut.setFitWidth(menuSize.getWidth() / (2f * buttonsScaleRatio));
        exitBut.setOnMouseClicked(event -> {
            /*curtainCall();
            Label textLabel = new Label("Leave game?");
            textLabel.setEffect(new DropShadow(5f, 0, 0, Color.DARKGRAY));*/

            //Save data before Exiting
            try {
                File saveFolder = new File(Settings.saveFolder);
                if (!saveFolder.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    saveFolder.mkdir();
                }
                ObjectOutputStream saveFile = new ObjectOutputStream(new FileOutputStream(Settings.savePath));
                saveFile.writeObject(mainBoard.boardController.getCurrentGame());
                saveFile.writeObject(mainBoard.boardController.getMoveBackup());
                saveFile.writeObject(scoreBox.getScoreValue());
                saveFile.writeObject(bestBox.getScoreValue());
                saveFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        buttonsBox.add(exitBut, 0, 3, 1, 1);

        buttonsBox.setPadding(new Insets(undoBut.getBoundsInLocal().getHeight() / 6f, 0, 0,
                undoBut.getBoundsInLocal().getHeight() / 1.75f));
        buttonsBox.setVgap(15f);
        actionBoxes.add(buttonsBox, 1, 0, 1, 2);

        //Finalization
        this.getChildren().add(logo);
        this.getChildren().add(actionBoxes);
    }

    public void updateScore(long score) {
        this.scoreBox.updateScore(score);
        if (scoreBox.getScoreValue() > bestBox.getScoreValue()) {
            bestBox.updateScore(score);
        }
    }

    public void loadScores(long score, long best) {
        this.scoreBox.updateScore(score);
        this.bestBox.updateScore(best);
    }

    /*private void curtainCall() {
        if (!mBoardGroup.mPane.getChildren().contains(boardWhiteCover) ) {
            mBoardGroup.mPane.getChildren().add(boardWhiteCover);
            boardWhiteCover.setLayoutX(-15f); boardWhiteCover.setLayoutY(-15f);
            FadeTransition coverAnim = new FadeTransition(Duration.millis(200), boardWhiteCover);
            coverAnim.setFromValue(0f);
            coverAnim.setToValue(1f);
            coverAnim.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    undoBut.disable();
                    restartBut.disable();
                    moreBut.disable();
                    exitBut.disable();
                    mMain.getMainScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            double inset = (MonitorScreen.getHeight() - boardSize.getHeight()) / 2;
                            System.out.println("" + boardWhiteCover.getBoundsInLocal().getMinY());
                            if (!((inset + boardWhiteCover.getBoundsInLocal().getMinX() < event.getX() &&
                                    event.getX() < inset + boardWhiteCover.getBoundsInLocal().getMaxX()) &&
                                    (inset + boardWhiteCover.getBoundsInLocal().getMinY() < event.getY() &&
                                        event.getY() < inset + boardWhiteCover.getBoundsInLocal().getMaxY()))) {
                                if (mBoardGroup.mPane.getChildren().contains(boardWhiteCover)) {
                                    FadeTransition coverAnim = new FadeTransition(Duration.millis(200), boardWhiteCover);
                                    coverAnim.setFromValue(1f);
                                    coverAnim.setToValue(0f);
                                    coverAnim.setOnFinished(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            mBoardGroup.mPane.getChildren().remove(boardWhiteCover);
                                            mMain.getMainScene().setOnMouseClicked(null);
                                            undoBut.enable();
                                            restartBut.enable();
                                            moreBut.enable();
                                            exitBut.enable();
                                        }
                                    });
                                    coverAnim.play();
                                }
                            }
                        }
                    });
                }
            });
            coverAnim.play();
        }
    }*///This method will be used for further effect in game - Under constructing
}
