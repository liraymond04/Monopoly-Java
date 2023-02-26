package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class Square {

    private String name;

    private Set<Player> players = new HashSet<>();

    protected abstract void landedOn(Player player);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Set<Player> getPlayers() {
        return players;
    }

}
