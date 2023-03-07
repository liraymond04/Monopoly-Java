package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

import model.MonopolyGame;
import model.Player;
import model.Square;
import model.square.PropertySquare;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MonopolyGame read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMonopolyGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses data from json to build MonopolyGame object
    private MonopolyGame parseMonopolyGame(JSONObject jsonObject) {
        ArrayList<Player> players = parsePlayers(jsonObject);
        int currentRound = jsonObject.getInt("current_round");
        int currentPlayer = jsonObject.getInt("current_player");
        boolean alreadyRolled = jsonObject.getBoolean(("already_rolled"));
        MonopolyGame monopolyGame = new MonopolyGame(players);
        monopolyGame.setCurrentRound(currentRound);
        monopolyGame.setCurrentPlayer(currentPlayer);
        monopolyGame.setAlreadyRolled(alreadyRolled);
        parseBoard(jsonObject, monopolyGame.getBoard(), players);
        // set current squares
        for (Player player : players) {
            int currentSquare = player.getCurrentSquare();
            monopolyGame.getBoard().get(currentSquare).addPlayer(player);
        }
        return monopolyGame;
    }

    // EFFECTS: parses data from json to build list of players
    private ArrayList<Player> parsePlayers(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("players");
        ArrayList<Player> players = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject next = (JSONObject) json;
            Player player = new Player(
                    next.getInt("index"),
                    next.getString("name"),
                    next.getInt("current_square"),
                    next.getInt("balance"));
            player.setProperties(parsePlayerProperty(next));
            players.add(player);
        }
        return players;
    }

    // EFFECTS: parses properties data from player
    private ArrayList<Integer> parsePlayerProperty(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("properties");
        ArrayList<Integer> properties = new ArrayList<>();
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            properties.add((Integer) iterator.next());
        }
        return properties;
    }

    // MODIFIES: board
    // EFFECTS: parses board data from save and updates board
    private void parseBoard(JSONObject jsonObject, ArrayList<Square> board, ArrayList<Player> players) {
        JSONArray jsonArray = jsonObject.getJSONArray("board");
        for (Object json : jsonArray) {
            JSONObject cur = (JSONObject) json;
            int index = cur.getInt("index");
            int ownedBy = cur.getInt("owned_by");
            int numberOfHouses = cur.getInt("number_of_houses");
            boolean mortgaged = cur.getBoolean("mortgaged");
            PropertySquare square = (PropertySquare) board.get(index);
            if (ownedBy != -1) {
                square.setOwnedBy(players.get(ownedBy));
            }
            for (int i = 0; i < numberOfHouses; i++) {
                square.addHouse();
            }
            square.setMortgaged(mortgaged);
        }
    }

}
