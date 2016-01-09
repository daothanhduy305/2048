package ebolo.parts;

import javafx.geometry.Point2D;

/**
 * Created by Ebolo on 07-Jan-16.
 * Data that works like a tag for animation preparation
 */
public class TransitionData {
    public PuzzlePiece mPuzzlePiece;
    public int fromIndex, toIndex, lastValue, newValue;
    public double pieceSize;
    public Point2D origin, destination;

    public TransitionData(PuzzlePiece puzzlePiece, int to) {
        mPuzzlePiece = puzzlePiece;
        fromIndex = puzzlePiece.getIndex();
        toIndex = to;
        lastValue = 0;
        newValue = 0;
        pieceSize = puzzlePiece.getPieceSize();
        origin = new Point2D(
                (fromIndex % 4) * (pieceSize + 10f),
                (fromIndex / 4) * (pieceSize + 10f)
        );
        reCalculate();
    }

    public void reCalculate() {
        destination = new Point2D(
                (toIndex % 4) * (pieceSize + 10f),
                (toIndex / 4) * (pieceSize + 10f)
        );
    }
}
