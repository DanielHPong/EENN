package com.danielhpong.EENN;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Class for the entities which inhabit the map.
 *
 * @author daniel.le
 */
public class Entity {

    UUID id;
    Random rand = new Random();
    NeuralNet net;
    int x, y;
    int food = 100;
    int age = 0;
    // A number pertaining to the specific genetic lineage of an Entity.
    int geneology;
    // A number pertaining to the number of ancestors an Entity has.
    int generation = 0;
    int children = 0;

    /**
     * Constructs a new entity based on a preexisting one.
     *
     * @param original
     *            is the entity to copy from.
     */
    public Entity(final Entity original) {
        this.id = UUID.randomUUID();
        this.rand = original.rand;
        this.net = original.net;
        this.x = original.x;
        this.y = original.y;
        this.food = original.food;
        this.age = original.age;
        this.geneology = original.geneology;
        this.generation = original.generation;
        this.children = original.children;
    }

    /**
     * Constructs a new entity with random values and associates it with a given
     * geneology.
     *
     * @param sigTable
     *            is the sig table to give the entity.
     * @param geneology
     *            is the id to associate with the entity's heritage.
     */
    public Entity(final ArrayList<Double> sigTable, final int geneology) {
        id = UUID.randomUUID();
        App.entityCount++;
        net = new NeuralNet(sigTable, rand.nextInt(3), rand.nextInt(2) + 6);
        this.geneology = geneology;
        x = rand.nextInt(128);
        y = rand.nextInt(128);
    }

