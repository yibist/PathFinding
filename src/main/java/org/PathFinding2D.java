package org;

import java.util.ArrayList;
import java.util.Arrays;

public class PathFinding2D {
    private boolean isSetUp = false;
    private CellState[][] grid;
    private final ArrayList<int[]> checkPointOrder = new ArrayList<>();
    int[][] gValues;
    int[][] hValues;
    boolean[][] closed;
    int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // straight
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // diagonals
    };

    PathFinding2D(int width, int height) {
        setGrid(width, height);
    }


    public void setCell(int x, int y, CellState cellState) {
        if (cellState == CellState.CheckPoint) {
            checkPointOrder.add(new int[]{x, y});
        }
        grid[y][x] = cellState;
    }

    public void setGrid(int width, int height) {
        this.grid = new CellState[height][width];
        for (CellState[] cellStates : grid) {
            Arrays.fill(cellStates, CellState.Normal);
        }
        isSetUp = false;
        checkPointOrder.clear();
    }

    public CellState[][] getGrid() {
        return this.grid;
    }


    public CellState getCell(int cellX, int cellY) {
        return this.grid[cellY][cellX];
    }

    public boolean hasStart() {
        for (CellState[] cellStates : grid) {
            for (CellState cellState : cellStates) {
                if (cellState == CellState.Start) return true;
            }
        }
        return false;
    }

    public void doPathFindingStep() {
        if (!isSetUp) {
            setUpPathFinding();
            return;
        }
        int minValue = Integer.MAX_VALUE;
        int minX = 0;
        int minY = 0;

        for (int y = 0; y < gValues.length; y++) {
            for (int x = 0; x < gValues[y].length; x++) {
                int val = gValues[y][x] + hValues[y][x];
                if (val > 0 && (val < minValue || (val == minValue && hValues[y][x] < hValues[minY][minX])) && !closed[y][x]) {
                    minValue = val;
                    minX = x;
                    minY = y;
                }
            }
        }
        doPathFindingStepOn(minX, minY);
    }


    private void doPathFindingStepOn(int x, int y) {
        closed[y][x] = true;
        if (x == checkPointOrder.getFirst()[0] && y == checkPointOrder.getFirst()[1]) {
            // end found
            setUpPathFinding();
            checkPointOrder.removeFirst();
            return;
        }
        if (grid[y][x] == CellState.Calculated) setCell(x, y, CellState.Visited);


        for (int i = 0; i < directions.length; i++) {
            int newX = x + directions[i][0];
            int newY = y + directions[i][1];

            if (newX < 0 || newY < 0 || newY >= grid.length || newX >= grid[newY].length) {
                continue;
            }

            if (grid[newY][newX] == CellState.Wall) {
                gValues[newY][newX] = -1;
                hValues[newY][newX] = -1;
                continue;
            } else if (grid[newY][newX] == CellState.Start) {
                continue;
            }


            int increase = 10;
            if (i >= 4) increase = 14;
            if (gValues[newY][newX] > gValues[y][x] + increase || gValues[newY][newX] == 0) {
                gValues[newY][newX] = gValues[y][x] + increase;

            }

            int dx = Math.abs(newX - checkPointOrder.getFirst()[0]);
            int dy = Math.abs(newY - checkPointOrder.getFirst()[1]);
            int straight = 10;
            int diagonal = 14;

            hValues[newY][newX] = diagonal * Math.min(dx, dy) + straight * (Math.max(dx, dy) - Math.min(dx, dy));
            if (grid[newY][newX] == CellState.Normal) setCell(newX, newY, CellState.Calculated);
        }
    }

    public void setUpPathFinding() {
        int startX = 0, startY = 0;
        closed = new boolean[grid.length][grid[0].length];
        gValues = new int[grid.length][grid[0].length];
        hValues = new int[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == CellState.Start) {
                    startX = x;
                    startY = y;
                } else if (grid[y][x] == CellState.Wall) {
                    gValues[y][x] = -1;
                    hValues[y][x] = -1;
                }
            }
        }
        closed[startX][startY] = true;
        doPathFindingStepOn(startX, startY);
        isSetUp = true;
    }

    public int[][] getFValues() {
        if (!isSetUp) return null;
        int[][] fValues = new int[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                fValues[y][x] = gValues[y][x] + hValues[y][x];
            }
        }
        return fValues;
    }

    public int[][] getgValues() {
        return gValues;
    }

    public int[][] gethValues() {
        return hValues;
    }
}
