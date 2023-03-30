package ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import model.MonopolyGame;
import model.Player;
import model.Square;
import model.square.PropertySquare;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
TODO:
 - game state manager
 - loop over state stack
 - push and pop game state
    - new abstract class
    - enter and leave methods
    - handle input, update, draw
 */

// scene that displays the main game
public class GameScene implements Scene {

    enum GameState {
        PAUSED,
        SAVED,
        SELECT_TURN_OPTION,
        ROLLING_DICE,
        ENDING_TURN,
        PASSING_GO
    }

    private final boolean autoSave = false;

    private GameState gameState;
    private GameState previousGameState;

    private Application application;
    private ConsoleRenderer screen;
    private ConsoleRenderer textGraphics;

    private MonopolyGame monopolyGame;
    private ArrayList<Square> board;

    private int pauseOption = 0;
    private int maxPauseOption = 3;
    private boolean pauseWarning = false;
    private boolean saveWarning = true;
    private int saveOption = 0;

    private int currentTurnOption = 0;
    private int maxTurnOption = 5;

    private int rollAmount;

    ArrayList<String> boardRender;

    // EFFECTS: constructor initializes screen object, monopoly game object, game state, and strings for rendering
    GameScene(Application application, ArrayList<Player> players, String saveName) {
        gameState = GameState.SELECT_TURN_OPTION;

        this.application = application;
        screen = application.getScreen();
        textGraphics = screen;

        application.setJsonWriter(saveName);
        if (players != null) {
            // new game
            monopolyGame = new MonopolyGame(players);
            application.saveMonopolyGame(monopolyGame);
        } else {
            // read save
            try {
                application.setJsonReader(saveName);
                monopolyGame = application.getJsonReader().read();
            } catch (Exception e) {
                System.out.println("Failed to open file");
            }
        }

        board = monopolyGame.getBoard();

        boardRender = initBoard();
    }

    // EFFECTS: array of strings for rendering the monopoly board to the screen
    @SuppressWarnings("methodlength")
    private ArrayList<String> initBoard() {
        return new ArrayList<>(Arrays.asList(
                "ROUND:                                                                                    \n",
                "        FREE   KNTCY CHANCE   IND    ILL   B&O.  ATLNTC  VNTNR  WATER  MRVN               \n",
                "        PARK    AVE           AVE    AVE   RAIL    AVE    AVE   WORKS  GRDNS              \n",
                "      ┌──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┐      \n",
                "      │      │      │      │      │      │      │      │      │      │      │      │ GO TO\n",
                "      │      │      │      │      │      │      │      │      │      │      │      │ JAIL \n",
                "      ├──────┼──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┼──────┤      \n",
                "  NY  │      │                                                              │      │ PCFC \n",
                " AVE  │      │                                                              │      │  AVE \n",
                "      ├──────┤                                                              ├──────┤      \n",
                " TEN  │      │                                                              │      │  NC  \n",
                " AVE  │      │                                                              │      │  AVE \n",
                "      ├──────┤                                                              ├──────┤      \n",
                " COM  │      │                                                              │      │  COM \n",
                "CHEST │      │                                                              │      │ CHEST\n",
                "      ├──────┤                                                              ├──────┤      \n",
                "ST.JA │      │                                                              │      │PNSLVA\n",
                " AVE  │      │                                                              │      │  AVE \n",
                "      ├──────┤                                                              ├──────┤      \n",
                "PNSLVA│      │                                                              │      │ SHORT\n",
                " RAIL │      │                                                              │      │ LINE \n",
                "      ├──────┤                                                              ├──────┤      \n",
                "VRGNIA│      │                                                              │      │CHANCE\n",
                " AVE  │      │                                                              │      │      \n",
                "      ├──────┤                                                              ├──────┤      \n",
                "STATES│      │                                                              │      │ PARK \n",
                " AVE  │      │                                                              │      │ PLACE\n",
                "      ├──────┤                                                              ├──────┤      \n",
                " ELCT │      │                                                              │      │LUXURY\n",
                " COMP │      │                                                              │      │  TAX \n",
                "      ├──────┤                                                              ├──────┤      \n",
                "ST.CH │      │                                                              │      │ BRDWK\n",
                " AVE  │      │                                                              │      │      \n",
                "      ├──────┼──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┼──────┤      \n",
                "VISIT │      │      │      │      │      │      │      │      │      │      │      │      \n",
                " JAIL │      │      │      │      │      │      │      │      │      │      │      │      \n",
                "      └──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┘      \n",
                "              CNNCT  VERMNT CHANCE  ORNTL  READ   TAX   BALTIC  COM   MEDTRN   GO         \n",
                "               AVE    AVE            AVE   RAIL          AVE   CHEST   AVE                \n"));
    }

