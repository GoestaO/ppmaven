/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.contentcreation.pplive.reportingClasses;

/**
 *
 * @author Gösta Ostendorf <goesta.o@gmail.com>
 */
public class RejectReportOverview {
    private int partnerId;
    private long quantity;

    public RejectReportOverview(int partnerId, long quantity) {
        this.partnerId = partnerId;
        this.quantity = quantity;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    
}
