package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class Cell {

    int food;
    ArrayList<Entity> entity = new ArrayList<Entity>();

    public Cell(Random rand) {
        food = rand.nextInt(50);
    }

    public void tick() {
        if (food < 99) {
            food++;
        }
        for (int i = 0; i < entity.size(); i++) {
            Entity cellentity = entity.get(i);
            if (cellentity.food <= 0) {
                App.entities--;
                entity.remove(i);
            } else if (cellentity.age >= 200) {
                App.entities--;
                entity.remove(i);
            } else {
                cellentity.tick(App.map);
            }
        }
    }

}
