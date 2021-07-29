package mainPackage;

import Algorithms.GraphAlgorithms.BFSAlgorithm;
import Algorithms.GraphAlgorithms.DFSAlgorithm;
import Algorithms.GraphAlgorithms.DijkstraAlgorithm;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import Algorithms.utils.*;

import Algorithms.utils.Point;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


//todo !!! to delete latest, delete node from graph, redraw all the graph

public class MainWindowController implements Initializable {

    private UndirectedGraph graph;
    private int counter;
    private Point lastPointClicked;
    private Node movingNode;


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
    private ChoiceBox<String> algorithmTypeChoiceBox;
    @FXML
    private ChoiceBox<String> algorithmChoiceBox;
    @FXML
    private ChoiceBox<Float> speedChoiceBox;
    @FXML
    private Label distanceLabel;


    @FXML
    private void handleMouseClicked(MouseEvent event) {
        lastPointClicked = new Point(event.getX(), event.getY());
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
    }

    @FXML
    private void runAlgorithm() {
        if (algorithmChoiceBox.getValue() != null) {
            if (algorithmTypeChoiceBox.getValue().equals("Graphs")) {
                DrawItems.resetCanvasWithGraph(mainCanvas, graph);
                switch (algorithmChoiceBox.getValue()) {
                    case "BFS" -> runBFSAlgorithm();
                    case "Dijkstra" -> runDijkstraAlgorithm();
                    case "DFS" -> runDFSAlgorithm();
                }
            } else if (algorithmTypeChoiceBox.getValue().equals("Grid-Type")) {


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
        DrawItems.drawLegend(mainCanvas);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        counter = 0;
        graph = new UndirectedGraph();
        algorithmChoiceBox.getItems().add("BFS");
        algorithmChoiceBox.getItems().add("DFS");
        algorithmChoiceBox.getItems().add("Dijkstra");
        algorithmChoiceBox.getItems().add("Floyd-Warshall");
        algorithmTypeChoiceBox.getItems().add("Grid-Type");
        algorithmTypeChoiceBox.getItems().add("Graphs");
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
        algorithmTypeChoiceBox.setValue("Select Algorithm Type");
        DrawItems.drawLegend(mainCanvas);
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
        int src = Integer.parseInt(startingNode.getText());
        int dest;
        try {
            dest = Integer.parseInt(endingNode.getText());
        }
        catch (Exception e){
            dest = -1;
        }
        if (graph.getNode(src) != null) {
            BFSAlgorithm bfs = new BFSAlgorithm(graph, mainCanvas.getGraphicsContext2D(), distanceLabel);
            bfs.start(src, dest, speed);
        }
    }

    private void runDFSAlgorithm() {
        float speed = speedChoiceBox.getValue();
        int src = Integer.parseInt(startingNode.getText());
        if (graph.getNode(src) != null) {
            DFSAlgorithm dfs = new DFSAlgorithm(graph, mainCanvas.getGraphicsContext2D());
            dfs.start(src);
        }
    }


    private void runDijkstraAlgorithm() {
        float speed = speedChoiceBox.getValue();
        int src = Integer.parseInt(startingNode.getText());
        int dest = Integer.parseInt(endingNode.getText());
        if (graph.getNode(src) != null && graph.getNode(dest) != null) {
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, mainCanvas.getGraphicsContext2D());
            dijkstra.start(src);
            float distance = dijkstra.getDistance()[dest];
            if (distance == Double.MAX_VALUE) {
                distance = -1;
            }
            distance = round(distance, 1);
            distanceLabel.setText("Distance From Node :" + src + " To Node: " + dest + " Is: " + distance);
        }
    }

    private static float round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    private boolean nodeInCanvas(Point p) {
        return p.x() > DrawItems.getRadius() && p.y() > DrawItems.getRadius() && p.x() < mainCanvas.getWidth() - DrawItems.getRadius() && p.y() < mainCanvas.getHeight() - DrawItems.getRadius();
    }
}