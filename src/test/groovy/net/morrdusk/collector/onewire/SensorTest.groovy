package net.morrdusk.collector.onewire


class SensorTest extends GroovyTestCase {
    
    void testAll() {
        def s1 = new Sensor("1D.91B80D000000")
        assertEquals(true, s1.isSupportedDevice())
        assertEquals(FamilyCode.COUNTER, s1.getFamilyCode())
        assertEquals("91B80D000000", s1.getDeviceId())
        assertEquals("1D.91B80D000000", s1.getUniqueId())

        def s2 = new Sensor("26.2D3011010000")
        assertEquals(true, s2.isSupportedDevice())
        assertEquals(FamilyCode.HUMIDITY, s2.getFamilyCode())
        assertEquals("2D3011010000", s2.getDeviceId())
        assertEquals("26.2D3011010000", s2.getUniqueId())

        def s3 = new Sensor("28.3E5D70020000")
        assertEquals(true, s3.isSupportedDevice())
        assertEquals(FamilyCode.THERMOMETER, s3.getFamilyCode())
        assertEquals("3E5D70020000", s3.getDeviceId())
        assertEquals("28.3E5D70020000", s3.getUniqueId())

        def s4 = new Sensor("56.3E5D70020000")
        assertEquals(false, s4.isSupportedDevice())
        assertEquals(FamilyCode.UNKNOWN, s4.getFamilyCode())
        assertEquals("3E5D70020000", s4.getDeviceId())
        assertEquals("56.3E5D70020000", s4.getUniqueId())
    }
}
