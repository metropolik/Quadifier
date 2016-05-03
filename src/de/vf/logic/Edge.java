package de.vf.logic;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Edge {
    private Point a;
    private Point b;
    private boolean orientNS;
    
    public Edge(Point a, Point b) {
        this.a = a;
        this.b = b;
        if (a.getX() == b.getX()) {
            orientNS = true;
        } else {
            orientNS = false;
        }
    }
    
    public boolean getOrientNS() {
        return orientNS;
    }
    
    public List<Point> getCountingPoints() {
        ArrayList<Point> al = new ArrayList<Point>();
        if (a != null) {
            al.add(a);
        }
        if (b != null) {
            al.add(b);
        }
        return al;
    }
    
    public boolean isPredecessor(Edge other) {
        return b.equals(other.getA());   
    }

    public Point getA() {
        return a;
    }   

    public Point getB() {
        return b;
    }   
    
    public void killA() {
        a = null;
    }
    
    public void killB() {
        b = null;
    }
    
    public String toString() {
        return String.valueOf(a) + " " + String.valueOf(b);
    }
}
