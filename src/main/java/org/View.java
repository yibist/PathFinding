package org;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

    GraphicsContext gc;

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

        canvas2D.setOnMousePressed(event -> handleCanvasClick(event.getX(), event.getY()));
        canvas2D.setOnMouseDragged(event -> handleCanvasClick(event.getX(), event.getY()));
    }

    private void handleCanvasClick(double x, double y) {
        int gridSize = getGridSize();
        int width = getWidth();
        int height = getHeight();

        x = Math.max(0, Math.min(x, width * gridSize - 1));
        y = Math.max(0, Math.min(y, height * gridSize - 1));

        int cellX = (int) (x / gridSize);
        int cellY = (int) (y / gridSize);

        pathFinding2D.SetState(cellX, cellY, GridState.Wall);
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
        GridState[][] grid = pathFinding2D.getGrid();
        gc.clearRect(0, 0, canvas2D.getWidth(), canvas2D.getHeight());
        gc.setStroke(Color.WHITE);
        int gridSize = getGridSize();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == GridState.Wall) {
                    gc.setFill(Color.RED);
                    gc.fillRect(j * gridSize, i * gridSize, gridSize, gridSize);
                }
                gc.strokeRect(j * gridSize, i * gridSize, gridSize, gridSize);
            }
        }
    }
}
