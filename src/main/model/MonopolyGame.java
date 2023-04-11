package model;

import model.square.EventSquare;
import model.square.FreeSquare;
import model.square.PropertySquare;
import model.square.PropertyType;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;
import java.util.Arrays;

// handles main game logic
public class MonopolyGame implements Writeable {

    private int currentRound = 1;

    private int currentPlayer = 0;
    private ArrayList<Player> players;
    private boolean alreadyRolled = false;

    private ArrayList<Square> board;

    // MODIFIES: this, players
    // EFFECTS: constructor initializes board data and sets player positions
    public MonopolyGame(ArrayList<Player> players) {
        this.players = players;
        EventLog.getInstance().logEvent(new Event("Initialized " + players.size() + " game players"));

        // init board
        board = initBoard();
        EventLog.getInstance().logEvent(new Event("Initialized game board"));

        for (Player player : players) {
            board.get(player.getCurrentSquare()).addPlayer(player);
            EventLog.getInstance().logEvent(new Event(
                    "Assigned player " + player.getName() + " to square " + player.getCurrentSquare()
                            + ", balance " + player.getBalance()));
        }
    }

    // EFFECTS: creates empty board
    @SuppressWarnings("methodlength")
    public static ArrayList<Square> initBoard() {
        return new ArrayList<>(Arrays.asList(
                new FreeSquare("Go"),
                new PropertySquare("Mediterranean Avenue", PropertyType.BROWN, 60, 50,
                        new ArrayList<>(Arrays.asList(2, 10, 30, 90, 160, 250))),
                new EventSquare("Community Chest", () -> {
                    System.out.println("Community Chest");
                    return null;
                }),
                new PropertySquare("Baltic Avenue", PropertyType.BROWN, 60, 50,
                        new ArrayList<>(Arrays.asList(4, 20, 60, 180, 320, 450))),
                new EventSquare("Income Tax", () -> {
                    System.out.println("Income Tax");
                    return null;
                }),
                new PropertySquare("Reading Railroad", PropertyType.RAILROAD, 200, -1,
                        new ArrayList<>(Arrays.asList(25, 50, 100, 200))),
                new PropertySquare("Oriental Avenue", PropertyType.LIGHT_BLUE, 100, 50,
                        new ArrayList<>(Arrays.asList(6, 30, 90, 270, 400, 550))),
                new EventSquare("Chance", () -> {
                    System.out.println("Chance");
                    return null;
                }),
                new PropertySquare("Vermont Avenue", PropertyType.LIGHT_BLUE, 100, 50,
                        new ArrayList<>(Arrays.asList(6, 30, 90, 270, 400, 550))),
                new PropertySquare("Connecticut Avenue", PropertyType.LIGHT_BLUE, 120, 50,
                        new ArrayList<>(Arrays.asList(8, 40, 100, 300, 450, 600))),
                new FreeSquare("Jail"),
                new PropertySquare("St. Charles Place", PropertyType.PINK, 140, 100,
                        new ArrayList<>(Arrays.asList(10, 50, 150, 450, 625, 750))),
                new PropertySquare("Electric Company", PropertyType.UTILITY, 150, -1, null),
                new PropertySquare("States Avenue", PropertyType.PINK, 140, 100,
                        new ArrayList<>(Arrays.asList(10, 50, 150, 450, 625, 750))),
                new PropertySquare("Virginia Avenue", PropertyType.PINK, 160, 100,
                        new ArrayList<>(Arrays.asList(12, 60, 180, 500, 700, 900))),
                new PropertySquare("Pennsylvania Railroad", PropertyType.RAILROAD, 200, -1,
                        new ArrayList<>(Arrays.asList(25, 50, 100, 200))),
                new PropertySquare("St. James Place", PropertyType.ORANGE, 180, 100,
                        new ArrayList<>(Arrays.asList(14, 70, 200, 550, 750, 950))),
                new EventSquare("Community Chest", () -> {
                    System.out.println("Community Chest");
                    return null;
                }),
                new PropertySquare("Tennessee Avenue", PropertyType.ORANGE, 180, 100,
                        new ArrayList<>(Arrays.asList(14, 70, 200, 550, 750, 950))),
                new PropertySquare("New York Avenue", PropertyType.ORANGE, 200, 100,
                        new ArrayList<>(Arrays.asList(16, 80, 220, 600, 800, 1000))),
                new FreeSquare("Free Parking"),
                new PropertySquare("Kentucky Avenue", PropertyType.RED, 220, 150,
                        new ArrayList<>(Arrays.asList(18, 90, 250, 700, 875, 1050))),
                new EventSquare("Chance", () -> {
                    System.out.println("Chance");
                    return null;
                }),
                new PropertySquare("Indiana Avenue", PropertyType.RED, 220, 150,
                        new ArrayList<>(Arrays.asList(18, 90, 250, 700, 875, 1050))),
                new PropertySquare("Illinois Avenue", PropertyType.RED, 240, 150,
                        new ArrayList<>(Arrays.asList(20, 100, 300, 750, 925, 1100))),
                new PropertySquare("B. & O. Railroad", PropertyType.RAILROAD, 200, -1,
                        new ArrayList<>(Arrays.asList(25, 50, 100, 200))),
                new PropertySquare("Atlantic Avenue", PropertyType.YELLOW, 260, 150,
                        new ArrayList<>(Arrays.asList(22, 110, 330, 800, 975, 1150))),
                new PropertySquare("Ventnor Avenue", PropertyType.YELLOW, 260, 150,
                        new ArrayList<>(Arrays.asList(22, 110, 330, 800, 975, 1150))),
                new PropertySquare("Water Works", PropertyType.UTILITY, 150, -1, null),
                new PropertySquare("Marvin Gardens", PropertyType.YELLOW, 280, 150,
                        new ArrayList<>(Arrays.asList(24, 120, 360, 850, 1025, 1200))),
                new EventSquare("Go to Jail", () -> {
                    System.out.println("Go to Jail");
                    return null;
                }),
                new PropertySquare("Pacific Avenue", PropertyType.GREEN, 300, 200,
                        new ArrayList<>(Arrays.asList(26, 130, 390, 900, 1100, 1275))),
                new PropertySquare("North Carolina Avenue", PropertyType.GREEN, 300, 200,
                        new ArrayList<>(Arrays.asList(26, 130, 390, 900, 1100, 1275))),
                new EventSquare("Community Chest", () -> {
                    System.out.println("Community Chest");
                    return null;
                }),
                new PropertySquare("Pennsylvania Avenue", PropertyType.GREEN, 320, 200,
                        new ArrayList<>(Arrays.asList(28, 150, 450, 1000, 1200, 1400))),
                new PropertySquare("Short Line", PropertyType.RAILROAD, 200, -1,
                        new ArrayList<>(Arrays.asList(25, 50, 100, 200))),
                new EventSquare("Chance", () -> {
                    System.out.println("Chance");
                    return null;
                }),
                new PropertySquare("Park Place", PropertyType.DARK_BLUE, 350, 200,
                        new ArrayList<>(Arrays.asList(35, 175, 500, 1100, 1300, 1500))),
                new EventSquare("Income Tax", () -> {
                    System.out.println("Income Tax");
                    return null;
                }),
                new PropertySquare("Boardwalk", PropertyType.DARK_BLUE, 400, 200,
                        new ArrayList<>(Arrays.asList(50, 200, 600, 1400, 1700, 2000)))
        ));
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public boolean isAlreadyRolled() {
        return alreadyRolled;
    }

    public void setAlreadyRolled(boolean alreadyRolled) {
        this.alreadyRolled = alreadyRolled;
    }

    // EFFECTS: return the current player
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // EFFECTS: return the next player in the cycle
    public Player getNextPlayer() {
        return players.get((currentPlayer + 1) % players.size());
    }

    // MODIFIES: this
    // EFFECTS: move current player to the next player in the cycle
    public Player nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
        if (currentPlayer == 0) {
            currentRound++;
        }
        return getCurrentPlayer();
    }

