package net.morrdusk.collector.onewire.db

import com.google.common.io.Files
import net.morrdusk.collector.onewire.domain.CounterReading

class ReadingsViewTest extends GroovyTestCase {

    File dbDir
    ReadingsDb db
    ReadingsView view

    void setUp() {
        dbDir = Files.createTempDir()
        println "dbdir: " + dbDir.getAbsolutePath()
        db = new ReadingsDb(dbDir.getAbsolutePath())
        view = new ReadingsView(db)
    }
    
    void tearDown() {
        println "Cleaning up"

        for (f in dbDir.listFiles()) {
            f.delete()
        }
        dbDir.delete()
    }

    void testSave() {
        def reading1 = new CounterReading("91B80D000000", 10, 20)
        def key1 = new ReadingKey("91B80D000000" + System.currentTimeMillis())
        view.save(key1, reading1)

        def res1 = view.getAll()
        assertEquals(1, res1.size())

        def list1 = res1.toList()
        assertEquals(reading1, list1.get(0).getValue())

        def reading2 = new CounterReading("91B80D000000", 40, 50)
        view.save(new ReadingKey("91B80D000000" + System.currentTimeMillis()), reading2)

        assertEquals(1, list1.size())   // list 1 is a copy
        assertEquals(2, res1.size())    // res1 is the live data

        def res2 = view.getAll()
        assertEquals(2, res2.size())

        def list2 = res2.toList()
        assertEquals(reading1, list2.get(0).getValue())
        assertEquals(reading2, list2.get(1).getValue())

        view.remove(key1)

        def res3 = view.getAll()
        assertEquals(1, res3.size())

        def list3 = res3.toList()
        assertEquals(reading2, list3.get(0).getValue())
    }
}
