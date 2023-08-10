package ui.state;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import ui.Application;
import ui.GameScene;

// class representing a pause game state
public class PauseState extends IState {
    private IState previousState;

    private int pauseOption = 0;
    private int maxPauseOption = 3;
    private boolean pauseWarning = false;
    private GameScene.SaveWarning saveWarning;
    private int saveOption = 0;

    public PauseState(MonopolyGame monopolyGame, GameScene.SaveWarning saveWarning, IState previousState,
                      GameScene.SwitchState switchState) {
        super(monopolyGame, GameState.PAUSED, switchState);
        this.saveWarning = saveWarning;
        setPreviousState(previousState);
    }

    public void setPreviousState(IState previousState) {
        this.previousState = previousState;
    }

    public IState getPreviousState() {
        return previousState;
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
        handlePauseOptionArrowInput(keyStroke);
        result = handlePauseOptionEnterInput(keyStroke);
        return result;
    }

    // REQUIRES: keyStroke != null
    // MODIFIES: this
    // EFFECTS: changes pause option with arrow keys
    private void handlePauseOptionArrowInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowDown:
                if (pauseOption < maxPauseOption) {
                    pauseOption++;
                } else {
                    pauseOption = 0;
                }
                break;
            case ArrowUp:
                if (pauseOption > 0) {
                    pauseOption--;
                } else {
                    pauseOption = maxPauseOption;
                }
                break;
            case ArrowLeft:
            case ArrowRight:
                saveOption = (saveOption + 1) % 2;
                break;
        }
    }

    // REQUIRES: keyStroke != null
    // MODIFIES: this
    // EFFECTS: behaviour for pause option when enter is pressed
    private boolean handlePauseOptionEnterInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case Enter:
                switch (pauseOption) {
                    case 0: // resume
                        switchState(previousState);
                        break;
                    case 1: // save game
                        switchState(new SaveState(monopolyGame, saveWarning, this, _switchState));
                        break;
                    default:
                        return handlePauseQuitting();
                }
                break;
        }
        return true;
    }

    // REQUIRES: keyStroke != null
    // MODIFIES: this
    // EFFECTS: behaviour for pause option to quit
    private boolean handlePauseQuitting() {
        if (pauseWarning && saveOption == 0) {
            pauseWarning = false;
            return true;
        }
        if (saveWarning.value && !(pauseWarning && saveOption == 1)) {
            pauseWarning = true;
            return true;
        }
        if (pauseOption == 2) {
            Application.getInstance().setMainMenu();
            return true;
        }
        return false;
    }

    @Override
    public void render() {
        renderPauseMenu(28, 12);
    }

    // EFFECTS: draw pause menu
    private void renderPauseMenu(int startX, int startY) {
        Application.drawBox(startX, startY, 34, 10);
        renderer.putString(startX + 2, startY, "Paused");
        int offsetX = 2;
        int offsetY = 2;
        renderer.putString(startX + offsetX, startY + offsetY, getPauseOption(0) + "Resume");
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
        renderer.putString(startX + offsetX, startY + offsetY + 1, getPauseOption(1) + "Save game");
        renderer.putString(startX + offsetX, startY + offsetY + 2, getPauseOption(2) + "Quit to main menu");
        renderer.putString(startX + offsetX, startY + offsetY + 3, getPauseOption(3) + "Quit to desktop");
        if (pauseWarning) {
            renderPauseWarning(startX + 6, startY + 4);
        }
    }

    // EFFECTS: draw save warning
    private void renderPauseWarning(int startX, int startY) {
        Application.drawBox(startX, startY, 23, 4);
        renderer.putString(startX + 2, startY + 1, "Exit without saving?");
        String no = (saveOption == 0 ? "> " : "  ") + "No";
        String yes = (saveOption == 1 ? "> " : "  ") + "Yes";
        renderer.putString(startX + 5, startY + 3, no + "    " + yes);

    }

    // EFFECTS: selection indicator for pause options
    private String getPauseOption(int option) {
        return (option == pauseOption) ? "> " : "  ";
    }

}
