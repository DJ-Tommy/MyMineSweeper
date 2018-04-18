package sweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JFrame {
    JPanel panelField;
    JLabel label;
    GameLogic gameLogic;
    private final int WIDTH_SIZE, HEIGHT_SIZE, SIZE_BLOCK, MAX_BOMBS;
    private final String TITLE;
    private Color[] COLOR_BOMBS = { Color.BLACK,
                                    Color.GREEN,
                                    Color.RED,
                                    Color.PINK,
                                    Color.ORANGE,
                                    Color.CYAN};
    private Color[] COLOR_NUMBERS = {   Color.BLACK,
                                        Color.RED,
                                        Color.MAGENTA,
                                        Color.ORANGE,
                                        Color.GREEN,
                                        Color.CYAN,
                                        Color.YELLOW,
                                        Color.PINK};

    public GameWindow(final int WIDTH_SIZE, final int HEIGHT_SIZE, final int SIZE_BLOCK, final int MAX_BOMBS, final String TITLE) {
        this.WIDTH_SIZE = WIDTH_SIZE;
        this.HEIGHT_SIZE = HEIGHT_SIZE;
        this.SIZE_BLOCK = SIZE_BLOCK;
        this.MAX_BOMBS = MAX_BOMBS;
        this.TITLE = TITLE;
        gameLogic = new GameLogic(WIDTH_SIZE, HEIGHT_SIZE, MAX_BOMBS, COLOR_BOMBS.length);
        initLabel();
        initGameField();
        initFrame();
    }

    private void changeLabel() {
        if (!gameLogic.ifGameOver()) {
            label.setText("Осталось найти мин: " + gameLogic.getCountOfBombs() + "  Осталось открыть: " + (gameLogic.getVolumeBoxOnField() - gameLogic.getOpenBoxOnTheField() - MAX_BOMBS));
        }
        if (gameLogic.ifGameOver()) {
            label.setText("Game Over");
        }
        if (!gameLogic.ifGameOver() && gameLogic.checkGameWin()) {
            label.setText("Ура. Вы победили!!!");
        }
    }

    private void initLabel() {
        label = new JLabel();
        changeLabel();
        add(label, BorderLayout.SOUTH);
    }

    private void initGameField() {
        panelField = new CanvasGameField();
        panelField.setPreferredSize(new Dimension(WIDTH_SIZE * SIZE_BLOCK + 1, HEIGHT_SIZE * SIZE_BLOCK + 2));
        panelField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelField.setBackground(Color.lightGray);
        panelField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = (int) (e.getX() / SIZE_BLOCK);
                int y = (int) (e.getY() / SIZE_BLOCK);
                if (e.getButton() == 3 && !gameLogic.ifGameOver() && !gameLogic.checkGameWin()) {
                    gameLogic.clickRightButton(x, y);
                }
                if (e.getButton() == 1 && !gameLogic.ifGameOver() && !gameLogic.checkGameWin()) {
                    gameLogic.clickLeftButton(x, y);
                }
                if (e.getButton() == 2) {
                    gameLogic.clickCenterButton(x, y);
                }
                changeLabel();
                panelField.repaint();
            }
        }); 
        add(panelField, BorderLayout.CENTER);
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setTitle(TITLE);
        pack();
        setLocationRelativeTo(null);
    }

    private class CanvasGameField extends JPanel {
        Font font_number = new Font("Times New Roman", 1, SIZE_BLOCK / 3 * 2);
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(font_number);
            for (int x = 0; x < WIDTH_SIZE; x++) {
                for (int y = 0; y < HEIGHT_SIZE; y++) {
                    if (gameLogic.getFieldMatrix(x, y) != StatusUpperField.OPENED) {
                        paintCloseBox(x, y, g);
                        if (gameLogic.getFieldMatrix(x, y) == StatusUpperField.FLAGGED) {
                            paintFlagOnField(x, y, g);
                        }
                    }
                    if (gameLogic.getFieldMatrix(x, y) == StatusUpperField.OPENED) {
                        paintOpenBox(x, y, g);
                        if (gameLogic.getBombMatrix(x, y) > 0) {
                            paintBombOnField(x, y, 0, g);
                            paintExplodedBombOnField(x, y, g);
                        }
                        if (gameLogic.getOpenedField(x, y) > 0) {
                            paintNumberOnField(x, y, g);
                        }
                    }
                }
            }
        }

        private void paintCloseBox(int x, int y, Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + SIZE_BLOCK - 1);
            g.drawLine(x * SIZE_BLOCK + 2, y * SIZE_BLOCK + SIZE_BLOCK - 1,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + SIZE_BLOCK - 1);
            g.setColor(Color.WHITE);
            g.drawLine(x * SIZE_BLOCK + 1, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + 1, y * SIZE_BLOCK + SIZE_BLOCK - 2);
            g.drawLine(x * SIZE_BLOCK + 1, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + 2);
        }

        private void paintOpenBox(int x, int y, Graphics g) {
            g.setColor(Color.WHITE);
            g.drawLine(x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + SIZE_BLOCK - 1);
            g.drawLine(x * SIZE_BLOCK + 2, y * SIZE_BLOCK + SIZE_BLOCK - 1,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + SIZE_BLOCK - 1);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(x * SIZE_BLOCK + 1, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + 1, y * SIZE_BLOCK + SIZE_BLOCK - 2);
            g.drawLine(x * SIZE_BLOCK + 1, y * SIZE_BLOCK + 2,
                    x * SIZE_BLOCK + SIZE_BLOCK - 2, y * SIZE_BLOCK + 2);
        }

        private void paintNumberOnField(int x, int y, Graphics g) {
            g.setColor(COLOR_NUMBERS[gameLogic.getOpenedField(x, y) - 1]);
            g.drawString(String.valueOf(gameLogic.getOpenedField(x, y)),
                    x * SIZE_BLOCK + SIZE_BLOCK / 3, (y + 1) * SIZE_BLOCK - SIZE_BLOCK / 4);
        }

        private void paintBombOnField(int x, int y, int COLOR_BOMB, Graphics g) {
            g.setColor(COLOR_BOMBS[COLOR_BOMB]);
            g.fillOval(x * SIZE_BLOCK + 6, y * SIZE_BLOCK + 6,
                    SIZE_BLOCK - 11, SIZE_BLOCK - 11);
        }

        private void paintFlagOnField(int x, int y, Graphics g) {
            int fx1 = x * SIZE_BLOCK + SIZE_BLOCK / 4;
                int fy1 = (y + 1) * SIZE_BLOCK - SIZE_BLOCK / 8 - 1;
            int fx2 = x * SIZE_BLOCK + SIZE_BLOCK / 4;
                int fy2 = y * SIZE_BLOCK + SIZE_BLOCK / 8 + 1;
            int fx3 = (x + 1) * SIZE_BLOCK - SIZE_BLOCK / 4;
                int fy3 = y * SIZE_BLOCK + SIZE_BLOCK / 8 * 3;
            int fx4 = x * SIZE_BLOCK + SIZE_BLOCK / 4;
                int fy4 = (y + 1) * SIZE_BLOCK - SIZE_BLOCK / 8 * 3;
            int[] fx = {fx2, fx3, fx4};
                int[] fy = {fy2, fy3, fy4};
            g.setColor(Color.RED);
            g.fillPolygon(fx, fy, 3);
            g.setColor(Color.BLACK);
            g.drawLine(fx1, fy1, fx2, fy2);
            g.drawLine(fx1 + 1, fy1, fx2 + 1, fy2);
        }

        private void paintExplodedBombOnField(int x, int y, Graphics g) {
            g.setColor(Color.RED);
            g.drawLine(x * SIZE_BLOCK + 4, y * SIZE_BLOCK + 4,
                    (x + 1) * SIZE_BLOCK - 4, (y + 1) * SIZE_BLOCK - 4);
            g.drawLine(x * SIZE_BLOCK + 5, y * SIZE_BLOCK + 4,
                    (x + 1) * SIZE_BLOCK - 3, (y + 1) * SIZE_BLOCK - 4);
            g.drawLine(x * SIZE_BLOCK + 3, y * SIZE_BLOCK + 4,
                    (x + 1) * SIZE_BLOCK - 5, (y + 1) * SIZE_BLOCK - 4);
            g.drawLine((x + 1) * SIZE_BLOCK - 4, y * SIZE_BLOCK + 4,
                    x * SIZE_BLOCK + 4, (y + 1) * SIZE_BLOCK - 4);
            g.drawLine((x + 1) * SIZE_BLOCK - 3, y * SIZE_BLOCK + 4,
                    x * SIZE_BLOCK + 5, (y + 1) * SIZE_BLOCK - 4);
            g.drawLine((x + 1) * SIZE_BLOCK - 5, y * SIZE_BLOCK + 4,
                    x * SIZE_BLOCK + 3, (y + 1) * SIZE_BLOCK - 4);
        }
    }
}