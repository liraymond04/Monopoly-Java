package model;

import model.square.EventSquare;
import model.square.FreeSquare;
import model.square.PropertySquare;
import model.square.PropertyType;

import java.util.ArrayList;
import java.util.Arrays;

public class MonopolyGame {

    private int currentRound = 1;

    private int currentPlayer = 0;
    private ArrayList<Player> players;

    private ArrayList<Square> board;

    public MonopolyGame(ArrayList<Player> players) {
        this.players = players;

        // init board
        board = initBoard();

        for (Player player : players) {
            board.get(0).addPlayer(player);
            player.setCurrentSquare(0);
        }
    }

    @SuppressWarnings("methodlength")
    private ArrayList<Square> initBoard() {
        return new ArrayList<>(Arrays.asList(
                new FreeSquare("Go", null),
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
                new FreeSquare("Jail", null),
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
                new FreeSquare("Free Parking", null),
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
                new FreeSquare("Go to Jail", () -> {
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

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public Player nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
        if (currentPlayer == 0) {
            currentRound++;
        }
        return getCurrentPlayer();
    }

    public boolean movePlayer(Player player, int move) {
        int currentSquare = player.getCurrentSquare();
        board.get(currentSquare).removePlayer(player);
        int moveAmount = currentSquare + move;
        int newSquare = moveAmount % board.size();
        board.get(newSquare).addPlayer(player);
        player.setCurrentSquare(newSquare);
        return moveAmount > newSquare;
    }

    public String testLand(int index) {
        Square square = board.get(index);
        square.landedOn(null);
        if (square instanceof PropertySquare) {
            PropertySquare propertySquare = (PropertySquare) square;
            propertySquare.addHouse();
            return String.valueOf(propertySquare.getNumberOfHouses());
        }
        return square.getName();
    }

    public ArrayList<Square> getBoard() {
        return board;
    }

}
