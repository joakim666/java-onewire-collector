package net.morrdusk.collector.onewire.readers

class CounterReaderTest extends GroovyTestCase {

    void testParseValues() {
        def reader = new CounterReader()

        def res = reader.parseValueString("12,13")
        assertEquals(2, res.size())
        assertEquals(12, res.get(0))
        assertEquals(13, res.get(1))

        res = reader.parseValueString(" 11 , 12  ")
        assertEquals(2, res.size())
        assertEquals(11, res.get(0))
        assertEquals(12, res.get(1))

        res = reader.parseValueString(" 16   ")
        assertEquals(1, res.size())
        assertEquals(16, res.get(0))


        boolean gotParseException = false
        try {
            reader.parseValueString(" 17, foo   ")
        } catch (NumberFormatException e) {
            gotParseException = true
        }
        assertEquals("Did not get NumberFormatException", true, gotParseException)
    }

    void testReadValues() {
        def reader = new CounterReader()

        def res = reader.readValues(new File("src/test/resources/test-sensors/1D.91B80D000000"))
        assertEquals(2, res.size())
        assertEquals(21191234, res.get(0))
        assertEquals(1, res.get(1))
    }

    void testFailedReadValues() {
        def reader = new CounterReader()

        boolean gotParseException = false;
        try {
            reader.readValues(new File("src/test/resources/test-sensors/1D.92B80D000000"))
        } catch (ParseException e) {
            gotParseException = true
        }
        assertEquals("Did not get ParseException", true, gotParseException)
    }
}
