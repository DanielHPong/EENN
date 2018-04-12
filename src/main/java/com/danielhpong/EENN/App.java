package com.danielhpong.EENN;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main class for the EENN simulator.
 *
 * @author daniel.le
 */
public class App {

    static Random rand = new Random();
    static ArrayList<Double> sigTable = generateSigTable();
    static int generations = 1;
    public static Cell[][] map = initializeMap();
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    static int entityCount = 0;
    static int time = 0;
    static int geneology = 0;
    public static Entity bestEntity = new Entity(sigTable, geneology++);

    /**
     * Spawns additional entities onto the map. Entities spawned are chosen
     * semi-randomly depending on "type" parameter. As time passes, fewer entities
     * with greater genetic variance are spawned. Restarts the simulation after 3000
     * cycles.
     *
     * @param type
     *            determins the preset entity type to spawn.
     */
    private static void prepareEntities(final String type) {
        if (time < 500) {
            // App.entities.add(new Entity(sigTable, geneology++));
            App.entities.add(new Entity(new NeuralNet(sigTable, type)));
        } else if (time < 1000 && time % 2 == 0) {
            // App.entities.add(new Entity(sigTable, geneology++));
            App.entities.add(new Entity(new NeuralNet(sigTable, type)));
        } else if (time < 1500 && entityCount < 100) {
            if (bestEntity != null) {
                if (rand.nextInt(2) == 0) {
                    App.entities.add(new Entity(bestEntity.net));
                } else {
                    // App.entities.add(new Entity(sigTable, geneology++));
                    App.entities.add(new Entity(new NeuralNet(sigTable, type)));
                }
            } else {
                final int choice = rand.nextInt(2);
                if (choice == 0) {
                    // App.entities.add(new Entity(sigTable, geneology++));
                    App.entities.add(new Entity(new NeuralNet(sigTable, type)));
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
                bestEntity.children--;
            }
        }
        if (time % 100 == 0) {
            for (int i = 0; i < 10; i++) {
                // App.entities.add(new Entity(sigTable, geneology++));
                App.entities.add(new Entity(new NeuralNet(sigTable, type)));
            }
        }
        if (time >= 3000 & entityCount < 100) {
            toFile(bestEntity, generations++);
            map = initializeMap();
            entities = new ArrayList<Entity>();
            geneology = 0;
            bestEntity = new Entity(sigTable, geneology++);
            entityCount = 0;
            time = 0;
        }
    }

    /**
     * Main method to run the simulation.
     *
     * @param args
     *            are console arguments and should not matter.
     * @throws IOException
     *             when the program cannot save the "best" entity to a log file.
     */
    public static void main(final String[] args) throws IOException {
        entityCount = 0;
        while (true) {
            prepareEntities("1");

            tickMap();
            tickEntities();
            /*
             * try { Thread.sleep(500); } catch (InterruptedException e) {
             * e.printStackTrace(); }
             */
            time++;
            System.out.println(" ");
            System.out.println(String.valueOf(time) + " : " + String.valueOf(entityCount));
        }
    }

    /**
     * Generates the 128 values between -64 and +64 for the sigmoid function
     * centered around 0 and incremented by 1 and places them in an array.
     *
     * @return
     */
    private static ArrayList<Double> generateSigTable() {
        final ArrayList<Double> sigTable = new ArrayList<Double>();
        for (int i = 0; i < 128; i++) {
            sigTable.add(i, 1 / (1 + Math.exp(-(i - 64))));
        }
        return sigTable;
    }

    /**
     * Creates an empty map for the simulation.
     *
     * @return a 128x128 2d array of Cell objects.
     */
    private static Cell[][] initializeMap() {
        final Cell[][] map = new Cell[128][128];
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                map[i][j] = new Cell();
            }
        }
        return map;
    }

    /**
     * Iterates through the globally stored map and updates all of the Cells.
     */
    private static void tickMap() {
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                map[i][j].tick();
            }
        }
    }

    /**
     * Iterates through the globally stored list of entities and updates them.
     */
    private static void tickEntities() {
        for (int i = 0; i < entities.size(); i++) {
            final Entity entity = entities.get(i);
            entity.tick();
            if (entity.food <= 0) {
                App.map[entity.x][entity.y].food += 20;
                App.entityCount--;
                App.entities.remove(i);
            }
            if (entity.age > (entity.children + 1) * 500) {
                App.map[entity.x][entity.y].food += 20;
                App.entityCount--;
                App.entities.remove(i);
            }
            if (bestEntity.age + (0.2 * bestEntity.age * bestEntity.children) < entity.age
                    + (0.2 * entity.age * entity.children)) {
                bestEntity = new Entity(entity);
                bestEntity.x = rand.nextInt(128);
                bestEntity.y = rand.nextInt(128);
                bestEntity.food = 100;
            }
        }
    }

    /**
     * Takes an entity and a generation object a writes them to a file.
     * 
     * @param entity
     *            is the entity whose genes we want to record.
     * @param generation
     *            is current number of simulations which have been simulated by a
     *            single run of the program.
     */
    private static void toFile(final Entity entity, final int generation) {
        final String path = "out/log.txt";
        String str = String.valueOf(generation) + " age:" + String.valueOf(entity.age) + " children:"
                + String.valueOf(entity.children) + " food:" + String.valueOf(entity.food) + "\n";
        for (int j = 1; j < entity.net.layers.length; j++) {
            for (final NeuralNode node : entity.net.layers[j]) {
                for (int i = 0; i < node.weights.length; i++) {
                    str += "w[" + i + "]=" + String.valueOf(node.weights[i]) + "; ";
                }
                str += "bias:" + String.valueOf(node.bias) + " type:" + node.type;
                str += "\n";
            }
            str += "\n";
        }
        str += "\n";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(str);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}