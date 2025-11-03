package org;

import javafx.scene.paint.Color;

public enum CellState {
    Normal(Color.TRANSPARENT),
    Wall(Color.RED),
    Stairs(Color.YELLOW),
    Start(Color.GREEN),
    End(Color.BLUE),
    CheckPoint(Color.LIGHTBLUE);


    public Color color;
    CellState(Color color) {
        this.color = color;
    }
}
