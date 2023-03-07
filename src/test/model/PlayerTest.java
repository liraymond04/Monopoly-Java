package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests for the Player class
public class PlayerTest {

    Player play1;
    Player play2;

    @BeforeEach
    public void before() {
        play1 = new Player(0, "play1", 0, 200);
        play2 = new Player(1, "play2", 2, 400);
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

    @Test
    public void testProperties() {
        ArrayList<Integer> properties = play1.getProperties();
        play1.addProperty(1);
        assertEquals(properties, new ArrayList<Integer>(Arrays.asList(1)));
        play1.addProperty(1);
        assertEquals(properties, new ArrayList<Integer>(Arrays.asList(1)));
        play1.addProperty(2);
        assertEquals(properties, new ArrayList<Integer>(Arrays.asList(1, 2)));
        play1.removeProperty(2);
        assertEquals(properties, new ArrayList<Integer>(Arrays.asList(1)));
        play1.removeProperty(3);
        assertEquals(properties, new ArrayList<Integer>(Arrays.asList(1)));
        play1.setProperties(new ArrayList<Integer>(Arrays.asList(3)));
        assertEquals(play1.getProperties(), new ArrayList<Integer>(Arrays.asList(3)));
    }

    @Test
    public void testToJson() {
        play1.addProperty(1);
        play1.addProperty(2);
        JSONObject json = new JSONObject();
        json.put("index", play1.getIndex());
        json.put("name", play1.getName());
        json.put("current_square", play1.getCurrentSquare());
        json.put("balance", play1.getBalance());
        JSONArray jsonArray = new JSONArray();
        for (Integer property : play1.getProperties()) {
            jsonArray.put(property);
        }
        json.put("properties", jsonArray);
        assertEquals(play1.toJson().toString(), json.toString());
    }

}
