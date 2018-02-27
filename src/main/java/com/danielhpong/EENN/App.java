package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App {
    
    static Random rand = new Random();
    static ArrayList<Double> sigTable = generateSigTable();
    public static Cell[][] map = initializeMap();
    static int tribes = 0;
    static int time = 0;
    
    public static void main( String[] args ) {
        while (true) {
            if (tribes < 20) {
                map[rand.nextInt(128)][rand.nextInt(128)].tribes.add(new Tribe(sigTable));
            }
            tickTribes();
            tickMap();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time++;
            System.out.println(" ");
            System.out.println(String.valueOf(time) + " : " + String.valueOf(tribes));
        }
    }
    
    private static ArrayList<Double> generateSigTable() {
        ArrayList<Double> sigTable = new ArrayList<Double>();
        for (int i = 0; i < 128; i++) {
            sigTable.add(i, 1 / (1 + Math.exp(-(i-64))));
        }
        return sigTable;
    }
    
    private static Cell[][] initializeMap() {
        Cell[][] map = new Cell[128][128];
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                map[i][j] = new Cell(rand);
            }
        }
        return map;
    }
    
    private static void tickMap() {
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                map[i][j].tick();;
            }
        }
    }
    
    private static void tickTribes() {
        for (int i = 0; i < tribes.size(); i++) {
            Tribe tribe = tribes.get(i);
            if (tribe.food <= 0) {
                tribes.remove(i);
            } else if (tribe.age >= 200) {
                tribes.remove(i);
            } else {
                tribe.tick(map);
            }
        }
    }
    
}
