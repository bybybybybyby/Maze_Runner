package maze;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Maze implements Serializable {

    private boolean printDebug = true;
    private static final long serialVersionUID = 1L;
    private GridItem[][] grid;
    private final ArrayList<GridItem> nodesAvailable;
    private final ArrayList<Edge> edgesAvailable;
    private final int height;
    private final int width;
    private Node entranceNode;
    private ArrayList<Node> unprocessedNodesForEscape;

//    public Maze() {
//        this(3, 3);
//    }

    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new GridItem[height][width];
        this.nodesAvailable = new ArrayList<>();
        this.edgesAvailable = new ArrayList<>();
        this.unprocessedNodesForEscape = new ArrayList<>();
    }

    public void start() {
        fillDefaultGridItems();
        setStartingNode();

        while (!edgesAvailable.isEmpty()) {
            chooseEdgeWithSmallestWeight();
        }

        setEntranceAndExit();



        /*
        DEBUG TESTING
         */
        if (printDebug) {
            System.out.println("*******************************************");
            System.out.println("Node Weights Representation");
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (grid[i][j] instanceof Edge) {
                        System.out.printf("%02d", ((Edge) grid[i][j]).getWeight());
                    } else if (grid[i][j] instanceof Node) {
                        System.out.print("NN");
                    } else if (grid[i][j] instanceof Border) {
                        System.out.print("BB");
                    } else {
                        System.out.print("--");
                    }
                }
                System.out.println();
            }
            System.out.println("*******************************************");
        }

        printMaze();
    }


    // Create Nodes, Edges, and Borders
    private void fillDefaultGridItems() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                // Create Nodes
                if (i % 2 == 1 && j % 2 == 1) {
                    Node newNode = new Node(i, j);
                    grid[i][j] = newNode;
                    nodesAvailable.add(newNode);
                }
                // Create Edges
                if (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0) {
                    Edge newEdge = new Edge(i, j);
                    grid[i][j] = newEdge;
                    newEdge.setRandomWeight();
                }
                // Create Borders along outside
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    grid[i][j] = new Border(i, j);
                }
            }
        }
    }


    // Set starting Node for generating map at index 1,1 (but, which probably can be random)
    private void setStartingNode() {
        Node startingNode = (Node) grid[1][1];
        startingNode.setConnected(true);
        nodesAvailable.remove((startingNode));
        addEdgesAvailable(startingNode);
    }


    // Set opening for entrance and exit on left and right sides
    private void setEntranceAndExit() {
        Random random = new Random();
        int rand1 = -1;
        int rand2 = -1;

        // If height is even, compensate by not allowing entrance and exit on lowest index.
        if (height % 2 == 0) {
            rand1 = random.nextInt((height - 1) / 2) * 2 + 1;
            rand2 = random.nextInt((height - 1) / 2) * 2 + 1;
        } else {
            rand1 = random.nextInt(height / 2) * 2 + 1;
            rand2 = random.nextInt(height / 2) * 2 + 1;
        }

        // Left opening
        Edge edge1 = new Edge(rand1, 0);
        edge1.setSelected(true);
        grid[rand1][0] = edge1;
        // Set the Node next to the opening as the entranceNode to use when calculating escape route
        this.entranceNode = (Node) this.grid[edge1.getRow()][edge1.getCol() + 1];

        // Right opening
        Edge edge2 = new Edge(rand2, width - 1);
        edge2.setSelected(true);
        grid[rand2][width - 1] = edge2;

        // Second right opening for cases where width is even
        if (width % 2 == 0) {
            Edge edge3 = new Edge(rand2, width - 2);
            edge3.setSelected(true);
            grid[rand2][width - 2] = edge3;
        }
    }


    // Add Edges to edgesAvailable ArrayList, which are
    // edges incident to Nodes that are not connected.
    private void addEdgesAvailable(Node addedNode) {
        nodesAvailable.remove((addedNode));
        int nodeRow = addedNode.getRow();
        int nodeCol = addedNode.getCol();
        Node checkNode = null;
        Edge checkEdge = null;

        // Check if edge to the right is available
        if (nodeCol < width - 2 && (grid[nodeRow][nodeCol + 2] instanceof Node)) {
            checkNode = (Node) grid[nodeRow][nodeCol + 2];
            checkEdge = (Edge) grid[nodeRow][nodeCol + 1];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow][nodeCol + 1]);   //TODO: DOUBLE CHECK
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge to the left is available
        if (nodeCol > 2) {
            checkNode = (Node) grid[nodeRow][nodeCol - 2];
            checkEdge = (Edge) grid[nodeRow][nodeCol - 1];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow][nodeCol - 1]); //TODO: DOUBLE CHECK
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge above is available
        if (nodeRow > 2) {
            checkNode = (Node) grid[nodeRow - 2][nodeCol];
            checkEdge = (Edge) grid[nodeRow - 1][nodeCol];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow - 1][nodeCol]);  //TODO: DOUBLE CHECK
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge below is available
        if (nodeRow < height - 2 && (grid[nodeRow + 2][nodeCol] instanceof Node)) {
            checkNode = (Node) grid[nodeRow + 2][nodeCol];
            checkEdge = (Edge) grid[nodeRow + 1][nodeCol];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow + 1][nodeCol]);  //TODO: DOUBLE CHECK
                nodesAvailable.remove(checkNode);
            }
        }
    }


    // Choose next edge on edgesAvailable ArrayList
    private void chooseEdgeWithSmallestWeight() {
        int smallest = Integer.MAX_VALUE;
        Edge edge = null;
        for (Edge e : edgesAvailable) {
            int currWeight = e.getWeight();
            if (currWeight < smallest) {
                smallest = currWeight;
                edge = e;
            }
        }
//        addNodeIncidents(edge);
        if (edge != null) {
            edge.setSelected(true);
            edge.setWall(0);
            edgesAvailable.remove(edge);
            // TODO: Add to addEdgesAvailable based on new connected Node
            ArrayList<GridItem> list = getSurroundingGridItems(edge);
            for (GridItem item : list) {
                // Check if one of the items is a Node and not connected yet.
                if (item instanceof Node && !((Node) item).isConnected()) {
                    addEdgesAvailable((Node) item);
                }
            }
        }

    }

    // Get items from above, right, below, and left of argument if in grid area.
    private ArrayList<GridItem> getSurroundingGridItems(GridItem gridItem) {
        int row = gridItem.getRow();
        int col = gridItem.getCol();
        ArrayList<GridItem> list = new ArrayList<>();

        // Check above
        if (row > 0) {
            list.add(grid[row - 1][col]);
        }
        // Check below
        if (row < this.height - 1) {
            list.add(grid[row + 1][col]);
        }
        // Check left
        if (col > 0) {
            list.add(grid[row][col - 1]);
        }
        // Check right
        if (col < this.width - 1) {
            list.add(grid[row][col + 1]);
        }

        return list;
    }

    // Print out maze.  "1" is wall (block character \u2588), "0" is passable
    public void printMaze() {

        if (printDebug) {
            System.out.println("***************************");
            System.out.println("EntranceNode=" + entranceNode.getRow() + ":" + entranceNode.getCol());
            System.out.println("***************************");
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GridItem currentGridItem = grid[i][j];
                // Nodes are open
                if (currentGridItem instanceof Node) {
                    System.out.print("  ");
                }
                // Edges can be open or wall
                else if (currentGridItem instanceof Edge) {
//                    if (currentGridItem.getWall() == 0) {
                    if (((Edge) currentGridItem).isSelected()) {
                        System.out.print("  ");
//                    } else if (currentGridItem.getWall() == 1) {
                    } else if (!((Edge) currentGridItem).isSelected()) {
                        System.out.print("\u2588\u2588");
                    }
                }
                // Border is wall
                else if (currentGridItem instanceof Border) {
                    System.out.print("\u2588\u2588");
                } else {
                    System.out.print("\u2588\u2588");
                }
            }
            System.out.println();
        }
    }


