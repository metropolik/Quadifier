package de.vf.logic;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.vf.logichelper.Vector2;
import de.vf.logichelper.Vector3;

public class Gobject {    
    private List<Point> outerEdgePoints;
    private List<Point> outerEdgePointsUntouched;
    public List<Point> selected; // only for debugging purposes
    public List<Edge> edges; // all the edges of this object //RAW
    public List<Quad> quads;
    
    public Gobject() {               
        outerEdgePoints = new ArrayList<Point>();       
        selected = new ArrayList<Point>();
        edges = new ArrayList<Edge>();
        quads = new ArrayList<Quad>();
        
    }       
    
    public void addEdge(Edge e) {
        edges.add(e);
    }    
    
    public void computeOuterEdge() {                
        System.out.println("Edges: " + edges.size());        
        ArrayList<Edge> sortedEdges = new ArrayList<Edge>();
        sortedEdges.add(edges.get(0));
        edges.remove(0);
        while(!edges.isEmpty()) {
            Edge pre = sortedEdges.get(sortedEdges.size() - 1); //get last one of list      
            
            for (int i = 0; i < edges.size(); i++) {
                Edge current = edges.get(i); 
                if (pre.isPredecessor(current)) {
                    edges.remove(i);
                    sortedEdges.add(current);
                    break;
                }
            }            
        }
        System.out.println("sorted!");
        
        
        Edge first = sortedEdges.get(0);
        Edge last = sortedEdges.get(sortedEdges.size() - 1);
        if (first.getOrientNS() == last.getOrientNS()) {
            first.killA();
            last.killB();
        }
                
        
        for (int i = 0; i < sortedEdges.size() - 1; i++) {
            if (sortedEdges.get(i).getOrientNS() == sortedEdges.get(i + 1).getOrientNS()) {
                sortedEdges.get(i).killB();
                sortedEdges.get(i + 1).killA();
            } else {
                sortedEdges.get(i).killB();
            }
            
        }
        
        outerEdgePoints = new ArrayList<Point>();        
        for (int i = 0; i < sortedEdges.size(); i++) {
            outerEdgePoints.addAll(sortedEdges.get(i).getCountingPoints());            
        }        

        if (outerEdgePoints.get(0).equals(outerEdgePoints.get(outerEdgePoints.size() - 1))) {
            outerEdgePoints.remove(outerEdgePoints.size() - 1);
        }
        outerEdgePointsUntouched = new ArrayList<Point>(outerEdgePoints);
        System.out.println("Corners: " + outerEdgePoints.size());           
    }
        
    
    public void quadify() {
        int lastTime = 2;
        while (outerEdgePoints.size() != 0) {
            if (outerEdgePoints.size() == lastTime) {
                System.err.println("model got a whole");
                return;
            }
            lastTime = outerEdgePoints.size();
            selected = new ArrayList<Point>();
            for (int i = 0; i < outerEdgePoints.size(); i++) {               
                Point a = outerEdgePoints.get(mod((i - 1), outerEdgePoints.size()));
                Point b = outerEdgePoints.get(i);
                Point c = outerEdgePoints.get(mod((i + 1), outerEdgePoints.size()));                               
                if (triangleIsValid(a, b, c)) {                    
                    Point ps_b = new Point(a.x, c.y);
                    Point ps_d = new Point(c.x, a.y);
                    Point d = ps_b.equals(b) ? ps_d : ps_b;                                                                              
                    
                    if (outerEdgePoints.get(mod((i - 2), outerEdgePoints.size())).equals(d) 
                            || outerEdgePoints.get(mod((i + 2), outerEdgePoints.size())).equals(d)) {
                        // is pickel quad
                        //System.out.println("d already existed => is pickel");                        
                        //remove all 4                                                 
                        outerEdgePoints.remove(a);
                        outerEdgePoints.remove(b);
                        outerEdgePoints.remove(c);
                        outerEdgePoints.remove(d);
                    } else {                         
                        //System.out.println("is reduceable");
                        outerEdgePoints.add(i, d);
                        outerEdgePoints.remove(a);
                        outerEdgePoints.remove(b);
                        outerEdgePoints.remove(c);                                                                       
                    }                    
                    addQuad(a, b, c, d);
                    //System.out.println("breaking");
                    break;
                }                
            }
            //System.out.println("remaining: " + outerEdgePoints.size());
        }
        System.out.println("created " + quads.size() + " quads");
    }
    
    public void addQuad(Point a, Point b, Point c, Point d) {        
        Quad q = new Quad(a, b, c, d);
        //System.out.println(q.toString());
        quads.add(q);
    }
    
