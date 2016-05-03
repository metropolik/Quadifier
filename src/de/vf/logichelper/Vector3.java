package de.vf.logichelper;

public class Vector3 {
    private double x, y, z;
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3() {      
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public String toString() {
        String out = "Vector <";
        out += String.valueOf(x) + " ";
        out += String.valueOf(y) + " ";
        out += String.valueOf(z) + ">";
        return out;
    }

    public double getEntry(int j) {
        if (j == 0) {
            return x;
        } else if (j == 1) {
            return y;
        } else if (j == 2) {
            return z;
        }
        return 0.0;
    }

    public void setEntry(int i, double v) {
        if (i == 0) {
            x = v;
        } else if (i == 1) {
            y = v;
        } else if (i == 2) {
            z = v;
        }       
        
    }
}
