package model.square;

import com.googlecode.lanterna.TextColor;

public enum PropertyType {
    BROWN,
    LIGHT_BLUE,
    PINK,
    ORANGE,
    RED,
    YELLOW,
    GREEN,
    DARK_BLUE,
    RAILROAD,
    UTILITY;

    public TextColor toColor() {
        switch (this) {
            case BROWN:
                return new TextColor.RGB(204, 102, 0);
            case LIGHT_BLUE:
                return new TextColor.RGB(0, 255, 255);
            case PINK:
                return new TextColor.RGB(255, 51, 255);
            case ORANGE:
                return new TextColor.RGB(255, 153, 51);
            case RED:
                return new TextColor.RGB(255, 0, 0);
            case YELLOW:
                return new TextColor.RGB(255, 255, 0);
            case GREEN:
                return new TextColor.RGB(0, 255, 0);
            case DARK_BLUE:
                return new TextColor.RGB(0, 0, 255);
            default:
                return null;
        }
    }
}
