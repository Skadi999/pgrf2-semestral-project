package project;

import global.LwjglWindow;


public class App {
    public static void main(String[] args) {
        new LwjglWindow(800, 800, new Renderer(), false);
    }
}
