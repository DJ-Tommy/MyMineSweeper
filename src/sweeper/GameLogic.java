package sweeper;

public class GameLogic {
    private MatrixBomb matrixBomb;
    private MatrixField matrixField;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private final int WIDTH_SIZE, HEIGHT_SIZE, MAX_BOMBS, COLOR_BOMBS;
    private int countOfBombs, openBoxOnTheField, volumeBoxOnField;

    protected GameLogic(int WIDTH_SIZE, int HEIGHT_SIZE, int MAX_BOMBS, int COLOR_BOMBS) {
        this.WIDTH_SIZE = WIDTH_SIZE;
        this.HEIGHT_SIZE = HEIGHT_SIZE;
        this.MAX_BOMBS = MAX_BOMBS;
        this.COLOR_BOMBS = COLOR_BOMBS;
        matrixBomb = new MatrixBomb(WIDTH_SIZE, HEIGHT_SIZE);
        matrixField = new MatrixField(WIDTH_SIZE, HEIGHT_SIZE);
        matrixBomb.setStartBombs(MAX_BOMBS, COLOR_BOMBS);
        setStartCount();
        setEmptyFieldMatrix();
    }

    protected void clickRightButton(int x, int y) {
        if (matrixField.getFieldMatrix(x, y) == StatusUpperField.FLAGGED) {
            matrixField.setFieldMatrix(x, y, StatusUpperField.CLOSED);
            setCountOfBombs(1);
            return;
        }
        if (matrixField.getFieldMatrix(x, y) == StatusUpperField.CLOSED) {
            matrixField.setFieldMatrix(x, y, StatusUpperField.FLAGGED);
            setCountOfBombs(-1);
            return;
        }
    }

    protected void clickLeftButton(int x, int y) {
        if (matrixField.getFieldMatrix(x, y) == StatusUpperField.CLOSED) {
            if (matrixField.getOpenedField(x, y) == 0 && matrixBomb.getBombMatrix(x, y) == 0) {
                openArroundBlocks(x, y);
            }
            matrixField.setFieldMatrix(x, y,StatusUpperField.OPENED);
        }
        if (matrixField.getFieldMatrix(x, y) == StatusUpperField.OPENED && matrixField.getOpenedField(x, y) > 0 && checkAroundNumber(x, y)) {
            openArroundBlocks(x, y);
        }
        chekGameOver(x, y);
        checkGameWin();
    }

    protected void clickCenterButton(int x, int y) {
        matrixBomb.setStartBombs(MAX_BOMBS, COLOR_BOMBS);
        matrixField.setStartField();
        setStartCount();
        setEmptyFieldMatrix();
        gameOver = false;
        gameWin = false;
    }

    protected boolean ifGameOver() {
        return gameOver;
    }

    private boolean chekGameOver(int x, int y) {
        if (matrixField.getFieldMatrix(x, y) == StatusUpperField.OPENED && matrixBomb.getBombMatrix(x, y) > 0) {
            gameOver = true;
            for (int xx = 0; xx < WIDTH_SIZE; xx++) {
                for (int yy = 0; yy < HEIGHT_SIZE; yy++) {
                    if (matrixField.getFieldMatrix(xx, yy) != StatusUpperField.OPENED && matrixBomb.getBombMatrix(xx, yy) > 0) {
                        matrixField.setFieldMatrix(xx, yy, StatusUpperField.OPENED);
                    }
                }
            }
        }
        return gameOver;
    }

    private boolean checkAroundNumber(int x, int y) {
        int count = 0;
        for (int xx = x - 1; xx < x + 2; xx++) {
            for (int yy = y - 1; yy < y + 2; yy++) {
                if (xx >= 0 && xx < WIDTH_SIZE && yy >= 0 && yy < HEIGHT_SIZE &&
                        matrixField.getFieldMatrix(xx, yy) == StatusUpperField.FLAGGED) {
                    if (xx == x && yy == y) {
                        continue;
                    }
                    count++;
                }
            }
        }
        return matrixField.getOpenedField(x, y) == count;
    }

    private void openArroundBlocks(int x, int y) {
        for (int xx = x - 1; xx < x + 2; xx++) {
            for (int yy = y - 1; yy < y + 2; yy++) {
                if (xx >= 0 && xx < WIDTH_SIZE && yy >= 0 && yy < HEIGHT_SIZE &&
                        matrixField.getFieldMatrix(xx, yy) == StatusUpperField.CLOSED) {
                    if (xx == x && yy == y) {
                        continue;
                    }
                    matrixField.setFieldMatrix(xx, yy, StatusUpperField.OPENED);
                    chekGameOver(xx, yy);
                    if (matrixField.getOpenedField(xx, yy) == 0 && matrixBomb.getBombMatrix(xx, yy) == 0) {
                        openArroundBlocks(xx, yy);
                    }
                }
            }
        }
    }

    protected boolean checkGameWin() {
        int count = 0;
        for (int x = 0; x < WIDTH_SIZE; x++) {
            for (int y = 0; y < HEIGHT_SIZE; y++) {
                if (matrixField.getFieldMatrix(x, y) == StatusUpperField.OPENED) {
                    count++;
                }
            }
        }
        setOpenBoxOnTheField(count);
        if (count + MAX_BOMBS >= getVolumeBoxOnField()) {
            gameWin = true;
        }
        return gameWin;
    }

    protected int getCountOfBombs() {
        return countOfBombs;
    }

    private void setCountOfBombs(int i) {
        countOfBombs += i;
    }

    protected void setStartCount() {
        countOfBombs = MAX_BOMBS;
        volumeBoxOnField = WIDTH_SIZE * HEIGHT_SIZE;
        openBoxOnTheField = 0;
    }

    private void setOpenBoxOnTheField(int i) {
        openBoxOnTheField = i;
    }

    protected int getOpenBoxOnTheField() {
        return openBoxOnTheField;
    }

    protected int getVolumeBoxOnField() {
        return volumeBoxOnField;
    }

    private void setEmptyFieldMatrix() {
        for (int x = 0; x < WIDTH_SIZE; x++) {
            for (int y = 0; y < HEIGHT_SIZE; y++) {
                int count = 0;
                if (matrixBomb.getBombMatrix(x, y) == 0) {
                    for (int xx = x - 1; xx < x + 2; xx++) {
                        for (int yy = y - 1; yy < y + 2; yy++) {
                            if (xx >= 0 && xx < WIDTH_SIZE && yy >= 0 && yy < HEIGHT_SIZE) {
                                if (xx == x && yy == y) {
                                    continue;
                                }
                                if (matrixBomb.getBombMatrix(xx, yy) > 0) {
                                    count++;
                                }
                            }
                        }
                    }
                }
                matrixField.setOpenedFieldMatrix(x, y, count);
            }
        }
    }

    protected StatusUpperField getFieldMatrix(int x, int y) {
        return matrixField.getFieldMatrix(x, y);
    }

    protected int getOpenedField(int x, int y) {
        return matrixField.getOpenedField(x, y);
    }

    protected int getBombMatrix(int x, int y) {
        return matrixBomb.getBombMatrix(x, y);
    }
}