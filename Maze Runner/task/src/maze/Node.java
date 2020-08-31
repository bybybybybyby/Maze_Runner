package maze;

public class Node {
    private int weight;
    private int wall;
    private int incidents;

    public Node() {
        this.weight = 99;
        this.wall = 1;
        this.incidents = 0;
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
}
