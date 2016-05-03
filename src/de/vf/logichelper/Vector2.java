package de.vf.logichelper;
import java.awt.Point;

public class Vector2 {
    private float x;
    private float y;
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2(Point a) {
        this.x = a.x;
        this.y = a.y;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2 add(Vector2 b) {
        return new Vector2(this.x + b.getX(), this.y + b.getY());
    }
    
    public Vector2 sub(Vector2 b) {
        return new Vector2(this.x - b.getX(), this.y - b.getY());
    }
    
    public float dot(Vector2 b) {
        return this.x * b.getX() + this.y * b.getY();
    }
}
