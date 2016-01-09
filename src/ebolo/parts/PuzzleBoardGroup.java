package ebolo.parts;

import ebolo.handlers.BoardControlPanel;
import ebolo.handlers.BoardKeysHandler;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.util.Duration;

/**
 * Created by Ebolo on 07-Jan-16.
 * This is the Parent group of Puzzle Board that one can interact with to
 * add fake tiles for animations.
 */
public class PuzzleBoardGroup extends Group {
    private PuzzleBoard puzzleBoard;
    final PuzzleBoardGroup thisBoardGroup;

    public PuzzleBoardGroup(Dimension2D dimension2D) {
        puzzleBoard = new PuzzleBoard(dimension2D, this);
        thisBoardGroup = this;
        this.getChildren().add(puzzleBoard);
    }

    public PuzzleBoard getPuzzleBoard() {
        return puzzleBoard;
    }

    //Use to generate Pop Up Animation
    public ScaleTransition playAppearAnim(PuzzlePiece puzzlePiece, BoardControlPanel controlPanel) {
        PuzzlePiece cachePiece = new PuzzlePiece(puzzlePiece.getPieceSize()); //Create a copycat
        this.getChildren().add(cachePiece.getShape());
        //Calculate the coordinate for copycat to be exactly over the original one
        double layoutX = (double)(puzzlePiece.getIndex() % 4) * (puzzlePiece.getPieceSize() + 10f) + 10f;
        double layoutY = (double)(puzzlePiece.getIndex() / 4) * (puzzlePiece.getPieceSize() + 10f) + 10f;
        cachePiece.setValue(puzzlePiece.getValue(), new Point2D(layoutX, layoutY));
        //Lay the copycat over the real one
        cachePiece.setX(layoutX);
        cachePiece.setY(layoutY);
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), cachePiece.getShape());
        transition.setByX(0.15f); transition.setByY(0.15f);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Remove the fake after done
                thisBoardGroup.getChildren().remove(cachePiece.getShape());
                //The KeysHandler was detached to prevent anything happening during the animation
                //So now, it is attached again after the animation finished
                puzzleBoard.getScene().setOnKeyPressed(new BoardKeysHandler(controlPanel));
            }
        });
        return transition;
    }

    //Use to generate Sliding Animation
    public TranslateTransition createSlideAnim(TransitionData data) {
        //Create the copycat over the real one
        PuzzlePiece cachePiece = new PuzzlePiece(data.pieceSize);
        int valueBakup = data.mPuzzlePiece.getValue(); //Backup the original value for later restoring
        data.mPuzzlePiece.setValue(0, null); //Make the real one disappear
        //Lay the copycat over the real one
        cachePiece.setPixelPos(data.origin);
        cachePiece.setValue(data.newValue, null);
        this.getChildren().add(cachePiece.getShape());
        //Create the transition
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), cachePiece.getShape());
        transition.setByX(data.destination.getX() - data.origin.getX());
        transition.setByY(data.destination.getY() - data.origin.getY());
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Remove the fake after done
                data.mPuzzlePiece.setValue(valueBakup, null);
                thisBoardGroup.getChildren().remove(cachePiece.getShape());
            }
        });

        return transition;
    }
}
