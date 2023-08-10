package ui.renderer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

public abstract class IRenderer {
    private int screenWidth;
    private int screenHeight;

    int getScreenWidth() {
        return screenWidth;
    }

    int getScreenHeight() {
        return screenHeight;
    }

    void setScreenWidth(int val) {
        screenWidth = val;
    }

    void setScreenHeight(int val) {
        screenHeight = val;
    }

    public abstract void putString(int x, int y, String string);

    public abstract void refresh() throws IOException;

    public abstract void close() throws IOException;

    public abstract void clear();

    public abstract KeyStroke pollInput() throws IOException;

    public abstract void setForegroundColor(TextColor.ANSI ansi);

    public abstract void setForegroundColor(TextColor color);
}
