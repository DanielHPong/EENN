package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Entity {

    UUID id;
    Random rand = new Random();
    NeuralNet net;
    int x, y;
    int food = 100;
    int age = 0;
    int geneology;
    int generation = 0;

    public Entity(ArrayList<Double> sigTable, int geneology) {
        id = UUID.randomUUID();
        App.entityCount++;
        //net = new NeuralNet(sigTable, rand.nextInt(14), rand.nextInt(14));
        net = new NeuralNet(sigTable, 21, 7);
        this.geneology = geneology;
        x = rand.nextInt(128);
        y = rand.nextInt(128);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Entity.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public void tick() {
        food = food-5;
        age++;
        FoodDirectionObject nfood = detectFood(0);
        FoodDirectionObject sfood = detectFood(1);
        FoodDirectionObject efood = detectFood(2);
        FoodDirectionObject wfood = detectFood(3);
        int decision = net.run(x, y, food, App.map[x][y].food, 
                nfood, efood, sfood, wfood);
        switch (decision) {
            case 1: // GO NORTH
                System.out.print("N");
                if (y < 127) {
                    y++;
                }
                break;
            case 2: // GO EAST
                System.out.print("E");
                if (x < 127) {
                    x++;
                }
                break;
            case 3: // GO SOUTH
                System.out.print("S");
                if (y > 0) {
                    y--;
                }
                break;
            case 4: // GO WEST
                System.out.print("W");
                if (x > 0) {
                    x--;
                }
                break;
            case 5: // FARM
                if (App.map[x][y].food >= 15) {
                    App.map[x][y].food -= 15;
                    food += 15;
                }
                System.out.print("F");
                break;
            case 6: // RAID
                System.out.print("R");
                break;
            case 7: // BREED
                if (food > 30) {
                    reproduce(1);
                }
                System.out.print("B");
                break;
            default: // DO NOTHING
                System.out.print("DN");
                break;
        }
        System.out.print("(" + String.valueOf(nfood.food) + "," + String.valueOf(efood.food) + "," + String.valueOf(sfood.food) + "," + String.valueOf(wfood.food) + ")");
        System.out.print(" - " + age + " - " + String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
        System.out.println("    " + String.valueOf(geneology) + "+" + String.valueOf(generation));
    }
    
    private Entity createChild(int gen) {
        Entity child = new Entity(App.sigTable, geneology);
        child.net = new NeuralNet(this.net, 2);
        child.food = 30;
        child.x = this.x;
        child.y = this.y;
        child.generation = gen;
        return child;
    }
    
    public void reproduce(int gen) {
        Entity child = createChild(this.generation+gen);
        /*int childx = x + 1;
        int childy = y + 1;
        if (childx > 127) {childx = 126;}
        if (childy > 127) {childy = 126;}*/
        child.x = rand.nextInt(128);
        child.y = rand.nextInt(128);
        App.entities.add(child);
        this.food = this.food - 30;
    }
    
    public FoodDirectionObject detectFood(int direction) {
        int output = 0;
        int cells = 0;
        if (direction == 0) {
            cells = 3;
            if (y < 127) {
                if (x > 0) {
                    output += App.map[x-1][y+1].food;
                }
                output += App.map[x][y+1].food;
                if (x < 127) {
                    output += App.map[x+1][y+1].food;
                }
                if (y < 126) {
                    cells = 8;
                    if (x > 1) {
                        output += App.map[x-2][y+2].food;
                    }
                    if (x > 0) {
                        output += App.map[x-1][y+2].food;
                    }
                    output += App.map[x][y+2].food;
                    if (x < 127) {
                        output += App.map[x+1][y+2].food;
                    }
                    if (x < 126) {
                        output += App.map[x+2][y+2].food;
                    }
                    if (y < 125) {
                        cells = 15;
                        if (x > 2) {
                            output += App.map[x-3][y+3].food;
                        }
                        if (x > 1) {
                            output += App.map[x-2][y+3].food;
                        }
                        if (x > 0) {
                            output += App.map[x-1][y+3].food;
                        }
                        output += App.map[x][y+3].food;
                        if (x < 127) {
                            output += App.map[x+1][y+3].food;
                        }
                        if (x < 126) {
                            output += App.map[x+2][y+3].food;
                        }
                        if (x < 125) {
                            output += App.map[x+3][y+3].food;
                        }
                    }
                }
            }
        }
        if (direction == 1) {
            if (y > 0) {
                cells = 3;
                if (x > 0) {
                    output += App.map[x-1][y-1].food;
                }
                output += App.map[x][y-1].food;
                if (x < 127) {
                    output += App.map[x+1][y-1].food;
                }
                if (y > 1) {
                    cells = 8;
                    if (x > 1) {
                        output += App.map[x-2][y-2].food;
                    }
                    if (x > 0) {
                        output += App.map[x-1][y-2].food;
                    }
                    output += App.map[x][y-2].food;
                    if (x < 127) {
                        output += App.map[x+1][y-2].food;
                    }
                    if (x < 126) {
                        output += App.map[x+2][y-2].food;
                    }
                    if (y > 2) {
                        cells = 15;
                        if (x > 2) {
                            output += App.map[x-3][y-3].food;
                        }
                        if (x > 1) {
                            output += App.map[x-2][y-3].food;
                        }
                        if (x > 0) {
                            output += App.map[x-1][y-3].food;
                        }
                        output += App.map[x][y-3].food;
                        if (x < 127) {
                            output += App.map[x+1][y-3].food;
                        }
                        if (x < 126) {
                            output += App.map[x+2][y-3].food;
                        }
                        if (x < 125) {
                            output += App.map[x+3][y-3].food;
                        }
                    }
                }
            }
        }
        if (direction == 2) {
            if (x < 127) {
                cells = 3;
                if (y > 0) {
                    output += App.map[x+1][y-1].food;
                }
                output += App.map[x+1][y].food;
                if (y < 127) {
                    output += App.map[x+1][y+1].food;
                }
                if (x < 126) {
                    cells = 8;
                    if (y > 1) {
                        output += App.map[x+2][y-2].food;
                    }
                    if (y > 0) {
                        output += App.map[x+2][y-1].food;
                    }
                    output += App.map[x+2][y].food;
                    if (y < 127) {
                        output += App.map[x+2][y+1].food;
                    }
                    if (y < 126) {
                        output += App.map[x+2][y+2].food;
                    }
                    if (x < 125) {
                        cells = 15;
                        if (y > 2) {
                            output += App.map[x+3][y-3].food;
                        }
                        if (y > 1) {
                            output += App.map[x+3][y-2].food;
                        }
                        if (y > 0) {
                            output += App.map[x+3][y-1].food;
                        }
                        output += App.map[x+3][y].food;
                        if (y < 127) {
                            output += App.map[x+3][y+1].food;
                        }
                        if (y < 126) {
                            output += App.map[x+3][y+2].food;
                        }
                        if (y < 125) {
                            output += App.map[x+3][y+3].food;
                        }
                    }
                }
            }
        }
        if (direction == 3) {
            if (x > 0) {
                cells = 3;
                if (y > 0) {
                    output += App.map[x-1][y-1].food;
                }
                output += App.map[x-1][y].food;
                if (y < 127) {
                    output += App.map[x-1][y+1].food;
                }
                if (x > 1) {
                    cells = 8;
                    if (y > 1) {
                        output += App.map[x-2][y-2].food;
                    }
                    if (y > 0) {
                        output += App.map[x-2][y-1].food;
                    }
                    output += App.map[x-2][y].food;
                    if (y < 127) {
                        output += App.map[x-2][y+1].food;
                    }
                    if (y < 126) {
                        output += App.map[x-2][y+2].food;
                    }
                    if (x > 2) {
                        cells = 15;
                        if (y > 2) {
                            output += App.map[x-3][y-3].food;
                        }
                        if (y > 1) {
                            output += App.map[x-3][y-2].food;
                        }
                        if (y > 0) {
                            output += App.map[x-3][y-1].food;
                        }
                        output += App.map[x-3][y].food;
                        if (y < 127) {
                            output += App.map[x-3][y+1].food;
                        }
                        if (y < 126) {
                            output += App.map[x-3][y+2].food;
                        }
                        if (y < 125) {
                            output += App.map[x-3][y+3].food;
                        }
                    }
                }
            }
        }
        return new FoodDirectionObject(output, cells);
    }
    
    public class FoodDirectionObject {
        
        public int food = 0;
        public int cells = 0;
        
        public FoodDirectionObject(int food, int cells) {
            this.food = food;
            this.cells = cells;
        }
        
    }
    
}
