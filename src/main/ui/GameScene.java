package ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import model.MonopolyGame;
import model.Player;
import model.Square;
import model.square.PropertySquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameScene implements Scene {

    enum GameState {
        SELECT_TURN_OPTION,
        ROLLING_DICE
    }

    private GameState gameState;

    private Application application;
    private Screen screen;

    private MonopolyGame monopolyGame;
    private ArrayList<Square> board;

    private int currentTurnOption = 0;
    private int maxTurnOption = 5;

    private int rollAmount;
    private boolean alreadyRolled = false;

    ArrayList<String> boardRender;

    GameScene(Application application, int numberOfPlayers, ArrayList<Player> players) {
        gameState = GameState.SELECT_TURN_OPTION;

        this.application = application;
        screen = application.getScreen();

        monopolyGame = new MonopolyGame(players);
        board = monopolyGame.getBoard();

        boardRender = initBoard();
    }

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

    @Override
    public boolean handleInput() throws IOException {
        boolean result = true;
        KeyStroke keyStroke = screen.pollInput();
        if (keyStroke != null) {
            System.out.println(keyStroke);
            screen.clear();

            switch (gameState) {
                case SELECT_TURN_OPTION:
                    result &= handleSelectTurnOptionArrowInput(keyStroke)
                            && handleSelectTurnOptionEnterInput(keyStroke);
                    break;
                case ROLLING_DICE:
                    gameState = GameState.SELECT_TURN_OPTION;
                    alreadyRolled = true;
                    break;
            }
            result &= handleDebugInput(keyStroke);
        }
        return result;
    }

    private boolean handleSelectTurnOptionArrowInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowDown:
                if (currentTurnOption < maxTurnOption) {
                    currentTurnOption++;
                } else {
                    currentTurnOption = 0;
                }
                break;
            case ArrowUp:
                if (currentTurnOption > 0) {
                    currentTurnOption--;
                } else {
                    currentTurnOption = maxTurnOption;
                }
                break;
        }
        return true;
    }

    private boolean handleSelectTurnOptionEnterInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case Enter:
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
        return true;
    }

    private void rollDice() {
        gameState = GameState.ROLLING_DICE;
        if (alreadyRolled) {
            return;
        }
        rollAmount = new Random().nextInt(12) + 1;
        Player player = monopolyGame.getCurrentPlayer();
        if (monopolyGame.movePlayer(player, rollAmount)) {
            player.addBalance(200);
        }
    }

    private void endTurn() {
        monopolyGame.nextPlayer();
        gameState = GameState.SELECT_TURN_OPTION;
        alreadyRolled = false;
        currentTurnOption = 0;
    }

    private boolean handleDebugInput(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case Enter:
//                    System.out.println(monopolyGame.testLand(3));
                break;
            case Character:
                if (keyStroke.getCharacter() == 'f') {
                    System.out.println(monopolyGame.getCurrentPlayer().getName());
                }
                if (keyStroke.getCharacter() == 'g') {
                    System.out.println(monopolyGame.nextPlayer().getName());
                }
                if (keyStroke.getCharacter() == 't') {
                    System.out.println(monopolyGame.getCurrentPlayer().getBalance());
                }
                break;
            case Escape:
                return false;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean update() {
        switch (gameState) {
            case SELECT_TURN_OPTION:
                break;
            case ROLLING_DICE:
                break;
        }

        return true;
    }

    @Override
    public boolean render() {
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

//        textGraphics.putString(4, 4, String.valueOf(numberOfPlayers));

        for (int i = 0; i < boardRender.size(); i++) {
            textGraphics.putString(2, i, boardRender.get(i));
        }

        // Go - 34, 77
        // 3 vertical, 7 horizontal

        textGraphics.putString(9, 0, String.valueOf(monopolyGame.getCurrentRound()));

        renderPlayerTurn(textGraphics, 20, 8);
        switch (gameState) {
            case SELECT_TURN_OPTION:
                break;
            case ROLLING_DICE:
//                renderPlayerTurn(textGraphics, 20, 8);
                renderRollingDice(textGraphics, 28, 12);
                break;
        }

        boardRender(textGraphics, 79, 34, 7, 3, 5, 0,
                String.valueOf(Symbols.BLOCK_SOLID), null);

        return true;
    }

    private void renderPlayerTurn(TextGraphics textGraphics, int startX, int startY) {
        Player currentPlayer = monopolyGame.getCurrentPlayer();
        textGraphics.putString(startX, startY, currentPlayer.getName() + "'s turn");

        int offsetX = 2;
        int offsetY = 2;

        // TODO
        //  - require player to roll to end turn
        //  - add alert window when passing GO

        if (alreadyRolled) {
            textGraphics.setForegroundColor(TextColor.ANSI.RED);
        }
        textGraphics.putString(startX + offsetX, startY + offsetY, getTurnOption(0) + "Roll dice");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(startX + offsetX, startY + offsetY + 1, getTurnOption(1) + "Mortgage");
        textGraphics.putString(startX + offsetX, startY + offsetY + 2, getTurnOption(2) + "Lift mortgage");
        textGraphics.putString(startX + offsetX, startY + offsetY + 3, getTurnOption(3) + "Sell property");
        textGraphics.putString(startX + offsetX, startY + offsetY + 4, getTurnOption(4) + "Sell house");
        textGraphics.putString(startX + offsetX, startY + offsetY + 5, getTurnOption(5) + "End Turn");
    }

    private void renderRollingDice(TextGraphics textGraphics, int startX, int startY) {
        Application.drawBox(textGraphics, startX, startY, 34, 4);
        if (!alreadyRolled) {
            textGraphics.putString(startX + 2, startY + 1, "Rolled a " + rollAmount);
        } else {
            textGraphics.putString(startX + 2, startY + 1, "Player has already rolled");
        }
        textGraphics.putString(startX + 2, startY + 3, "Press any button to continue...");
    }

    private String getTurnOption(int turnOption) {
        return (currentTurnOption == turnOption) ? "> " : "  ";
    }

    private void boardRender(TextGraphics textGraphics,
                             int startX, int startY, int horizontalSize, int verticalSize,
                             int horizontalOffset, int verticalOffset,
                             String s, TextColor color) {
        int length = (board.size() + 1) / 4;
        boardRenderRows(textGraphics, startX, startY, length,
                -horizontalSize, 0, horizontalOffset, verticalOffset, 0, s, color);
        boardRenderRows(textGraphics, startX - length * horizontalSize, startY, length,
                0, -verticalSize, horizontalOffset, verticalOffset, length, s, color);
        boardRenderRows(textGraphics, startX - length * horizontalSize, startY - length * verticalSize, length,
                horizontalSize, 0, horizontalOffset, verticalOffset, length * 2, s, color);
        boardRenderRows(textGraphics, startX, startY - (length - 1) * verticalSize, length - 1,
                0, verticalSize, horizontalOffset, verticalOffset, length * 3, s, color);
    }

    private void boardRenderRows(TextGraphics textGraphics,
                                 int startX, int startY, int length,
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
            squareRender(textGraphics, square, isProperty, startX, startY, horizontalMove, verticalMove,
                    horizontalOffset, verticalOffset, s, color, i, houses);
        }
    }

    private void squareRender(TextGraphics textGraphics,
                              Square square, boolean isProperty,
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
