package Algorithms.utils;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;


// need to make this class draw graph, which only draws the graph
public class DrawItems {
    static final int radius = 10;

    public static void drawLegend(Canvas mainCanvas) {
        int yOffSet = radius * 2 + 5;
        int xOffSet = radius + 5;
        int textXOffSet = xOffSet + radius + 5;
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        Point red = new Point(xOffSet, yOffSet);
        Point yellow = new Point(xOffSet, yOffSet * 2);
        Point blue = new Point(xOffSet, yOffSet * 3);
        Point aForGrayEdge = new Point(xOffSet - radius, yOffSet * 4 - radius);
        Point bForGrayEdge = new Point(xOffSet + radius, yOffSet * 4 + radius);
        Point aForBlackEdge = new Point(xOffSet - radius, yOffSet * 5 - radius);
        Point bForBlackEdge = new Point(xOffSet + radius, yOffSet * 5 + radius);
        drawCircle(gc, red, Color.RED);
        drawCircle(gc, yellow, Color.YELLOW);
        drawCircle(gc, blue, Color.BLUE);
        drawLine(gc, aForGrayEdge, bForGrayEdge, Color.GRAY);
        drawLine(gc, aForBlackEdge, bForBlackEdge, Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.strokeText("Discovered Node", textXOffSet, yOffSet + 3);
        gc.strokeText("Current Node", textXOffSet, yOffSet * 2 + 3);
        gc.strokeText("Finished Node", textXOffSet, yOffSet * 3 + 3);
        gc.strokeText("Un-visited Edge", textXOffSet, yOffSet * 4 + 3);
        gc.strokeText("Visited Edge", textXOffSet, yOffSet * 5 + 3);
    }


    public static void drawGraph(GraphicsContext gc, UndirectedGraph graph) {
        for (Node node : graph.getV()) {
            drawNode(gc, graph.getNode(node.getKey()), Color.ORANGE, Color.BLACK);
            for (Edge n : graph.getE(node.getKey())) {
                drawEdge(gc, n, Color.GRAY);
            }
        }
    }

    // this method draws the edge
    public static void drawEdge(GraphicsContext gc, Edge edge, Color edgeColor) {
        if (edge != null) {
            Node src = edge.getSrc();
            Node dest = edge.getDest();
            float weight = edge.getWeight();
            double textPosX = (src.getLocation().x() + dest.getLocation().x()) / 2;
            double textPosY = (src.getLocation().y() + dest.getLocation().y()) / 2;
            gc.setLineWidth(1.3);
            gc.setStroke(edgeColor);
            gc.strokeLine(src.getLocation().x(), src.getLocation().y(), dest.getLocation().x(), dest.getLocation().y());
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(1);
            gc.strokeText("" + weight, textPosX, textPosY);
            drawNode(gc, src, src.getNodeColor(), src.getTextColor());
            drawNode(gc, dest, dest.getNodeColor(), dest.getTextColor());
        }
    }


    public static void resetCanvasWithGraph(Canvas mainCanvas, UndirectedGraph graph) {
        mainCanvas.getGraphicsContext2D().setFill(Color.WHITE);
        mainCanvas.getGraphicsContext2D().fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        drawGraph(mainCanvas.getGraphicsContext2D(), graph);
        drawLegend(mainCanvas);
    }

    public static void drawLine(GraphicsContext gc, Point a, Point b, Color color) {
        if (a != null && b != null) {
            gc.setStroke(color);
            gc.setLineWidth(1);
            gc.strokeLine(a.x(), a.y(), b.x(), b.y());
        }
    }

    // this method draws the node
    public static void drawNode(GraphicsContext gc, Node node, Color nodeColor, Color textColor) {
        if (node != null) {
            Point location = node.getLocation();
            int data = node.getKey();
            gc.setFill(nodeColor);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.fillOval(location.x() - radius, location.y() - radius, radius * 2, radius * 2);
            gc.strokeOval(location.x() - radius, location.y() - radius, radius * 2, radius * 2);
            gc.setStroke(textColor);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("" + data, location.x(), location.y() + 3);
            node.setNodeColor(nodeColor);
            node.setTextColor(textColor);
        }
    }

    // this method draws circle
    private static void drawCircle(GraphicsContext gc, Point p, Color color) {
        if (p != null) {
            gc.setFill(color);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.fillOval(p.x() - radius, p.y() - radius, radius * 2, radius * 2);
            gc.strokeOval(p.x() - radius, p.y() - radius, radius * 2, radius * 2);
        }
    }


    public static int getRadius() {
        return radius;
    }
}
