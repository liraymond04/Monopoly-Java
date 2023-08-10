package ui;

import com.googlecode.lanterna.Symbols;
import model.MonopolyGame;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.renderer.IRenderer;
import ui.renderer.LanternaRenderer;

import java.util.ArrayList;

// handles application window logic
public class Application {
    private static final String SAVES = "./data/saves/";

    private static final Application instance;
    private static IRenderer renderer;

    static {
        try {
            instance = new Application(94, 40);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Application getInstance() {
        return instance;
    }

    private Scene scene;

    private MainMenu mainMenu;
    private GameScene gameScene;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructor initializes screen dimensions
    private Application(int screenWidth, int screenHeight) throws Exception {
        renderer = new LanternaRenderer(screenWidth, screenHeight);
    }

    public void init() {
        createMainMenu();
        setMainMenu();
    }

    public static String getSaves() {
        return SAVES;
    }

    // MODIFIES: textGraphics
    // EFFECTS: draws box with ANSI characters
    public static void drawBox(int x, int y, int width, int height) {
        renderer.putString(x, y, "╭");
        renderer.putString(x + width, y, "╮");
        renderer.putString(x, y + height, "╰");
        renderer.putString(x + width, y + height, "╯");
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                renderer.putString(x + i, y + j, " ");
            }
        }
        for (int i = 1; i < height; i++) {
            renderer.putString(x, y + i, String.valueOf(Symbols.SINGLE_LINE_VERTICAL));
            renderer.putString(x + width, y + i, String.valueOf(Symbols.SINGLE_LINE_VERTICAL));
        }
        for (int i = 1; i < width; i++) {
            renderer.putString(x + i, y, String.valueOf(Symbols.SINGLE_LINE_HORIZONTAL));
            renderer.putString(x + i, y + height, String.valueOf(Symbols.SINGLE_LINE_HORIZONTAL));
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
            renderer.refresh();
        }

        renderer.close();
    }

    public IRenderer getRenderer() {
        return renderer;
    }

    private void setScene(Scene scene) {
        this.scene = scene;
    }

    // MODIFIES: this
    // EFFECTS: creates new instance of main menu
    public void createMainMenu() {
        mainMenu = new MainMenu();
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
        gameScene = new GameScene(players, saveName);
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
