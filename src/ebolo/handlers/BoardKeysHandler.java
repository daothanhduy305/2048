package ebolo.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Created by Ebolo on 04-Jan-16.
 * The custom Keys handler for the Application
 */
public class BoardKeysHandler implements EventHandler<KeyEvent> {
    private BoardControlPanel mControlPanel;

    public BoardKeysHandler(BoardControlPanel controlPanel) {
        mControlPanel = controlPanel;
    }

    @Override
    public void handle(KeyEvent event) {
        mControlPanel.movePieces(event.getCode());
    }
}
