package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class Entity {

    Random rand = new Random();
    NeuralNet net;
    int x, y;
    int food = 100;
    int age = 0;

    public Entity(ArrayList<Double> sigTable) {
        App.entities++;
        net = new NeuralNet(sigTable);
        x = rand.nextInt(128);
        y = rand.nextInt(128);
    }
    
    public void tick(Cell[][] map) {
        food = food-5;
        age++;
        int nfood = 0;
        int sfood = 0;
        int efood = 0;
        int wfood = 0;
        if (y < 127) {nfood = map[x][y+1].food;}
        if (y > 0) {sfood = map[x][y-1].food;}
        if (x < 127) {efood = map[x+1][y].food;}
        if (x > 0) {wfood = map[x-1][y].food;}
        int decision = net.run(x, y, food, map[x][y].food, 
                nfood, efood, sfood, wfood);
        switch (decision) {
            case 1: // GO NORTH
                System.out.print("N - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (y < 127) {
                    y++;
                }
                break;
            case 2: // GO EAST
                System.out.print("E - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (x < 127) {
                    x++;
                }
                break;
            case 3: // GO SOUTH
                System.out.print("S - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (y > 0) {
                    y--;
                }
                break;
            case 4: // GO WEST
                System.out.print("W - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (x > 0) {
                    x--;
                }
                break;
            case 5: // FARM
                if (App.map[x][y].food >= 10) {
                    App.map[x][y].food -= 10;
                    food += 10;
                }
                System.out.print("F - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            case 6: // RAID
                System.out.print("R - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            case 7: // BREED
                Entity child = createChild();
                map[x][y].entity.add(child);
                this.food = this.food / 2;
                System.out.print("B - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            default: // DO NOTHING
                System.out.print("DN - " + age + " - ");
                System.out.println(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
        }
    }
    
    private Entity createChild() {
        Entity child = new Entity(App.sigTable);
        child.net = this.net;
        child.food = this.food / 2;
        child.x = this.x;
        child.y = this.y;
        return child;
    }
    
}
