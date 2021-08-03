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

import java.util.Comparator;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
    private UndirectedGraph graph;
    private boolean[] visited;
    private float[] distance;
    private int[] pred;
    private int size;
    private GraphicsContext gc;
    private final float inf = Float.MAX_VALUE;
    private final Label distanceLabel;
    private final Label pathLabel;


    public DijkstraAlgorithm(UndirectedGraph graph, GraphicsContext gc, Label distanceLabel, Label pathLabel) {
        this.graph = graph;
        this.gc = gc;
        this.size = graph.getNodeSize();
        this.visited = new boolean[size];
        this.distance = new float[size];
        this.pred = new int[size];
        this.distanceLabel = distanceLabel;
        this.pathLabel = pathLabel;
    }

    public void start(int startNode, int destNode, float speed) {
        long runningSpeed = (long) (1000 / speed);
        Task<Void> algorithmTask = new Task<>() {
            @Override
            protected Void call() {
                PriorityQueue<Node> pq = new PriorityQueue<>(new WeightComparator());
                for (int i = 0; i < size; i++) {
                    distance[i] = inf;
                    pred[i] = -1;
                }
                visited[startNode] = true;
                distance[startNode] = 0;
                pq.add(graph.getNode(startNode));
                while (!pq.isEmpty()) {
                    Node n = pq.poll();
                    DrawItems.drawNode(gc, n, Color.YELLOW, Color.BLACK);
                    sleep(runningSpeed);
                    for (Edge v : graph.getE(n.getKey())) {
                        DrawItems.drawEdge(gc, v, Color.BLACK);
                        if (!visited[v.getDest().getKey()]) {
                            DrawItems.drawNode(gc, v.getDest(), Color.RED, Color.BLACK);
                            sleep(runningSpeed);
                            float tempDist = distance[n.getKey()] + v.getWeight();
                            if (tempDist < distance[v.getDest().getKey()]) {
                                distance[v.getDest().getKey()] = tempDist;
                                pred[v.getDest().getKey()] = n.getKey();
                            }
                            if (!pq.contains(v.getDest())) {
                                pq.add(v.getDest());
                            }
                            if (v.getDest().getKey() == destNode) {
                                // need to update label like this, otherwise it crashes.
                                Platform.runLater(() ->
                                        distanceLabel.setText("Distance From Node: " + startNode + " To Node: " + destNode + " Is: " + round(distance[destNode], 1)));

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

    public static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    public float[] getDistance() {
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


    private class WeightComparator implements Comparator<Node> {

        /**
         * Overrides compare method by tag (weight), if node1 is greater than node2 return 1,
         * if node1 is less than node2 return -1, else return 0.
         * used in the priorityQueue.
         *
         * @param node1 - node1 to compare
         * @param node2 - node2 to compare
         * @return If node1 is greater than node2 return 1, if node1 is less than node2 return -1, else return 0.
         */
        @Override
        public int compare(Node node1, Node node2) {
            double sum1 = distance[node1.getKey()];
            double sum2 = distance[node2.getKey()];
            if (sum1 > sum2) {
                return 1;
            } else if (sum1 < sum2) {
                return -1;
            }
            return 0;
        }
    }


}

