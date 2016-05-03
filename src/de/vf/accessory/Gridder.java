package de.vf.accessory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import de.vf.logic.Cell;
import de.vf.logic.Gobject;

public class Gridder extends JPanel {
    private final static int NORTH = 1;
    private final static int EAST = 2;
    private final static int SOUTH = 4;
    private final static int WEST = 8;    
    private final static int GON = 16;
    private final static int TAKEN = 32;
    private final static int dimen = 30;
    
    private int gobjc;
    private Color gonc;      
    private Cell[][] cells;
    private ArrayList<Gobject> gobjs;
    private BufferedImage canvas;    
    
    public Gridder() {
        cells = new Cell[20][20];
        gobjs = new ArrayList<Gobject>();
        gonc = new Color(50, 150, 250); 
        
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(i * dimen, j * dimen, (i + 1) * dimen, (j + 1) * dimen, gonc);
            }
        }                        
        drawCanvas();    
        updateCorners();        
        for (int i = 0; i < gobjs.size(); i++) {
            gobjs.get(i).computeOuterEdge();            
        }
        gobjc = 0;
        repaint();
    }
    
    public void updateCorners() {        
        //find first on none taken block
        gobjs = new ArrayList<Gobject>();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if ((cell(i, j) & GON) > 0 && (cell(i, j) & TAKEN) == 0) { //general on and not taken   
                    gobjs.add(new Gobject());
                    flood4(i, j);                    
                }
            }
        }
        System.out.println("Found " + String.valueOf(gobjs.size()) + " objects");
        
    }       
    
    public void quadify() {
        gobjs.get(gobjc).quadify();
        gobjc++;
        repaint();
    }
    
    public void flood4(int x, int y) {
        //return if cell is already taken
        if ((cell(x, y) & TAKEN) > 0) {
            return;
        }
        
        boolean isBorder = false;
        setCell(x, y, GON | TAKEN);       
        //NORTH
        if ( y - 1 >= 0 && isGon(x, y - 1)) {            
            flood4(x, y - 1);
        } else {
            setCell(x, y, cell(x, y) | NORTH);
            isBorder = true;
        }
        //EAST
        if ( x + 1 < cells.length && isGon(x + 1, y)) {
            flood4(x + 1, y);
        } else {
            setCell(x, y, cell(x, y) | EAST);
            isBorder = true;
        }
        //SOUTH
        if ( y + 1 < cells[0].length && isGon(x, y + 1)) {
            flood4(x, y + 1);
        } else {
            setCell(x, y, cell(x, y) | SOUTH);
            isBorder = true;
        }
        //WEST
        if ( x - 1 >= 0 && isGon(x - 1, y)) {
            flood4(x - 1, y);
        } else {
            setCell(x, y, cell(x, y) | WEST);
            isBorder = true;
        }
        //border
        if (isBorder) {            
            currentGobj().edges.addAll(cells[x][y].getEdges());
        }
        
    }
    
    private Gobject currentGobj() {
        return gobjs.get(gobjs.size() - 1);
    }
    
    public int cell(int x, int y) {
        return cells[x][y].getStatus();
    }
    
    public boolean isGon(int x, int y) {
        return (cell(x, y) & GON) > 0;
    }
    
    public void setCell(int x, int y, int status) {
        cells[x][y].setStatus(status);
    }
    
    public void paintGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;        
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].paintBG(g2);
            }
        }
        
        g2.setColor(new Color(0, 0, 0));  
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].paintGO(g2);
            }
        }
        
//        g2.setColor(new Color(250, 150, 50));
//        Stroke oldStroke = g2.getStroke();
//        g2.setStroke(new BasicStroke(3));
//        for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells.length; j++) {
//                cells[i][j].paintDO(g2);
//            }
//        }
//        g2.setStroke(oldStroke);        
        g2.setColor(new Color(0, 0, 0)); 
    }       
    
    public void drawCanvas() {
        int wh = 605;
        double fs = 100.0;
        canvas = new BufferedImage(wh, wh, BufferedImage.TYPE_INT_RGB);
        OpenSimplexNoise noise = new OpenSimplexNoise(System.currentTimeMillis());
        
        for (int i = 0; i < wh; i++) {
            for (int j = 0; j < wh; j++) {
                double value = noise.eval( (double) i / fs, (double) j / fs, 0.0);                
                int rgb = 0x010101 * (int)((value + 1) * 127.5);
                canvas.setRGB(i, j, rgb);
            }
        }        
        
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                double value = noise.eval( cells[i][j].centerX() / fs, cells[i][j].centerY() / fs, 0.0);
                cells[i][j] = new Cell(i * dimen, j * dimen, (i + 1) * dimen, (j + 1) * dimen, gonc);
                if (value > 0.15) {
                    cells[i][j].setStatus(16);
                } else {
                    cells[i][j].setStatus(0);                    
                }
            }
        }
        
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
            
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
        paintGrid(g);
        
        if (gobjs != null) {
            for (int i = 0; i < gobjs.size(); i++) {
                gobjs.get(i).paintGobject(g2);
            }
        }               
    }
    
}
