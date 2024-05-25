package com.petermurphy.snakegame;

public class StPatrick {
    private int patricksXPos;
    private int patricksYPos;

    public StPatrick(int patricksXPos, int patricksYPos) {
        this.patricksXPos = patricksXPos;
        this.patricksYPos = patricksYPos;
    }

    public int getPatricksXPos() {
        return patricksXPos;
    }

    public void setPatricksXPos(int patricksXPos) {
        this.patricksXPos = patricksXPos;
    }

    public int getPatricksYPos() {
        return patricksYPos;
    }

    public void setPatricksYPos(int patricksYPos) {
        this.patricksYPos = patricksYPos;
    }
}
