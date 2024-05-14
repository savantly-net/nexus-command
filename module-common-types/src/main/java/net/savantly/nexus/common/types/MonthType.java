package net.savantly.nexus.common.types;

public enum MonthType {

    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int value;

    MonthType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MonthType fromValue(int value) {
        for (MonthType month : MonthType.values()) {
            if (month.getValue() == value) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month value: " + value);
    }
    
}
