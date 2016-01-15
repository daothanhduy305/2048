package ebolo.gui;

import ebolo.data.Settings;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;

/**
 * Created by Ebolo on 02-Jan-16.
 * This is the class of the Board interface
 */
public class PuzzleBoard extends GridPane {
    private final AppBoard mainBoard;

    public PuzzleBoard(AppBoard appBoard) {
        this.mainBoard = appBoard;
        Dimension2D size = (new Settings()).getBoardSize();
        double paddingValue = (new Settings()).getPaddingValue();
        this.setPrefSize(size.getWidth(), size.getHeight());
        this.setBackground(new Background(new BackgroundFill(Settings.board_color, new CornerRadii(10f), null)));
        this.setHgap(paddingValue);
        this.setVgap(paddingValue);
        this.setPadding(new Insets(paddingValue));
        this.setEffect(Settings.globalShadow);
    }

    public AppBoard getMainBoard() {
        return mainBoard;
    }
}
