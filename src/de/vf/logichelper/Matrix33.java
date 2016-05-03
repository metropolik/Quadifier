package de.vf.logichelper;

public class Matrix33 {
    private double[][] data;
    
    public Matrix33(double[][] data) throws WrongDimensionsException {
        if (data.length != 3 || data[0].length != 3) {
            throw new WrongDimensionsException("Error, the double array has the wrong dimensions");
        }
        
        this.data = data;
    }
    
    public Matrix33() {
        this.data = new double[3][3];
    }
    
    public String toString() {
        String out = "";
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                out += String.valueOf(data[i][j]) + "\t";
            }
            if (i != data.length - 1) {
                out += "\n";    
            }           
        }
        return out;
    }
    
    public Vector3 multiply(Vector3 vek) {
        Vector3 out = new Vector3();
        for (int i = 0; i < data.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < data.length; j++) {
                sum += data[i][j] * vek.getEntry(j);
            }
            out.setEntry(i, sum);
        }   
        return out;
    }
    
    public static Matrix33 createTranslateMatrix(int tx, int ty) {
        double[][] vals = { {1, 0, tx},
                            {0, 1, ty},
                            {0, 0, 1}};
        
        try {
            return new Matrix33(vals);
        } catch (WrongDimensionsException e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    public static Matrix33 createScalingMatrix(double s) {
        double[][] vals = { {s, 0, 0},
                            {0, s, 0},
                            {0, 0, 1}};
        
        try {
            return new Matrix33(vals);
        } catch (WrongDimensionsException e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    public static Matrix33 createScalingMatrix(double sx, double sy) {
        double[][] vals = { {sx, 0, 0},
                            {0, sy, 0},
                            {0, 0, 1}};
        
        try {
            return new Matrix33(vals);
        } catch (WrongDimensionsException e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    public static Matrix33 createScalingMatrixWithOffset(double s, double tx, double ty) {
        double[][] vals = { {s, 0, -tx * s + tx},
                            {0, s, -ty * s + ty},
                            {0, 0,   1}};
        
        try {
            return new Matrix33(vals);
        } catch (WrongDimensionsException e) { 
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static Matrix33 createRotatingMatrix(double a) {
        double[][] vals = { {Math.cos(a), -Math.sin(a), 0},
                            {Math.sin(a), Math.cos(a), 0},
                            {0,         0,          1}};
        
        try {
            return new Matrix33(vals);
        } catch (WrongDimensionsException e) { 
            e.printStackTrace();
        }
        return null;
    }
}

