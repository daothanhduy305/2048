package ebolo.parts;

import com.sun.istack.internal.Nullable;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Created by Ebolo on 02-Jan-16.
 * Data type: Piece, unit of the Board
 */
public class PuzzlePiece extends Rectangle {
    private int value, index;
    private int stepUp, stepDown, stepRight, stepLeft;
    private Label valueLabel;
    private Group shape;
    private final double pieceSize;

    public PuzzlePiece(double size) {
        this.pieceSize = size;
        this.valueLabel= new Label("");
        this.value = 0;
        this.setSmooth(true);
        this.setWidth(pieceSize); this.setHeight(pieceSize);
        this.setArcWidth(15); this.setArcHeight(15);
        shape = new Group(this, valueLabel);
        refresh();
    }

    public int getValue() {
        return value;
    }

    public int getStep(KeyCode direction) {
        switch (direction) {
            case UP:
                return this.stepUp;
            case DOWN:
                return this.stepDown;
            case RIGHT:
                return this.stepRight;
            case LEFT:
                return this.stepLeft;
        }
        return -1;
    }

    public void setStep(KeyCode direction, int value) {
        switch (direction) {
            case UP:
                this.stepUp = value;
                break;
            case DOWN:
                this.stepDown = value;
                break;
            case LEFT:
                this.stepLeft = value;
                break;
            case RIGHT:
                this.stepRight = value;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setValue(int value, @Nullable Point2D topLeft) {
        int fontSize = 60;
        this.value = value;
        if (value >= 131072) {
            fontSize = 35;
        } else if (value >= 16384) {
            fontSize = 40;
        } else if (value >= 1024) {
            fontSize = 50;
        }
        centralizedLabel(fontSize, ((topLeft == null)? null : topLeft));
        paintPiece();
    }

    public void setValue(int value, boolean refresh) {
        if (refresh) {
            setValue(value, null);
        } else {
            this.value = value;
        }
    }

    public void refresh() {
        setValue(this.value, null);
    }

    public Group getShape() {
        return shape;
    }

    private void centralizedLabel(int fontSize, @Nullable Point2D topLeft) {
        String currentLabel = "";
        if (value > 0) {
            currentLabel += value;
            FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(new Font("Segoe UI Black", fontSize));
            valueLabel.setFont(new Font("Segoe UI Black", fontSize));
            valueLabel.setLayoutX((pieceSize - fontMetrics.computeStringWidth(currentLabel)) / 2 + this.getLayoutX() +
                    ((topLeft != null)? topLeft.getX() : 0));
            valueLabel.setLayoutY((pieceSize - fontMetrics.getLineHeight()) / 2 + this.getLayoutY() +
                    ((topLeft != null)? topLeft.getY() : 0));
        } else {
            valueLabel.setLayoutY(0);
        }
        valueLabel.setText(currentLabel);
    }

    private void paintPiece() {
        Color pieceColor = Color.rgb(255, 255, 255);
        switch (value) {
            case 0:
                pieceColor = Color.rgb(204, 192, 178);
                break;
            case 4:
                pieceColor = Color.rgb(254, 255, 217);
                break;
            case 8:
                pieceColor = Color.rgb(240, 176, 120);
                break;
            case 16:
                pieceColor = Color.rgb(245, 149, 101);
                break;
            case 32:
                pieceColor = Color.rgb(245, 124, 95);
                break;
            case 64:
                pieceColor = Color.rgb(246, 93, 59);
                break;
            case 128:
                pieceColor = Color.rgb(255, 211, 117);
                break;
            case 256:
                pieceColor = Color.rgb(255, 207, 44);
                break;
            case 512:
                pieceColor = Color.rgb(255, 181, 10);
                break;
            case 1024:
                pieceColor = Color.rgb(0, 185, 255);
                break;
            case 2048:
                pieceColor = Color.rgb(0, 149, 205);
                break;
            case 4096:
                pieceColor = Color.rgb(0, 89, 123);
                break;
            case 8192:
                pieceColor = Color.rgb(0, 255, 144);
                break;
            case 16384:
                pieceColor = Color.rgb(0, 212, 120);
                break;
            case 32768:
                pieceColor = Color.rgb(0, 157, 88);
                break;
            case 65536:
                pieceColor = Color.rgb(204, 0, 255);
                break;
            case 131072:
                pieceColor = Color.rgb(165, 0, 206);
                break;
        }
        valueLabel.setTextFill((value < 8)? Color.rgb(54, 54, 54) : Color.rgb(255, 255, 255));
        this.setFill(pieceColor);
    }

    public double getPieceSize() {
        return pieceSize;
    }

    public void setPixelPos(Point2D topLeft) {
        this.setLayoutX(topLeft.getX() + 10f);
        this.setLayoutY(topLeft.getY() + 10f);
    }
}
