package net.morrdusk.collector.onewire.readers


class TemperatureReaderTest extends GroovyTestCase {

    void testParseValue() {
        def reader = new TemperatureReader()

        BigDecimal res = reader.parseValueString("21.12")
        assertEquals("21.12".toBigDecimal(), res)

        res = reader.parseValueString("  21.13456  ")
        assertEquals("21.13456".toBigDecimal(), res)

        res = reader.parseValueString("  -7.7812  ")
        assertEquals("-7.7812".toBigDecimal(), res)

        boolean gotException = false
        try {
            reader.parseValueString("abc")
        } catch (NumberFormatException e) {
            gotException = true
        }
        assertEquals("Expected NumberFormatException", true, gotException)
    }

    void testReadValues() {
        def reader = new TemperatureReader()

        def res = reader.readValue(new File("src/test/resources/test-sensors/28.3E5D70020000"))
        assertEquals(new BigDecimal("-2.25"), res)
    }

    void testFailedReadValues() {
        def reader = new TemperatureReader()

        boolean gotParseException = false;
        try {
            reader.readValue(new File("src/test/resources/test-sensors/28.3E5D80020000"))
        } catch (ParseException e) {
            gotParseException = true
        }
        assertTrue("Did not get ParseException", gotParseException)
    }

    void testReadErrorValue() {
        def reader = new TemperatureReader()

        boolean gotParseException = false;
        try {
            reader.readValue(new File("src/test/resources/test-sensors/28.4E5D70020000"))
        } catch (ParseException e) {
            gotParseException = true
        }
        assertTrue("Did not get ParseException", gotParseException)
    }
}
