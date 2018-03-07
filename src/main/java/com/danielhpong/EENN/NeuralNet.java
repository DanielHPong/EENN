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
            this.HIDDEN_LAYER_DEPTH = 1;
            this.HIDDEN_LAYER_WIDTH = 8;
            this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];
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
        } else if (type.equalsIgnoreCase("2")) {
            sig = sigTable;
            this.HIDDEN_LAYER_DEPTH = 0;
            this.HIDDEN_LAYER_WIDTH = 8;
            this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];
            NeuralNode[] input = new NeuralNode[INPUT_SIZE];
            for (int i = 0; i < input.length; i++) {
                input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
            }
            layers[0] = input;
            
            
            double[] w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];
            
            w[0]=4.206843754176134; w[1]=2.175484824772645; w[2]=-1.3719489385995876; w[3]=-2.6861955921344536; w[4]=5.506602101969652; w[5]=2.011375118962671; w[6]=-3.720166387566133; w[7]=-1.8386203946684307;
            output[0] = new NeuralNode(sig, layers[0], w, 3.014716466377953, "sigmoid");
            
            w[0]=2.6039088283651437; w[1]=-2.564056689309962; w[2]=-1.9738088566447554; w[3]=2.5643862808856928; w[4]=4.045802651498017; w[5]=-5.060095669529928; w[6]=-4.673848424580508; w[7]=1.1910851112933132;
            output[1] = new NeuralNode(sig, layers[0], w, -4.237062295693943, "sigmoid");
            
            w[0]=1.5578702188813143; w[1]=-3.6892055019207475; w[2]=1.9087993128289211; w[3]=0.04200396186655908; w[4]=-1.5944880724035633; w[5]=1.7770741958810854; w[6]=4.91322928202431; w[7]=0.22873804123371627;
            output[2] = new NeuralNode(sig, layers[0], w, -1.0606049911263755, "sigmoid");
            
            w[0]=-1.4575816875085184; w[1]=-2.7303077653381083; w[2]=-3.237053506613622; w[3]=-2.2435778601599066; w[4]=-3.243841622938069; w[5]=0.5587803862124712; w[6]=3.2469856496662857; w[7]=-1.7031372019405524;
            output[3] = new NeuralNode(sig, layers[0], w, -3.4701365200726895, "linear");
            
            w[0]=-2.8187750338088686; w[1]=-0.19586082049798326; w[2]=2.7294062959263936; w[3]=3.217432522292676; w[4]=3.450640202136543; w[5]=5.059625060221028; w[6]=0.10738178700976403; w[7]=-3.8411034573630856;
            output[4] = new NeuralNode(sig, layers[0], w, 3.6771857525862766, "sigmoid");
            
            w[0]=1.9170311953594954; w[1]=-1.0455055310385268; w[2]=3.078143259277667; w[3]=3.943820956162547; w[4]=-5.9216551034414895; w[5]=4.043527252569913; w[6]=-3.309659892429141; w[7]=0.4945579513588152;
            output[5] = new NeuralNode(sig, layers[0], w, -0.19050619459145007, "linear");
            
            w[0]=2.361119435522575; w[1]=-2.003398396942029; w[2]=3.3771095269051608; w[3]=1.687113270840715; w[4]=-4.183278284762794; w[5]=2.1119720497467807; w[6]=3.623093583634132; w[7]=0.8007069054459608;
            output[6] = new NeuralNode(sig, layers[0], w, 0.33550678107182325, "sigmoid");
            
            layers[HIDDEN_LAYER_DEPTH+1] = output;
        } else if (type.equalsIgnoreCase("3")) {
            sig = sigTable;
            this.HIDDEN_LAYER_DEPTH = 0;
            this.HIDDEN_LAYER_WIDTH = 8;
            this.layers = new NeuralNode[HIDDEN_LAYER_DEPTH+2][];
            NeuralNode[] input = new NeuralNode[INPUT_SIZE];
            for (int i = 0; i < input.length; i++) {
                input[i] = new NeuralNode(sig, new NeuralNode[0], INPUT_SIZE);
            }
            layers[0] = input;
            
            
            double[] w = new double[8]; for (int i = 0; i < w.length; i++) { w[i] = 0.0; }
            NeuralNode[] output = new NeuralNode[OUTPUT_SIZE];
            
            w[0]=-4.5323420487261545; w[1]=-3.334766754988137; w[2]=2.744817727622904; w[3]=-3.8965381912838106; w[4]=0.9221931693574594; w[5]=4.379941546288282; w[6]=2.0419499887738666; w[7]=3.002579441530683;
            output[0] = new NeuralNode(sig, layers[0], w, -2.5269075312430664, "sigmoid");
            
            w[0]=0.7642263121580829; w[1]=-5.105083817571424; w[2]=2.6291407798264346; w[3]=3.308832632597443; w[4]=-1.2711333669178098; w[5]=-3.043849086374302; w[6]=-0.3629458745776372; w[7]=-5.463653278452279;
            output[1] = new NeuralNode(sig, layers[0], w, 1.7338198183230829, "linear");
            
            w[0]=-3.8451200054284342; w[1]=-2.7870706502416294; w[2]=0.8131224332470595; w[3]=1.748625104844392; w[4]=1.3735106658512155; w[5]=-1.2734841914015607; w[6]=1.8194066814482435; w[7]=-2.1922300870761884;
            output[2] = new NeuralNode(sig, layers[0], w, -3.8844048721160704, "sigmoid");
            
            w[0]=1.9646173466331986; w[1]=3.4178061057470366; w[2]=2.036262029978265; w[3]=-2.198948878732767; w[4]=2.7797360067849537; w[5]=2.3933430539133886; w[6]=2.226019379109082; w[7]=-6.106870907024871;
            output[3] = new NeuralNode(sig, layers[0], w, 1.4141366065485992, "linear");
            
            w[0]=2.2057092223340176; w[1]=-1.2710493567782328; w[2]=-0.49341687728981215; w[3]=5.265929046048289; w[4]=5.1444746941431125; w[5]=2.7969400034694436; w[6]=1.1767224121311952; w[7]=-3.566597783386654;
            output[4] = new NeuralNode(sig, layers[0], w, -1.3591361614362802, "sigmoid");
            
            w[0]=2.7270470552648285; w[1]=-5.462504861446398; w[2]=0.9992759359314171; w[3]=-2.0650065267353894; w[4]=-2.095085745531989; w[5]=0.26627688351621775; w[6]=-1.6440722607418434; w[7]=-1.0215425058925889;
            output[5] = new NeuralNode(sig, layers[0], w, -1.3065345849177024, "sigmoid");
            
            w[0]=4.532814482265659; w[1]=0.4359797187515787; w[2]=4.639509707603866; w[3]=0.8416849940973369; w[4]=-1.5391717824927293; w[5]=-0.23108863253788825; w[6]=-3.6630292452696986; w[7]=-2.7080229706717978;
            output[6] = new NeuralNode(sig, layers[0], w, 1.3892616028785791, "linear");
            
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
