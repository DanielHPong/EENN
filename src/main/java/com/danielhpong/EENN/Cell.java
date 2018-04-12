package com.danielhpong.EENN;

import java.util.Random;

/**
 * Class for the cells are used to tile the map.
 *
 * @author daniel.le
 */
public class Cell {

    Random rand = new Random();
    int food;

    /**
     * Constructor to produce a new cell with food from 0 to 99 inclusive.
     */
    public Cell() {
        food = rand.nextInt(100);
    }

    /**
     * Updates the cell with new values.
     */
    public void tick() {
        if (food < 99) {
            if (rand.nextInt(2) == 1) {
                food = food + 1;
            }
        }
        if (food < 0) {
            food = 0;
        }
    }

}
