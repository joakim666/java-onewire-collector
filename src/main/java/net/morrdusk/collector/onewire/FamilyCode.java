package net.morrdusk.collector.onewire;

public enum FamilyCode {
    COUNTER("1D"),
    HUMIDITY("26"),
    THERMOMETER("28"),
    UNKNOWN("??");

    private String code;

    private FamilyCode() {
    }

    FamilyCode(String code) {
        this.code = code;
    }

    public static FamilyCode getFamilyCode(String str) {
        for (FamilyCode familyCode : FamilyCode.values()) {
            if (familyCode.code.equals(str)) {
                return familyCode;
            }
        }
        return UNKNOWN;
    }
}