    public int mod(int x, int n) {
        int r = x % n;
        if (r < 0)
        {
            r += n;
        }
        return r;
    }
        
    
    //b is expected to be the center
    public boolean triangleIsValid(Point a, Point b, Point c) {        
        if (!upDown(a, b, c)) {
            return false; //triangle is not in gobject
        }
        
        
//        for (int i = 0; i < outerEdgePoints.size(); i++) {
//            Point p = outerEdgePoints.get(i);
//            if (pointInTriangle(a, b, c, p)) {
//                return false; // a point is the triangle so stop
//            }
//        }
        
        //test if a quad would be possible
        
        for (int i = 0; i < outerEdgePointsUntouched.size(); i++) {
            Point p = outerEdgePointsUntouched.get(i);
            if (pointInQuad(a, c, p)) {
                return false; // a point is the quad so stop
            }
        }
        return true;        
    }
    
    public boolean pointAlreadyExists(Point p) {
        int count = 0;
        for (int i = 0; i < outerEdgePoints.size(); i++) {
            if (outerEdgePoints.get(i).equals(p)) {
                count++;
                if (count >= 2) { // one is the point itself and one is the other
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean pointInTriangle(Point a, Point b, Point c, Point p) {
        Vector3 r = barycentric(a, b, c, p);
        return (r.getY() >= 0.0 && r.getZ() >= 0 && (r.getY() + r.getZ()) <= 1.0);
    }
    
    //takes corner point a and b of a quad to see if p is in the quad
    public boolean pointInQuad(Point a, Point c, Point p) {
        int ax = a.x < c.x ? a.x : c.x;
        int cx = a.x < c.x ? c.x : a.x;
        int ay = a.y < c.y ? a.y : c.y;
        int cy = a.y < c.y ? c.y : a.y;
        return p.x > ax && p.x < cx
                && p.y > ay && p.y < cy;
    }
    
    public Vector3 barycentric(Point a, Point b, Point c, Point p) {
        Vector2 v0 = new Vector2(b).sub(new Vector2(a));
        Vector2 v1 = new Vector2(c).sub(new Vector2(a));
        Vector2 v2 = new Vector2(p).sub(new Vector2(a));
        float d00 = v0.dot(v0);
        float d01 = v0.dot(v1);
        float d11 = v1.dot(v1);
        float d20 = v2.dot(v0);
        float d21 = v2.dot(v1);
        float denom = d00 * d11 - d01 * d01;
        float v = (d11 * d20 - d01 * d21) / denom;
        float w = (d00 * d21 - d01 * d20) / denom;
        float u = 1.0f - v - w;
        return new Vector3(u, v, w);
    }           
           
    
    /**
     * Figures out whether a triangle is in or out of the shape
     * 
     * @param n the index of the center vertex of the triangle
     * @return true = potential candidate, false = triangle is not in the model
     */
    public boolean upDown(Point a, Point b, Point c) {                   
        Point ba = new Point(a.x - b.x, a.y - b.y);
        Point bc = new Point(c.x - b.x, c.y - b.y);
        boolean tt = !crossp(ba, bc);
        if (!(isRemainingPolygonClockwise() ^ tt)) {
            System.err.println("CLOCKWISE DOES NOT ALIGN");
        }
        return tt;
    }
    
    public boolean isRemainingPolygonClockwise() {
        int sum = 0;
        for (int i = 0; i < outerEdgePoints.size() - 1; i++) {
            Point n = outerEdgePoints.get(i + 1);
            Point c = outerEdgePoints.get(i);
            sum += (n.x - c.x) * (n.y + c.y);
        }
        return sum > 0;
    }
        
    public boolean crossp(Point a, Point b) {
        return (a.x * b.y - a.y * b.x) > 0;
    }
    
    public void paintGobject(Graphics2D g) {        
        g.setColor(new Color(0, 0, 0));
        for (int i = 0; i < outerEdgePoints.size(); i++) {
            Point cp = outerEdgePoints.get(i);                        
            g.fillRect(cp.x - 3, cp.y - 3, 7, 7);                       
        }
        g.setColor(new Color(250, 0, 0));
        for (int i = 0; i < selected.size(); i++) {
            Point cp = selected.get(i);  
            g.fillRect(cp.x - 3, cp.y - 3, 7,7);
        }        
        
        //all quads
        for (int i = 0; i < quads.size(); i++) {            
            quads.get(i).paint(g);
        }
        //edge line of remaining
        g.setColor(new Color(250, 50, 50));
        for (int i = 0; i < outerEdgePoints.size(); i++) {
            int size = outerEdgePoints.size();
            Point a = outerEdgePoints.get(mod(i - 1, size));
            Point b = outerEdgePoints.get(mod(i , size));
            g.drawLine(a.x, a.y, b.x, b.y);
        }
                
    }
    
    
    
    
    
}
