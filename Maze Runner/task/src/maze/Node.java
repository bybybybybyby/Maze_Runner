package maze;

import java.io.Serializable;

public class Node extends GridItem implements Serializable {
    private boolean connected;
    private int distance;
    private Node prev;


    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.connected = false;
        this.wall = 0;
        this.distance = 0;
        this.prev = null;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
