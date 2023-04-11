package ui;

import java.io.IOException;

// interface for application scenes
public interface Scene {
    boolean handleInput() throws IOException;

    boolean update();

    boolean render();
}
