package ebolo.data;

import com.sun.istack.internal.Nullable;
import ebolo.gui.PuzzlePiece;
import javafx.geometry.Point2D;

/**
 * Created by Ebolo on 07-Jan-16.
 * Data that works like a tag for animation preparation
 */
public class TransitionData {
    public final PuzzlePiece fromPuzzlePiece;
    public final Point2D origin;
    public PuzzlePiece toPuzzlePiece;
    public int toIndex;
    public int lastValue;
    public int newValue;
    public Point2D destination;

    public TransitionData(PuzzlePiece fromPiece) {
        this.fromPuzzlePiece = fromPiece;
        int fromIndex = fromPuzzlePiece.getIndex();
        lastValue = 0;
        newValue = 0;
        origin = (new Settings()).getCoordOfPiece(fromIndex);
        reCalculate();
    }

    public void reCalculate() {
        destination = (new Settings()).getCoordOfPiece(toIndex);
    }

    public void setDestination(@Nullable PuzzlePiece destination) {
        this.toPuzzlePiece = (destination == null) ? null : destination;
        this.toIndex = (destination == null) ? -1 : toPuzzlePiece.getIndex();
    }
}
