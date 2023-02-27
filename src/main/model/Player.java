package model;

import model.square.PropertySquare;

import java.util.ArrayList;

// defines data specific to individual players
public class Player {

    enum Color {
        RED,
        GREEN,
        BLUE,
        YELLOW
    }

    private int index;

    private String name;
    private Color color;
    private int currentSquare;

    private int balance;
    private ArrayList<PropertySquare> properties;

    // EFFECTS: constructor initializes position in play order, name, color, current square position, and balance
    public Player(int index, String name, Color color, int currentSquare, int balance) {
        this.index = index;
        this.name = name;
        this.color = color;
        this.currentSquare = currentSquare;
        this.balance = balance;
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
    }

    public int getBalance() {
        return balance;
    }

    // MODIFIES: this
    // EFFECTS: adds given value to balance, and returns new balance
    public int addBalance(int balance) {
        this.balance += balance;
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: removes given value to balance, and returns new balance
    public int removeBalance(int balance) {
        this.balance -= balance;
        return this.balance;
    }

}
