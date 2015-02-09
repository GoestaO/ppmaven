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
public class RejectReportBemerkung2 {

    private int partnerId;

    private String bemerkung2;

    private long quantity;

    public RejectReportBemerkung2(int partnerId, String bemerkung2, long quantity) {
        this.partnerId = partnerId;
        this.bemerkung2 = bemerkung2;
        this.quantity = quantity;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getBemerkung2() {
        return bemerkung2;
    }

    public void setBemerkung2(String bemerkung2) {
        this.bemerkung2 = bemerkung2;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
