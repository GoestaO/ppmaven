package de.contentcreation.pplive.util;

import java.io.Serializable;

public class Quadrupel<W, X, Y, Z> implements Serializable {

    private W w;
    private X x;
    private Y y;
    private Z z;

    public Quadrupel(W w, X x, Y y, Z z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quadrupel() {
    }

    
    public W getW() {
        return w;
    }

    public void setW(W w) {
        this.w = w;
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public Z getZ() {
        return z;
    }

    public void setZ(Z z) {
        this.z = z;
    }
    
    

}
