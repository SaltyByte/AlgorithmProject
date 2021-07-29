package Algorithms.GraphUtil;

import Algorithms.utils.Point;

import java.util.Collection;
import java.util.HashMap;

public class UndirectedGraph {
    private final HashMap<Integer, Node> nodes;
    private final HashMap<Integer, HashMap<Integer, Edge>> edges;
    private int edgeSize;

    // Graph constructor
    public UndirectedGraph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.edgeSize = 0;
    }

    public void addNode(int key, Point location) {
        if (!nodes.containsKey(key)) {
            nodes.put(key, new Node(key, location));
        }
    }

    public void addNode(Node node) {
        if (!nodes.containsKey(node.getKey())) {
            nodes.put(node.getKey(), node);
        }
    }

    public Edge getEdge(int src, int dest) {
        if (dest != src && edges.containsKey(src) && edges.get(src).containsKey(dest)) {
            return edges.get(src).get(dest);
        }
        return null;
    }

    public void removeNode(Node n) {
        if (n != null){
            int size = getE(n.getKey()).size();
            for (int i = 0; i < size; i++) {
                Edge e = getE(n.getKey()).iterator().next();
                removeEdge(e);
            }
            nodes.remove(n.getKey());
        }
    }

    public void removeEdge(Edge e) {
        if (e != null) {
            edges.get(e.getSrc().getKey()).remove(e.getDest().getKey());
            edges.get(e.getDest().getKey()).remove(e.getSrc().getKey());
            edgeSize--;
        }
    }

    public void addEdge(int src, int dest, float weight) {
        if (dest != src && weight > 0 && nodes.containsKey(src) && nodes.containsKey(dest)) {
            if (!edges.containsKey(src)) {
                HashMap<Integer, Edge> innerMap = new HashMap<>();
                edges.put(src, innerMap);
            }
            if (!edges.containsKey(dest)) {
                HashMap<Integer, Edge> innerMap = new HashMap<>();
                edges.put(dest, innerMap);
            }
            if (!edges.get(src).containsKey(dest)) {
                Node n1 = nodes.get(src);
                Node n2 = nodes.get(dest);
                Edge e1 = new Edge(n1, n2, weight);
                Edge e2 = new Edge(n2, n1, weight);
                edges.get(src).put(dest, e1);
                edges.get(dest).put(src, e2);
                edgeSize++;
            }
        }
    }

    public Collection<Node> getV() {
        return nodes.values();
    }


    public Collection<Edge> getE(int key) {
        if (!edges.containsKey(key)) {
            return new HashMap<Integer, Edge>().values();
        }
        return edges.get(key).values();
    }

    public Node getNode(int data) {
        return nodes.get(data);
    }

    public int getEdgeSize() {
        return edgeSize;
    }

    public int getNodeSize() {
        return nodes.size();
    }
}
