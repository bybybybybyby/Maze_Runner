package maze;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the size of a maze (odd numbers, e.g.: 7 9)");
        int height = scanner.nextInt();
        int width = scanner.nextInt();

        Maze maze = new Maze(height, width);
        maze.start();

    }
}
