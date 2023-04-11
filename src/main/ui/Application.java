package ui;

import com.googlecode.lanterna.Symbols;
import model.Event;
import model.EventLog;
import model.MonopolyGame;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;

// handles application window logic
public class Application extends JFrame implements WindowListener {
    private static final String SAVES = "./data/saves/";

    private ConsoleRenderer screen;
    private Scene scene;

    private MainMenu mainMenu;
    private GameScene gameScene;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructor initializes screen dimensions
    Application(int screenWidth, int screenHeight, int charWidth, int charHeight) throws Exception {
        setTitle("Monopoly");

        screen = new ConsoleRenderer(screenWidth, screenHeight, charWidth, charHeight);
        screen.setBackground(Color.BLACK);

        add(screen, BorderLayout.CENTER);
        setSize(screenWidth * charWidth, screenHeight * charHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowListener(this);

//        screen.startScreen();
//        screen.setCursorPosition(null); // turn off cursor

        createMainMenu();
        setMainMenu();
    }

    public static String getSaves() {
        return SAVES;
    }

    // MODIFIES: textGraphics
    // EFFECTS: draws box with ANSI characters
    public static void drawBox(ConsoleRenderer textGraphics, int x, int y, int width, int height) {
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

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public ConsoleRenderer getScreen() {
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

    // EFFECTS: runs when window is opened, required implementation for window listener
    @Override
    public void windowOpened(WindowEvent e) {

    }

    // EFFECTS: print event log to console when window is closing
    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Event Log: ");
        Date prevDate = null;
        for (Event event : EventLog.getInstance()) {
            System.out.println(event + "\n");
        }
    }

    // EFFECTS: runs when window is closed, required implementation for window listener
    @Override
    public void windowClosed(WindowEvent e) {

    }

    // EFFECTS: runs when window is iconified, required implementation for window listener
    @Override
    public void windowIconified(WindowEvent e) {

    }

    // EFFECTS: runs when window is deiconified, required implementation for window listener
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    // EFFECTS: runs when window is activated, required implementation for window listener
    @Override
    public void windowActivated(WindowEvent e) {

    }

    // EFFECTS: runs when window is deactivated, required implementation for window listener
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
