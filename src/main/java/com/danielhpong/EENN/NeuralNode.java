package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNode {
    
    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[] children;
    NeuralNode[] parents;
    double[] weights;
    double bias;
    double value;
    
    public NeuralNode(ArrayList<Double> sigTable, NeuralNode[] children, int LAYER_WIDTH) {
        sig = sigTable;
        this.children = children;
        weights = new double[children.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (rand.nextDouble()-0.5)*(64/LAYER_WIDTH);
        }
        bias = rand.nextDouble();
    }
    
    public void calculateValue() {
        double childrenValue = 0;
        for (int i = 0; i < children.length; i++) {
            childrenValue += children[i].value*weights[i];
        }
        childrenValue += bias;
        this.value = sigmoid(childrenValue);
    }

    public double sigmoid(double x1) {
        double x = x1 + 64;
        int low = (int) Math.floor(x);
        int high = (int) Math.ceil(x);
        if (high == low) {
            return sig.get((int) x);
        } else if (x > 128) {
            return 1.0;
        } else if (x < 0) {
            return 0.0;
        } else {
            return sig.get(low)
                    + ((sig.get(high) - sig.get(low)) / (high - low)
                            *(x - low));
        }
    }
    
}
