package net.morrdusk.collector.onewire;

public class Sensor {
    private final String deviceDirectory;
    private final FamilyCode familyCode;
    private final String deviceId;

    public Sensor(String chipAddress) {
        if (chipAddress == null || chipAddress.length() != 15) {
            throw new IllegalArgumentException("No or wrong sized chip address");
        }
        this.deviceDirectory = chipAddress;
        familyCode = FamilyCode.getFamilyCode(deviceDirectory.substring(0, 2));
        deviceId = deviceDirectory.substring(3, 3 + 12);
    }
    
    public FamilyCode getFamilyCode() {
        return familyCode;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public String getUniqueId() {
        return deviceDirectory;
    }

    public boolean isSupportedDevice() {
        boolean supported = false;
        switch (familyCode) {
            case COUNTER:
            case HUMIDITY:
            case THERMOMETER:
                supported = true;
                break;
            default:
                break;
        }
        return supported;
    }
}
