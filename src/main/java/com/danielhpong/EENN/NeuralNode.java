package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNode {
    
    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[] parents;
    double[] weights;
    double bias;
    double value;
    String type;
    
    public NeuralNode(ArrayList<Double> sigTable, NeuralNode[] parents, int LAYER_WIDTH) {
        sig = sigTable;
        this.parents = parents;
        weights = new double[parents.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (rand.nextDouble()-0.5)*(64/LAYER_WIDTH);
        }
        bias = (rand.nextDouble()-0.5)*LAYER_WIDTH;
        int choice = rand.nextInt(2);
        if (choice == 0) {
            type = "sigmoid";
        } else if (choice == 1) {
            type = "linear";
        }
    }
    
    public NeuralNode(ArrayList<Double> sigTable, NeuralNode[] parents, double[] weights, double bias, String type) {
        this.sig = sigTable;
        this.parents = parents;
        this.weights = weights;
        this.bias = bias;
        this.type = type;
    }
    
    public void calculateValue() {
        double parentValue = 0;
        for (int i = 0; i < parents.length; i++) {
            parentValue += parents[i].value*weights[i];
        }
        parentValue += bias;
        if (this.type.equalsIgnoreCase("sigmoid")) {
            this.value = sigmoid(parentValue);
        } else if (this.type.equalsIgnoreCase("linear")) {
            this.value = linear(parentValue);
        }
    }

    public double sigmoid(double x1) {
        double x = x1 + 64;
        int low = (int) Math.floor(x);
        int high = (int) Math.ceil(x);
        if (high >= 128) {
            return 1.0;
        } else if (low < 0) {
            return 0.0;
        } else if (high == low) {
            return sig.get((int) x);
        } else {
            return sig.get(low)
                    + ((sig.get(high) - sig.get(low)) / (high - low)
                            *(x - low));
        }
    }
    
    public double linear(double x1) {
        return x1;
    }
    
}
