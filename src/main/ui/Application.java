package ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.MonopolyGame;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.util.ArrayList;

// handles application window logic
public class Application {
    private static final String SAVES = "./data/saves/";

    private Screen screen;
    private Scene scene;

    private MainMenu mainMenu;
    private GameScene gameScene;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructor initializes screen dimensions
    Application(int screenWidth, int screenHeight) throws Exception {
        TerminalSize terminalSize = new TerminalSize(screenWidth, screenHeight);
        screen = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize).createScreen();

        screen.startScreen();
        screen.setCursorPosition(null); // turn off cursor

        createMainMenu();
        setMainMenu();
    }

    public static String getSaves() {
        return SAVES;
    }

    // MODIFIES: textGraphics
    // EFFECTS: draws box with ANSI characters
    public static void drawBox(TextGraphics textGraphics, int x, int y, int width, int height) {
        textGraphics.putString(x, y, "╭");
        textGraphics.putString(x + width, y, "╮");
        textGraphics.putString(x, y + height, "╰");
        textGraphics.putString(x + width, y + height, "╯");
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                textGraphics.putString(x + i, y + j, " ");
            }
        }
        for (int i = 1; i < height; i++) {
            textGraphics.putString(x, y + i, String.valueOf(Symbols.SINGLE_LINE_VERTICAL));
            textGraphics.putString(x + width, y + i, String.valueOf(Symbols.SINGLE_LINE_VERTICAL));
        }
        for (int i = 1; i < width; i++) {
            textGraphics.putString(x + i, y, String.valueOf(Symbols.SINGLE_LINE_HORIZONTAL));
            textGraphics.putString(x + i, y + height, String.valueOf(Symbols.SINGLE_LINE_HORIZONTAL));
        }
    }

    // MODIFIES: this
    // EFFECTS: handles main game loop, and executes scene functions
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

    // MODIFIES: this
    // EFFECTS: creates new instance of main menu
    public void createMainMenu() {
        mainMenu = new MainMenu(this);
    }

    // REQUIRES: mainMenu != null
    // MODIFIES: this
    // EFFECTS: sets active scene to the main menu
    public void setMainMenu() {
        setScene(mainMenu);
    }

    // MODIFIES: this
    // EFFECTS: creates new instance of game with provided list of players
    public void createGameScene(ArrayList<Player> players, String saveName) {
        gameScene = new GameScene(this, players, saveName);
    }

    // REQUIRES: gameScene != null
    // MODIFIES: this
    // EFFECTS: sets active scene to game scene
    public void setGameScene() {
        setScene(gameScene);
    }

    // EFFECTS: make new json writer for file
    public void setJsonWriter(String fileName) {
        jsonWriter = new JsonWriter(SAVES + fileName);
    }

    // EFFECTS: make new json reader for file
    public void setJsonReader(String fileName) {
        jsonReader = new JsonReader(SAVES + fileName);
    }

    public JsonReader getJsonReader() {
        return jsonReader;
    }

    // REQUIRES: jsonWriter != null
    // EFFECTS: saves monopoly game data to json
    public void saveMonopolyGame(MonopolyGame monopolyGame) {
        try {
            jsonWriter.open();
            jsonWriter.write(monopolyGame);
            jsonWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to open file");
        }
    }
}
