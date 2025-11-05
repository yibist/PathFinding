package org;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    private Button pathFindButton;
    @FXML
    private ComboBox comboBox;

    private CellState drawState = null;
    private GraphicsContext gc;
    private Image imageToDraw;
    private List<Image> images;

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
        gc = canvas2D.getGraphicsContext2D();

        widthText.setTextFormatter(new TextFormatter<>(integerFilter));
        heightText.setTextFormatter(new TextFormatter<>(integerFilter));
        gridSizeText.setTextFormatter(new TextFormatter<>(integerFilter));

        images = getImages();
        for (Image img : images) {
            comboBox.getItems().add(img);
        }
        comboBox.getItems().add(null);

        comboBox.setOnAction(event -> {
            imageToDraw = (Image) comboBox.getValue();
            if (imageToDraw == null) {
                drawGrid();
                return;
            };
            widthText.setText(String.valueOf((int) imageToDraw.getWidth() / getGridSize()));
            heightText.setText(String.valueOf((int) imageToDraw.getHeight() / getGridSize()));
            setGrid();
        });

        pathFindButton.setOnAction(event -> {
            pathFinding2D.doPathFindingStep();
            drawGrid();
        });

        setGridButton.setOnAction(event -> {
            setGrid();
        });
        setGrid();
        drawGrid();

        canvas2D.setOnMousePressed(event -> handleCanvasClick(getCellX(event.getX()), getCellY(event.getY()), event.getButton()));
        canvas2D.setOnMouseDragged(event -> handleCanvasDrag(getCellX(event.getX()), getCellY(event.getY())));
        canvas2D.setOnMouseExited(event -> drawState = null);
    }

    private List<Image> getImages() {
        final File folder = new File("/images");
        final List<Image> images = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/images/imageIndex.txt")))) {
            reader.lines().forEach(name ->
                    images.add(new Image(getClass().getResourceAsStream("/images/" + name))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return images;
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
        int[][] fValues = pathFinding2D.getFValues();
        int[][] gValues = pathFinding2D.getgValues();
        int[][] hValues = pathFinding2D.gethValues();

        if (imageToDraw != null) {
            gc.drawImage(imageToDraw, 0, 0);
        }
        System.out.println("asd");
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                gc.setFill(grid[y][x].color);
                gc.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
                gc.strokeRect(x * gridSize, y * gridSize, gridSize, gridSize);

                if (fValues != null) {
                    gc.strokeText(String.valueOf(gValues[y][x]), x * gridSize + 1, y * gridSize + 10 + gridSize*0/3);
                    gc.strokeText(String.valueOf(hValues[y][x]), x * gridSize + 1, y * gridSize + 10 + gridSize*1/3);
                    gc.strokeText(String.valueOf(fValues[y][x]), x * gridSize + 1, y * gridSize + 10 + gridSize*2/3);

                }
            }
        }
    }
}
