package Algorithms.GraphAlgorithms;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import Algorithms.utils.DrawItems;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Stack;

public class DFSAlgorithm {
    private UndirectedGraph graph;
    private boolean[] visited;
    private GraphicsContext gc;



    public DFSAlgorithm(UndirectedGraph graph, GraphicsContext gc) {
        this.graph = graph;
        this.gc = gc;
        this.visited = new boolean[graph.getNodeSize()];

    }

    public void start(int startNode, float speed) { // fix this algorithm, all wrong here
        long runningSpeed = (long) (1000 / speed);
        Task<Void> algorithmTask = new Task<>() {
            @Override
            protected Void call() {
                DFS(startNode, runningSpeed);
                return null;
            }
        };
        new Thread(algorithmTask).start();
    }

    private void DFS(int nextNode, long runningSpeed){
        visited[nextNode] = true;
        DrawItems.drawNode(gc, graph.getNode(nextNode), Color.RED, Color.BLACK);
        sleep(runningSpeed);
        for (Edge e : graph.getE(nextNode)) {
            if (!visited[e.getDest().getKey()]){
                DrawItems.drawEdge(gc, e, Color.BLACK);
                DFS(e.getDest().getKey(), runningSpeed);
            }
        }
        DrawItems.drawNode(gc, graph.getNode(nextNode), Color.BLUE, Color.WHITE);
        sleep(runningSpeed);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
