package ui;

import com.googlecode.lanterna.TextColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Class that uses swing as renderer for old console rendering functions
public class ConsoleRenderer extends JPanel {

    private int charWidth;
    private int charHeight;

    private BufferedImage bufferedImage;
    private Graphics2D g2d;

    // EFFECTS: constructor initializes screen dimensions
    public ConsoleRenderer(int screenWidth, int screenHeight, int charWidth, int charHeight) {
        this.charHeight = charHeight;
        this.charWidth = charWidth;

        bufferedImage = new BufferedImage(screenWidth * charWidth, screenHeight * charHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        g2d = (Graphics2D) bufferedImage.getGraphics();
        Font font = new Font("Monospaced", Font.PLAIN, 16);
        g2d.setFont(font);
        addKeyListener(InputManager.getSingleton());
        setDoubleBuffered(true);
        setFocusable(true);
    }

    // MODIFIES: g
    // EFFECTS: sends buffered image to be painted
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);
    }

    // MODIFIES: this
    // EFFECTS: refresh screen
    public void refresh() {
        repaint();
    }

    // MODIFIES: this, g2d
    // EFFECTS: clear screen
    public void clear() {
//        g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g2d.setBackground(new Color(0, 0, 0, 255));
        g2d.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        repaint();

    }

    // MODIFIES: InputManager
    // EFFECTS: poll input manager
    int pollInput() {
        return InputManager.getSingleton().poll();
    }

    // MODIFIES: g2d
    // EFFECTS: draw string to screen
    public void putString(int x, int y, String string) {
        g2d.drawString(string, x * charWidth, y * charHeight);
    }

    // MODIFIES: g2d
    // EFFECTS: change drawing foreground color
    public void setForegroundColor(TextColor color) {
        if (color == null) {
            return;
        }
        g2d.setColor(color.toColor());
    }

}
