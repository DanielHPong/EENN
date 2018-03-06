package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

import com.danielhpong.EENN.Entity.FoodDirectionObject;

public class NeuralNet {

    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[][] layers;
    int INPUT_SIZE = 8;
    int OUTPUT_SIZE = 7;
    int HIDDEN_LAYER_DEPTH;
    int HIDDEN_LAYER_WIDTH;

    public NeuralNet(ArrayList<Double> sigTable, int HIDDEN_LAYER_DEPTH, int HIDDEN_LAYER_WIDTH) {
        sig = sigTable;
        this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];
        this.HIDDEN_LAYER_DEPTH = HIDDEN_LAYER_DEPTH;
        this.HIDDEN_LAYER_WIDTH = HIDDEN_LAYER_WIDTH;
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
    
    public NeuralNet(NeuralNet parent, Double mutationFactor, Double mutationChance) {
        sig = parent.sig;
        layers = parent.layers.clone();
        HIDDEN_LAYER_DEPTH = parent.HIDDEN_LAYER_DEPTH;
        HIDDEN_LAYER_WIDTH = parent.HIDDEN_LAYER_WIDTH;
        for (int i = 0; i < layers.length; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                if (rand.nextInt(2) == 1) {
                    for (int k = 0; k < layers[i][j].weights.length; k++) {
                        if (rand.nextInt(2) == 1) {
                            if (rand.nextInt(101) <= 100*mutationChance) {
                                layers[i][j].weights[k] += 2*(rand.nextDouble()-0.5)*mutationFactor;
                                int choice = rand.nextInt(2);
                                if (choice == 0) {
                                    layers[i][j].type = "sigmoid";
                                } else if (choice == 1) {
                                    layers[i][j].type = "linear";
                                }
                            } else {
                                layers[i][j].weights[k] += (rand.nextDouble()-0.5)*mutationFactor;
                            }
                        }
                    }
                }
                if (rand.nextInt(2) == 1) {
                    layers[i][j].bias += (rand.nextDouble()-0.5)*mutationFactor;
                }
            }
        }
        
        if (rand.nextInt(101) <= 100*mutationChance) {
            HIDDEN_LAYER_DEPTH++;
            NeuralNode[][] newLayers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];
            for (int i = 0; i < HIDDEN_LAYER_DEPTH; i++) {
                newLayers[i] = layers[i];
            }
            newLayers[HIDDEN_LAYER_DEPTH] = layers[rand.nextInt(layers.length)];
            for (int i = 0; i < newLayers[HIDDEN_LAYER_DEPTH].length; i++) {
                if (rand.nextInt(2) == 1) {
                    for (int k = 0; k < newLayers[HIDDEN_LAYER_DEPTH][i].weights.length; k++) {
                        if (rand.nextInt(2) == 1) {
                            newLayers[HIDDEN_LAYER_DEPTH][i].weights[k] += (rand.nextDouble()-0.5)*mutationFactor;
                        }
                    }
                }
                if (rand.nextInt(2) == 1) {
                    newLayers[HIDDEN_LAYER_DEPTH][i].bias += (rand.nextDouble()-0.5)*mutationFactor;
                }
            }
            newLayers[HIDDEN_LAYER_DEPTH+1] = layers[HIDDEN_LAYER_DEPTH];
            layers = newLayers;
        }
    }
    
    public NeuralNet(ArrayList<Double> sigTable, String type) {
        if (type.equalsIgnoreCase("1")) {
            sig = sigTable;
            this.layers = new NeuralNode[3][];
            this.HIDDEN_LAYER_DEPTH = 1;
            this.HIDDEN_LAYER_WIDTH = 8;
            NeuralNode[] input = new NeuralNode[INPUT_SIZE];
            for (int i = 0; i < input.length; i++) {
                input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
            }
            layers[0] = input;
            
            
            double[] w;
            NeuralNode[] layer1 = new NeuralNode[HIDDEN_LAYER_WIDTH];
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[4]=3.0;
            layer1[0] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[5]=3.0;
            layer1[1] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[6]=3.0;
            layer1[2] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[7]=3.0;
            layer1[3] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[2]=-1.0; w[3]=2.0;
            layer1[4] = new NeuralNode(sig, layers[0], w, -0.50, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[3]=5.0;
            layer1[5] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[2]=2.0; w[4]=0.25; w[5]=0.25; w[6]=0.25; w[7]=0.25;
            layer1[6] = new NeuralNode(sig, layers[0], w, -2.00, "linear");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            layer1[7] = new NeuralNode(sig, layers[0], w, 0.0, "sigmoid");
            
            layers[1] = layer1;
            
            
            NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[0]=1.0; w[5]=-0.50;
            output[0] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[1]=1.0; w[5]=-0.50;
            output[1] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[2]=1.0; w[5]=-0.50;
            output[2] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[3]=1.0; w[5]=-0.50;
            output[3] = new NeuralNode(sig, layers[1], w, -0.35, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[4]=1.0;
            output[4] = new NeuralNode(sig, layers[1], w, 0.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            output[5] = new NeuralNode(sig, layers[1], w, -100.0, "sigmoid");
            
            w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            w[6]=1.0;
            output[6] = new NeuralNode(sig, layers[1], w, 0.0, "linear");
            
            layers[HIDDEN_LAYER_DEPTH+1] = output;
        }
    }
    
    public int run(int x, int y, int food, int cellFood, 
            FoodDirectionObject nfood, FoodDirectionObject efood, FoodDirectionObject sfood, FoodDirectionObject wfood) {
        
        layers[0][0].value = (double) x / 128;
        layers[0][1].value = (double) y / 128;
        layers[0][2].value = (double) food / 100;
        layers[0][3].value = (double) cellFood / 100;
        layers[0][4].value = (double) nfood.food / (15*100);
        layers[0][5].value = (double) efood.food / (15*100);
        layers[0][6].value = (double) sfood.food / (15*100);
        layers[0][7].value = (double) wfood.food / (15*100);
        
        for (int i = 1; i <= HIDDEN_LAYER_DEPTH+1; i++) {
            for (int j = 0; j < layers[i].length; j++) {
                layers[i][j].calculateValue();
                //System.out.print(String.valueOf(layers[i][j].value) + " ");
            }
            //System.out.println("");
        }
        /*for (int j = 0; j < layers[layers.length-1].length; j++) {
            layers[layers.length-1][j].calculateValue();
            System.out.print(String.valueOf(layers[layers.length-1][j].value) + " ");
        }
        System.out.println("");*/
        
        int retval = 0;
        for (int i = 0; i < layers[HIDDEN_LAYER_DEPTH+1].length; i++) {
            if (layers[HIDDEN_LAYER_DEPTH+1][retval].value < layers[HIDDEN_LAYER_DEPTH+1][i].value) {
                retval = i;
            }
        }
        return retval+1;
    }
    
}
