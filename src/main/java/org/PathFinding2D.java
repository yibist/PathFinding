package org;

import java.util.ArrayList;
import java.util.Arrays;

public class PathFinding2D {
    private CellState[][] grid;
    private ArrayList<int[]> checkPointOrder = new ArrayList<int[]>();

    PathFinding2D(int width, int height) {
        setGrid(width, height);
    }


    public void setCell(int x, int y, CellState cellState) {
        if (cellState == CellState.CheckPoint) {
            checkPointOrder.add(new int[] {x, y});
        }
        grid[y][x] = cellState;
    }

    public void setGrid(int width, int height) {
        this.grid = new CellState[height][width];
        for (CellState[] cellStates : grid) {
            Arrays.fill(cellStates, CellState.Normal);
        }
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


    public ArrayList<int[]> findPath() {
        int startX, startY;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[x].length; x++) {
                if (grid[y][x] == CellState.Start) {
                    startX = x;
                    startY = y;
                }
            }
        }

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        int[][] gValues = new int[grid.length][grid[0].length];
        int[][] hValues = new int[grid.length][grid[0].length];

        return new ArrayList<>();
    }

    int[][] calculateGValues(boolean[][] visited, int startX, int startY) {
        int[][] gValues = new int[grid.length][grid[0].length];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == CellState.Wall) {
                    gValues[y][x] = -1;
                } else {
                    gValues[y][x] = Integer.MAX_VALUE;
                }
            }
        }

        gValues[startY][startX] = 0;

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!visited[y][x] || grid[y][x] == CellState.Wall) continue;

                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (ny < 0 || ny >= grid.length || nx < 0 || nx >= grid[0].length)
                        continue;

                    if (grid[ny][nx] == CellState.Wall) continue;

                    int moveCost = (Math.abs(dir[0]) + Math.abs(dir[1]) == 2) ? 14 : 10;
                    int newG = gValues[y][x] + moveCost;

                    if (newG < gValues[ny][nx]) {
                        gValues[ny][nx] = newG;
                    }
                }
            }
        }

        return gValues;
    }


    int movementCost(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (dx == 1 && dy == 1) ? 14 : 10; // diagonal or straight
    }

}
