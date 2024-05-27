package com.petermurphy.snakegame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    private static final int BACKGROUND_WIDTH = 600;
    private static final int INFO_HEIGHT = 60;
    private static final int BACKGROUND_HEIGHT = BACKGROUND_WIDTH + INFO_HEIGHT;
    private static final int PIXEL_SIZE = 10;
    private static final int MAX_SNAKE_PIXELS = 3600;
    private static final int UPDATE_DELAY = 100;
    private static final int RANDOM_POS = (BACKGROUND_HEIGHT - INFO_HEIGHT) / PIXEL_SIZE;

    private static int snakePixels;
    private int level = 1;

    private final int[] snakeXPositions = new int[MAX_SNAKE_PIXELS];
    private final int[] snakeYPositions = new int[MAX_SNAKE_PIXELS];
    private int appleXPos;
    private int appleYPos;
    private int stPatXPos;
    private int stPatYPos;

    private Polygon irelandShape;

    private boolean leftMove = false;
    private boolean rightMove = true;
    private boolean upMove = false;
    private boolean downMove = false;
    private boolean gameOn = true;

    private Timer timer;

    public GamePanel() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.cyan);
        setFocusable(true);
        irelandShape = IrelandPolygon.getIrelandPolygon();
        setPreferredSize(new Dimension(BACKGROUND_WIDTH, BACKGROUND_HEIGHT));
        initGame();
    }

    private void initGame() {
        snakePixels = 3;

        for (int z = 0; z < snakePixels; z++) {
            snakeXPositions[z] = 50 - z * 10;
            snakeYPositions[z] = (BACKGROUND_HEIGHT - INFO_HEIGHT) / 2;
        }

        locateApple();
        locateStPat();

        timer = new Timer(UPDATE_DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (gameOn) {
            // Draw Ireland shape
            g.setColor(Color.green);
            //g.drawPolygon(irelandShape);
            g.fillPolygon(irelandShape);

            // Draw the apple.
            g.setColor(Color.red);
            g.fillRect(appleXPos, appleYPos, PIXEL_SIZE, PIXEL_SIZE);

            // Draw St Patrick.
            g.setColor(Color.blue);
            g.fillRect(stPatXPos, stPatYPos, PIXEL_SIZE, PIXEL_SIZE);

            // Draw the snake.
            for (int z = 0; z < snakePixels; z++) {
                if (z == 0) {
                    g.setColor(Color.pink); // Snake's head
                } else {
                    g.setColor(Color.magenta); // Snake's body
                }
                g.fillRect(snakeXPositions[z], snakeYPositions[z], PIXEL_SIZE, PIXEL_SIZE);
            }

            // Draw the game info area.
            g.setColor(Color.gray);
            g.fillRect(0, 0, BACKGROUND_WIDTH, INFO_HEIGHT);

            // Display game info.
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            g.drawString("Level: " + level, 10, 15);
            g.drawString("Delay: " + UPDATE_DELAY + " ms", 10, 30);
            int snakeSpeed = 1000 / UPDATE_DELAY;
            g.drawString("Speed: " + snakeSpeed + " pixels/second", 10, 45);

            // Ensure all drawing and graphics operations are completed before the method is returned.
            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "You have been banished by St. Patrick";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BACKGROUND_WIDTH - metr.stringWidth(msg)) / 2, BACKGROUND_HEIGHT / 2);
    }

    private void checkApple() {
        if ((snakeXPositions[0] == appleXPos) && (snakeYPositions[0] == appleYPos)) {
            snakePixels++;
            level++;
            locateApple();
            locateStPat();
        }
    }

    private void checkStPat() {
        if ((snakeXPositions[0] == stPatXPos) && (snakeYPositions[0] == stPatYPos)) {
            snakePixels--;
            locateStPat();
        }
    }


    private void move() {
        for (int z = snakePixels; z > 0; z--) {
            snakeXPositions[z] = snakeXPositions[(z - 1)];
            snakeYPositions[z] = snakeYPositions[(z - 1)];
        }

        if (leftMove) {
            snakeXPositions[0] -= PIXEL_SIZE;
        }

        if (rightMove) {
            snakeXPositions[0] += PIXEL_SIZE;
        }

        if (upMove) {
            snakeYPositions[0] -= PIXEL_SIZE;
        }

        if (downMove) {
            snakeYPositions[0] += PIXEL_SIZE;
        }
    }

    // Check collision with game boundaries.
    private void checkCollision() {
        for (int z = snakePixels; z > 0; z--) {

            if ((z > 3) && (snakeXPositions[0] == snakeXPositions[z]) && (snakeYPositions[0] == snakeYPositions[z])) {
                gameOn = false;
            }
        }

        if (snakeYPositions[0] >= BACKGROUND_HEIGHT) {
            gameOn = false;
        }
        if (snakeYPositions[0] < INFO_HEIGHT) {
            gameOn = false;
        }
        if (snakeXPositions[0] >= BACKGROUND_WIDTH) {
            gameOn = false;
        }
        if (snakeXPositions[0] < 0) {
            gameOn = false;
        }
        if (!irelandShape.contains(snakeXPositions[0], snakeYPositions[0])) {
            gameOn = false;
        }

        if (!gameOn) {
            timer.stop();
        }
    }

    private void locateStPat() {
        do {
            int r = (int) (Math.random() * RANDOM_POS);
            stPatXPos = r * PIXEL_SIZE;

            r = (int) (Math.random() * RANDOM_POS);
            stPatYPos = r * PIXEL_SIZE + INFO_HEIGHT;
        } while (!irelandShape.contains(stPatXPos, stPatYPos));
    }


    private void locateApple() {
        do {
            int r = (int) (Math.random() * RANDOM_POS);
            appleXPos = r * PIXEL_SIZE;

            r = (int) (Math.random() * RANDOM_POS);
            appleYPos = r * PIXEL_SIZE + INFO_HEIGHT;
        } while (!irelandShape.contains(appleXPos, appleYPos));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOn) {
            checkApple();
            checkStPat();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightMove)) {
                leftMove = true;
                upMove = false;
                downMove = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftMove)) {
                rightMove = true;
                upMove = false;
                downMove = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downMove)) {
                upMove = true;
                rightMove = false;
                leftMove = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upMove)) {
                downMove = true;
                rightMove = false;
                leftMove = false;
            }
        }
    }
}

