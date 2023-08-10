package ui.state;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import model.Player;
import ui.GameScene;

// class representing a select turn option game state
public class SelectTurnOptionState extends IState {

    public SelectTurnOptionState(MonopolyGame monopolyGame, GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.SELECT_TURN_OPTION, switchState);
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public boolean handleInput(KeyStroke keyStroke) throws Exception {
        boolean result = true;
        System.out.println(GameScene.turnOptions.current);
        handleSelectTurnOptionArrowInput(keyStroke);
        handleSelectTurnOptionEnterInput(keyStroke);
        return result;
    }

    // REQUIRES: keyStroke != null
    // MODIFIES: this
    // EFFECTS: changes selected turn option with arrow keys
    private void handleSelectTurnOptionArrowInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowDown:
                if (GameScene.turnOptions.current < GameScene.turnOptions.max) {
                    GameScene.turnOptions.current++;
                } else {
                    GameScene.turnOptions.current = 0;
                }
                break;
            case ArrowUp:
                if (GameScene.turnOptions.current > 0) {
                    GameScene.turnOptions.current--;
                } else {
                    GameScene.turnOptions.current = GameScene.turnOptions.max;
                }
                break;
        }
    }

    // REQUIRES: keyStroke != null
    // MODIFIES: this
    // EFFECTS: behaviour for selected turn option when enter is pressed
    private void handleSelectTurnOptionEnterInput(KeyStroke keyStroke) throws Exception {
        switch (keyStroke.getKeyType()) {
            case Enter:
                GameScene.saveWarning.value = true;
                switch (GameScene.turnOptions.current) {
                    case 0: // roll dice
                        switchState(new RollDiceState(monopolyGame, _switchState));
                        break;
                    case 1: // mortgage
                        break;
                    case 2: // lift mortgage
                        break;
                    case 3: // sell property
                        break;
                    case 4: // sell house
                        break;
                    case 5: // end turn
                        switchState(new EndTurnState(monopolyGame, _switchState));
                        break;
                }
                break;
        }
    }

    @Override
    public void render() {
        renderPlayerTurn(20, 8);
    }

    // EFFECTS: draw player turn information
    private void renderPlayerTurn(int startX, int startY) {
        Player currentPlayer = monopolyGame.getCurrentPlayer();
        renderer.putString(startX, startY, currentPlayer.getName() + "'s turn");

        int offsetX = 2;
        int offsetY = 8;

        renderer.putString(startX, startY + 2, "Current balance: " + currentPlayer.getBalance());

        playerTurnOptions(startX, startY, offsetX, offsetY);
    }

    // EFFECTS: draw turn options
    private void playerTurnOptions(int startX, int startY, int offsetX, int offsetY) {
        if (monopolyGame.isAlreadyRolled()) {
            renderer.setForegroundColor(TextColor.ANSI.RED);
        }
        renderer.putString(startX + offsetX, startY + offsetY, getTurnOption(0) + "Roll dice");
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
        renderer.putString(startX + offsetX, startY + offsetY + 1, getTurnOption(1) + "Mortgage");
        renderer.putString(startX + offsetX, startY + offsetY + 2, getTurnOption(2) + "Lift mortgage");
        renderer.putString(startX + offsetX, startY + offsetY + 3, getTurnOption(3) + "Sell property");
        renderer.putString(startX + offsetX, startY + offsetY + 4, getTurnOption(4) + "Sell house");
        if (!monopolyGame.isAlreadyRolled()) {
            renderer.setForegroundColor(TextColor.ANSI.RED);
        }
        renderer.putString(startX + offsetX, startY + offsetY + 5, getTurnOption(5) + "End Turn");
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
    }

    // EFFECTS: selection indicator for turn options
    private String getTurnOption(int turnOption) {
        return (GameScene.turnOptions.current == turnOption) ? "> " : "  ";
    }
}
