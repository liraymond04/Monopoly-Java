package persistence;


import model.MonopolyGame;
import model.Player;
import model.Square;
import model.square.PropertySquare;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// tests for json reader
class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MonopolyGame monopolyGame = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMonopolyGame() {
        JsonReader reader = new JsonReader("./data/tests/testEmptyMonopolyGame.json");
        try {
            MonopolyGame monopolyGame = reader.read();
            assertEquals(1, monopolyGame.getCurrentRound());
            assertEquals(0, monopolyGame.getCurrentPlayer().getIndex());
            assertFalse(monopolyGame.isAlreadyRolled());
            ArrayList<Square> initBoard = MonopolyGame.initBoard();
            assertTrue(areBoardsEqual(initBoard, monopolyGame.getBoard()));
            Player play1 = new Player(0, "play1", 0, 200);
            Player play2 = new Player(1, "play2", 0, 200);
            ArrayList<Player> players = new ArrayList<>(Arrays.asList(play1, play2));
            assertTrue(arePlayersEqual(players, monopolyGame.getPlayers()));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderSavedMonopolyGame() {
        JsonReader reader = new JsonReader("./data/tests/testSavedMonopolyGame.json");
        try {
            MonopolyGame monopolyGame = reader.read();
            assertEquals(9, monopolyGame.getCurrentRound());
            assertEquals(2, monopolyGame.getCurrentPlayer().getIndex());
            Player play1 = new Player(0, "play1", 3, 200);
            play1.addProperty(5);
            Player play2 = new Player(1, "play2", 20, 415);
            play2.addProperty(1);
            play2.addProperty(3);
            Player play3 = new Player(2, "play3", 5, 50);
            play3.addProperty(14);
            play3.addProperty(15);
            ArrayList<Player> players = new ArrayList<>(Arrays.asList(play1, play2, play3));
            assertTrue(arePlayersEqual(players, monopolyGame.getPlayers()));
            ArrayList<Square> initBoard = MonopolyGame.initBoard();
            PropertySquare square = (PropertySquare) initBoard.get(5);
            square.setOwnedBy(play1);
            square.setMortgaged(true);
            square = (PropertySquare) initBoard.get(1);
            square.setOwnedBy(play2);
            square = (PropertySquare) initBoard.get(3);
            square.setOwnedBy(play2);
            square.addHouse();
            square.addHouse();
            square = (PropertySquare) initBoard.get(14);
            square.setOwnedBy(play3);
            square.setMortgaged(true);
            square = (PropertySquare) initBoard.get(15);
            square.setOwnedBy(play3);
            assertTrue(areBoardsEqual(initBoard, monopolyGame.getBoard()));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    // REQUIRES: prop1 != null && prop2 != null
    // EFFECTS: check if two properties have the same values
    boolean arePropertiesEqual(ArrayList<Integer> prop1, ArrayList<Integer> prop2) {
        if (prop1.size() != prop2.size()) {
            return false;
        }
        for (int i = 0; i < prop1.size(); i++) {
            if (prop1.get(i) != prop2.get(i)) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: check if two players have the same values
    boolean arePlayerEqual(Player player1, Player player2) {
        if (player1 == null && player2 == null) {
            return true;
        } else if (player1 == null && player2 != null) {
            return false;
        } else if (player1 != null && player2 == null) {
            return false;
        }
        boolean result = player1.getName().equals(player2.getName());
        result &= arePropertiesEqual(player1.getProperties(), player2.getProperties());
        result &= player1.getIndex() == player2.getIndex();
        result &=player1.getCurrentSquare() == player2.getCurrentSquare();
        result &= player1.getBalance() == player2.getBalance();
        return result;
    }

    // REQUIRES: players1 != null && players2 != null
    // EFFECTS: check if two player lists have the same values
    boolean arePlayersEqual(ArrayList<Player> players1, ArrayList<Player> players2) {
        if (players1.size() != players2.size()) {
            return false;
        }
        for (int i = 0; i < players1.size(); i++) {
            if (!arePlayerEqual(players1.get(i), players2.get(i))) {
                return false;
            }
        }
        return true;
    }

    // REQUIRES: board1 != null && board2 != null
    // EFFECTS: checks if two provided boards have the same values
    boolean areBoardsEqual(ArrayList<Square> board1, ArrayList<Square> board2) {
        for (int i = 0; i < board1.size(); i++) {
            boolean result = true;
            Square square1 = board1.get(i);
            Square square2 = board2.get(i);
            result &= square1.getName() == square2.getName();
            if (square1 instanceof PropertySquare) {
                PropertySquare propertySquare1 = (PropertySquare) square1;
                PropertySquare propertySquare2 = (PropertySquare) square2;
                result &= (propertySquare1.getNumberOfHouses() == propertySquare2.getNumberOfHouses())
                        && (propertySquare1.isMortgaged() == propertySquare2.isMortgaged())
                        && arePlayerEqual(propertySquare1.getOwnedBy(), propertySquare2.getOwnedBy());
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }

}