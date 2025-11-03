package org;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;

public class View {
    @FXML
    private Canvas canvas2D;
    @FXML
    private TextField widthText;
    @FXML
    private TextField heightText;
    @FXML
    private TextField gridSizeText;
    @FXML
    private Button setGridButton;

    private CellState drawState = null;
    private GraphicsContext gc;

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) {
            return change; // Accept change
        }
        return null; // Reject non-numeric input
    };


    private final PathFinding2D pathFinding2D;

    public View() {
        this.pathFinding2D = new PathFinding2D(5, 5);
    }

    @FXML
    public void initialize() {
        widthText.setTextFormatter(new TextFormatter<>(integerFilter));
        heightText.setTextFormatter(new TextFormatter<>(integerFilter));
        gridSizeText.setTextFormatter(new TextFormatter<>(integerFilter));
        gc = canvas2D.getGraphicsContext2D();

        setGridButton.setOnAction(event -> {
            setGrid();
        });
        setGrid();
        drawGrid();

        canvas2D.setOnMousePressed(event -> handleCanvasClick(getCellX(event.getX()), getCellY(event.getY()), event.getButton()));
        canvas2D.setOnMouseDragged(event -> handleCanvasDrag(getCellX(event.getX()), getCellY(event.getY())));
        canvas2D.setOnMouseExited(event -> drawState = null);
    }

    private int getCellX(double x) {
        int gridSize = getGridSize();
        int width = getWidth();
        x = Math.max(0, Math.min(x, width * gridSize - 1));
        return (int) (x / gridSize);
    }

    private int getCellY(double y) {
        int gridSize = getGridSize();
        int height = getHeight();
        y = Math.max(0, Math.min(y, height * gridSize - 1));
        return (int) (y / gridSize);
    }

    private void handleCanvasDrag(int x, int y) {
        if (drawState == null) return;
        pathFinding2D.setCell(x, y, drawState);
        drawGrid();
    }

    private void handleCanvasClick(int x, int y, MouseButton button) {
        if (button == MouseButton.SECONDARY) {
            if (!pathFinding2D.hasStart()) {
                pathFinding2D.setCell(x, y, CellState.Start);
            } else {
                pathFinding2D.setCell(x, y, CellState.CheckPoint);
            }
            drawGrid();
            return;
        }

        if (button == MouseButton.MIDDLE) {
            drawState = CellState.Normal;
            return;
        }


        if (pathFinding2D.getCell(x, y) == CellState.Wall) {
            drawState = CellState.Stairs;
        } else {
            drawState = CellState.Wall;
        }
        drawGrid();
    }

    private int getWidth() {
        return Integer.parseInt(widthText.getText());
    }

    private int getHeight() {
        return Integer.parseInt(heightText.getText());
    }

    private int getGridSize() {
        return Integer.parseInt(gridSizeText.getText());
    }

    private void setGrid() {
        canvas2D.setWidth(getWidth() * getGridSize());
        canvas2D.setHeight(getHeight() * getGridSize());
        pathFinding2D.setGrid(getWidth(), getHeight());
        drawGrid();
    }

    private void drawGrid() {
        CellState[][] grid = pathFinding2D.getGrid();
        gc.clearRect(0, 0, canvas2D.getWidth(), canvas2D.getHeight());
        gc.setStroke(Color.WHITE);
        int gridSize = getGridSize();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                gc.setFill(grid[y][x].color);
                gc.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
                gc.strokeRect(x * gridSize, y * gridSize, gridSize, gridSize);
            }
        }
    }
}
