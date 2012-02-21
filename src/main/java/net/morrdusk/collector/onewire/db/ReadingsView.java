package net.morrdusk.collector.onewire.db;

import com.google.inject.Inject;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.StoredEntrySet;
import com.sleepycat.collections.StoredMap;
import net.morrdusk.collector.onewire.domain.Reading;

public class ReadingsView {

    private final StoredMap<ReadingKey,Reading> readingsMap;

    @Inject
    public ReadingsView(ReadingsDb db) {
        final StoredClassCatalog catalog = db.getClassCatalog();

        final EntryBinding<ReadingKey> readingKeyBinding = new SerialBinding<ReadingKey>(catalog, ReadingKey.class);
        final SerialBinding<Reading> readingValueBinding = new SerialBinding<Reading>(catalog, Reading.class);

        readingsMap = new StoredMap<ReadingKey, Reading>(db.getReadingsDatabase(), readingKeyBinding, readingValueBinding, true);
    }

    public final StoredEntrySet<ReadingKey, Reading> getAll() {
        return (StoredEntrySet<ReadingKey, Reading>) readingsMap.entrySet();
    }

    public void save(ReadingKey key, Reading reading) {
        readingsMap.put(key, reading);
    }

    public void remove(ReadingKey key) {
        readingsMap.remove(key);
    }
}
