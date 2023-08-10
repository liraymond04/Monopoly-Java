package ui.renderer;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class LanternaRenderer extends IRenderer {
    private final Screen screen;
    private final TextGraphics textGraphics;

    public LanternaRenderer(int screenWidth, int screenHeight) throws IOException {
        setScreenWidth(screenWidth);
        setScreenHeight(screenHeight);

        TerminalSize terminalSize = new TerminalSize(screenWidth, screenHeight);
        screen = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize).createScreen();
        textGraphics = screen.newTextGraphics();

        screen.startScreen();
        screen.setCursorPosition(null); // turn off cursor
    }

    @Override
    public void putString(int x, int y, String string) {
        textGraphics.putString(x, y, string);
    }

    @Override
    public void refresh() throws IOException {
        screen.refresh();
    }

    @Override
    public void close() throws IOException {
        screen.close();
    }

    @Override
    public void clear() {
        screen.clear();
    }

    @Override
    public KeyStroke pollInput() throws IOException {
        return screen.pollInput();
    }

    @Override
    public void setForegroundColor(TextColor.ANSI ansi) {
        textGraphics.setForegroundColor(ansi);
    }

    @Override
    public void setForegroundColor(TextColor color) {
        textGraphics.setForegroundColor(color);
    }
}
