package model;

import model.square.PropertySquare;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

// defines data specific to individual players
public class Player implements Writeable {

    private int index;

    private String name;
    private int currentSquare;

    private int balance;
    private ArrayList<Integer> properties; // save positions on board

    // EFFECTS: constructor initializes position in play order, name, color, current square position, and balance
    public Player(int index, String name, int currentSquare, int balance) {
        this.index = index;
        this.name = name;
        this.currentSquare = currentSquare;
        this.balance = balance;
        properties = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(int square) {
        currentSquare = square;
        EventLog.getInstance().logEvent(new Event("Set player " + getName() + " current square to " + currentSquare));
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<Integer> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Integer> properties) {
        this.properties = properties;
    }

    // MODIFIES: this
    // EFFECTS: adds position of property to properties if not already added
    public void addProperty(int position) {
        if (!properties.contains(position)) {
            properties.add(position);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes position of property to properties if it exists
    public void removeProperty(int position) {
        if (properties.contains(position)) {
            properties.remove((Integer) position);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds given value to balance, and returns new balance
    public int addBalance(int balance) {
        this.balance += balance;
        EventLog.getInstance().logEvent(new Event("Add " + balance + " to player " + getName() + " balance"
                + "\nPlayer balance now " + this.balance));
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: removes given value to balance, and returns new balance
    public int removeBalance(int balance) {
        this.balance -= balance;
        EventLog.getInstance().logEvent(new Event("Remove " + balance + " from player " + getName() + " balance"
                + "\nPlayer balance now " + this.balance));
        return this.balance;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("index", index);
        json.put("name", name);
        json.put("current_square", currentSquare);
        json.put("balance", balance);
        json.put("properties", propertiesToJson());
        return json;
    }

    // EFFECTS: serialize current properties to json array
    private JSONArray propertiesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Integer propertySquare : properties) {
            jsonArray.put(propertySquare);
        }

        return jsonArray;
    }

}
