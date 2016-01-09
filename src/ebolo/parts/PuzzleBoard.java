package ebolo.parts;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Created by Ebolo on 02-Jan-16.
 * This is the class of the Board interface
 */
public class PuzzleBoard extends GridPane {
    private Dimension2D mDimension;
    private PuzzleBoardGroup mPuzzleBoardGroup;

    public PuzzleBoard(Dimension2D dimension, PuzzleBoardGroup puzzleBoardGroup) {
        this.mPuzzleBoardGroup = puzzleBoardGroup;
        this.mDimension = dimension;
        this.setHgap(10); this.setVgap(10);
        this.setPadding(new Insets(10));
        this.setBackground(new Background(new BackgroundFill(Color.rgb(186, 172, 160), null, null)));
    }

    public Dimension2D getmDimension() {
        return mDimension;
    }

    public static boolean isVertical(KeyCode direction) {
        return (direction == KeyCode.DOWN || direction == KeyCode.UP);
    }

    public PuzzleBoardGroup getmPuzzleBoardGroup() {
        return mPuzzleBoardGroup;
    }
}
