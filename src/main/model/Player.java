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

    private int index;

    private String name;
    private Color color;
    private int currentSquare;

    private int balance;
    private ArrayList<PropertySquare> properties;

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

    public int addBalance(int balance) {
        this.balance += balance;
        return this.balance;
    }

    public int removeBalance(int balance) {
        this.balance -= balance;
        return this.balance;
    }

}
