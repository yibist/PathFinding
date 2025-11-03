package org;

public class PathFinding2D {
    private GridState[][] grid;

    PathFinding2D(int width, int height) {
        setGrid(width, height);
    }


    public void SetState(int x, int y, GridState gridState) {
        grid[y][x] = gridState;
    }

    public void setGrid(int width, int height) {
        this.grid = new GridState[height][width];
    }

    public GridState[][] getGrid() {
        return this.grid;
    }


}
