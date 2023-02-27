package model;

import model.square.FreeSquare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// tests for the Square class
public class SquareTest {

    private Square square;

    @BeforeEach
    public void before() {
        square = new FreeSquare("test");
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
        Player play1 = new Player(0, "play1", null, 0, 200);
        Player play2 = new Player(1, "play2", null, 0, 200);
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

}
