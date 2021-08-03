package mainPackage;

import Algorithms.GraphAlgorithms.BFSAlgorithm;
import Algorithms.GraphAlgorithms.DFSAlgorithm;
import Algorithms.GraphAlgorithms.DijkstraAlgorithm;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import Algorithms.utils.*;

import Algorithms.utils.Point;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable {

    private UndirectedGraph graph;
    private int counter;
    private Point lastPointClicked;
    private Node movingNode;

    /**
     * Tooltip variables
     */
    @FXML
    private Tooltip helpTooltip;
    @FXML
    private Tooltip algorithmTooltip;
    @FXML
    private Tooltip speedTooltip;
    @FXML
    private Tooltip startNodeTooltip;
    @FXML
    private Tooltip endNodeTooltip;
    @FXML
    private Tooltip weightTooltip;
    @FXML
    private Tooltip addNodeTooltip;
    @FXML
    private Tooltip clearCanvasTooltip;
    @FXML
    private Tooltip resetCanvasTooltip;


    @FXML
    private Canvas mainCanvas;
    @FXML
    private RadioButton addNode;
    @FXML
    private TextField startingNode;
    @FXML
    private TextField endingNode;
    @FXML
    private TextField weightForEdge;
    @FXML
    private ChoiceBox<String> algorithmChoiceBox;
    @FXML
    private ChoiceBox<Float> speedChoiceBox;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label pathLabel;


    @FXML
    private void handleMouseClicked(MouseEvent event) {
        lastPointClicked = new Point(event.getX(), event.getY());
        DrawItems.drawGraph(mainCanvas.getGraphicsContext2D(), graph);
        movingNode = getNodeFromCanvas(lastPointClicked);
    }

    @FXML
    private void handleMouseRelease(MouseEvent event) {
        movingNode = null;
    }

    @FXML
    private void handleMouseClickOnCanvas(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        if (canAddNode(p) && addNode.isSelected() && p.distance(lastPointClicked) < DrawItems.getRadius() && event.getButton() == MouseButton.PRIMARY) {
            addNode(p);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            addEdge(p);
        }
    }

    @FXML
    private void handleMouseDragFromNode(MouseEvent event) {
        Point drawAt = new Point(event.getX(), event.getY());
        if (canAddEdge(lastPointClicked) && event.getButton() == MouseButton.SECONDARY) {
            DrawItems.resetCanvasWithGraph(mainCanvas, graph);
            DrawItems.drawLine(mainCanvas.getGraphicsContext2D(), lastPointClicked, drawAt, Color.GRAY);
        } else if (event.getButton() == MouseButton.PRIMARY && movingNode != null && canAddNode(drawAt) && nodeInCanvas(drawAt)) {
            movingNode.setLocation(drawAt);
            resetGraphColors();
        }
    }

    @FXML
    private void resetGraphColors() {
        DrawItems.resetCanvasWithGraph(mainCanvas, graph);
        distanceLabel.setText("");
        pathLabel.setText("");
    }

    @FXML
    private void runAlgorithm() {
        if (algorithmChoiceBox.getValue() != null) {
            DrawItems.resetCanvasWithGraph(mainCanvas, graph);
            switch (algorithmChoiceBox.getValue()) {
                case "BFS" -> runBFSAlgorithm();
                case "Dijkstra" -> runDijkstraAlgorithm();
                case "DFS" -> runDFSAlgorithm();
            }
        }
    }

    @FXML
    private void clearCanvas() {
        graph = new UndirectedGraph();
        counter = 0;
        lastPointClicked = null;
        mainCanvas.getGraphicsContext2D().setFill(Color.WHITE);
        mainCanvas.getGraphicsContext2D().fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        distanceLabel.setText("");
        pathLabel.setText("");
        DrawItems.drawLegend(mainCanvas);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        counter = 0;
        graph = new UndirectedGraph();
        algorithmChoiceBox.getItems().add("BFS");
        algorithmChoiceBox.getItems().add("DFS");
        algorithmChoiceBox.getItems().add("Dijkstra");
        speedChoiceBox.getItems().add(0.25f);
        speedChoiceBox.getItems().add(0.50f);
        speedChoiceBox.getItems().add(0.75f);
        speedChoiceBox.getItems().add(1f);
        speedChoiceBox.getItems().add(1.25f);
        speedChoiceBox.getItems().add(1.50f);
        speedChoiceBox.getItems().add(1.75f);
        speedChoiceBox.getItems().add(2f);
        mainCanvas.getGraphicsContext2D().setFill(Color.WHITE);
        mainCanvas.getGraphicsContext2D().fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        speedChoiceBox.setValue(1.0f);
        algorithmChoiceBox.setValue("Select Algorithm");
        DrawItems.drawLegend(mainCanvas);

        addNodeTooltip.setShowDelay(Duration.seconds(0.5));
        resetCanvasTooltip.setShowDelay(Duration.seconds(0.5));
        weightTooltip.setShowDelay(Duration.seconds(0.5));
        clearCanvasTooltip.setShowDelay(Duration.seconds(0.5));
        speedTooltip.setShowDelay(Duration.seconds(0.5));
        startNodeTooltip.setShowDelay(Duration.seconds(0.5));
        endNodeTooltip.setShowDelay(Duration.seconds(0.5));
        algorithmTooltip.setShowDelay(Duration.seconds(0.5));


        addNodeTooltip.setText("Select this radio button to add new nodes to the canvas.");
        resetCanvasTooltip.setText("Click me to reset the whole canvas, whole graph will be deleted.");
        weightTooltip.setText("Leave this field empty to have random weights on the graph.\nelse fill this field to add a numeric weight to each new edge.");
        clearCanvasTooltip.setText("Click me to reset the colors of the graph back to its normal stage.");
        speedTooltip.setText("Change the speed of the algorithm");
        startNodeTooltip.setText("Starting node for the selected algorithm.");
        endNodeTooltip.setText("End node for the selected algorithm");
        algorithmTooltip.setText("Select type of the algorithm you wish to execute.");

        helpTooltip.setShowDelay(Duration.seconds(0.5));
        helpTooltip.setShowDuration(Duration.minutes(1));
        helpTooltip.setText("Hello and welcome to my graph algorithm visualizer.\nTo start the process, firstly add nodes to the graph.\nAfter adding nodes to the graph, connect each node with an edge, to do so use the right mouse button.\nafter you're happy with your graph, select your preferred algorithm and run it, simple as that!\nAlso note that if you hover any label or button, you'll get additional help.");

    }


    private void addEdge(Point p1) {
        Node n1 = getNodeFromCanvas(p1);
        Node n2 = getNodeFromCanvas(lastPointClicked);
        if (n1 != null && n2 != null && graph.getEdge(n1.getKey(), n2.getKey()) == null && n1.getKey() != n2.getKey()) {
            String str = weightForEdge.getText();
            float weight;
            try {
                weight = Float.parseFloat(str);
            } catch (Exception e) {
                weight = (float) Math.random() * 90 + 10;
                weight = round(weight, 1);
            }
            graph.addEdge(n1.getKey(), n2.getKey(), weight);
        }
        DrawItems.resetCanvasWithGraph(mainCanvas, graph);
    }

    private void addNode(Point p) {
        graph.addNode(counter, p);
        DrawItems.drawNode(mainCanvas.getGraphicsContext2D(), graph.getNode(counter), Color.ORANGE, Color.BLACK);
        counter++;
    }

    private boolean canAddNode(Point p) {
        for (Node n : graph.getV()) {
            if (n.getLocation().distance(p) <= DrawItems.getRadius() * 2 && movingNode != n) {
                return false;
            }
        }
        return true;
    }

    private boolean canAddEdge(Point p) {
        for (Node n : graph.getV()) {
            if (n.getLocation().distance(p) < DrawItems.getRadius()) {
                return true;
            }
        }
        return false;
    }

    private Node getNodeFromCanvas(Point p) {
        int radiusMax = DrawItems.getRadius() + 3;
        for (Node n : graph.getV()) {
            if (n.getLocation().distance(p) < radiusMax) {
                return n;
            }
        }
        return null;
    }


    private void runBFSAlgorithm() {
        float speed = speedChoiceBox.getValue();
        int src = -1;
        int dest;
        try {
            src = Integer.parseInt(startingNode.getText());
        } catch (Exception ignored) {
        }
        try {
            dest = Integer.parseInt(endingNode.getText());
        } catch (Exception e) {
            dest = -1;
        }
        if (src >= 0 && graph.getNode(src) != null) {
            BFSAlgorithm bfs = new BFSAlgorithm(graph, mainCanvas.getGraphicsContext2D(), distanceLabel, pathLabel);
            bfs.start(src, dest, speed);
        }
    }

    private void runDFSAlgorithm() {
        float speed = speedChoiceBox.getValue();
        int src = -1;
        int dest;
        try {
            src = Integer.parseInt(startingNode.getText());
        } catch (Exception ignored) {
        }
        try {
            dest = Integer.parseInt(endingNode.getText());
        } catch (Exception e) {
            dest = -1;
        }
        if (src >= 0 && graph.getNode(src) != null) {
            DFSAlgorithm dfs = new DFSAlgorithm(graph, mainCanvas.getGraphicsContext2D(), distanceLabel, pathLabel);
            dfs.start(src);
        }
    }


    private void runDijkstraAlgorithm() {
        float speed = speedChoiceBox.getValue();
        int src = -1;
        int dest = -1;
        try {
            src = Integer.parseInt(startingNode.getText());
        } catch (Exception ignored) {
        }
        try {
            dest = Integer.parseInt(endingNode.getText());
        } catch (Exception ignored) {
        }
        if (src >= 0 && dest >= 0 && graph.getNode(src) != null && graph.getNode(dest) != null) {
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, mainCanvas.getGraphicsContext2D(), distanceLabel, pathLabel);
            dijkstra.start(src, dest, speed);
        }
    }

    public static float round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    private boolean nodeInCanvas(Point p) {
        return p.x() > DrawItems.getRadius() && p.y() > DrawItems.getRadius() && p.x() < mainCanvas.getWidth() - DrawItems.getRadius() && p.y() < mainCanvas.getHeight() - DrawItems.getRadius();
    }
}