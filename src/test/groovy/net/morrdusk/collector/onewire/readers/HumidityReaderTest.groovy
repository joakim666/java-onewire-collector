package net.morrdusk.collector.onewire.readers


class HumidityReaderTest extends GroovyTestCase {
    
    void testParseValue() {
        def reader = new HumidityReader()

        BigDecimal res = reader.parseValueString("21.12")
        assertEquals("21.12".toBigDecimal(), res)

        res = reader.parseValueString("  21.13456  ")
        assertEquals("21.13456".toBigDecimal(), res)

        boolean gotException = false
        try {
            reader.parseValueString("abc")
        } catch (NumberFormatException e) {
            gotException = true
        }
        assertEquals("Expected NumberFormatException", true, gotException)
    }

    void testReadValues() {
        def reader = new HumidityReader()

        def res = reader.readValue(new File("src/test/resources/test-sensors/26.2D3011010000"))
        assertEquals(new BigDecimal("24.7563"), res)
    }

    void testFailedReadValues() {
        def reader = new HumidityReader()

        boolean gotParseException = false;
        try {
            reader.readValue(new File("src/test/resources/test-sensors/26.2D3013010000"))
        } catch (ParseException e) {
            gotParseException = true
        }
        assertTrue("Did not get ParseException", gotParseException)
    }

    void testReadErrorValue() {
        def reader = new HumidityReader()

        boolean gotParseException = false;
        try {
            reader.readValue(new File("src/test/resources/test-sensors/26.3D3011010000"))
        } catch (ParseException e) {
            gotParseException = true
        }
        assertTrue("Did not get ParseException", gotParseException)
    }
}
