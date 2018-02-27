package com.danielhpong.EENN;

import java.util.Random;

public class Cell {

    int food;

    public Cell(Random rand) {
        food = rand.nextInt(50);
    }
    
    public void tick() {
        if (food < 99) {
            food++;
        }
    }

}
