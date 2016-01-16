package ebolo.data;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 * Created by Ebolo on 14-Jan-16.
 * This is the class where all the setting values be determined.
 * They can be used for further in-game setting option
 */
public class Settings {
    //Size properties
    public static final int tileNumber = 4;
    public static final int totalTileNumber = tileNumber * tileNumber;
    //Colors
    public static final Color board_color = Color.rgb(186, 172, 160);
    public static final Color background_color = Color.rgb(253, 254, 230);
    public static final Color[] tiles_color = {
            Color.rgb(204, 192, 178),   //Empty tile
            Color.rgb(255, 255, 255),   //Tile 2
            Color.rgb(254, 255, 217),   //Tile 4
            Color.rgb(240, 176, 120),   //Tile 8
            Color.rgb(245, 149, 101),   //Tile 16
            Color.rgb(245, 124, 95),    //Tile 32
            Color.rgb(246, 93, 59),     //Tile 64
            Color.rgb(255, 211, 117),   //Tile 128
            Color.rgb(255, 207, 44),    //Tile 256
            Color.rgb(255, 181, 10),    //Tile 512
            Color.rgb(0, 185, 255),     //Tile 1024
            Color.rgb(0, 149, 205),     //Tile 2048
            Color.rgb(0, 89, 123),      //Tile 4096
            Color.rgb(0, 255, 144),     //Tile 8192
            Color.rgb(0, 212, 120),     //Tile 16384
            Color.rgb(0, 157, 88),      //Tile 32768
            Color.rgb(204, 0, 255),     //Tile 65536
            Color.rgb(165, 0, 206)      //Tile 131072
    };
    public static final Color small_text_color = Color.rgb(54, 54, 54);
    public static final Color large_text_color = Color.rgb(255, 255, 255);
    public static final DropShadow globalShadow = new DropShadow(4f, 1.5f, 1.5f, Color.DARKGRAY);

    //Others
    public static final String saveFolder = "saves";
    public static final String savePath = saveFolder + "/lastGame.sav";
    public static final String imagesFolder = "file:images/";
    private static final Rectangle2D monitorShape = Screen.getPrimary().getBounds();
    private final Dimension2D boardSize;
    private final Dimension2D pieceSize;
    private final Dimension2D menuSize;
    private final double paddingValue;

    public Settings() {
        double percentOfTile = 100f - (tileNumber + 1) * 2f;
        this.boardSize = new Dimension2D(getScreenHeight() / 1.1, getScreenHeight() / 1.1);
        this.pieceSize = new Dimension2D(((this.boardSize.getWidth() * percentOfTile) / 100f) / tileNumber,
                ((this.boardSize.getWidth() * percentOfTile) / 100f) / tileNumber);
        this.menuSize = new Dimension2D((getScreenWidth() - getScreenHeight()), getScreenHeight());
        this.paddingValue = (boardSize.getHeight() - this.pieceSize.getHeight() * (double) tileNumber) /
                (double) (tileNumber + 1);
    }

    public static double getScreenHeight() {
        return monitorShape.getHeight();
    }

    public static double getScreenWidth() {
        return monitorShape.getWidth();
    }

    public static FontMetrics getFontMetrics(double fontSize) {
        return (Toolkit.getToolkit().getFontLoader().getFontMetrics(new Font("Segoe UI Black", fontSize)));
    }

    public static boolean isVertical(KeyCode direction) {
        return (direction == KeyCode.DOWN || direction == KeyCode.UP);
    }

    public Dimension2D getBoardSize() {
        return this.boardSize;
    }

    public Dimension2D getPieceSize() {
        return this.pieceSize;
    }

    public Dimension2D getMenuSize() {
        return this.menuSize;
    }

    public double getPaddingValue() {
        return this.paddingValue;
    }

    public Point2D getCoordOfPiece(int index) {
        return (new Point2D(
                ((getScreenHeight() - this.boardSize.getHeight()) / 2f) + (index % tileNumber + 1) * this.paddingValue +
                        (index % tileNumber) * this.pieceSize.getWidth(),
                ((getScreenHeight() - this.boardSize.getHeight()) / 2f) + (index / tileNumber + 1) * this.paddingValue +
                        (index / tileNumber) * this.pieceSize.getWidth()
        ));
    }
}
