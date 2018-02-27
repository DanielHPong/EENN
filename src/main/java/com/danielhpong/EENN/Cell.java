package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class Cell {

    int food;
    ArrayList<Tribe> tribes = new ArrayList<Tribe>();

    public Cell(Random rand) {
        food = rand.nextInt(50);
    }
    
    public void tick() {
        if (food < 99) {
            food++;
        }
        for (Tribe tribe : tribes) {
            tribe.tick(App.map);
        }
    }

}
