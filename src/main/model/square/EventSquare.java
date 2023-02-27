package model.square;

import model.Player;
import model.Square;

import java.util.concurrent.Callable;

// Squares that activate events when landed on
public class EventSquare extends Square {

    Callable<Void> callOnLand;

    // EFFECTS: constructor initializes name and callback function
    public EventSquare(String name, Callable<Void> callable) {
        setName(name);
        callOnLand = callable;
    }

    // EFFECTS: runs callback function when player lands on event square
    @Override
    public void landedOn(Player player) {
        try {
            callOnLand.call();
        } catch (Exception e) {
            return;
        }
    }
}
