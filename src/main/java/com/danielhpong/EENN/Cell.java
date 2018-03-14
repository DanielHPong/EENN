package com.danielhpong.EENN;

import java.util.Random;

public class Cell {

    Random rand = new Random();
    int food;

    public Cell() {
        food = rand.nextInt(100);
    }

    public void tick() {
        if (food < 99) {
            if (rand.nextInt(2) == 1) {
                food = food+1;
            }
        }
        if (food < 0) {
            food = 0;
        }
    }

}
