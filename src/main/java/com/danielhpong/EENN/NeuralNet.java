package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

import com.danielhpong.EENN.Entity.FoodDirectionObject;

/**
 * Class representing the neural net that informs an Entity's decisions.
 *
 * @author daniel.le
 */
public class NeuralNet {

    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[][] layers;
    int INPUT_SIZE = 6;
    int OUTPUT_SIZE = 7;
    int HIDDEN_LAYER_DEPTH;
    int HIDDEN_LAYER_WIDTH;

    /**
     * Constructs a new neural net with random values.
     *
     * @param sigTable
     *            is the sigmoid table used to optimize the activation function.
     * @param HIDDEN_LAYER_DEPTH
     *            is the number of hidden layers to use.
     * @param HIDDEN_LAYER_WIDTH
     *            is the width of hidden layers to use.
     */
    public NeuralNet(final ArrayList<Double> sigTable, final int HIDDEN_LAYER_DEPTH, final int HIDDEN_LAYER_WIDTH) {
        sig = sigTable;
        this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH + 2][];
        this.HIDDEN_LAYER_DEPTH = HIDDEN_LAYER_DEPTH;
        this.HIDDEN_LAYER_WIDTH = HIDDEN_LAYER_WIDTH;
        final NeuralNode[] input = new NeuralNode[INPUT_SIZE];
        for (int i = 0; i < input.length; i++) {
            input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
        }
        layers[0] = input;
        for (int i = 1; i <= HIDDEN_LAYER_DEPTH; i++) {
            final NeuralNode[] hidden = new NeuralNode[HIDDEN_LAYER_WIDTH];
            for (int j = 0; j < HIDDEN_LAYER_WIDTH; j++) {
                hidden[j] = new NeuralNode(sig, layers[i - 1], HIDDEN_LAYER_WIDTH);
            }
            layers[i] = hidden;
        }
        final NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];
        for (int i = 0; i < output.length; i++) {
            output[i] = new NeuralNode(sig, layers[HIDDEN_LAYER_DEPTH], OUTPUT_SIZE);
        }
        layers[HIDDEN_LAYER_DEPTH + 1] = output;
    }

    /**
     * Constructs a neural net based on a preexisting neural net and mutated by
     * given mutation factors.
     *
     * @param parent
     *            is the neural net to base the new neural net off of.
     * @param mutationFactor
     *            is the magnitude (strength) of mutation to apply to the parent.
     * @param mutationChance
     *            is the chance (probability) to mutate weights/structure.
     */
    public NeuralNet(final NeuralNet parent, final Double mutationFactor, final Double mutationChance) {
        sig = parent.sig;
        layers = parent.layers.clone();
        HIDDEN_LAYER_DEPTH = parent.HIDDEN_LAYER_DEPTH;
        HIDDEN_LAYER_WIDTH = parent.HIDDEN_LAYER_WIDTH;
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                if (rand.nextInt(2) == 1) {
                    for (int k = 0; k < layers[i][j].weights.length; k++) {
                        if (rand.nextInt(2) == 1) {
                            if (rand.nextInt(101) <= 100 * mutationChance) {
                                layers[i][j].weights[k] += (rand.nextDouble() - 0.5) * mutationFactor;
                                final int choice = rand.nextInt(2);
                                if (choice == 0) {
                                    layers[i][j].type = "sigmoid";
                                } else if (choice == 1) {
                                    layers[i][j].type = "linear";
                                }
                            } else {
                                layers[i][j].weights[k] += (rand.nextDouble() - 0.5) * mutationFactor;
                            }
                        }
                    }
                }
                if (rand.nextInt(2) == 1) {
                    layers[i][j].bias += (rand.nextDouble() - 0.5) * mutationFactor;
                }
            }
        }

        if (rand.nextInt(101) <= 100 * mutationChance) {
            HIDDEN_LAYER_DEPTH++;
            final NeuralNode[][] newLayers = new NeuralNode[HIDDEN_LAYER_DEPTH + 2][];
            for (int i = 0; i < HIDDEN_LAYER_DEPTH; i++) {
                newLayers[i] = layers[i];
            }
            newLayers[HIDDEN_LAYER_DEPTH] = layers[rand.nextInt(layers.length)];
            for (int i = 0; i < newLayers[HIDDEN_LAYER_DEPTH].length; i++) {
                if (rand.nextInt(2) == 1) {
                    for (int k = 0; k < newLayers[HIDDEN_LAYER_DEPTH][i].weights.length; k++) {
                        if (rand.nextInt(2) == 1) {
                            newLayers[HIDDEN_LAYER_DEPTH][i].weights[k] += 2 * (rand.nextDouble() - 0.5)
                                    * mutationFactor;
                        }
                    }
                }
                if (rand.nextInt(2) == 1) {
                    newLayers[HIDDEN_LAYER_DEPTH][i].bias += 2 * (rand.nextDouble() - 0.5) * mutationFactor;
                }
            }
            newLayers[HIDDEN_LAYER_DEPTH + 1] = layers[HIDDEN_LAYER_DEPTH];
            layers = newLayers;
        }
    }

    /**
     * Constructes a neural net based on preset templates.
     *
     * @param sigTable
     *            is the sigmoid table used to optimize the activation function.
     * @param type
     *            is the type of neural net to create.
     */
    public NeuralNet(final ArrayList<Double> sigTable, final String type) {
        if (type.equalsIgnoreCase("1")) {
            sig = sigTable;
            this.HIDDEN_LAYER_DEPTH = 1;
            this.HIDDEN_LAYER_WIDTH = 8;
            this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH + 2][];
            final NeuralNode[] input = new NeuralNode[INPUT_SIZE];
            for (int i = 0; i < input.length; i++) {
                input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
            }
            layers[0] = input;

            double[] w;
            final NeuralNode[] layer1 = new NeuralNode[HIDDEN_LAYER_WIDTH];

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[2] = 3.0;
            layer1[0] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[3] = 3.0;
            layer1[1] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[4] = 3.0;
            layer1[2] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[5] = 3.0;
            layer1[3] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[0] = -1.0;
            w[1] = 2.0;
            layer1[4] = new NeuralNode(sig, layers[0], w, -0.50, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[1] = 5.0;
            layer1[5] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[0] = 2.0;
            w[2] = 0.25;
            w[3] = 0.25;
            w[4] = 0.25;
            w[5] = 0.25;
            layer1[6] = new NeuralNode(sig, layers[0], w, -2.00, "linear");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            layer1[7] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");

            layers[1] = layer1;

            final NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[0] = 1.0;
            w[5] = -0.50;
            output[0] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[1] = 1.0;
            w[5] = -0.50;
            output[1] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[2] = 1.0;
            w[5] = -0.50;
            output[2] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[3] = 1.0;
            w[5] = -0.50;
            output[3] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[4] = 1.0;
            output[4] = new NeuralNode(sig, layers[1], w, 0.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            output[5] = new NeuralNode(sig, layers[1], w, -100.0, "sigmoid");

            w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            w[6] = 1.0;
            output[6] = new NeuralNode(sig, layers[1], w, 0.0, "linear");

            layers[HIDDEN_LAYER_DEPTH + 1] = output;
        } else if (type.equalsIgnoreCase("2")) {
            sig = sigTable;
            this.HIDDEN_LAYER_DEPTH = 0;
            this.HIDDEN_LAYER_WIDTH = 8;
            this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH + 2][];
            final NeuralNode[] input = new NeuralNode[INPUT_SIZE];
            for (int i = 0; i < input.length; i++) {
                input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
            }
            layers[0] = input;

            final double[] w = new double[8];
            for (int i = 0; i < w.length; i++) {
                w[i] = 0.0;
            }
            final NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];

            w[0] = 1.4797114962749083;
            w[1] = 0.5622796647586965;
            w[2] = 0.5567927622154674;
            w[3] = -0.20334821409937198;
            w[4] = -1.61934326915859;
            w[5] = -5.098752932370131;
            output[0] = new NeuralNode(sig, layers[0], w, -1.2108682641018682, "linear");

            w[0] = 4.589707187227614;
            w[1] = -4.155403561235169;
            w[2] = -3.0600206900324;
            w[3] = -2.9744451275269816;
            w[4] = -2.33940485663701;
            w[5] = 2.0437561245410705;
            output[1] = new NeuralNode(sig, layers[0], w, 0.8594136421670034, "sigmoid");

            w[0] = 2.657583513595645;
            w[1] = 1.5746500899463785;
            w[2] = -3.3868412244212722;
            w[3] = -3.3384158258121124;
            w[4] = 3.3506806791345642;
            w[5] = -2.690185713719373;
            output[2] = new NeuralNode(sig, layers[0], w, -4.591962182897939, "sigmoid");

            w[0] = -1.4334719228333839;
            w[1] = -5.409089475079917;
            w[2] = -2.792674472425941;
            w[3] = 3.8675091001711674;
            w[4] = 3.3408975861119656;
            w[5] = 2.5771423062726897;
            output[3] = new NeuralNode(sig, layers[0], w, -0.7809008499488663, "linear");

            w[0] = 3.698498846698975;
            w[1] = 3.4107350012327835;
            w[2] = -0.902961044373252;
            w[3] = -0.3832313820573464;
            w[4] = -2.3120003841227534;
            w[5] = -3.631527406173725;
            output[4] = new NeuralNode(sig, layers[0], w, 1.784423058504056, "linear");

            w[0] = 1.1282471203035003;
            w[1] = 4.669487674382495;
            w[2] = -0.8460328151272064;
            w[3] = 3.1951857851055583;
            w[4] = -4.475147576855759;
            w[5] = -3.916517137622468;
            output[5] = new NeuralNode(sig, layers[0], w, -4.82245549533798, "sigmoid");

            w[0] = 3.3392213118942564;
            w[1] = -2.859084578590136;
            w[2] = 0.9733220572198829;
            w[3] = 2.062672445518953;
            w[4] = -3.0000022569126155;
            w[5] = -3.4250222976605276;
            output[6] = new NeuralNode(sig, layers[0], w, -2.800232129554234, "sigmoid");

            layers[HIDDEN_LAYER_DEPTH + 1] = output;
        }
    }

    /**
     * Uses the neural net to make a decision based on inputs.
     * 
     * @param x
     *            is the x position of the associated Entity.
     * @param y
     *            is the y position of the associated Entity.
     * @param food
     *            is the amount of food of the associated Entity.
     * @param cellFood
     *            is the amount of food in the Cell occupied by the associated
     *            Entity.
     * @param nfood
     *            is the amount of food north of the associated Entity.
     * @param efood
     *            is the amount of food east of the associated Entity.
     * @param sfood
     *            is the amount of food south of the associated Entity.
     * @param wfood
     *            is the amount of food west of the associated Entity.
     * @return
     */
    public int run(final int x, final int y, final int food, final int cellFood, final FoodDirectionObject nfood,
            final FoodDirectionObject efood, final FoodDirectionObject sfood, final FoodDirectionObject wfood) {

        layers[0][0].value = (double) food / 100;
        layers[0][1].value = (double) cellFood / 100;
        layers[0][2].value = (double) nfood.food / (15 * 100);
        layers[0][3].value = (double) efood.food / (15 * 100);
        layers[0][4].value = (double) sfood.food / (15 * 100);
        layers[0][5].value = (double) wfood.food / (15 * 100);

        for (int i = 1; i <= HIDDEN_LAYER_DEPTH + 1; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                layers[i][j].calculateValue();
                // System.out.print(String.valueOf(layers[i][j].value) + " ");
            }
            // System.out.println("");
        }
        /*
         * for (int j = 0; j < layers[layers.length-1].length; j++) {
         * layers[layers.length-1][j].calculateValue();
         * System.out.print(String.valueOf(layers[layers.length-1][j].value) + " "); }
         * System.out.println("");
         */

        int retval = 0;
        for (int i = 0; i < layers[HIDDEN_LAYER_DEPTH + 1].length; i++) {
            if (layers[HIDDEN_LAYER_DEPTH + 1][retval].value < layers[HIDDEN_LAYER_DEPTH + 1][i].value) {
                retval = i;
            }
        }
        return retval + 1;
    }

}
