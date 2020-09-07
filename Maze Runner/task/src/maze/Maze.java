package maze;

import java.util.ArrayList;

public class Maze {

    private GridItem[][] grid;
    private ArrayList<GridItem> nodesAvailable;
    private ArrayList<Edge> edgesAvailable;
    private final int height;
    private final int width;


    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new GridItem[height][width];
        this.nodesAvailable = new ArrayList<>();
        this.edgesAvailable = new ArrayList<>();
    }

    public void start() {
        fillDefaultGridItems();
        setStartingNode();
//        setEntrance();

        while (!edgesAvailable.isEmpty()) {
            chooseEdgeWithSmallestWeight();
        }

        //TODO: setEnd() ?

        /////////////////// TEST PRINT OUT
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
        /////////////////////^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        printMaze();
    }


    // Create Nodes, Edges, and Borders
    public void fillDefaultGridItems() {

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


    // Set starting Node (which can be random)
    public void setStartingNode() {
        Node startingNode = (Node) grid[1][1];
        startingNode.setConnected(true);
        nodesAvailable.remove((startingNode));
        addEdgesAvailable(startingNode);
    }


    // Set opening for start
    private void setEntrance() {
        //TODO: randomize instead of preset start/end
        grid[1][0] = new Edge(1, 0);
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
        if (nodeCol < width - 2) {
            checkNode = (Node) grid[nodeRow][nodeCol + 2];
//            if (checkNode != null && !checkNode.isConnected()) {
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
//            if (checkNode != null && !checkNode.isConnected()) {
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
//            if (checkNode != null && !checkNode.isConnected()) {
            checkEdge = (Edge) grid[nodeRow - 1][nodeCol];
            if (checkNode != null && nodesAvailable.contains(checkNode) &&
                    !checkEdge.isSelected() && !edgesAvailable.contains(checkEdge)) {
                edgesAvailable.add((Edge) grid[nodeRow - 1][nodeCol]);  //TODO: DOUBLE CHECK
                nodesAvailable.remove(checkNode);
            }
        }

        // Check if edge below is available
        if (nodeRow < height - 2) {
            checkNode = (Node) grid[nodeRow + 2][nodeCol];
//            if (checkNode != null && !checkNode.isConnected()) {
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
                if ( item instanceof Node && !((Node) item).isConnected()) {
                    addEdgesAvailable((Node)item);
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
        System.out.println("HEREEE I GOOOP PRINT MAZZE");
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
//                    }  else {
                        System.out.print("\u2588\u2588");
                    }
                }
                // Border is wall
                else if (currentGridItem instanceof Border) {
                    System.out.print("\u2588\u2588");
                }
                else {
                    System.out.print("\u2588\u2588");
                }
            }
            System.out.println();
        }
    }


}

