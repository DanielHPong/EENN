package com.danielhpong.EENN;

import java.io.IOException;
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
    static int entities = 0;
    static int time = 0;
    static int geneology = 0;

    public static void main( String[] args ) throws IOException {
        while (true) {
            if (entities < 200) {
                map[rand.nextInt(128)][rand.nextInt(128)].entity.add(new Entity(sigTable, geneology++));
            }
            tickMap();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time++;
            System.out.println(" ");
            System.out.println(String.valueOf(time) + " : " + String.valueOf(entities));
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

}
