package model;

public abstract class Square {

    private String name;

    protected abstract void landedOn(Player player);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
