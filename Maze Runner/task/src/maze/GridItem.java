package maze;

abstract class GridItem {

    int row;
    int col;
    int wall;
    boolean escapePath = false;


    public int getRow() {
        return row;
    }

//    public void setRow(int row) {
//        this.row = row;
//    }

    public int getCol() {
        return col;
    }

//    public void setCol(int col) {
//        this.col = col;
//    }

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
