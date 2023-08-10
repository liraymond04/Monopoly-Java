package ui.state;

import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import model.Player;
import ui.Application;
import ui.GameScene;

import java.util.Random;

// class representing a roll dice game state
public class RollDiceState extends IState {
    private int rollAmount;

    RollDiceState(MonopolyGame monopolyGame, GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.ROLLING_DICE, switchState);
    }

    @Override
    public void enter() {
        if (monopolyGame.isAlreadyRolled()) {
            return;
        }
        rollAmount = new Random().nextInt(11) + 2; // 2-12
    }

    @Override
    public void leave() {

    }

    @Override
    public boolean handleInput(KeyStroke keyStroke) throws Exception {
        rolledDice();
        return true;
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and moves player with value from rolled dice
    private void rolledDice() {
        if (monopolyGame.isAlreadyRolled()) {
            switchState(GameScene.turnOptionState);
            return;
        }
        Player player = monopolyGame.getCurrentPlayer();
        if (monopolyGame.movePlayer(player, rollAmount)) {
            switchState(new PassGoState(monopolyGame, _switchState));
        } else {
            switchState(GameScene.turnOptionState);
        }
        monopolyGame.setAlreadyRolled(true);
        if (GameScene.autoSave) {
            Application.getInstance().saveMonopolyGame(monopolyGame);
        }
    }

    @Override
    public void render() {
        renderRollingDice(28, 12);
    }

    // EFFECTS: draw rolling dice alert window
    private void renderRollingDice(int startX, int startY) {
        Application.drawBox(startX, startY, 34, 4);
        if (!monopolyGame.isAlreadyRolled()) {
            renderer.putString(startX + 2, startY + 1, "Rolled a " + rollAmount);
        } else {
            renderer.putString(startX + 2, startY + 1, "Player has already rolled");
        }
        renderer.putString(startX + 2, startY + 3, "Press any button to continue...");
    }
}
