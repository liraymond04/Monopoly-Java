package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests for the Player class
public class PlayerTest {

    Player play1;
    Player play2;

    @BeforeEach
    public void before() {
        play1 = new Player(0, "play1", Player.Color.BLUE, 0, 200);
        play2 = new Player(1, "play2", Player.Color.RED, 2, 400);
    }

    @Test
    public void testPlayer() {
        assertEquals(0, play1.getIndex());
        assertEquals(1, play2.getIndex());
        assertEquals("play1", play1.getName());
        assertEquals("play2", play2.getName());
        assertEquals(0, play1.getCurrentSquare());
        assertEquals(2, play2.getCurrentSquare());
        play1.setCurrentSquare(1);
        assertEquals(1, play1.getCurrentSquare());
        assertEquals(200, play1.getBalance());
        play1.addBalance(50);
        assertEquals(250, play1.getBalance());
        play1.removeBalance(100);
        assertEquals(150, play1.getBalance());
    }

}
