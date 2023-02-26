package ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenu implements Scene {

    private static final int PLAYER_NAME_MAX_LENGTH = 24;

    private Application application;
    private Screen screen;

    private ArrayList<String> banner;

    private int numberOfPlayers;
    private int minPlayers = 2;
    private int maxPlayers = 8;

    private int currentOption = 0;
    private int maxOption = 2;

    MainMenu(Application application) {
        this.application = application;
        screen = application.getScreen();

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

    @Override
    public boolean handleInput() throws IOException {
        KeyStroke keyStroke = screen.pollInput();
        if (keyStroke != null) {
            System.out.println(keyStroke);
            screen.clear();

            KeyType keyType = keyStroke.getKeyType();

            handleArrowInput(keyType);
            if (!handleEnterInput(keyType))  {
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

    private boolean handleEnterInput(KeyType keyType) {
        switch (keyType) {
            case Enter:
                switch (currentOption) {
                    case 1: // play
                        System.out.println("Play with " + numberOfPlayers + " players");
                        ArrayList<Player> players = initPlayers();
                        if (players == null) {
                            break;
                        }
                        application.createGameScene(numberOfPlayers, players);
                        application.setGameScene();
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

    private ArrayList<Player> initPlayers() {
        ArrayList<Player> result = new ArrayList<>();
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                result.add(createNewPlayer(textGraphics, 20, 11, 50, 3, i));
            } catch (Exception e) {
                System.out.print("Failed to add player " + (i + 1));
                return null;
            }
        }
        return result;
    }

    private Player createNewPlayer(TextGraphics textGraphics, int x, int y,
                                   int width, int height, int i) throws Exception {
        String name = "";
        while (true) {
            String textBox = "Enter Player " + (i + 1) + " name: " + name;
            KeyStroke keyStroke = screen.pollInput();
            render();
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case Enter:
                        return new Player(i, name, null, 0, 200);
                    case Escape:
                        throw new Exception();
                    default:
                        name = updateName(keyStroke, name);
                        break;
                }
            }
            Application.drawBox(textGraphics, x, y, width, height);
            textGraphics.putString(x + 3,y + 1, textBox);
            screen.refresh();
        }
    }

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

    private String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? ""
                : (s.substring(0, s.length() - 1));
    }

    @Override
    public boolean update() {

        return true;
    }

    @Override
    public boolean render() {
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

        int x = 11;
        int y = 10;
        for (int i = 0; i < banner.size(); i++) {
            textGraphics.putString(x, y + i, banner.get(i));
        }

        String playerString = (numberOfPlayers > minPlayers ? "<" : " ")
                + " " + numberOfPlayers + " "
                + (numberOfPlayers < maxPlayers ? ">" : " ");
        textGraphics.putString(x, y + banner.size() + 2,  selected(0)
                + "Number of Players (2-8):  " + playerString);

        textGraphics.putString(x, y + banner.size() + 4,  selected(1)
                + "Play");

        textGraphics.putString(x, y + banner.size() + 6,  selected(2)
                + "Quit");

        return true;
    }

    private String selected(int option) {
        return currentOption == option ? "> " : "  ";
    }
}
