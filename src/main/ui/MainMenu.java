package ui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import model.Player;
import ui.renderer.IRenderer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainMenu implements Scene {

    private static final int PLAYER_NAME_MAX_LENGTH = 24;

    private final Application application;
    private final IRenderer renderer;

    private final ArrayList<String> banner;

    private int numberOfPlayers;
    private final int minPlayers = 2;
    private final int maxPlayers = 8;

    private int currentOption = 0;
    private int maxOption = 2;

    // EFFECTS: constructor initializes screen object, number of players, and ASCII banner
    MainMenu() {
        application = Application.getInstance();
        renderer = application.getRenderer();

        banner = new ArrayList<>(Arrays.asList(
                    "███╗   ███╗ ██████╗ ███╗   ██╗ ██████╗ ██████╗  ██████╗ ██╗  ██╗   ██╗\n",
                    "████╗ ████║██╔═══██╗████╗  ██║██╔═══██╗██╔══██╗██╔═══██╗██║  ╚██╗ ██╔╝\n",
                    "██╔████╔██║██║   ██║██╔██╗ ██║██║   ██║██████╔╝██║   ██║██║   ╚████╔╝ \n",
                    "██║╚██╔╝██║██║   ██║██║╚██╗██║██║   ██║██╔═══╝ ██║   ██║██║    ╚██╔╝  \n",
                    "██║ ╚═╝ ██║╚██████╔╝██║ ╚████║╚██████╔╝██║     ╚██████╔╝███████╗██║   \n",
                    "╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚═╝      ╚═════╝ ╚══════╝╚═╝   \n"
            ));

        numberOfPlayers = minPlayers;
    }

    // MODIFIES: this
    // EFFECTS: handles main input logic
    @Override
    public boolean handleInput() throws IOException {
        KeyStroke keyStroke = renderer.pollInput();
        if (keyStroke != null) {
            System.out.println(keyStroke);
            renderer.clear();

            KeyType keyType = keyStroke.getKeyType();

            handleArrowInput(keyType);
            if (!handleEnterInput(keyType)) {
                return false;
            }

            switch (keyType) {
                case Escape:
                    return false;
                default:
                    break;
            }
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: change number of players with horizontal arrows, and options with vertical arrows
    private void handleArrowInput(KeyType keyType) {
        switch (keyType) {
            case ArrowRight:
                if (numberOfPlayers < maxPlayers) {
                    numberOfPlayers++;
                }
                break;
            case ArrowLeft:
                if (numberOfPlayers > minPlayers) {
                    numberOfPlayers--;
                }
                break;
            case ArrowDown:
                if (currentOption < maxOption) {
                    currentOption++;
                }
                break;
            case ArrowUp:
                if (currentOption > 0) {
                    currentOption--;
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: behaviour for selected option when enter is pressed
    private boolean handleEnterInput(KeyType keyType) {
        switch (keyType) {
            case Enter:
                switch (currentOption) {
                    case 0: // new game
                        newGame();
                        break;
                    case 1: // load game
                        loadGame();
                        break;
                    case 2: // quit
                        return false;
                    default:
                        break;
                }
                break;
        }
        return true;
    }

    // EFFECTS: handles making a new game
    private void newGame() {
        System.out.println("Play with " + numberOfPlayers + " players");
        ArrayList<Player> players = initPlayers();
        if (players == null) {
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(new java.util.Date());
        application.createGameScene(players, timeStamp + ".json");
        application.setGameScene();
    }

    private void loadGame() {
        try {
            application.createGameScene(null, chooseSave());
        } catch (Exception e) {
            System.out.println("Failed to load save");
            renderer.clear();
            return;
        }
        application.setGameScene();
    }

    // EFFECTS: return list of new players
    private ArrayList<Player> initPlayers() {
        ArrayList<Player> result = new ArrayList<>();
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                result.add(createNewPlayer(20, 11, 50, 3, i));
            } catch (Exception e) {
                System.out.print("Failed to add player " + (i + 1));
                return null;
            }
        }
        return result;
    }

    // REQUIRES: textGraphics != null
    // MODIFIES: this
    // EFFECTS: create individual player object with input box
    private Player createNewPlayer(int x, int y,
                                   int width, int height, int i) throws Exception {
        String name = "";
        while (true) {
            String textBox = "Enter Player " + (i + 1) + " name: " + name;
            KeyStroke keyStroke = renderer.pollInput();
            render();
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Enter:
                        return new Player(i, name, 0, 200);
                    case Escape:
                        throw new Exception();
                    default:
                        name = updateName(keyStroke, name);
                        break;
                }
            }
            Application.drawBox(x, y, width, height);
            renderer.putString(x + 3,y + 1, textBox);
            renderer.refresh();
        }
    }

    // REQUIRES: keyStroke != null
    // EFFECTS: update name string with keystroke
    private String updateName(KeyStroke keyStroke, String name) {
        String result = name;
        switch (keyStroke.getKeyType()) {
            case Character:
                if (result.length() < PLAYER_NAME_MAX_LENGTH) {
                    result += keyStroke.getCharacter();
                }
                break;
            case Backspace:
                result = removeLastChar(result);
                break;
        }
        return result;
    }

    // EFFECTS: remove last character from string
    private String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? ""
                : (s.substring(0, s.length() - 1));
    }

    // EFFECTS: return name of save to load
    private String chooseSave() throws Exception {
        // TODO - scrolling for more files
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
        ArrayList<String> saves = getSaves();
        int currentSaveOption = 0;
        while (true) {
            KeyStroke keyStroke = renderer.pollInput();
            render();
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Enter:
                        return saves.get(currentSaveOption);
                    case Escape:
                        throw new Exception();
                    case ArrowUp:
                        currentSaveOption--;
                        break;
                    case ArrowDown:
                        currentSaveOption++;
                        break;
                }
            }
            chooseSaveRender(saves, currentSaveOption);
        }
    }

    // REQUIRES: textGraphics != null
    // EFFECTS: renders choose save window
    private void chooseSaveRender(ArrayList<String> saves, int option) throws IOException {
        if (option < 0) {
            option = saves.size() - 1;
        } else if (option >= saves.size()) {
            option = 0;
        }
        Application.drawBox(20, 11, 50, 14);
        renderer.putString(22, 11, "Choose save");
        drawSaves(23, 12, saves, option);
        renderer.refresh();
    }

    // EFFECTS: get filenames in saves folder
    private ArrayList<String> getSaves() {
        ArrayList<String> saves = new ArrayList<>();
        File folder = new File(Application.getSaves());
        for (File file : Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".json")))) {
            saves.add(file.getName());
        }
        return saves;
    }

    // REQUIRES: textGraphics != null
    // EFFECTS: draws list of saves
    private void drawSaves(int startX, int startY, ArrayList<String> saves, int option) {
        for (int i = 0; i < saves.size(); i++) {
            renderer.putString(startX, startY + i, (option == i ? "> " : "  ") + saves.get(i));
        }
    }

    // EFFECTS: handles drawing to the screen
    @Override
    public boolean update() {

        return true;
    }

    // EFFECTS: handles drawing to the screen
    @Override
    public boolean render() {
        renderer.setForegroundColor(TextColor.ANSI.WHITE);

        int x = 11;
        int y = 10;
        for (int i = 0; i < banner.size(); i++) {
            renderer.putString(x, y + i, banner.get(i));
        }

        String playerString = (numberOfPlayers > minPlayers ? "<" : " ")
                + " " + numberOfPlayers + " "
                + (numberOfPlayers < maxPlayers ? ">" : " ");
        renderer.putString(x, y + banner.size() + 2,  selected(0)
                + "New game with number of players (2-8):  " + playerString);

        renderer.putString(x, y + banner.size() + 4,  selected(1)
                + "Load game from file");

        renderer.putString(x, y + banner.size() + 6,  selected(2)
                + "Quit");

        return true;
    }

    // EFFECTS: selection indicator for turn options
    private String selected(int option) {
        return currentOption == option ? "> " : "  ";
    }

}