    /**
     * Overrides the default equals method for entities.
     */
    @Override
    public boolean equals(final Object obj) {
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

    /**
     * Constructs an entity from a given neural net.
     *
     * @param net
     *            is the neural net to use.
     */
    public Entity(final NeuralNet net) {
        id = UUID.randomUUID();
        App.entityCount++;
        this.net = net;
        x = rand.nextInt(128);
        y = rand.nextInt(128);
    }

    /**
     * Updates the entity, allowing it to make a decision using its neural net.
     */
    public void tick() {
        food = food - 5;
        age++;
        final FoodDirectionObject nfood = detectFood(0);
        final FoodDirectionObject sfood = detectFood(1);
        final FoodDirectionObject efood = detectFood(2);
        final FoodDirectionObject wfood = detectFood(3);
        final int decision = net.run(x, y, food, App.map[x][y].food, nfood, efood, sfood, wfood);
        System.out.print(String.valueOf(net.HIDDEN_LAYER_DEPTH) + " ");
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
            final int farmVal = App.map[x][y].food / 2;
            App.map[x][y].food -= farmVal;
            food += farmVal;
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
        System.out.print("(" + String.valueOf(nfood.food) + "," + String.valueOf(efood.food) + ","
                + String.valueOf(sfood.food) + "," + String.valueOf(wfood.food) + ")");
        System.out.print(
                " - " + age + " - " + String.valueOf(x) + ", " + String.valueOf(y) + " : " + String.valueOf(food));
        System.out.print(" [" + String.valueOf(App.map[x][y].food) + "]");
        System.out.println("    " + String.valueOf(geneology) + "+" + String.valueOf(generation));
    }

    /**
     * Creates a genetically similar child using the current entity as its parent.
     *
     * @param gen
     *            is the generation of the entity.
     * @return a new Entity based on the genetics of the parent.
     */
    private Entity createChild(final int gen) {
        final Entity child = new Entity(App.sigTable, geneology);
        child.net = new NeuralNet(this.net, 0.5, 0.05);
        child.food = 50;
        child.x = this.x;
        child.y = this.y;
        child.generation = gen;
        return child;
    }

    /**
     * Causes the current entity to reproduce: creating a new child and updating it
     * accordingly.
     *
     * @param gen
     *            is the number of generations to increment the child by.
     */
    public void reproduce(final int gen) {
        final Entity child = createChild(this.generation + gen);
        /*
         * int childx = x + 1; int childy = y + 1; if (childx > 127) {childx = 126;} if
         * (childy > 127) {childy = 126;}
         */
        child.x = rand.nextInt(128);
        child.y = rand.nextInt(128);
        App.entities.add(child);
        this.food = this.food - 30;
        this.children++;
    }

    /**
     * Returns a FoodDirectionObject given a direction representing the amount of
     * food and number of valid cells in that direction.
     *
     * @param direction
     *            is 0 : NORTH 1 : EAST 2 : SOUTH 3 : WEST
     * @return a FoodDirectionObject for the specified direction.
     */
    public FoodDirectionObject detectFood(final int direction) {
        int output = 0;
        int cells = 0;
        if (direction == 0) {
            cells = 3;
            if (y < 127) {
                if (x > 0) {
                    output += App.map[x - 1][y + 1].food;
                }
                output += App.map[x][y + 1].food;
                if (x < 127) {
                    output += App.map[x + 1][y + 1].food;
                }
                if (y < 126) {
                    cells = 8;
                    if (x > 1) {
                        output += App.map[x - 2][y + 2].food;
                    }
                    if (x > 0) {
                        output += App.map[x - 1][y + 2].food;
                    }
                    output += App.map[x][y + 2].food;
                    if (x < 127) {
                        output += App.map[x + 1][y + 2].food;
                    }
                    if (x < 126) {
                        output += App.map[x + 2][y + 2].food;
                    }
                    if (y < 125) {
                        cells = 15;
                        if (x > 2) {
                            output += App.map[x - 3][y + 3].food;
                        }
                        if (x > 1) {
                            output += App.map[x - 2][y + 3].food;
                        }
                        if (x > 0) {
                            output += App.map[x - 1][y + 3].food;
                        }
                        output += App.map[x][y + 3].food;
                        if (x < 127) {
                            output += App.map[x + 1][y + 3].food;
                        }
                        if (x < 126) {
                            output += App.map[x + 2][y + 3].food;
                        }
                        if (x < 125) {
                            output += App.map[x + 3][y + 3].food;
                        }
                    }
                }
            }
        }
        if (direction == 1) {
            if (y > 0) {
                cells = 3;
                if (x > 0) {
                    output += App.map[x - 1][y - 1].food;
                }
                output += App.map[x][y - 1].food;
                if (x < 127) {
                    output += App.map[x + 1][y - 1].food;
                }
                if (y > 1) {
                    cells = 8;
                    if (x > 1) {
                        output += App.map[x - 2][y - 2].food;
                    }
                    if (x > 0) {
                        output += App.map[x - 1][y - 2].food;
                    }
                    output += App.map[x][y - 2].food;
                    if (x < 127) {
                        output += App.map[x + 1][y - 2].food;
                    }
                    if (x < 126) {
                        output += App.map[x + 2][y - 2].food;
                    }
                    if (y > 2) {
                        cells = 15;
                        if (x > 2) {
                            output += App.map[x - 3][y - 3].food;
                        }
                        if (x > 1) {
                            output += App.map[x - 2][y - 3].food;
                        }
                        if (x > 0) {
                            output += App.map[x - 1][y - 3].food;
                        }
                        output += App.map[x][y - 3].food;
                        if (x < 127) {
                            output += App.map[x + 1][y - 3].food;
                        }
                        if (x < 126) {
                            output += App.map[x + 2][y - 3].food;
                        }
                        if (x < 125) {
                            output += App.map[x + 3][y - 3].food;
                        }
                    }
                }
            }
        }
        if (direction == 2) {
            if (x < 127) {
                cells = 3;
                if (y > 0) {
                    output += App.map[x + 1][y - 1].food;
                }
                output += App.map[x + 1][y].food;
                if (y < 127) {
                    output += App.map[x + 1][y + 1].food;
                }
                if (x < 126) {
                    cells = 8;
                    if (y > 1) {
                        output += App.map[x + 2][y - 2].food;
                    }
                    if (y > 0) {
                        output += App.map[x + 2][y - 1].food;
                    }
                    output += App.map[x + 2][y].food;
                    if (y < 127) {
                        output += App.map[x + 2][y + 1].food;
                    }
                    if (y < 126) {
                        output += App.map[x + 2][y + 2].food;
                    }
                    if (x < 125) {
                        cells = 15;
                        if (y > 2) {
                            output += App.map[x + 3][y - 3].food;
                        }
                        if (y > 1) {
                            output += App.map[x + 3][y - 2].food;
                        }
                        if (y > 0) {
                            output += App.map[x + 3][y - 1].food;
                        }
                        output += App.map[x + 3][y].food;
                        if (y < 127) {
                            output += App.map[x + 3][y + 1].food;
                        }
                        if (y < 126) {
                            output += App.map[x + 3][y + 2].food;
                        }
                        if (y < 125) {
                            output += App.map[x + 3][y + 3].food;
                        }
                    }
                }
            }
        }
        if (direction == 3) {
            if (x > 0) {
                cells = 3;
                if (y > 0) {
                    output += App.map[x - 1][y - 1].food;
                }
                output += App.map[x - 1][y].food;
                if (y < 127) {
                    output += App.map[x - 1][y + 1].food;
                }
                if (x > 1) {
                    cells = 8;
                    if (y > 1) {
                        output += App.map[x - 2][y - 2].food;
                    }
                    if (y > 0) {
                        output += App.map[x - 2][y - 1].food;
                    }
                    output += App.map[x - 2][y].food;
                    if (y < 127) {
                        output += App.map[x - 2][y + 1].food;
                    }
                    if (y < 126) {
                        output += App.map[x - 2][y + 2].food;
                    }
                    if (x > 2) {
                        cells = 15;
                        if (y > 2) {
                            output += App.map[x - 3][y - 3].food;
                        }
                        if (y > 1) {
                            output += App.map[x - 3][y - 2].food;
                        }
                        if (y > 0) {
                            output += App.map[x - 3][y - 1].food;
                        }
                        output += App.map[x - 3][y].food;
                        if (y < 127) {
                            output += App.map[x - 3][y + 1].food;
                        }
                        if (y < 126) {
                            output += App.map[x - 3][y + 2].food;
                        }
                        if (y < 125) {
                            output += App.map[x - 3][y + 3].food;
                        }
                    }
                }
            }
        }
        return new FoodDirectionObject(output, cells);
    }

    /**
     * A class for a FoodDirectionObject which specifies the amount of food and
     * number of cells found in a given direction from a given position.
     *
     * @author daniel.le
     */
    public class FoodDirectionObject {

        public int food = 0;
        public int cells = 0;

        /**
         * Constructor for a FoodDirectionObject
         * 
         * @param food
         *            is the amount of food found in that direction.
         * @param cells
         *            is the number of valid cells in that direction.
         */
        public FoodDirectionObject(final int food, final int cells) {
            this.food = food;
            this.cells = cells;
        }

    }

}
