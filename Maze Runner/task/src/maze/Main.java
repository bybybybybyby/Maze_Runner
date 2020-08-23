package maze;

public class Main {
    public static void main(String[] args) {

        int[][] maze = new int[10][10];
        String[] rows = new String[10];

        rows[0] = "1111111111";
        rows[1] = "0000000001";
        rows[2] = "1111101111";
        rows[3] = "1000000001";
        rows[4] = "1111011111";
        rows[5] = "1000011111";
        rows[6] = "1011110001";
        rows[7] = "1000000101";
        rows[8] = "1111111100";
        rows[9] = "1111111111";

        // Set values into maze[][], based on rows[]
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                maze[i][j] = Integer.parseInt(rows[i].substring(j, j + 1));
                System.out.println("i="+i+", j="+j+", maze="+maze[i][j]);
            }
        }

        // Print out maze.  "1" is wall (block character \u2588), "0" is passable
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (maze[i][j] == 1) {
                    System.out.print("\u2588\u2588");
                } else if (maze[i][j] == 0) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

    }
}
