package ui.state;

import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import ui.Application;
import ui.GameScene;

// class representing a save game state
public class SaveState extends IState {
    PauseState pauseState;

    SaveState(MonopolyGame monopolyGame, GameScene.SaveWarning saveWarning, PauseState pauseState,
              GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.SAVED, switchState);
        this.pauseState = pauseState;
        saveWarning.value = false;
    }

    @Override
    public void enter() {
        Application.getInstance().saveMonopolyGame(monopolyGame);
    }

    @Override
    public void leave() {

    }

    @Override
    public boolean handleInput(KeyStroke keyStroke) throws Exception {
        switchState(pauseState);
        return true;
    }

    @Override
    public void render() {
        renderSaving(28, 12);
    }

    // EFFECTS: draw saved game alert window
    private void renderSaving(int startX, int startY) {
        Application.drawBox(startX, startY, 34, 4);
        renderer.putString(startX + 2, startY + 1, "Saved game");
        renderer.putString(startX + 2, startY + 3, "Press any button to continue...");
    }
}
