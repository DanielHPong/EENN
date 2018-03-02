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
            food = food+3;
        }
        for (int i = 0; i < entity.size(); i++) {
            Entity cellentity = entity.get(i);
            //if (cellentity.generation > App.bestEntity.generation && cellentity.age > App.bestEntity.age) {
                //App.bestEntity = cellentity;
            //}
            if (cellentity.food <= 0) {
                App.entities--;
                entity.remove(i);
                food+=20;
            } else if (cellentity.age >= 200) {
                App.entities--;
                entity.remove(i);
                food+=20;
            } else {
                cellentity.tick();
            }
        }
    }

}
