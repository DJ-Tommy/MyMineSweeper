import sweeper.GameWindow;

public class StartSweeper {
    private final static int WIDTH_SIZE = 40;
    private final static int HEIGHT_SIZE = 20;
    private final static int SIZE_BLOCK = 25;
    private static int MAX_BOMBS = 50;
    private final static String TITLE = "Java Sweeper";

    public static void main(String[] args) {
        if (MAX_BOMBS > WIDTH_SIZE * HEIGHT_SIZE / 6) {
            MAX_BOMBS = WIDTH_SIZE * HEIGHT_SIZE / 6;
        }
        new GameWindow(WIDTH_SIZE, HEIGHT_SIZE, SIZE_BLOCK, MAX_BOMBS, TITLE);
    }
}