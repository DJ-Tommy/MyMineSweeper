package sweeper;

public class MatrixBomb {
    private int[][] bombMatrix; // статус 0 - нет бомбы, 1 - есть бомба
    private final int WIDTH_SIZE, HEIGHT_SIZE;

    protected MatrixBomb(int WIDTH_SIZE, int HEIGHT_SIZE) {
        this.WIDTH_SIZE = WIDTH_SIZE;
        this.HEIGHT_SIZE = HEIGHT_SIZE;
        bombMatrix = new int[WIDTH_SIZE][HEIGHT_SIZE];
    }

    protected void setStartBombs(int MAX_BOMBS, int COLOR_BOMBS) {
        for (int x = 0; x < WIDTH_SIZE; x++) {
            for (int y = 0; y < HEIGHT_SIZE; y++) {
                bombMatrix[x][y] = 0;
            }
        }
        for (int i = 0; i < MAX_BOMBS; i++) {
            boolean check = true;
            while (check) {
                int x = (int) (Math.random() * WIDTH_SIZE);
                int y = (int) (Math.random() * HEIGHT_SIZE);
                int color = (int)(Math.random() * (COLOR_BOMBS)) + 1; // Задать разноцветные бомбы
                if (bombMatrix[x][y] == 0) {
                    bombMatrix[x][y] = color;
                    check = false;
                }
            }
        }
    }

    protected int getBombMatrix(int x, int y) {
        return bombMatrix[x][y];
    }
}