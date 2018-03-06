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
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    static int entityCount = 0;
    static int time = 0;
    static int geneology = 0;
    public static Entity bestEntity = new Entity(sigTable, geneology++);

    public static void main( String[] args ) throws IOException {
        entityCount = 0;
        while (true) {
            if (time < 500) {
                App.entities.add(new Entity(sigTable, geneology++));
                //App.entities.add(new Entity(new NeuralNet(sigTable, "1")));
            } else if (time < 1000 && time % 2 == 0) {
                App.entities.add(new Entity(sigTable, geneology++));
                //App.entities.add(new Entity(new NeuralNet(sigTable, "1")));
            } else if (time < 1500 && entityCount < 100) {
                if (bestEntity != null) {
                    int choice = rand.nextInt(2);
                    if (choice == 0) {
                        App.entities.add(new Entity(bestEntity.net));
                    } else if (choice == 1) {
                        App.entities.add(new Entity(sigTable, geneology++));
                        //App.entities.add(new Entity(new NeuralNet(sigTable, "1")));
                    } else {
                        App.entities.add(new Entity(sigTable, geneology++));
                    }
                } else {
                    int choice = rand.nextInt(2);
                    if (choice == 0) {
                        App.entities.add(new Entity(sigTable, geneology++));
                        //App.entities.add(new Entity(new NeuralNet(sigTable, "1")));
                    } else {
                        App.entities.add(new Entity(sigTable, geneology++));
                    }
                }
            } else if (time < 2000 && entityCount < 50) {
                App.entities.add(new Entity(bestEntity.net));
            }
            if (time % 500 == 0) {
                for (int i = 0; i < 5; i++) {
                    App.entities.add(new Entity(bestEntity.net));
                    bestEntity.reproduce(1);
                }
            }
            if (time % 100 == 0) {
                for (int i = 0; i < 10; i++) {
                    App.entities.add(new Entity(sigTable, geneology++));
                }
            }
            
            tickMap();
            tickEntities();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time++;
            System.out.println(" ");
            System.out.println(String.valueOf(time) + " : " + String.valueOf(entityCount));
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
                map[i][j] = new Cell();
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
    
    private static void tickEntities() {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.tick();
            if (entity.food <= 0) {
                App.map[entity.x][entity.y].food += 20;
                App.entityCount--;
                App.entities.remove(i);
            }
            if (entity.age > (entity.children+1)*500) {
                App.map[entity.x][entity.y].food += 20;
                App.entityCount--;
                App.entities.remove(i);
            }
            if (bestEntity.age+(0.2*bestEntity.age*bestEntity.children) < entity.age+(0.2*entity.age*entity.children)) {
                bestEntity = new Entity(entity.net);
                entityCount--;
            }
        }
    }

}