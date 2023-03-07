package model.square;

import com.googlecode.lanterna.TextColor;
import model.Player;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// tests for the PropertySquare class
public class PropertySquareTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private PropertySquare square;
    private PropertySquare squareRail;

    @BeforeEach
    public void before() {
        System.setOut(new PrintStream(outContent));
        square = new PropertySquare("test", PropertyType.DARK_BLUE, 50, 20,
                new ArrayList<>(Arrays.asList(
                        2, 5, 10, 20, 30
        )));
        squareRail = new PropertySquare("test", PropertyType.RAILROAD, 50, 20,
                new ArrayList<>(Arrays.asList(
                        2, 5, 10, 20, 30
                )));
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

        assertEquals(PropertyType.DARK_BLUE, square.getType());
        assertEquals(new TextColor.RGB(0, 0, 255), square.getType().toColor());
        assertEquals(PropertyType.RAILROAD, squareRail.getType());
        assertNull(squareRail.getType().toColor());

        assertFalse(square.isMortgaged());
        square.setMortgaged(true);
        assertTrue(square.isMortgaged());
    }

    @Test
    public void testTypeColors() {
        ArrayList<PropertySquare> propertySquares = new ArrayList<>(Arrays.asList(
                new PropertySquare("test", PropertyType.BROWN, 50, 20, null),
                new PropertySquare("test", PropertyType.LIGHT_BLUE, 50, 20, null),
                new PropertySquare("test", PropertyType.PINK, 50, 20, null),
                new PropertySquare("test", PropertyType.ORANGE, 50, 20, null),
                new PropertySquare("test", PropertyType.RED, 50, 20, null),
                new PropertySquare("test", PropertyType.YELLOW, 50, 20, null),
                new PropertySquare("test", PropertyType.GREEN, 50, 20, null)
        ));
        ArrayList<TextColor> checks = new ArrayList<>(Arrays.asList(
                new TextColor.RGB(204, 102, 0),
                new TextColor.RGB(0, 255, 255),
                new TextColor.RGB(255, 51, 255),
                new TextColor.RGB(255, 153, 51),
                new TextColor.RGB(255, 0, 0),
                new TextColor.RGB(255, 255, 0),
                new TextColor.RGB(0, 255, 0)
        ));

        int index = 0;
        for (PropertySquare propertySquare : propertySquares) {
            assertEquals(checks.get(index), propertySquare.getType().toColor());
            index++;
        }
    }

    @Test
    public void testSquarePlayers() {
        Set<Player> players = new HashSet<>();
        Player play1 = new Player(0, "play1", 0, 200);
        Player play2 = new Player(1, "play2", 0, 200);
        players.add(play1);
        players.add(play2);
        square.setOwnedBy(play2);

        for (Player player  : players) {
            square.addPlayer(player);
        }

        assertEquals(players, square.getPlayers());
        players.remove(play1);
        square.removePlayer(play1);
        assertEquals(players, square.getPlayers());
        assertEquals(play2, square.getOwnedBy());
    }

    @Test
    public void testLanding() {
        Player play1 = new Player(0, "play1", 0, 200);
        Player play2 = new Player(1, "play2", 0, 200);
        square.setOwnedBy(play1);
        square.landedOn(play2);
        assertEquals(0, square.getNumberOfHouses());
        square.addHouse();
        assertEquals(1, square.getNumberOfHouses());
        square.landedOn(play2);
        square.addHouse();
        square.addHouse();
        square.addHouse();
        assertEquals(4, square.getNumberOfHouses());
        square.addHouse();
        assertEquals(4, square.getNumberOfHouses());
        square.landedOn(play2);
        square.addHouse();
        square.landedOn(play2);
        square.removeHouse();
        assertEquals(3, square.getNumberOfHouses());
        square.landedOn(play2);
        square.removeHouse();
        square.removeHouse();
        square.removeHouse();
        assertEquals(0, square.getNumberOfHouses());
        square.landedOn(play2);
        square.removeHouse();
        assertEquals(0, square.getNumberOfHouses());
        square.landedOn(play2);

        String[] outs = outContent.toString().split("\n");
        String[] checks = {
                "play2 pays 2 to play1", "play2 pays 5 to play1",
                "play2 pays 30 to play1", "play2 pays 30 to play1",
                "play2 pays 20 to play1", "play2 pays 2 to play1",
                "play2 pays 2 to play1"
        };

        for (int i = 0; i < checks.length; i++) {
            assertEquals(checks[i], outs[i]);
        }
    }

    @Test
    public void testToJson() {
        JSONObject json = new JSONObject();
        int ownedBy;
        if (square.getOwnedBy() == null) {
            ownedBy = -1;
        } else {
            ownedBy = square.getOwnedBy().getIndex();
        }
        json.put("owned_by", ownedBy);
        json.put("number_of_houses", square.getNumberOfHouses());
        json.put("mortgaged", square.isMortgaged());
        assertEquals(square.toJson().toString(), json.toString());
    }

}
