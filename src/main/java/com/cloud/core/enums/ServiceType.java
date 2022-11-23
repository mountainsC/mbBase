package com.cloud.core.enums;

public enum ServiceType {
    /**
     * mibao
     */
    Mibao("132594546"),
    /**
     * mibao
     */
    MibaoMer("583048jr");
    private String value = "";

    private ServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
