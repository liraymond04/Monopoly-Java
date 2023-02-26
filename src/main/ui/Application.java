package ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Player;

import java.util.ArrayList;

public class Application {
    private Screen screen;
    private Scene scene;

    private MainMenu mainMenu;
    private GameScene gameScene;

    Application(int screenWidth, int screenHeight) throws Exception {
        TerminalSize terminalSize = new TerminalSize(screenWidth, screenHeight);
        screen = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize).createScreen();

        screen.startScreen();
        screen.setCursorPosition(null); // turn off cursor

        createMainMenu();
        setMainMenu();
    }

    public void start() throws Exception {
        boolean run = true;
        while (run) {
            run = scene.handleInput()
                    && scene.update()
                    && scene.render();
            screen.refresh();
        }

        screen.close();
    }

    public Screen getScreen() {
        return screen;
    }

    private void setScene(Scene scene) {
        this.scene = scene;
    }

    public void createMainMenu() {
        mainMenu = new MainMenu(this);
    }

    public void setMainMenu() {
        setScene(mainMenu);
    }

    public void createGameScene(int numberOfPlayers, ArrayList<Player> players) {
        gameScene = new GameScene(this, numberOfPlayers, players);
    }

    public void setGameScene() {
        setScene(gameScene);
    }
}
