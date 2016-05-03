package de.vf.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Quad {
    private Point a, b, c, d;
    Quad(Point a, Point b, Point c, Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    public void paint(Graphics2D g) {          
        g.setColor(new Color(0, 60, 0));
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        drawLine(g, a, b);
        drawLine(g, b, c);
        drawLine(g, c, d);
        drawLine(g, d, a);
        g.setStroke(oldStroke);  
    }    
    
    public void drawLine(Graphics2D g, Point v, Point w) {
        g.drawLine(v.x, v.y, w.x, w.y);
    }
    
    public String toString() {
        String o = "";
        o += a.toString() + " ";
        o += b.toString() + " ";
        o += c.toString() + " ";
        o += d.toString() + " ";
        return o;
    }
    
    
}
