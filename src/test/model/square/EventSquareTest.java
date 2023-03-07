package model.square;

import model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests for the EventSquare class
public class EventSquareTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private EventSquare square;
    private EventSquare squareNoCallback;

    @BeforeEach
    public void before() {
        System.setOut(new PrintStream(outContent));
        square = new EventSquare("test", () -> {
            System.out.println("callback");
            return null;
        });
        squareNoCallback = new EventSquare("test", null);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSquare() {
        assertEquals("test", square.getName());
        square.setName("test1");
        assertEquals("test1", square.getName());
    }

    @Test
    public void testSquarePlayers() {
        Set<Player> players = new HashSet<>();
        Player play1 = new Player(0, "play1", 0, 200);
        Player play2 = new Player(1, "play2", 0, 200);
        players.add(play1);
        players.add(play2);

        for (Player player  : players) {
            square.addPlayer(player);
        }

        assertEquals(players, square.getPlayers());
        players.remove(play1);
        square.removePlayer(play1);
        assertEquals(players, square.getPlayers());
    }

    @Test
    public void testLanding() {
        Player play1 = new Player(0, "play1", 0, 200);
        square.landedOn(play1);
        squareNoCallback.landedOn(play1);

        String[] outs = outContent.toString().split("\n");
        String[] checks = {
                "callback", "No callback provided"
        };

        for (int i = 0; i < checks.length; i++) {
            assertEquals(checks[i], outs[i]);
        }
    }

}
