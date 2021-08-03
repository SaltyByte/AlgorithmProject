package Algorithms.GraphAlgorithms;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

import java.util.Stack;

public class DFSAlgorithm {
    private UndirectedGraph graph;
    private boolean[] visited;
    private GraphicsContext gc;
    private final Label distanceLabel;
    private final Label pathLabel;


    public DFSAlgorithm(UndirectedGraph graph, GraphicsContext gc, Label distanceLabel, Label pathLabel){
        this.graph = graph;
        this.gc = gc;
        this.visited = new boolean[graph.getNodeSize()];
        this.distanceLabel = distanceLabel;
        this.pathLabel = pathLabel;
    }
    public void start(int startNode){
        Stack<Node> stack = new Stack<>();
        visited[startNode] = true;
        stack.push(graph.getNode(startNode));
        while (!stack.isEmpty()){
            Node n = stack.pop();
            if (!visited[n.getKey()]) {
                visited[n.getKey()] = true;
                for (Edge v : graph.getE(n.getKey())){
                    stack.push(v.getDest());
                }
            }
        }
    }
}
