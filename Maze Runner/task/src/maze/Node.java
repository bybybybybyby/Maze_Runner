package maze;

public class Node {
    private int weight;
    private int wall;
    private int incidents;
    private int row;
    private int col;

    public Node(int row, int col) {
        this.weight = 99;
        this.wall = 1;
        this.incidents = 0;
        this.row = row;
        this.col = col;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public int getIncidents() {
        return incidents;
    }

    public void setIncidents(int incidents) {
        this.incidents = incidents;
    }

    public void increaseIncidents() {
        this.incidents++;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
