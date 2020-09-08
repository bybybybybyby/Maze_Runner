package maze;

import java.io.Serializable;

public class Border extends GridItem implements Serializable {

    public Border(int row, int col) {
        this.row = row;
        this.col = col;
        this.wall = 1;
    }

}
