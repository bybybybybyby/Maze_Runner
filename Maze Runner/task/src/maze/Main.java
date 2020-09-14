package maze;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean currentMazeAvailable = false;
        Maze maze = null;

        while (true) {
            System.out.println("=== Menu ===");

            if (currentMazeAvailable) {
                System.out.println("1. Generate a new maze.\n" +
                        "2. Load a maze.\n" +
                        "3. Save the maze.\n" +
                        "4. Display the maze.\n" +
                        "5. Find the escape.\n" +
                        "0. Exit");

                String input = scanner.nextLine();

                if (input.equals("1")) {
                    System.out.println("Enter the size of a new maze");
                    int size = Integer.parseInt(scanner.nextLine());
                    maze = new Maze(size, size);
                    maze.start();
                    currentMazeAvailable = true;
                } else if (input.equals("2")) {
                    //TODO:
                    System.out.println("Filename to load: ");
                    String filename = scanner.nextLine();
                    try {
//                        maze = new Maze();
                        maze = deserialize(filename);
                    } catch (IOException e) {
                        System.out.println("Deserialize error: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Deserialize ClassNotFoundException error: " + e.getMessage());
                    }
                    currentMazeAvailable = true;
                } else if (input.equals("3")) {
                    System.out.print("Enter a filename: ");
                    String filename = scanner.nextLine();
                    try {
//                        maze.serialize(maze, filename);
                        serialize(maze, filename);
                    } catch (IOException e) {
                        System.out.println("Serialize error: " + e.getMessage());
                    }
                } else if (input.equals("4")) {
                    maze.printMaze();
                } else if (input.equals("5")) {
                    maze.findEscape();
                } else if (input.equals("0")) {
                    System.exit(0);
                }
            } else {
                System.out.println("1. Generate a new maze \n" +
                        "2. Load a maze \n" +
                        "0. Exit \n");

                String input = scanner.nextLine();

                if (input.equals("1")) {
                    System.out.println("Enter the size of a new maze");
                    int size = Integer.parseInt(scanner.nextLine());
                    maze = new Maze(size, size);
                    maze.start();
                    currentMazeAvailable = true;
                } else if (input.equals("2")) {
                    //TODO:
                    System.out.println("Filename to load: ");
                    String filename = scanner.nextLine();
                    try {
//                        maze = new Maze();
                        maze = deserialize(filename);
                    } catch (IOException e) {
                        System.out.println("Deserialize error: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Deserialize ClassNotFoundException error: " + e.getMessage());
                    }
                    currentMazeAvailable = true;
                } else if (input.equals("0")) {
                    System.exit(0);
                }

            }
        }
    }

    public static void serialize(Object obj, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    public static Maze deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return (Maze) obj;
    }

}
