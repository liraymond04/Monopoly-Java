package ui;

import java.io.IOException;

// interface for application scenes
public interface Scene {
    boolean handleInput() throws Exception;

    boolean update();

    boolean render();
}
