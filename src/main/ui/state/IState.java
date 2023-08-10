package ui.state;

import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import ui.Application;
import ui.GameScene;
import ui.renderer.IRenderer;

// abstract class that represents a game state
public abstract class IState {
    protected final MonopolyGame monopolyGame;
    protected final IRenderer renderer;
    @SuppressWarnings({"checkstyle:MemberName", "checkstyle:SuppressWarnings"})
    private final GameState _enum;
    @SuppressWarnings({"checkstyle:MemberName", "checkstyle:SuppressWarnings"})
    GameScene.SwitchState _switchState;

    IState(MonopolyGame monopolyGame, GameState state, GameScene.SwitchState switchState) {
        this.monopolyGame = monopolyGame;
        this._enum = state;
        _switchState = switchState;
        renderer = Application.getInstance().getRenderer();
        try {
            enter();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public GameState getEnum() {
        return _enum;
    }

    public void switchState(IState state) {
        leave();
        _switchState.setState(state);
        _switchState.call();
    }

    public abstract void enter() throws Exception;

    public abstract void leave();

    public abstract boolean handleInput(KeyStroke keyStroke) throws Exception;

    public abstract void render();
}
