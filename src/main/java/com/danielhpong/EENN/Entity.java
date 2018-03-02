package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class Entity {

    Random rand = new Random();
    NeuralNet net;
    int x, y;
    int food = 100;
    int age = 0;
    int geneology;

    public Entity(ArrayList<Double> sigTable, int geneology) {
        App.entities++;
        //net = new NeuralNet(sigTable, rand.nextInt(14), rand.nextInt(14));
        net = new NeuralNet(sigTable, rand.nextInt(7), rand.nextInt(7));
        this.geneology = geneology;
        x = rand.nextInt(128);
        y = rand.nextInt(128);
    }
    
    public void tick() {
        food = food-5;
        age++;
        int nfood = 0;
        int sfood = 0;
        int efood = 0;
        int wfood = 0;
        if (y < 127) {nfood = App.map[x][y+1].food;}
        if (y > 0) {sfood = App.map[x][y-1].food;}
        if (x < 127) {efood = App.map[x+1][y].food;}
        if (x > 0) {wfood = App.map[x-1][y].food;}
        int decision = net.run(x, y, food, App.map[x][y].food, 
                nfood, efood, sfood, wfood);
        switch (decision) {
            case 1: // GO NORTH
                System.out.print("N - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (y < 127) {
                    y++;
                }
                break;
            case 2: // GO EAST
                System.out.print("E - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (x < 127) {
                    x++;
                }
                break;
            case 3: // GO SOUTH
                System.out.print("S - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (y > 0) {
                    y--;
                }
                break;
            case 4: // GO WEST
                System.out.print("W - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                if (x > 0) {
                    x--;
                }
                break;
            case 5: // FARM
                if (App.map[x][y].food >= 15) {
                    App.map[x][y].food -= 15;
                    food += 15;
                }
                System.out.print("F - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            case 6: // RAID
                System.out.print("R - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            case 7: // BREED
                reproduce();
                System.out.print("B - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
            default: // DO NOTHING
                System.out.print("DN - " + age + " - ");
                System.out.print(String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
                break;
        }
        System.out.println("    " + String.valueOf(geneology));
    }
    
    private Entity createChild() {
        Entity child = new Entity(App.sigTable, geneology);
        child.net = new NeuralNet(this.net, 1);
        child.food = this.food / 2;
        child.x = this.x;
        child.y = this.y;
        return child;
    }
    
    public void reproduce() {
        Entity child = createChild();
        int childx = x+1;
        int childy = y+1;
        if (childx > 127) {childx = 126;}
        if (childy > 127) {childy = 126;}
        App.map[childx][childy].entity.add(child);
        this.food = this.food / 2;
    }
    
}
