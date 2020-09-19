package maze;

import java.io.Serializable;

abstract class GridItem implements Serializable {

    int row;
    int col;
    int wall;
    boolean escapePath = false;


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public boolean isEscapePath() {
        return escapePath;
    }

    public void setEscapePath(boolean escapePath) {
        this.escapePath = escapePath;
    }
}
