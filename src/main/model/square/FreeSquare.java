package model.square;

import model.Player;
import model.Square;

import java.util.concurrent.Callable;

public class FreeSquare extends Square {

    Callable<Void> callOnLand;

    public FreeSquare(String name, Callable<Void> callable) {
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
