package ebolo.gui;

import ebolo.data.Settings;
import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Created by Ebolo on 12-Jan-16.
 * Score box element
 */
class ScoreBox extends Rectangle {
    public final Group boxGroup;
    private final Label scoreLabel;
    private final Label boxLabel;
    private final Dimension2D boxSize;
    private final double boxLabelSize = 40f;
    private long scoreValue;

    public ScoreBox(Dimension2D size) {
        this.boxSize = size;
        //Initialization labels
        this.scoreValue = 0;
        this.scoreLabel = new Label("" + scoreValue);
        this.scoreLabel.setFont(new Font("Segoe UI Black", 45f));
        this.scoreLabel.setTextFill(Color.WHITE);
        this.boxLabel = new Label("SCORE");
        this.boxLabel.setFont(new Font("Segoe UI Black", boxLabelSize));
        this.boxLabel.setTextFill(Settings.background_color);

        this.setSmooth(true);
        this.setHeight(size.getHeight());
        this.setWidth(size.getWidth());
        this.setArcHeight(15f);
        this.setArcWidth(15f);
        this.setFill(Settings.board_color);

        //Add components
        this.boxGroup = new Group(this, boxLabel, scoreLabel);

        //Post processing
        centralize();
    }

    private void centralize() {
        this.boxLabel.setLayoutX((this.boxSize.getWidth() - Settings.getFontMetrics(this.boxLabelSize).
                computeStringWidth(this.boxLabel.getText())) / 2f);
        this.boxLabel.setLayoutY(this.boxSize.getHeight() / 10f);
        this.scoreLabel.setLayoutX((this.boxSize.getWidth() - Settings.getFontMetrics(45).
                computeStringWidth(this.scoreLabel.getText())) / 2f);
        this.scoreLabel.setLayoutY(this.boxSize.getHeight() / 10 + Settings.getFontMetrics(this.boxLabelSize).getLineHeight() +
                (this.boxSize.getHeight() - (this.boxSize.getHeight() / 10 + Settings.getFontMetrics(this.boxLabelSize).
                        getLineHeight())) / 20f);
        this.setEffect(Settings.globalShadow);
    }

    public void updateScore(long score) {
        this.scoreLabel.setText("" + (this.scoreValue += score));
        centralize();
    }

    public long getScoreValue() {
        return this.scoreValue;
    }

    public void resetScore() {
        updateScore(this.scoreValue = 0);
    }
}