//    // Find the escape path (using Dijkstra's algorithm)
//    public GridItem[][] findEscape() {
//        GridItem[][] escapeGrid = new GridItem[height][width];
//
//        /*
//        Set all Nodes' distances from source as MAX_VALUE
//        Add all Nodes to unprocessedNodesForEscape ArrayList
//         */
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                GridItem currentGridItem = grid[i][j];
//                if (currentGridItem instanceof Node) {
//                    ((Node) currentGridItem).setDistance(Integer.MAX_VALUE);
//                    unprocessedNodesForEscape.add((Node) currentGridItem);
//                }
//            }
//        }
//
//        // Set entranceNode distance as 0; this is our source Node from which all distances are calculated
//        entranceNode.setDistance(0);
//
//        /*
//        Find unprocessed node with smallest distance.  Check all unprocessed neighbors of node, whether the distance
//        is less than current distance.  Update current distance only if it is smaller.
//        */
//        if (!unprocessedNodesForEscape.isEmpty()) {
//            int smallestDist = Integer.MAX_VALUE;
//            Node chosenNode = null;
//            for (Node u : unprocessedNodesForEscape) {
//                int currentNodeDist = u.getDistance();
//                if (currentNodeDist < smallestDist) {
//                    smallestDist = currentNodeDist;
//                    chosenNode = u;
//                }
//            }
//
//        }


    //TODO: Try BFS instead of Dijstra's
    public GridItem[][] findEscape() {
        GridItem[][] escapeGrid = new GridItem[height][width];

        if (printDebug) {
            System.out.println("***************************************************");
            System.out.println("NODE DISTANCES");
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    GridItem currentGridItem = grid[i][j];
                    if (currentGridItem instanceof Node) {
                        System.out.println(currentGridItem.getRow() + ":" +
                                currentGridItem.getCol() + " - " + ((Node) currentGridItem).getDistance());
                    }
                }
            }
            System.out.println("***************************************************");
        }

        return escapeGrid;
    }


}

