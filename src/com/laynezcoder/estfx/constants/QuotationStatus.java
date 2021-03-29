package com.laynezcoder.estfx.constants;

public enum QuotationStatus {

    EXISTENT("Existent"),
    NOT_EXISTENT("Not existent"),
    REALIZED("Realized"),
    NOT_REALIZED("Not realized"),
    REPORTED("Reported"),
    NOT_REPORTED("Not reported");

    private final String status;

    QuotationStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
}
