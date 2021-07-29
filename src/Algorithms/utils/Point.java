package Algorithms.utils;

import java.util.Objects;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x();
        this.y = p.y();
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double distance(Point other) {
        double dx = this.x() - other.x();
        double dy = this.y() - other.y();
        double t = (dx * dx + dy * dy);
        return Math.sqrt(t);
    }

    public boolean distanceFromPoint(Point p2) {
        return (this.distance(p2) < 0.001);
    }

    @Override
    public String toString() {
        return "[" + x() + ", " + y() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
