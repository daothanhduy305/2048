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
        boxSize = size;

        //Initialization labels
        scoreValue = 0;
        scoreLabel = new Label("" + scoreValue);
        scoreLabel.setFont(new Font("Segoe UI Black", 45f));
        scoreLabel.setTextFill(Color.WHITE);
        boxLabel = new Label("SCORE");
        boxLabel.setFont(new Font("Segoe UI Black", boxLabelSize));
        boxLabel.setTextFill(Settings.background_color);

        this.setSmooth(true);
        this.setHeight(size.getHeight());
        this.setWidth(size.getWidth());
        this.setArcHeight(15f);
        this.setArcWidth(15f);
        this.setFill(Settings.board_color);

        //Add components
        boxGroup = new Group(this, boxLabel, scoreLabel);

        //Post processing
        centralize();
    }

    private void centralize() {
        boxLabel.setLayoutX((boxSize.getWidth() - Settings.getFontMetrics(boxLabelSize).
                computeStringWidth(boxLabel.getText())) / 2f);
        boxLabel.setLayoutY(boxSize.getHeight() / 10f);
        scoreLabel.setLayoutX((boxSize.getWidth() - Settings.getFontMetrics(45).
                computeStringWidth(scoreLabel.getText())) / 2f);
        scoreLabel.setLayoutY(boxSize.getHeight() / 10 + Settings.getFontMetrics(boxLabelSize).getLineHeight() +
                (boxSize.getHeight() - (boxSize.getHeight() / 10 + Settings.getFontMetrics(boxLabelSize).
                        getLineHeight())) / 20f);
        this.setEffect(Settings.globalShadow);
    }

    public void updateScore(long score) {
        scoreLabel.setText("" + (scoreValue += score));
        centralize();
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void resetScore() {
        updateScore(this.scoreValue = 0);
    }
}
