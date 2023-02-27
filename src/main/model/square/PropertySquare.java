package model.square;

import model.Player;
import model.Square;

import java.util.ArrayList;

// Squares that are designated as properties
public class PropertySquare extends Square {

    private PropertyType type;

    private int price; // mortgage half of price, lift for 10% interest
    private int house; // sells for half price
    private ArrayList<Integer> rent; // owning colour set doubles rent with no houses

    private Player ownedBy;

    private int numberOfHouses = 0;
    private boolean mortgaged = false;

    // EFFECTS: constructor initializes name, property type, price, house prices, and rent amounts
    public PropertySquare(String name, PropertyType type, int price, int house, ArrayList<Integer> rent) {
        setName(name);
        this.type = type;
        this.price = price;
        this.house = house;
        this.rent = rent;
    }

    // MODIFIES: this, player
    // EFFECTS: buy or auction if not owned, otherwise pay rent
    @Override
    public void landedOn(Player player) {
        payRent(player);
    }

    // REQUIRES: ownedBy != null
    // MODIFIES: this, player
    // EFFECTS: removes balance from landed player to owner of property
    public void payRent(Player player) {
        System.out.println(player.getName() + " pays " + rent.get(numberOfHouses) + " to " + ownedBy.getName());
    }

    public PropertyType getType() {
        return type;
    }

    public int getNumberOfHouses() {
        return numberOfHouses;
    }

    public Player getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Player player) {
        ownedBy = player;
    }

    // MODIFIES: this
    // EFFECTS: adds houses while number of houses is less than 4
    public int addHouse() {
        if (numberOfHouses < 4) {
            numberOfHouses++;
        }
        return numberOfHouses;
    }

    // MODIFIES: this
    // EFFECTS: removes houses while number of houses is greater than zero
    public int removeHouse() {
        if (numberOfHouses > 0) {
            numberOfHouses--;
        }
        return numberOfHouses;
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }

}

