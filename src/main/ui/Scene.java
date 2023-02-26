package ui;

import java.io.IOException;

public interface Scene {
    boolean handleInput() throws IOException;

    boolean update();

    boolean render();
}
