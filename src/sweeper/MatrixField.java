package sweeper;

public class MatrixField {
    private StatusUpperField[][] closedFieldMatrix; // статус 0 - открыто, 1 - закрыто, 2 - флаг бомбы,
    private int[][] openedFieldMatrix; // статус 0 - нет цифры, 1-8 количество бомб вокруг
    private final int WIDTH_SIZE, HEIGHT_SIZE;

    protected MatrixField(int WIDTH_SIZE, int HEIGHT_SIZE) {
        this.WIDTH_SIZE = WIDTH_SIZE;
        this.HEIGHT_SIZE = HEIGHT_SIZE;
        closedFieldMatrix = new StatusUpperField[WIDTH_SIZE][HEIGHT_SIZE];
        openedFieldMatrix = new int[WIDTH_SIZE][HEIGHT_SIZE];
        setStartField();
    }

    protected void setStartField() {
        for (int x = 0; x < WIDTH_SIZE; x++) {
            for (int y = 0; y < HEIGHT_SIZE; y++) {
                closedFieldMatrix[x][y] = StatusUpperField.CLOSED;
            }
        }
    }

    protected StatusUpperField getFieldMatrix(int x, int y) {
        return closedFieldMatrix[x][y];
    }

    protected void setFieldMatrix(int x, int y, StatusUpperField status) {
        closedFieldMatrix[x][y] = status;
    }

    protected int getOpenedField(int x, int y) {
        return openedFieldMatrix[x][y];
    }

    protected void setOpenedFieldMatrix(int x, int y, int number) {
        openedFieldMatrix[x][y] = number;
    }
}