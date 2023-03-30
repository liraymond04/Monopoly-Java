package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// class that manages input from the keyboard
public class InputManager implements KeyListener {

    private static InputManager singleton = new InputManager();

    private static final int KEY_COUNT = 256;

    private enum KeyState {
        RELEASED, // Not down
        PRESSED,  // Down, but not the first time
        ONCE      // Down for the first time
    }

    // Current state of the keyboard
    private boolean[] currentKeys = null;

    // Polled keyboard state
    private KeyState[] keys = null;

    // EFFECTS: constructs input manager
    private InputManager() {
        currentKeys = new boolean[ KEY_COUNT ];
        keys = new KeyState[ KEY_COUNT ];
        for (int i = 0; i < KEY_COUNT; i++) {
            keys[ i ] = KeyState.RELEASED;
        }
    }

    // EFFECTS: returns instance of input manager
    public static InputManager getSingleton() {
        return singleton;
    }

    // MODIFIES: this
    // EFFECTS: poll keyboard for input and update key states
    public int poll() {
        int pressed = -1;
        for (int i = 0; i < KEY_COUNT; i++)  {
            // Set the key state
            if (currentKeys[ i ]) {
                // If the key is down now, but was not
                // down last frame, set it to ONCE,
                // otherwise, set it to PRESSED
                if (keys[ i ] == KeyState.RELEASED) {
                    keys[i] = KeyState.ONCE;
                    pressed = i;
                } else {
                    keys[i] = KeyState.PRESSED;
                }
            } else {
                keys[ i ] = KeyState.RELEASED;
            }
        }
        return pressed;
    }

    // EFFECTS: return true if provided key is pressed down, false otherwise
    public boolean keyDown(int keyCode) {
        return keys[ keyCode ] == KeyState.ONCE || keys[ keyCode ] == KeyState.PRESSED;
    }

    // EFFECTS: return true if provided key is pressed down once, false otherwise
    public boolean keyDownOnce(int keyCode) {
        return keys[ keyCode ] == KeyState.ONCE;
    }

    // MODIFIES: this
    // EFFECTS: update key press states
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[ keyCode ] = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: update key release states
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >= 0 && keyCode < KEY_COUNT) {
            currentKeys[ keyCode ] = false;
        }
    }

    // EFFECTS: unused method but required implementation
    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }
}