    // MODIFIES: this
    // EFFECTS: handles main input logic
    @Override
    public boolean handleInput() throws IOException {
        boolean result = true;
        int keyStroke = screen.pollInput();
        if (keyStroke != -1) {
            System.out.println(keyStroke);
            screen.clear();
            result &= handleInputState(keyStroke);
            result &= handleDebugInput(keyStroke);
        }
        return result;
    }

    // EFFECTS: input behaviour for different game states
    private boolean handleInputState(int keyStroke) {
        boolean result = true;
        if (gameState == GameState.PAUSED) {
            handlePauseButton(keyStroke);
            handlePauseOptionArrowInput(keyStroke);
            result &= handlePauseOptionEnterInput(keyStroke);
        } else if (gameState == GameState.SAVED) {
            saved();
        } else if (gameState == GameState.SELECT_TURN_OPTION) {
            handlePauseButton(keyStroke);
            handleSelectTurnOptionArrowInput(keyStroke);
            handleSelectTurnOptionEnterInput(keyStroke);
        } else if (gameState == GameState.ROLLING_DICE) {
            handlePauseButton(keyStroke);
            rolledDice();
        } else if (gameState == GameState.ENDING_TURN) {
            handlePauseButton(keyStroke);
            endedTurn();
        } else if (gameState == GameState.PASSING_GO) {
            handlePauseButton(keyStroke);
            passedGo();
        }
        return result;
    }

    // REQUIRES: keyStroke != -1
    // EFFECTS: handles pausing with ESC
    private void handlePauseButton(int keyStroke) {
        switch (keyStroke) {
            case KeyEvent.VK_ESCAPE:
                pauseWarning = false;
//                saveWarning = true; // toggle true when other changes are made
                saveOption = 0;
                if (gameState == GameState.PAUSED) {
                    gameState = previousGameState;
                    break;
                }
                previousGameState = gameState;
                gameState = GameState.PAUSED;
                break;
        }
    }

