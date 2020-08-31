package maze;

import java.util.Arrays;
import java.util.Random;

public class Maze {

    private final int[][] matrix;
    private final int[][] matrixWeight;
    private final int height;
    private final int width;


    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.matrix = new int[height][width];
        this.matrixWeight = new int[height][width];
    }

    public void start() {
        fillDefaultMatrixWeight();
        randomizeNodeWeights();
        setStartAndEnd();

        /////////////////// TEST PRINT OUT
        System.out.println("Node Weights Representation");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.printf("%02d" + " ", matrixWeight[i][j]);
            }
            System.out.println();
        }
        //////////////////////////////////////////


        populateMaze();
        printMaze();
    }

    // Fill all weights  as 99 which are outsides walls not to be used
    public void fillDefaultMatrixWeight() {
        for (int[] arr : matrixWeight) {
            Arrays.fill(arr, 99);
        }
    }


    // Set values into maze[][]
    public void populateMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //TODO: set actual, not just walls
                matrix[i][j] = 1;
            }
        }
    }

    // Print out maze.  "1" is wall (block character \u2588), "0" is passable
    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // wall is 1, open space is 0
                if (matrix[i][j] == 1) {
                    System.out.print("\u2588\u2588");
                } else if (matrix[i][j] == 0) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    // Assign random values to each node, which will be used to find MST
    private void randomizeNodeWeights() {
        Random random = new Random();
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                matrixWeight[i][j] = random.nextInt(80) + 10;
            }
        }
    }

    // set 2 openings for start and exit
    private void setStartAndEnd() {
        //TODO: randomize instead of preset start/end
        matrixWeight[1][0] = 1;
        matrixWeight[height - 2][width - 1] = 1;
    }


}

