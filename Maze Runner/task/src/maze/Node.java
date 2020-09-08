package maze;

import java.io.Serializable;

public class Node extends GridItem implements Serializable {
    private int incidents;
    private boolean connected;


    public Node(int row, int col) {
        this.row = row;
        this.col = col;
        this.incidents = 0;
        this.connected = false;
        this.wall = 0;
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
}
