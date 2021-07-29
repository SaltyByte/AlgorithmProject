package Algorithms.GraphAlgorithms;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import javafx.scene.canvas.GraphicsContext;

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


    public DijkstraAlgorithm(UndirectedGraph graph, GraphicsContext gc){
        this.graph = graph;
        this.gc = gc;
        this.size = graph.getNodeSize();
        this.visited = new boolean[size];
        this.distance = new float[size];
        this.pred = new int[size];
    }
    public void start(int startNode){
        PriorityQueue<Node> pq = new PriorityQueue<>(new WeightComparator());
        for (int i = 0; i < size; i++) {
            distance[i] = inf;
            pred[i] = -1;
        }
        visited[startNode] = true;
        distance[startNode] = 0;
        pq.add(graph.getNode(startNode));
        while (!pq.isEmpty()){
            Node n = pq.poll();
            for (Edge v : graph.getE(n.getKey())){
                if (!visited[v.getDest().getKey()]) {
                    float tempDist = distance[n.getKey()] + v.getWeight();
                    if (tempDist < distance[v.getDest().getKey()]) {
                        distance[v.getDest().getKey()] = tempDist;
                        pred[v.getDest().getKey()] = n.getKey();
                    }
                    if (!pq.contains(v.getDest())) {
                        pq.add(v.getDest());
                    }
                }
            }
            visited[n.getKey()] = true;
        }
    }
    public float[] getDistance() {
        return distance;
    }
    public int[] getPred() {
        return pred;
    }


    private class WeightComparator implements Comparator<Node> {

        /**
         * Overrides compare method by tag (weight), if node1 is greater than node2 return 1,
         * if node1 is less than node2 return -1, else return 0.
         * used in the priorityQueue.
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

