package model.square;

import model.Player;
import model.Square;

import java.util.concurrent.Callable;

// Squares that have no special behaviour when landed on
public class FreeSquare extends Square {

    // EFFECTS: constructor initializes the name of the square
    public FreeSquare(String name) {
        setName(name);
    }

    // EFFECTS: runs when player lands on the square
    @Override
    public void landedOn(Player player) {

    }

}
