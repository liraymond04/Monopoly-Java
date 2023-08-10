package ui;

// main application class
public class Main {
    public static void main(String[] args) throws Exception {
        Application.getInstance().init();
        Application.getInstance().start();
    }
}
