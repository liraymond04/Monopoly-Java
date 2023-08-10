package ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import model.MonopolyGame;
import model.Player;
import model.Square;
import model.square.PropertySquare;
import ui.renderer.IRenderer;
import ui.state.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

// scene that displays the main game
public class GameScene implements Scene {

    public class SwitchState implements Callable<Void> {
        IState state;

        public void setState(IState state) {
            this.state = state;
        }

        public Void call() {
            gameState = state;
            return null;
        }
    }

    private final SwitchState switchState = new SwitchState();

    public static class SaveWarning {
        @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
        public boolean value = true;
    }

    public static final SaveWarning saveWarning = new SaveWarning();

    public static class TurnOptions {
        @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
        public int current = 0;
        public final int max = 5;
    }

    public static final TurnOptions turnOptions = new TurnOptions();

    private IState gameState;
    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public static SelectTurnOptionState turnOptionState;

    private final IRenderer renderer;

    private MonopolyGame monopolyGame;
    private final ArrayList<Square> board;

    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public static boolean autoSave = false;

    ArrayList<String> boardRender;

    // EFFECTS: constructor initializes screen object, monopoly game object, game state, and strings for rendering
    GameScene(ArrayList<Player> players, String saveName) {
        Application application = Application.getInstance();
        renderer = application.getRenderer();

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

        turnOptionState = new SelectTurnOptionState(monopolyGame, switchState);
        gameState = turnOptionState;
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
    public boolean handleInput() throws Exception {
        boolean result = true;
        KeyStroke keyStroke = renderer.pollInput();
        if (keyStroke != null) {
            System.out.println(keyStroke);
            renderer.clear();
            result &= handleInputState(keyStroke);
            result &= handleDebugInput(keyStroke);
        }
        return result;
    }

    // EFFECTS: input behaviour for different game states
    private boolean handleInputState(KeyStroke keyStroke) throws Exception {
        handlePauseButton(keyStroke);
        return gameState.handleInput(keyStroke);
    }

    // REQUIRES: keyStroke != null
    // EFFECTS: handles pausing with ESC
    private void handlePauseButton(KeyStroke keyStroke) throws Exception {
        switch (keyStroke.getKeyType()) {
            case Escape:
                if (gameState.getEnum() == GameState.PAUSED) {
                    gameState.switchState(((PauseState) gameState).getPreviousState());
                    break;
                }
                gameState = new PauseState(monopolyGame, saveWarning, gameState, switchState);
                break;
        }
    }

    // REQUIRES: keyStroke != null
    // EFFECTS: inputs for debug functions
    private boolean handleDebugInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case Enter:
//                    System.out.println(monopolyGame.testLand(3));
                break;
            case Character:
                if (keyStroke.getCharacter() == 'f') { // print current player
                    System.out.println(monopolyGame.getCurrentPlayer().getName());
                }
                if (keyStroke.getCharacter() == 'g') { // move next player
                    System.out.println(monopolyGame.nextPlayer().getName());
                }
                if (keyStroke.getCharacter() == 'h') { // print balance
                    System.out.println(monopolyGame.getCurrentPlayer().getBalance());
                }
                if (keyStroke.getCharacter() == 'j') {  // move just before GO
                    monopolyGame.movePlayer(monopolyGame.getCurrentPlayer(), 39);
                }
                break;
            default:
                break;
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
        renderer.setForegroundColor(TextColor.ANSI.WHITE);

        for (int i = 0; i < boardRender.size(); i++) {
            renderer.putString(2, i, boardRender.get(i));
        }

        // Go - 34, 77
        // 3 vertical, 7 horizontal

        renderer.putString(9, 0, String.valueOf(monopolyGame.getCurrentRound()));

        turnOptionState.render();

        renderState();

        boardRender(79, 34, 7, 3, 5, 0,
                String.valueOf(Symbols.BLOCK_SOLID), null);

        return true;
    }

    // EFFECTS: draw game state specific items
    void renderState() {
        if (gameState.getEnum() == GameState.SELECT_TURN_OPTION) {
            return;
        }
        gameState.render();
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
        renderer.setForegroundColor(color);

        if (isProperty) {
            renderer.putString(drawX + horizontalOffset, drawY + verticalOffset, s);
            if (houses > 0) {
                renderer.putString(drawX + horizontalOffset, drawY + verticalOffset + 1,
                        houses > 4 ? "H" : String.valueOf(houses));
            }
        }

        int index = 0;
        int maxInLine = 4;
        renderer.setForegroundColor(TextColor.ANSI.WHITE);
        for (Player player : square.getPlayers()) {
            renderer.putString(drawX + index % maxInLine, drawY + index / maxInLine,
                    String.valueOf(player.getIndex() + 1));
            index++;
        }
    }

}
