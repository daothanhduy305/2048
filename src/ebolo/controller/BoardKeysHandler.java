package ebolo.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Created by Ebolo on 04-Jan-16.
 * The custom Keys handler for the Application
 */
public class BoardKeysHandler implements EventHandler<KeyEvent> {
    private final PuzzleBoardController mControlPanel;

    public BoardKeysHandler(PuzzleBoardController controlPanel) {
        mControlPanel = controlPanel;
    }

    @Override
    public void handle(KeyEvent event) {
        mControlPanel.movePieces(event.getCode());
    }
}
