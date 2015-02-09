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
public class RejectReportBemerkung1 {

    private int partnerId;

    private String bemerkung1;

    private long quantity;

    public RejectReportBemerkung1(int partnerId, String bemerkung1, long quantity) {
        this.partnerId = partnerId;
        this.bemerkung1 = bemerkung1;
        this.quantity = quantity;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getBemerkung1() {
        return bemerkung1;
    }

    public void setBemerkung1(String bemerkung1) {
        this.bemerkung1 = bemerkung1;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