    // REQUIRES: keyStroke != -1
    // MODIFIES: this
    // EFFECTS: changes pause option with arrow keys
    private void handlePauseOptionArrowInput(int keyStroke) {
        switch (keyStroke) {
            case KeyEvent.VK_DOWN:
                if (pauseOption < maxPauseOption) {
                    pauseOption++;
                } else {
                    pauseOption = 0;
                }
                break;
            case KeyEvent.VK_UP:
                if (pauseOption > 0) {
                    pauseOption--;
                } else {
                    pauseOption = maxPauseOption;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                saveOption = (saveOption + 1) % 2;
                break;
        }
    }

    // REQUIRES: keyStroke != -1
    // MODIFIES: this
    // EFFECTS: behaviour for pause option when enter is pressed
    private boolean handlePauseOptionEnterInput(int keyStroke) {
        switch (keyStroke) {
            case KeyEvent.VK_ENTER:
                switch (pauseOption) {
                    case 0: // resume
                        gameState = previousGameState;
                        break;
                    case 1: // save game
                        gameState = GameState.SAVED;
                        saveWarning = false;
                        break;
                    default:
                        return handlePauseQuitting();
                }
                break;
        }
        return true;
    }

    // REQUIRES: keyStroke != -1
    // MODIFIES: this
    // EFFECTS: behaviour for pause option to quit
    private boolean handlePauseQuitting() {
        if (pauseWarning && saveOption == 0) {
            pauseWarning = false;
            return true;
        }
        if (saveWarning && !(pauseWarning && saveOption == 1)) {
            pauseWarning = true;
            return true;
        }
        if (pauseOption == 2) {
            application.setMainMenu();
            return true;
        }
        return false;
    }

    // REQUIRES: keyStroke != -1
    // MODIFIES: this
    // EFFECTS: changes selected turn option with arrow keys
    private void handleSelectTurnOptionArrowInput(int keyStroke) {
        switch (keyStroke) {
            case KeyEvent.VK_DOWN:
                if (currentTurnOption < maxTurnOption) {
                    currentTurnOption++;
                } else {
                    currentTurnOption = 0;
                }
                break;
            case KeyEvent.VK_UP:
                if (currentTurnOption > 0) {
                    currentTurnOption--;
                } else {
                    currentTurnOption = maxTurnOption;
                }
                break;
        }
    }

    // REQUIRES: keyStroke != -1
    // MODIFIES: this
    // EFFECTS: behaviour for selected turn option when enter is pressed
    private void handleSelectTurnOptionEnterInput(int keyStroke) {
        saveWarning = true;
        switch (keyStroke) {
            case KeyEvent.VK_ENTER:
                switch (currentTurnOption) {
                    case 0: // roll dice
                        rollDice();
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
                        endTurn();
                        break;
                }
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: revert game state to pause menu and save
    private void saved() {
        gameState = GameState.PAUSED;
        application.saveMonopolyGame(monopolyGame);
    }

    // MODIFIES: this
    // EFFECTS: set game state to rolling dice, set new value for rolled dice if not already rolled
    private void rollDice() {
        gameState = GameState.ROLLING_DICE;
        if (monopolyGame.isAlreadyRolled()) {
            return;
        }
        rollAmount = new Random().nextInt(11) + 2; // 2-12
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and moves player with value from rolled dice
    private void rolledDice() {
        gameState = GameState.SELECT_TURN_OPTION;
        if (monopolyGame.isAlreadyRolled()) {
            return;
        }
        Player player = monopolyGame.getCurrentPlayer();
        if (monopolyGame.movePlayer(player, rollAmount)) {
            passGo();
        }
        monopolyGame.setAlreadyRolled(true);
        if (autoSave) {
            application.saveMonopolyGame(monopolyGame);
        }
    }

    // MODIFIES: this
    // EFFECTS: set state to ending turn
    private void endTurn() {
        gameState = GameState.ENDING_TURN;
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and pass turn to next player
    private void endedTurn() {
        gameState = GameState.SELECT_TURN_OPTION;
        if (!monopolyGame.isAlreadyRolled()) {
            return;
        }
        monopolyGame.nextPlayer();
        monopolyGame.setAlreadyRolled(false);
        currentTurnOption = 0;
        if (autoSave) {
            application.saveMonopolyGame(monopolyGame);
        }
    }

    // MODIFIES: this
    // EFFECTS: set state to passing go
    private void passGo() {
        gameState = GameState.PASSING_GO;
    }

    // MODIFIES: this
    // EFFECTS: revert game state, and give reward balance after passing GO
    private void passedGo() {
        gameState = GameState.SELECT_TURN_OPTION;
        monopolyGame.getCurrentPlayer().addBalance(200);
    }

    // REQUIRES: keyStroke != null
    // EFFECTS: inputs for debug functions
    private boolean handleDebugInput(int keyStroke) {
        // TODO - move debug to debug console
        if (keyStroke >= 65 && keyStroke <= 90) {
            if ((char) keyStroke == 'F') { // print current player
                System.out.println(monopolyGame.getCurrentPlayer().getName());
            }
            if ((char) keyStroke == 'G') { // move next player
                System.out.println(monopolyGame.nextPlayer().getName());
            }
            if ((char) keyStroke == 'H') { // print balance
                System.out.println(monopolyGame.getCurrentPlayer().getBalance());
            }
            if ((char) keyStroke == 'J') {  // move just before GO
                monopolyGame.movePlayer(monopolyGame.getCurrentPlayer(), 39);
            }
            if ((char) keyStroke == 'M') {
                monopolyGame.getCurrentPlayer().addBalance(10);
            }
        }
        if (keyStroke == KeyEvent.VK_ENTER) {
//            System.out.println(monopolyGame.testLand(3));
        }
        return true;
    }

    // EFFECTS: handles changes to models
    @Override
    public boolean update() {

        return true;
    }

    // EFFECTS: handles drawing to the screen
    @Override
    public boolean render() {
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

//        textGraphics.putString(4, 4, String.valueOf(numberOfPlayers));

        for (int i = 0; i < boardRender.size(); i++) {
            textGraphics.putString(2, i, boardRender.get(i));
        }

        // Go - 34, 77
        // 3 vertical, 7 horizontal

        textGraphics.putString(1, 1,"ROUND: " + String.valueOf(monopolyGame.getCurrentRound()));

        renderPlayerTurn(20, 8);

        renderState();

        boardRender(79, 34, 7, 3, 5, 0,
                String.valueOf(Symbols.BLOCK_SOLID), null);

        return true;
    }

    // EFFECTS: draw game state specific items
    void renderState() {
        switch (gameState) {
            case PAUSED:
                renderPauseMenu(40, 14);
                break;
            case SAVED:
                renderSaving(28, 12);
                break;
            case SELECT_TURN_OPTION:
                break;
            case ROLLING_DICE:
                renderRollingDice(28, 12);
                break;
            case ENDING_TURN:
                renderEndingTurn(28, 12);
                break;
            case PASSING_GO:
                renderPassingGo(28, 12);
                break;
        }
    }

    // EFFECTS: draw pause menu
    private void renderPauseMenu(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 10);
        textGraphics.putString(startX + 2, startY - 1, "Paused");
        int offsetX = 2;
        int offsetY = 2;
        textGraphics.putString(startX + offsetX, startY + offsetY, getPauseOption(0) + "Resume");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(startX + offsetX, startY + offsetY + 1, getPauseOption(1) + "Save game");
        textGraphics.putString(startX + offsetX, startY + offsetY + 2, getPauseOption(2) + "Quit to main menu");
        textGraphics.putString(startX + offsetX, startY + offsetY + 3, getPauseOption(3) + "Quit to desktop");
        if (pauseWarning) {
            renderPauseWarning(startX + 6, startY + 5);
        }
    }

    private void renderSaving(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 4);
        textGraphics.putString(startX + 2, startY + 1, "Saved game");
        textGraphics.putString(startX + 2, startY + 3, "Press any button to continue...");
    }

    // EFFECTS: draw save warning
    private void renderPauseWarning(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 23, 4);
        textGraphics.putString(startX + 2, startY + 1, "Exit without saving?");
        String no = (saveOption == 0 ? "> " : "  ") + "No";
        String yes = (saveOption == 1 ? "> " : "  ") + "Yes";
        textGraphics.putString(startX + 5, startY + 3, no + "    " + yes);

    }

    // EFFECTS: selection indicator for pause options
    private String getPauseOption(int option) {
        return (option == pauseOption) ? "> " : "  ";
    }

    // EFFECTS: draw player turn information
    private void renderPlayerTurn(int startX, int startY) {
        Player currentPlayer = monopolyGame.getCurrentPlayer();
        textGraphics.putString(startX, startY, currentPlayer.getName() + "'s turn");

        int offsetX = 2;
        int offsetY = 8;

        textGraphics.putString(startX, startY + 2, "Current balance: " + currentPlayer.getBalance());

        playerTurnOptions(startX, startY, offsetX, offsetY);
    }

    // EFFECTS: draw turn options
    private void playerTurnOptions(int startX, int startY, int offsetX, int offsetY) {
        if (monopolyGame.isAlreadyRolled()) {
            textGraphics.setForegroundColor(TextColor.ANSI.RED);
        }
        textGraphics.putString(startX + offsetX, startY + offsetY, getTurnOption(0) + "Roll dice");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(startX + offsetX, startY + offsetY + 1, getTurnOption(1) + "Mortgage");
        textGraphics.putString(startX + offsetX, startY + offsetY + 2, getTurnOption(2) + "Lift mortgage");
        textGraphics.putString(startX + offsetX, startY + offsetY + 3, getTurnOption(3) + "Sell property");
        textGraphics.putString(startX + offsetX, startY + offsetY + 4, getTurnOption(4) + "Sell house");
        if (!monopolyGame.isAlreadyRolled()) {
            textGraphics.setForegroundColor(TextColor.ANSI.RED);
        }
        textGraphics.putString(startX + offsetX, startY + offsetY + 5, getTurnOption(5) + "End Turn");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
    }

    // EFFECTS: draw rolling dice alert window
    private void renderRollingDice(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 4);
        if (!monopolyGame.isAlreadyRolled()) {
            textGraphics.putString(startX + 2, startY + 1, "Rolled a " + rollAmount);
        } else {
            textGraphics.putString(startX + 2, startY + 1, "Player has already rolled");
        }
        textGraphics.putString(startX + 2, startY + 3, "Press any button to continue...");
    }

    // EFFECTS: draw ending turn alert window
    private void renderEndingTurn(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 4);
        if (monopolyGame.isAlreadyRolled()) {
            textGraphics.putString(startX + 2, startY + 1, "It is now "
                    + monopolyGame.getNextPlayer().getName() + "'s turn");
        } else {
            textGraphics.putString(startX + 2, startY + 1, "Please roll before ending turn");
        }
        textGraphics.putString(startX + 2, startY + 3, "Press any button to continue...");
    }

