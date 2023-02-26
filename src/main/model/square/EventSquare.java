package model.square;

import model.Player;
import model.Square;

import java.util.concurrent.Callable;

public class EventSquare extends Square {

    Callable<Void> callOnLand;

    public EventSquare(String name, Callable<Void> callable) {
        setName(name);
        callOnLand = callable;
    }

    @Override
    public void landedOn(Player player) {
        try {
            callOnLand.call();
        } catch (Exception e) {
            return;
        }
    }
}
