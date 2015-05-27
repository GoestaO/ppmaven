/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.util;

import java.io.Serializable;

public class Tuple<X, Y> implements Serializable {

    private X x;

    private Y y;

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

    public Tuple() {
    }

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

}
