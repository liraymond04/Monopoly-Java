package ui.state;

import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import ui.Application;
import ui.GameScene;

// class representing an ending turn game state
public class EndTurnState extends IState {
    EndTurnState(MonopolyGame monopolyGame, GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.ENDING_TURN, switchState);
    }

    @Override
    public void enter() throws Exception {

    }

    @Override
    public void leave() {

    }

    @Override
    public boolean handleInput(KeyStroke keyStroke) throws Exception {
        endedTurn();
        switchState(GameScene.turnOptionState);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and pass turn to next player
    private void endedTurn() {
        if (!monopolyGame.isAlreadyRolled()) {
            return;
        }
        monopolyGame.nextPlayer();
        monopolyGame.setAlreadyRolled(false);
        GameScene.turnOptions.current = 0;
        if (GameScene.autoSave) {
            Application.getInstance().saveMonopolyGame(monopolyGame);
        }
    }

    @Override
    public void render() {
        renderEndingTurn(28, 12);
    }

    // EFFECTS: draw ending turn alert window
    private void renderEndingTurn(int startX, int startY) {
        Application.drawBox(startX, startY, 34, 4);
        if (monopolyGame.isAlreadyRolled()) {
            renderer.putString(startX + 2, startY + 1, "It is now "
                    + monopolyGame.getNextPlayer().getName() + "'s turn");
        } else {
            renderer.putString(startX + 2, startY + 1, "Please roll before ending turn");
        }
        renderer.putString(startX + 2, startY + 3, "Press any button to continue...");
    }
}