package ebolo.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Ebolo on 13-Jan-16.
 * Menu buttons
 */
class ActionButton extends ImageView {
    private final String prePath = "file:src/ebolo/images/";
    private final String fileType = ".png";
    private final String mName;
    private final ImageView thisButton;

    public ActionButton(String buttonName) {
        thisButton = this;
        mName = buttonName;
        this.enable();
        this.setSmooth(true);
        this.setPreserveRatio(true);
    }

    private void enable() {
        this.setImage(new Image(prePath + mName + fileType));
        this.setOnMouseEntered(event -> thisButton.setImage(new Image(prePath + mName + "Glow" + fileType)));
        this.setOnMouseExited(event -> thisButton.setImage(new Image(prePath + mName + fileType)));
        this.setOnMousePressed(event -> thisButton.setImage(new Image(prePath + mName + "Pressed" + fileType)));
        this.setOnMouseReleased(event -> thisButton.setImage(new Image(prePath + mName + "Glow" + fileType)));
    }

    public void disable() {
        this.setImage(new Image(prePath + mName + "Dis" + fileType));
        this.setOnMouseEntered(null);
        this.setOnMouseExited(null);
        this.setOnMousePressed(null);
        this.setOnMouseReleased(null);
    }
}
