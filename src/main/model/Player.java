package model;

import model.square.PropertySquare;

import java.util.ArrayList;

public class Player {

    enum Color {
        RED,
        GREEN,
        BLUE,
        YELLOW
    }

    private String name;
    private Color color;
    private int currentSquare;

    private int balance;
    private ArrayList<PropertySquare> properties;

    public Player(String name, Color color, int currentSquare, int balance) {
        this.name = name;
        this.color = color;
        this.currentSquare = currentSquare;
        this.balance = balance;
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

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
