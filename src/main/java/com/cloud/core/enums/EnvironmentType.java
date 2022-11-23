package com.cloud.core.enums;

public enum EnvironmentType {
    /**
     * Official
     */
    Official("80r023r"),
    /**
     * Testing
     */
    Testing("rj304rn0349n");
    private String value = "";

    private EnvironmentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
