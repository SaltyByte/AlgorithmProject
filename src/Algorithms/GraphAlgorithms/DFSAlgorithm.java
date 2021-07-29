package Algorithms.GraphAlgorithms;

import Algorithms.GraphUtil.Edge;
import Algorithms.GraphUtil.Node;
import Algorithms.GraphUtil.UndirectedGraph;
import javafx.scene.canvas.GraphicsContext;

import java.util.Stack;

public class DFSAlgorithm {
    private UndirectedGraph graph;
    private boolean[] visited;
    private GraphicsContext gc;


    public DFSAlgorithm(UndirectedGraph graph, GraphicsContext gc){
        this.graph = graph;
        this.gc = gc;
        this.visited = new boolean[graph.getNodeSize()];

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
