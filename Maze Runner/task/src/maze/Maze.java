package maze;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Maze implements Serializable {

    private boolean printDebug = false;
    private static final long serialVersionUID = 1L;
    private GridItem[][] grid;
    private final ArrayList<GridItem> nodesAvailable;
    private final ArrayList<Edge> edgesAvailable;
    private final int height;
    private final int width;
    private Node entranceNode;
    private Node exitNode;
    private ArrayList<Node> visitedNodesForEscape;
    private Queue<Node> queueForEscape;


    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new GridItem[height][width];
        this.nodesAvailable = new ArrayList<>();
        this.edgesAvailable = new ArrayList<>();
        this.visitedNodesForEscape = new ArrayList<>();
        this.queueForEscape = new LinkedList<>();
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

        printMaze(false);
    }


    // Create Nodes, Edges, and Borders
    private void fillDefaultGridItems() {

        // Initially fill everything as Border (wall)
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GridItem newBorder = new Border(i, j);
                grid[i][j] = newBorder;
            }
        }

        // Next, set Nodes and Edges
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                // Create Nodes
                if (i % 2 == 1 && j % 2 == 1) {
                    GridItem newNode = new Node(i, j);
                    grid[i][j] = newNode;
                    nodesAvailable.add(newNode);
                }
                // Create Edges
                if (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0) {
                    GridItem newEdge = new Edge(i, j);
                    ((Edge) newEdge).setRandomWeight();

                    grid[i][j] = newEdge;
                }
