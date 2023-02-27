package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// tests for the MonopolyGame class
class MonopolyGameTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private MonopolyGame monopolyGame;
    private ArrayList<Player> players;

    @BeforeEach
    public void before() {
        System.setOut(new PrintStream(outContent));
        players = new ArrayList<>(Arrays.asList(
                new Player(0, "player1", null, 0, 200),
                new Player(1, "player2", null, 0, 200)
        ));
        monopolyGame = new MonopolyGame(players);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testGetPlayers() {
        assertEquals(1, monopolyGame.getCurrentRound());
        assertEquals(players.get(0), monopolyGame.getCurrentPlayer()); // reg cur
        assertEquals(players.get(1), monopolyGame.getNextPlayer()); // reg get next
        assertEquals(players.get(1), monopolyGame.nextPlayer()); // reg next
        assertEquals(players.get(1), monopolyGame.getCurrentPlayer());
        assertEquals(players.get(0), monopolyGame.getNextPlayer()); // loop get next
        assertEquals(players.get(0), monopolyGame.nextPlayer()); // loop next
        assertEquals(2, monopolyGame.getCurrentRound());
    }

    @Test
    public void testPlayerMove() {
        Player player = players.get(0);
        ArrayList<Square> board = monopolyGame.getBoard();
        monopolyGame.movePlayer(player, 2);
        assertEquals(board.get(player.getCurrentSquare()), board.get(2));
        assertFalse(monopolyGame.movePlayer(player, 2)); // pos 4
        assertTrue(monopolyGame.movePlayer(player, 39)); // move whole board, GO
        assertEquals(board.get(player.getCurrentSquare()), board.get(4)); // comes back around
    }

    @Test
    public void testCallbacks() {
        Player player = players.get(0);
        ArrayList<Square> board = monopolyGame.getBoard();

        board.get(2).landedOn(player);
        board.get(4).landedOn(player);
        board.get(7).landedOn(player);
        board.get(17).landedOn(player);
        board.get(22).landedOn(player);
        board.get(29).landedOn(player);
        board.get(32).landedOn(player);
        board.get(35).landedOn(player);
        board.get(37).landedOn(player);

        String[] outs = outContent.toString().split("\n");
        String[] checks = {
                "Community Chest", "Income Tax", "Chance",
                "Community Chest", "Chance", "Go to Jail",
                "Community Chest", "Chance", "Income Tax"
        };

        for (int i = 0; i < checks.length; i++) {
            assertEquals(checks[i], outs[i]);
        }
    }

}