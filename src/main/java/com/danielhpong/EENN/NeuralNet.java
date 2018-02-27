package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNet {

    Random rand = new Random();
    ArrayList<Double> sig;
    NeuralNode[] nodes1 = new NeuralNode[8];
    NeuralNode[] nodes2 = new NeuralNode[8];
    NeuralNode[] nodes3 = new NeuralNode[8];
    NeuralNode[] nodes4 = new NeuralNode[7];
    NeuralNode[] nodes5 = new NeuralNode[7];

    public NeuralNet(ArrayList<Double> sigTable) {
        sig = sigTable;
        for (int i = 0; i < 8; i++) {
            nodes1[i] = new NeuralNode(sig, 0, new NeuralNode[0]);
        }
        for (int i = 0; i < 8; i++) {
            nodes2[i] = new NeuralNode(sig, 0, nodes1);
        }
        for (int i = 0; i < 8; i++) {
            nodes3[i] = new NeuralNode(sig, 0, nodes2);
        }
        for (int i = 0; i < 7; i++) {
            nodes4[i] = new NeuralNode(sig, 0, nodes3);
        }
        for (int i = 0; i < 7; i++) {
            nodes5[i] = new NeuralNode(sig, 0, nodes4);
        }
    }
    
    public int run(int x, int y, int food, int cellFood, int nfood, int efood, int sfood, int wfood) {
        nodes1[0].value = (double) x / 128;
        nodes1[1].value = (double) y / 128;
        nodes1[2].value = (double) food / 100;
        nodes1[3].value = (double) cellFood / 100;
        nodes1[4].value = (double) nfood / 100;
        nodes1[5].value = (double) efood / 100;
        nodes1[6].value = (double) sfood / 100;
        nodes1[7].value = (double) wfood / 100;
        for (int i = 0; i < nodes1.length; i++) {
            //System.out.print(String.valueOf(nodes1[i].value) + " ");
        }
        //System.out.println(" ");
        for (int i = 0; i < nodes2.length; i++) {
            nodes2[i].calculateValue();
            //System.out.print(String.valueOf(nodes2[i].value) + " ");
        }
        //System.out.println(" ");
        for (int i = 0; i < nodes3.length; i++) {
            nodes3[i].calculateValue();
            //System.out.print(String.valueOf(nodes3[i].value) + " ");
        }
        //System.out.println(" ");
        for (int i = 0; i < nodes4.length; i++) {
            nodes4[i].calculateValue();
            //System.out.print(String.valueOf(nodes4[i].value) + " ");
        }
        for (int i = 0; i < nodes5.length; i++) {
            nodes5[i].calculateValue();
            //System.out.print(String.valueOf(nodes4[i].value) + " ");
        }
        //System.out.println(" ");
        //System.out.println(" ");

        int retval = 0;
        for (int i = 0; i < nodes5.length; i++) {
            if (nodes5[retval].value < nodes5[i].value) {
                retval = i;
            }
        }
        return retval+1;
    }
    
}
