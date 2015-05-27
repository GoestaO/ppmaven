/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.util;

import java.io.Serializable;

public class Triple<X, Y, Z> implements Serializable {

    private X x;

    private Y y;

    private Z z;

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

    public Triple() {
    }

    public Triple(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Triple{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

}
