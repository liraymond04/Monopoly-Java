package ui.state;

import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import ui.Application;
import ui.GameScene;

// class representing a passing go game state
public class PassGoState extends IState {

    PassGoState(MonopolyGame monopolyGame, GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.PASSING_GO, switchState);
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public boolean handleInput(KeyStroke keyStroke) throws Exception {
        passedGo();
        return true;
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and give reward balance after passing GO
    private void passedGo() {
        monopolyGame.getCurrentPlayer().addBalance(200);
        switchState(GameScene.turnOptionState);
    }

    @Override
    public void render() {
        renderPassingGo(28, 12);
    }

    // EFFECTS: draw passing go alert window
    private void renderPassingGo(int startX, int startY) {
        Application.drawBox(startX, startY, 34, 4);
        renderer.putString(startX + 2, startY + 1,
                monopolyGame.getCurrentPlayer().getName() + " passed GO, collect 200");
        renderer.putString(startX + 2, startY + 3, "Press any button to continue...");
    }
}
