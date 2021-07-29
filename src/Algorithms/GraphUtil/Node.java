package Algorithms.GraphUtil;

import Algorithms.utils.Point;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Node {
    private int key;
    private Point location;
    private Color textColor;
    private Color nodeColor;


    public Node(int data, Point location) {
        this.key = data;
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location){
        this.location = location;
    }

    public void setData(int data) {
        this.key = data;
    }

    public int getKey() {
        return key;
    }

    public Color getNodeColor() {
        return nodeColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setNodeColor(Color nodeColor) {
        this.nodeColor = nodeColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "Node Data:{data= " + key + ", location= " + location + '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Node node = (Node) other;
        return key == node.key && node.location.equals(location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, location);
    }
}