    // MODIFIES: this, player
    // EFFECTS: move player given number of spaces, and return whether they pass GO
    public boolean movePlayer(Player player, int move) {
        int currentSquare = player.getCurrentSquare();
        board.get(currentSquare).removePlayer(player);
        int moveAmount = currentSquare + move;
        int newSquare = moveAmount % board.size();
        board.get(newSquare).addPlayer(player);
        EventLog.getInstance().logEvent(new Event("Move player " + player.getName() + " " + move + " spaces"));
        player.setCurrentSquare(newSquare);
        if (moveAmount > newSquare) {
            EventLog.getInstance().logEvent(new Event("Player " + player.getName() + " passed GO"));
        }
        return moveAmount > newSquare;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Square> getBoard() {
        return board;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("current_round", currentRound);
        json.put("current_player", currentPlayer);
        json.put("already_rolled", alreadyRolled);
        json.put("players", playersToJson());
        json.put("board", boardToJson());
        return json;
    }

    // EFFECTS: serialize list of players as JSON array
    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Player player : players) {
            jsonArray.put(player.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: serialize game board as JSON array
    private JSONArray boardToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < board.size(); i++) {
            Square square = board.get(i);
            if (!(square instanceof PropertySquare)) {
                continue;
            }
            JSONObject property = ((PropertySquare) square).toJson();
            property.put("index", i);
            jsonArray.put(property);
        }

        return jsonArray;
    }
}
