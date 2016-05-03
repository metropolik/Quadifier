package de.vf.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import de.vf.logichelper.Matrix33;
import de.vf.logichelper.Vector3;


public class Cell {
    private final static int NORTH = 1;
    private final static int EAST = 2;
    private final static int SOUTH = 4;
    private final static int WEST = 8;
    
    private int x, y, x2, y2;
    private int status;    
    private Color gonc;
    
    public Cell(int x, int y, int x2, int y2, Color gonc) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;  
        this.gonc = gonc;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int i) {
        status = i;
    }
    
    public double centerX() {
        return (double) (x + x2) / 2.0;
    }
    
    public double centerY() {
        return (double) (y + y2) / 2.0;
    }
    
    public void activate(int bit) {
        status = (status | ((int) Math.pow(2, bit)));
    }        
    
    public void appyMatrix(Matrix33 m) {
        Vector3 topLeft = new Vector3(x, y, 1.0);
        Vector3 ntopLeft = m.multiply(topLeft);     
        x = (int) ntopLeft.getX();
        y = (int) ntopLeft.getY();
        
        Vector3 botRight = new Vector3(x2, y2, 1.0);
        Vector3 nbotRight = m.multiply(botRight);
        x2 = (int) nbotRight.getX();
        y2 = (int) nbotRight.getY();
    }
    
    public List<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        Point a = new Point(x, y);
        Point b = new Point(x2, y);
        Point c = new Point(x2, y2);
        Point d = new Point(x, y2);
        
        if ((status & NORTH) > 0) {
            edges.add(new Edge(a, b));
        }
        if ((status & EAST) > 0) {
            edges.add(new Edge(b, c));
        }
        if ((status & SOUTH) > 0) {
            edges.add(new Edge(c, d));
        }
        if ((status & WEST) > 0) {
            edges.add(new Edge(d, a));
        }        
        return edges;
    }
    
    public void paintBG(Graphics2D g) {     
        //background
        if (status != 0) { 
            g.setColor(gonc);
            g.fillRect(x, y, x2 - x, y2 - y);            
        }
    }
    
    public void setBGColor(Color col) {
        this.gonc = col;
    }
    
    public void paintGO(Graphics2D g) {
        //general outline
        if (status != 15) {            
            g.drawRect(x, y, x2 - x, y2 - y);
        }
    }
    
    public void paintDO(Graphics2D g) {        
        //directed outline                        
        if ((status & 1) > 0) { // N            
            g.drawLine(x, y, x2, y);
        } 
        if ((status & 2) > 0) { // O
            g.drawLine(x2, y, x2, y2); 
        } 
        if ((status & 4) > 0) { // S
            g.drawLine(x, y2, x2, y2);
        } 
        if ((status & 8) > 0) { // W
            g.drawLine(x, y, x, y2);
        }              
    }
}
