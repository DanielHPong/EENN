package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNet {

    int INPUT_SIZE = 8;
    int OUTPUT_SIZE = 7;
    int HIDDEN_LAYER_DEPTH = 10;
    int HIDDEN_LAYER_WIDTH = 7;
    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[][] layers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];

    public NeuralNet(ArrayList<Double> sigTable) {
        sig = sigTable;
        NeuralNode[] input = new NeuralNode[INPUT_SIZE];
        for (int i = 0; i < input.length; i++) {
            input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
        }
        layers[0] = input;
        for (int i = 1; i <= HIDDEN_LAYER_DEPTH; i++) {
            NeuralNode[] hidden = new NeuralNode[HIDDEN_LAYER_WIDTH];
            for (int j = 0; j < HIDDEN_LAYER_WIDTH; j++) {
                hidden[j] = new NeuralNode(sig, layers[i-1], HIDDEN_LAYER_WIDTH);
            }
            layers[i] = hidden;
        }
        NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];
        for (int i = 0; i < output.length; i++) {
            output[i] = new NeuralNode(sig, layers[HIDDEN_LAYER_DEPTH], OUTPUT_SIZE);
        }
        layers[HIDDEN_LAYER_DEPTH+1] = output;
    }
    
    public int run(int x, int y, int food, int cellFood, int nfood, int efood, int sfood, int wfood) {
        
        layers[0][0].value = (double) x / 128;
        layers[0][1].value = (double) y / 128;
        layers[0][2].value = (double) food / 100;
        layers[0][3].value = (double) cellFood / 100;
        layers[0][4].value = (double) nfood / 100;
        layers[0][5].value = (double) efood / 100;
        layers[0][6].value = (double) sfood / 100;
        layers[0][7].value = (double) wfood / 100;
        
        for (int i = 1; i <= HIDDEN_LAYER_DEPTH+1; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                layers[i][j].calculateValue();
            }
        }
        
        int retval = 0;
        for (int i = 0; i < layers[HIDDEN_LAYER_DEPTH+1].length; i++) {
            if (layers[HIDDEN_LAYER_DEPTH+1][retval].value < layers[HIDDEN_LAYER_DEPTH+1][i].value) {
                retval = i;
            }
        }
        return retval+1;
    }
    
}
