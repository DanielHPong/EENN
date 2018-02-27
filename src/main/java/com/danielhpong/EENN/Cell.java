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
        for (int i = 0; i < tribes.size(); i++) {
            Tribe tribe = tribes.get(i);
            if (tribe.food <= 0) {
                tribes.remove(i);
            } else if (tribe.age >= 200) {
                tribes.remove(i);
            } else {
                tribe.tick(App.map);
            }
        }
    }

}
