package model.square;

import model.Player;
import model.Square;

import java.util.ArrayList;

public class PropertySquare extends Square {

    private PropertyType type;

    private int price; // mortgage half of price, lift for 10% interest
    private int house; // sells for half price
    private ArrayList<Integer> rent; // owning colour set doubles rent with no houses

    private Player ownedBy;

    private int numberOfHouses = 0;
    private boolean mortgaged = false;

    public PropertySquare(String name, PropertyType type, int price, int house, ArrayList<Integer> rent) {
        setName(name);
        this.type = type;
        this.price = price;
        this.house = house;
        this.rent = rent;
    }

    @Override
    public void landedOn(Player player) {
        payRent(player);
    }

    public void payRent(Player player) {

    }

    public PropertyType getType() {
        return type;
    }

    public int getNumberOfHouses() {
        return numberOfHouses;
    }

    public int addHouse() {
        if (numberOfHouses < 5) {
            numberOfHouses++;
        }
        return numberOfHouses;
    }

    public int removeHouse() {
        if (numberOfHouses > 0) {
            numberOfHouses++;
        }
        return numberOfHouses;
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

}

