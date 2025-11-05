package org;

import javafx.scene.paint.Color;

public enum CellState {
    Normal(Color.TRANSPARENT),
    Visited(Color.GRAY),
    Calculated(Color.LIGHTGRAY),
    Wall(Color.RED),
    Stairs(Color.YELLOW),
    Start(Color.GREEN),
    End(Color.BLUE),
    CheckPoint(Color.LIGHTBLUE);


    public final Color color;
    CellState(Color color) {
        this.color = color;
    }
}
