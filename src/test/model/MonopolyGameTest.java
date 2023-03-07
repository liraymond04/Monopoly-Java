package model;

import model.square.PropertySquare;
import org.json.JSONArray;
import org.json.JSONObject;
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
                new Player(0, "player1", 0, 200),
                new Player(1, "player2", 0, 200)
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
        assertTrue(monopolyGame.movePlayer(player, 40)); // move whole board, GO
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
        board.get(30).landedOn(player);
        board.get(33).landedOn(player);
        board.get(36).landedOn(player);
        board.get(38).landedOn(player);

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

    @Test
    public void testToJson() {
        monopolyGame.setCurrentPlayer(1);
        monopolyGame.setCurrentRound(5);
        monopolyGame.setAlreadyRolled(true);
        PropertySquare propertySquare = (PropertySquare) monopolyGame.getBoard().get(5);
        monopolyGame.setPlayers(new ArrayList<>(Arrays.asList(
                new Player(0, "player1", 0, 200),
                new Player(1, "player2", 0, 200)
        )));
        propertySquare.setOwnedBy(monopolyGame.getPlayers().get(0));

        JSONObject json = new JSONObject();
        JSONArray playerJson = new JSONArray();
        for (Player player : monopolyGame.getPlayers()) {
            playerJson.put(player.toJson());
        }
        JSONArray boardJson = new JSONArray();
        for (int i = 0; i < monopolyGame.getBoard().size(); i++) {
            Square square = monopolyGame.getBoard().get(i);
            if (!(square instanceof PropertySquare)) {
                continue;
            }
            JSONObject property = ((PropertySquare) square).toJson();
            property.put("index", i);
            boardJson.put(property);
        }
        json.put("current_round", monopolyGame.getCurrentRound());
        json.put("current_player", monopolyGame.getCurrentPlayer().getIndex());
        json.put("already_rolled", monopolyGame.isAlreadyRolled());
        json.put("players", playerJson);
        json.put("board", boardJson);

        assertEquals(monopolyGame.toJson().toString(), json.toString());
    }


}