package com.petermurphy.snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    private final int backgroundWidth =600;
    private final int backgroundHeight = 600;
    private final int pixelSize = 10;
    private final int maxSnakePixels = 3600;
    private final int updateDelay = 140;
    private int snakePixels;
    private final int infoAreaHeight = 60;

    private final int randomPos = (backgroundHeight - infoAreaHeight) / pixelSize;

    private int level = 1;

    private final int[] snakeXPositions = new int[maxSnakePixels];
    private final int[] snakeYPositions = new int[maxSnakePixels];

    private int appleXPos;
    private int appleYPos;

    private int stPatXPos;
    private int stPatYPos;

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
        setBackground(Color.lightGray);
        setFocusable(true);

        setPreferredSize(new Dimension(backgroundWidth, backgroundHeight));
        initGame();
    }

    private void initGame() {
        snakePixels = 3;

        for (int z = 0; z < snakePixels; z++) {
            snakeXPositions[z] = 50 - z * 10;
            snakeYPositions[z] = (backgroundHeight - infoAreaHeight) / 2;
        }

        locateApple();
        locateStPat();

        timer = new Timer(updateDelay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (gameOn) {
            // Draw the apple.
            g.setColor(Color.red);
            g.fillRect(appleXPos, appleYPos, pixelSize, pixelSize);

            // Draw St Patrick.
            g.setColor(Color.blue);
            g.fillRect(stPatXPos, stPatYPos, pixelSize, pixelSize);

            // Draw the snake.
            for (int z = 0; z < snakePixels; z++) {
                if (z == 0) {
                    g.setColor(Color.green); // Snake's head
                } else {
                    g.setColor(Color.white); // Snake's body
                }
                g.fillRect(snakeXPositions[z], snakeYPositions[z], pixelSize, pixelSize);
            }

            // Draw the game info area.
            g.setColor(Color.gray);
            g.fillRect(0, 0, backgroundWidth, infoAreaHeight);

            // Display game info.
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            g.drawString("Level: " + level, 10, 15);
            g.drawString("Delay: " + updateDelay + " ms", 10, 30);
            int snakeSpeed = 1000 / updateDelay;
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
        g.drawString(msg, (backgroundWidth - metr.stringWidth(msg)) / 2, backgroundHeight / 2);
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
            snakeXPositions[0] -= pixelSize;
        }

        if (rightMove) {
            snakeXPositions[0] += pixelSize;
        }

        if (upMove) {
            snakeYPositions[0] -= pixelSize;
        }

        if (downMove) {
            snakeYPositions[0] += pixelSize;
        }
    }

    // Check collision with game boundaries.
    private void checkCollision() {
        for (int z = snakePixels; z > 0; z--) {

            if ((z > 3) && (snakeXPositions[0] == snakeXPositions[z]) && (snakeYPositions[0] == snakeYPositions[z])) {
                gameOn = false;
            }
        }

        if (snakeYPositions[0] >= backgroundHeight) {
            gameOn = false;
        }
        if (snakeYPositions[0] < infoAreaHeight) {
            gameOn = false;
        }
        if (snakeXPositions[0] >= backgroundWidth) {
            gameOn = false;
        }
        if (snakeXPositions[0] < 0) {
            gameOn = false;
        }

        if (!gameOn) {
            timer.stop();
        }
    }

    private void locateStPat() {
        int r = (int) (Math.random() * randomPos);
        stPatXPos = r * pixelSize;

        r = (int) (Math.random() * randomPos);
        stPatYPos = r * pixelSize + infoAreaHeight;
    }


    private void locateApple() {
        int r = (int) (Math.random() * randomPos);
        appleXPos = r * pixelSize;

        r = (int) (Math.random() * randomPos);
        appleYPos = r * pixelSize + infoAreaHeight;
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

