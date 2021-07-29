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
    private UndirectedGraph graph;
    private boolean[] visited;
    private int[] distance;
    private int[] pred;
    private int size;
    private GraphicsContext gc;
    private final int inf = Integer.MAX_VALUE;
    private Label distanceLabel;


    public BFSAlgorithm(UndirectedGraph graph, GraphicsContext gc, Label distanceLabel ) {
        this.graph = graph;
        this.gc = gc;
        this.size = graph.getNodeSize();
        this.visited = new boolean[size];
        this.distance = new int[size];
        this.pred = new int[size];
        this.distanceLabel = distanceLabel;
    }

    /**
     * This is a bfs algorithm to find shortest path between two nodes.
     * This function uses Task as a thread to run besides the UI thread
     *
     * @param startNode - starting node id
     * @param destNode - end node id
     */
    public void start(int startNode, int destNode, float speed) {
        long runningSpeed = (long)(1000 / speed);
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
                        DrawItems.drawEdge(gc, v,Color.BLACK);
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
                                    distanceLabel.setText("Distance From Node :" + startNode + " To Node: " + destNode + " Is: " + distance[destNode]));
                        }
                    }
                    DrawItems.drawNode(gc, n,Color.BLUE, Color.WHITE);
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

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
