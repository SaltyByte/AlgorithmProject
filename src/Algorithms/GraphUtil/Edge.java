package Algorithms.GraphUtil;

public class Edge {
    private Node src;
    private Node dest;
    private float weight;

    public Edge(Node src, Node dest, float weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Node getSrc() {
        return src;
    }

    public Node getDest() {
        return dest;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "Edge Data:{src= " + src + ", dest= " + dest + ", weight= " + weight + '}';
    }
}
