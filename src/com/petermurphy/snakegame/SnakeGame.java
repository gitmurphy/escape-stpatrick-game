package com.petermurphy.snakegame;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        add(new GamePanel());
        setResizable(false);
        pack();

        setTitle("Escape St. Patrick");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame frame = new SnakeGame();
        frame.setVisible(true);
    }
}