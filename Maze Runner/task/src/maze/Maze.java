package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze {

    private Node[][] matrix;
    private ArrayList<Node> nodesAvailable;

    private final int height;
    private final int width;


    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.matrix = new Node[height][width];
        this.nodesAvailable = new ArrayList<>();
    }

    public void start() {
        fillDefaultNodes();
        randomizeNodeWeights();
        setStartAndEnd();

        //TODO: Check available Nodes in nodesAvailable arraylist
        //TODO: Choose smallest weight Nodes for MST.
        //TODO: Repeat until no nodesAvailable left. isEmpty().

        /////////////////// TEST PRINT OUT
        System.out.println("Node Weights Representation");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.printf("%02d" + " ", matrix[i][j].getWeight());
            }
            System.out.println();
        }
        //////////////////////////////////////////

//        populateMaze();
        printMaze();
    }

    // Create all new nodes
    public void fillDefaultNodes() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = new Node(i, j);
            }
        }
    }

    // Assign random values to each node, which will be used to find MST
    private void randomizeNodeWeights() {
        Random random = new Random();
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                matrix[i][j].setWeight(random.nextInt(80) + 10);
            }
        }
    }

    // set 2 openings for start and exit
    private void setStartAndEnd() {
        //TODO: randomize instead of preset start/end
        matrix[1][0].setWeight(1);
        matrix[height - 2][width - 1].setWeight(1);

        addNodeIncidents(matrix[1][0]);
        addNodeIncidents(matrix[height - 2][width - 1]);
    }


//    // Set values into maze[][]
//    public void populateMaze() {
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                //TODO: set actual, not just walls
//                matrix[i][j].setWall(1);
//            }
//        }
//    }

    // Increase Node incident count to adjacent nodes
    private void addNodeIncidents(Node node) {
        int row = node.getRow();
        int col = node.getCol();

        // Check node above
        if (row > 1) {
            matrix[row - 1][col].increaseIncidents();
            addNodesAvailable(matrix[row - 1][col]);
        }
        // Check node below
        if (row < height - 2) {
            matrix[row + 1][col].increaseIncidents();
            addNodesAvailable(matrix[row + 1][col]);
        }
        // Check node to the left
        if (col > 1) {
            matrix[row][col - 1].increaseIncidents();
            addNodesAvailable(matrix[row][col - 1]);
        }
        // Check node to the right
        if (col < width - 2) {
            matrix[row][col + 1].increaseIncidents();
            addNodesAvailable(matrix[row][col + 1]);
        }

    }

    // Add Nodes to nodesAvailable ArrayList if it has only ONE edge incident
    private void addNodesAvailable(Node node) {
        // Do not add if it is preset outer wall of weight 99
        if (node.getWeight() == 99) {
            return;
        }

        if (node.getIncidents() == 1) {
            nodesAvailable.add(node);
        } else if (node.getIncidents() > 1) {
            nodesAvailable.remove(node);
        }
    }


    // Print out maze.  "1" is wall (block character \u2588), "0" is passable
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // wall is 1, open space is 0
                if (matrix[i][j].getWall() == 1) {
                    System.out.print("\u2588\u2588");
                } else if (matrix[i][j].getWall() == 0) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }


}

