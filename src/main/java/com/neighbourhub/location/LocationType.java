package com.neighbourhub.location;

public enum LocationType {
    TOWN("town"),
    DISTRICT("district"),
    GEO("geo");

    private final String code;

    LocationType(String code) {
        this.code = code;
    }
}
