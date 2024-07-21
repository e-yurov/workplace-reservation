package com.rc.mentorship.workplace_reservation.util.filter;

public enum FilterType {
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUALS("gte"),
    LESS_THAN("lt"),
    LESS_THAN_OR_EQUALS("lte"),
    EQUALS("eq");

    private final String shortName;

    FilterType(String shortName) {
        this.shortName = shortName;
    }

    public static FilterType getByShortName(String shortName) {
        for (var value: FilterType.values()) {
            if (value.shortName.equals(shortName)) {
                return value;
            }
        }
        return null;
    }
}
