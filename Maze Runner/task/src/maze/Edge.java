package maze;

import java.io.Serializable;
import java.util.Random;

public class Edge extends GridItem implements Serializable {

    private int weight;
    private boolean selected;

    public Edge(int row, int col) {
        this.row = row;
        this.col = col;
        this.selected = false;
        this.wall = 1;
    }

    public void setRandomWeight() {
        Random random = new Random();
        setWeight(random.nextInt(80) + 10);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
