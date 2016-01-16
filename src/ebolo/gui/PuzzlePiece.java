package ebolo.gui;

import com.sun.istack.internal.Nullable;
import com.sun.javafx.tk.FontMetrics;
import ebolo.data.Settings;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * Created by Ebolo on 02-Jan-16.
 * Data type: Piece, unit of the Board
 */
public class PuzzlePiece extends Pane {
    private final Label valueLabel;
    private final Dimension2D size;
    private int value, index;
    private int stepUp, stepDown, stepRight, stepLeft;

    public PuzzlePiece() {
        this.size = (new Settings()).getPieceSize();
        this.valueLabel= new Label("");
        this.value = 0;
        this.setPrefSize(size.getWidth(), size.getHeight());
        this.setValue(value);
        this.getChildren().add(valueLabel);
    }

    public PuzzlePiece(int mIndex) {
        this();
        this.index = mIndex;
    }

    public int getValue() {
        return value;
    }

    public void setValue(@Nullable Integer value) {
        int fontSize = 60;
        if (value != null) {
            this.value = value;
        }
        if (this.value >= 131072) {
            fontSize = 35;
        } else if (this.value >= 16384) {
            fontSize = 40;
        } else if (this.value >= 1024) {
            fontSize = 50;
        }
        centralizedLabel(fontSize);
        paintPiece();
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
        return this.index;
    }

    public void setValue(int value, boolean refresh) {
        if (refresh) {
            setValue(value);
        } else {
            this.value = value;
        }
    }

    private void centralizedLabel(int fontSize) {
        String currentLabel = "";
        if (this.value > 0) {
            currentLabel += this.value;
            FontMetrics fontMetrics = Settings.getFontMetrics(fontSize);
            this.valueLabel.setFont(new Font("Segoe UI Black", fontSize));
            this.valueLabel.setLayoutX((size.getWidth() - fontMetrics.computeStringWidth(currentLabel)) / 2);
            this.valueLabel.setLayoutY((size.getHeight() - fontMetrics.getLineHeight()) / 2);
        }
        this.valueLabel.setText(currentLabel);
    }

    private void paintPiece() {
        this.valueLabel.setTextFill((value < 8) ? Settings.small_text_color : Settings.large_text_color);
        this.setBackground(new Background(new BackgroundFill(Settings.tiles_color[logaritOf2OfValue()],
                new CornerRadii(10f), null)));
    }

    private int logaritOf2OfValue() {
        if (this.value == 0) {
            return 0;
        } else {
            int pow = 2;
            int result = 1;
            while (pow != this.value) {
                pow *= 2;
                result++;
            }
            return result;
        }
    }

    public void setLayout(Point2D topLeft) {
        this.setLayoutX(topLeft.getX());
        this.setLayoutY(topLeft.getY());
    }
}
