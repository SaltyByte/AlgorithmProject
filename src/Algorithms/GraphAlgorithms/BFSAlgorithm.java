package Algorithms.GraphAlgorithms;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;

import Algorithms.utils.DrawItems;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Queue;


public class BFSAlgorithm {
    private final UndirectedGraph graph;
    private final boolean[] visited;
    private final int[] distance;
    private final int[] pred;
    private final int size;
    private final GraphicsContext gc;
    private final int inf = Integer.MAX_VALUE;
    private final Label distanceLabel;
    private final Label pathLabel;


    public BFSAlgorithm(UndirectedGraph graph, GraphicsContext gc, Label distanceLabel, Label pathLabel) {
        this.graph = graph;
        this.gc = gc;
        this.size = graph.getNodeSize();
        this.visited = new boolean[size];
        this.distance = new int[size];
        this.pred = new int[size];
        this.distanceLabel = distanceLabel;
        this.pathLabel = pathLabel;
    }

    /**
     * This is a bfs algorithm to find shortest path between two nodes.
     * This function uses Task as a thread to run besides the UI thread
     *
     * @param startNode - starting node id
     * @param destNode  - end node id
     */
    public void start(int startNode, int destNode, float speed) {
        long runningSpeed = (long) (1000 / speed);
        Task<Void> algorithmTask = new Task<>() {
            @Override
            protected Void call() {
                Queue<Node> queue = new ArrayDeque<>();
                for (int i = 0; i < size; i++) {
                    distance[i] = inf;
                    pred[i] = -1;
                }
                visited[startNode] = true;
                distance[startNode] = 0;
                queue.add(graph.getNode(startNode));
                while (!queue.isEmpty()) {
                    Node n = queue.poll();
                    DrawItems.drawNode(gc, n, Color.YELLOW, Color.BLACK);
                    sleep(runningSpeed);
                    for (Edge v : graph.getE(n.getKey())) {
                        DrawItems.drawEdge(gc, v, Color.BLACK);
                        if (!visited[v.getDest().getKey()]) {
                            DrawItems.drawNode(gc, v.getDest(), Color.RED, Color.BLACK);
                            sleep(runningSpeed);
                            visited[v.getDest().getKey()] = true;
                            distance[v.getDest().getKey()] = distance[v.getSrc().getKey()] + 1;
                            pred[v.getDest().getKey()] = n.getKey();
                            queue.add(v.getDest());
                        }
                        if (v.getDest().getKey() == destNode) {
                            // need to update label like this, otherwise it crashes.
                            Platform.runLater(() ->
                                    distanceLabel.setText("Distance From Node: " + startNode + " To Node: " + destNode + " Is: " + distance[destNode]));
                            Platform.runLater(() -> {
                                        StringBuilder path = new StringBuilder();
                                        int parent = destNode;
                                        path.append(getReverseInt(parent));
                                        parent = pred[parent];
                                        while (parent != -1) { // loop to build path
                                            path.append(">-").append(getReverseInt(parent));
                                            parent = pred[parent];
                                        }
                                        path.reverse();
                                        pathLabel.setText("Path: " + path);
                                    }
                            );
                        }
                    }
                    DrawItems.drawNode(gc, n, Color.BLUE, Color.WHITE);
                    visited[n.getKey()] = true;
                    sleep(runningSpeed);
                }
                return null;
            }
        };
        new Thread(algorithmTask).start();
    }


    public int[] getDistance() {
        return distance;
    }

    public int[] getPred() {
        return pred;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getReverseInt(int value) {
        int resultNumber = 0;
        for (int i = value; i != 0; i /= 10) {
            resultNumber = resultNumber * 10 + i % 10;
        }
        return resultNumber;
    }

}