//                // Create Borders along outside
//                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
//                    grid[i][j] = new Border(i, j);
//                }
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
        // Set the Node next to the left opening as the entranceNode to use when calculating escape route
        this.entranceNode = (Node) this.grid[edge1.getRow()][edge1.getCol() + 1];

        // Right opening
        Edge edge2 = new Edge(rand2, width - 1);
        edge2.setSelected(true);
        grid[rand2][width - 1] = edge2;
        // Set the Node next to the right opening as the exitNode to use when calculating escape route
        this.exitNode = (Node) this.grid[edge2.getRow()][edge2.getCol() - 1];

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
                edgesAvailable.add((Edge) grid[nodeRow][nodeCol + 1]);
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge to the left is available
        if (nodeCol > 2) {
            checkNode = (Node) grid[nodeRow][nodeCol - 2];
            checkEdge = (Edge) grid[nodeRow][nodeCol - 1];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow][nodeCol - 1]);
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge above is available
        if (nodeRow > 2) {
            checkNode = (Node) grid[nodeRow - 2][nodeCol];
            checkEdge = (Edge) grid[nodeRow - 1][nodeCol];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow - 1][nodeCol]);
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge below is available
        if (nodeRow < height - 2 && (grid[nodeRow + 2][nodeCol] instanceof Node)) {
            checkNode = (Node) grid[nodeRow + 2][nodeCol];
            checkEdge = (Edge) grid[nodeRow + 1][nodeCol];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow + 1][nodeCol]);
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
    public void printMaze(boolean escapePath) {

        if (printDebug) {
            System.out.println("***************************");
            System.out.println("EntranceNode=" + entranceNode.getRow() + ":" + entranceNode.getCol());
            System.out.println("***************************");
        }


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GridItem currentGridItem = grid[i][j];

                if (escapePath && currentGridItem.isEscapePath()) {
                    System.out.print("//");
                }
                // Nodes are open
                else if (currentGridItem instanceof Node) {
                    System.out.print("  ");
                }
                // Edges can be open or wall
                else if (currentGridItem instanceof Edge) {
                    if (((Edge) currentGridItem).isSelected()) {
                        System.out.print("  ");
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

    /*
    Using BFS (Breadth first search)
    The algorithm goes like this:
    1. Choose an initial node of a graph, mark it as visited and set its distance to 0.
    2. Consider all unvisited neighbors of the nodes visited at the previous step and mark all of them as visited.
    Set the distance to each of these nodes to d + 1, where d is the distance to the nodes processed at the previous step.
    3. Repeat step 2 until all the nodes are visited.
     */
    public void findEscape() {
        queueForEscape.add(entranceNode);

        while (!queueForEscape.isEmpty()) {
            if (printDebug) {
                System.out.println("*****************************************************");
                System.out.println("NODES IN QUEUE");
                for (Node node : queueForEscape) {
                    System.out.println(node.getRow() + ":" + node.getCol());
                }
            }

            // Pop the first node from queue
            Node currentNode = queueForEscape.remove();
            visitedNodesForEscape.add(currentNode);
            if (printDebug) {
                System.out.println("*** POPPED NODE=" + currentNode.getRow() + ":" + currentNode.getCol());
            }
            // If this node is the exit Node, then search is over
            if (currentNode.equals(exitNode)) {
                drawEscapePath(exitNode);
                System.out.println("Found Exit Node!");
            } else {
                // Otherwise, add this node's children to the end of the queue and repeat the steps
                findAccessibleNeighborNodes(currentNode);
            }
        }

        visitedNodesForEscape.clear();

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
    }


    /**
     * Check surrounding nodes and retrieve neighbor nodes that are unvisited and can be reached (no wall in between)
     * Get Nodes from above, right, below, and left of argument Node, if in grid area.
     *
     * @param node The node to check around
     */
    private void findAccessibleNeighborNodes(Node node) {
        int row = node.getRow();
        int col = node.getCol();

        // Check if a Node exists 2 rows above, and if the Edge 1 row above is set as passable (not a wall)
        if (row > 1) {
            Node nodeAbove = (Node) grid[row - 2][col];
            if (grid[row - 1][col].getWall() == 0 && !visitedNodesForEscape.contains(nodeAbove)) {
                visitedNodesForEscape.add(nodeAbove);  // Add node to visited list
                nodeAbove.setDistance(node.getDistance() + 1);  // Set distance for node as previous distance + 1
                nodeAbove.setPrev(node);  // Set the node we are coming from as the previous to this node
                queueForEscape.add(nodeAbove);  // Add node to the queue
            }
        }

        // Check if a Node exists 2 rows below, and if the Edge 1 row below is set as passable (not a wall)
        if (row < this.height - 2) {
            Node nodeBelow = (Node) grid[row + 2][col];
            if (grid[row + 1][col].getWall() == 0 && !visitedNodesForEscape.contains(nodeBelow)) {
                visitedNodesForEscape.add(nodeBelow);
                nodeBelow.setDistance(node.getDistance() + 1);
                nodeBelow.setPrev(node);  // Set the node we are coming from as the previous to this node
                queueForEscape.add(nodeBelow);
            }
        }

        // Check if a Node exists 2 columns to the left, and if the Edge 1 column to the left is passable (not a wall)
        if (col > 1) {
            Node nodeLeft = (Node) grid[row][col - 2];
            if (grid[row][col - 1].getWall() == 0 && !visitedNodesForEscape.contains(nodeLeft)) {
                visitedNodesForEscape.add(nodeLeft);
                nodeLeft.setDistance(node.getDistance() + 1);
                nodeLeft.setPrev(node);  // Set the node we are coming from as the previous to this node
                queueForEscape.add(nodeLeft);
            }
        }

        // Check if a Node exists 2 columns to the right, and if the Edge 1 column to the right is passable (not a wall)
        if (col < this.width - 2) {
            Node nodeRight = (Node) grid[row][col + 2];
            if (col < this.width - 2 && grid[row][col + 1].getWall() == 0 && !visitedNodesForEscape.contains(nodeRight)) {
                visitedNodesForEscape.add(nodeRight);
                nodeRight.setDistance(node.getDistance() + 1);
                nodeRight.setPrev(node);  // Set the node we are coming from as the previous to this node
                queueForEscape.add(nodeRight);
            }
        }
    }

    // Draw the escape path.  After the exitNode is found, it uses previous Node to backtrack until it reaches source.
    private void drawEscapePath(Node exitNode) {
        GridItem currentItem = exitNode;

        while (true) {
            currentItem.setEscapePath(true);
            if (currentItem == entranceNode) {
                break;
            } else {
                currentItem = ((Node) currentItem).getPrev();
            }
        }
        printMaze(true);
    }
}