    // EFFECTS: draw passing go alert window
    private void renderPassingGo(int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 4);
        textGraphics.putString(startX + 2, startY + 1,
                monopolyGame.getCurrentPlayer().getName() + " passed GO, collect 200");
        textGraphics.putString(startX + 2, startY + 3, "Press any button to continue...");
    }

    // EFFECTS: selection indicator for turn options
    private String getTurnOption(int turnOption) {
        return (currentTurnOption == turnOption) ? "> " : "  ";
    }

    // REQUIRES: boardRender != null && board != null
    // EFFECTS: draw board from board model
    private void boardRender(int startX, int startY, int horizontalSize, int verticalSize,
                             int horizontalOffset, int verticalOffset,
                             String s, TextColor color) {
        int length = (board.size() + 1) / 4;
        boardRenderRows(startX, startY, length,
                -horizontalSize, 0, horizontalOffset, verticalOffset, 0, s, color);
        boardRenderRows(startX - length * horizontalSize, startY, length,
                0, -verticalSize, horizontalOffset, verticalOffset, length, s, color);
        boardRenderRows(startX - length * horizontalSize, startY - length * verticalSize, length,
                horizontalSize, 0, horizontalOffset, verticalOffset, length * 2, s, color);
        boardRenderRows(startX, startY - (length - 1) * verticalSize, length - 1,
                0, verticalSize, horizontalOffset, verticalOffset, length * 3 + 1, s, color);
    }

    // REQUIRES: boardRender != null && board != null
    // EFFECTS: draw rows from board model
    private void boardRenderRows(int startX, int startY, int length,
                                 int horizontalMove, int verticalMove,
                                 int horizontalOffset, int verticalOffset, int iterateStart,
                                 String s, TextColor c) {
        for (int i = 0; i < length; i++) {
            Square square = board.get(i + iterateStart);
            TextColor color = c;

            boolean isProperty = square instanceof PropertySquare;

            int houses = 0;
            if (isProperty) {
                PropertySquare propertySquare = (PropertySquare) square;
                TextColor propertyColor = propertySquare.getType().toColor();
                if (color == null && propertyColor != null) {
                    color =  propertyColor;
                }
                houses = propertySquare.getNumberOfHouses();
            }
            squareRender(square, isProperty, startX, startY, horizontalMove, verticalMove,
                    horizontalOffset, verticalOffset, s, color, i, houses);
        }
    }

    // REQUIRES: boardRender != null && board != null
    // EFFECTS: draw individual square details
    private void squareRender(Square square, boolean isProperty,
                              int startX, int startY,
                              int horizontalMove, int verticalMove,
                              int horizontalOffset, int verticalOffset,
                              String s, TextColor color, int i, int houses) {
        int drawX = startX + i * horizontalMove;
        int drawY = startY + i * verticalMove;
        textGraphics.setForegroundColor(color);

        if (isProperty) {
            textGraphics.putString(drawX + horizontalOffset, drawY + verticalOffset, s);
            if (houses > 0) {
                textGraphics.putString(drawX + horizontalOffset, drawY + verticalOffset + 1,
                        houses > 4 ? "H" : String.valueOf(houses));
            }
        }

        int index = 0;
        int maxInLine = 4;
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        for (Player player : square.getPlayers()) {
            textGraphics.putString(drawX + index % maxInLine, drawY + index / maxInLine,
                    String.valueOf(player.getIndex() + 1));
            index++;
        }
    }

}
