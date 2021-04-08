package com.laynezcoder.estfx.constants;

public enum UserType {
    ADMINSTRATOR("Administrator"), 
    USER("User");

    private final String userType;
    
    private UserType(String userType) {
        this.userType = userType;
    }
    
    public String value() {
        return this.userType;
    }
}
