package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class representing a node in a neural net.
 *
 * @author daniel.le
 */
public class NeuralNode {

    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[] parents;
    double[] weights;
    double bias;
    double value;
    String type;

    /**
     * Generates a neural node with random values.
     *
     * @param sigTable
     *            is the sigmoid table used to optimize the activation function.
     * @param parents
     *            is the array of Neural Nodes which make up the previous layer in a
     *            neural net.
     * @param LAYER_WIDTH
     *            is the width of the previous layer in a neural net.
     */
    public NeuralNode(final ArrayList<Double> sigTable, final NeuralNode[] parents, final int LAYER_WIDTH) {
        sig = sigTable;
        this.parents = parents;
        weights = new double[parents.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (rand.nextDouble() - 0.5) * (64 / LAYER_WIDTH);
        }
        bias = (rand.nextDouble() - 0.5) * LAYER_WIDTH;
        final int choice = rand.nextInt(2);
        if (choice == 0) {
            type = "sigmoid";
        } else if (choice == 1) {
            type = "linear";
        }
    }

    /**
     * Generates a neural node with specific values.
     *
     * @param sigTable
     *            is the sigmoid table used to optimize the activation function.
     * @param parents
     *            is the array of Neural Nodes which make up the previous layer in a
     *            neural net.
     * @param weights
     *            is an array of doubles to use when calculating activation.
     * @param bias
     *            is a double representing the bias when calculation activation.
     * @param type
     *            is the type of activation function to use.
     */
    public NeuralNode(final ArrayList<Double> sigTable, final NeuralNode[] parents, final double[] weights,
            final double bias, final String type) {
        this.sig = sigTable;
        this.parents = parents;
        this.weights = weights;
        this.bias = bias;
        this.type = type;
    }

    /**
     * Updates the value of a neural net based on its parents and parameters.
     */
    public void calculateValue() {
        double parentValue = 0;
        for (int i = 0; i < parents.length; i++) {
            parentValue += parents[i].value * weights[i];
        }
        parentValue += bias;
        if (this.type.equalsIgnoreCase("sigmoid")) {
            this.value = sigmoid(parentValue);
        } else if (this.type.equalsIgnoreCase("linear")) {
            this.value = linear(parentValue);
        }
    }

    /**
     * Returns a linear approximation of the sigmoid function based on a given
     * value.
     *
     * @param x1
     *            is a double to estimate the sigmoid of.
     * @return a double which is near the exact value of the sigmoid function for
     *         the input.
     */
    public double sigmoid(final double x1) {
        final double x = x1 + 64;
        final int low = (int) Math.floor(x);
        final int high = (int) Math.ceil(x);
        if (high >= 128) {
            return 1.0;
        } else if (low < 0) {
            return 0.0;
        } else if (high == low) {
            return sig.get((int) x);
        } else {
            return sig.get(low) + ((sig.get(high) - sig.get(low)) / (high - low) * (x - low));
        }
    }

    /**
     * Returns the exact value of the input.
     * 
     * @param x1
     *            is a double to take as an input.
     * @return a double which is exactly the same as the input.
     */
    public double linear(final double x1) {
        return x1;
    }

}
