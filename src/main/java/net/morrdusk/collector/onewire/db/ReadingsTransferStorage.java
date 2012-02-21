package net.morrdusk.collector.onewire.db;

import com.google.inject.Inject;
import com.sleepycat.collections.StoredEntrySet;
import net.morrdusk.collector.onewire.domain.Reading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadingsTransferStorage {
    private ReadingsView dbView;
    private final List<Map.Entry<ReadingKey,Reading>> toBeRemoved = new ArrayList<Map.Entry<ReadingKey, Reading>>();
    private StoredEntrySet<ReadingKey,Reading> entries;

    @Inject
    public ReadingsTransferStorage(ReadingsView view) {
        this.dbView = view;
    }

    /**
     * Return readings to be sent to the server.
     *
     * @return
     */
    public List<Reading> collectReadingsToSend() {
        entries = dbView.getAll();

        final List<Reading> readValues = new ArrayList<Reading>();

        for (Map.Entry<ReadingKey, Reading> entry : entries) {
            toBeRemoved.add(entry);
            readValues.add(entry.getValue());
        }

        return readValues;
    }

    /**
     * Notification that the readings where successfully uploaded to
     * the server.
     *
     * Which means they can be removed from the local storage.
     *
     */
    public void uploadedSuccessfully() {
        for (Map.Entry<ReadingKey, Reading> entry : toBeRemoved) {
            entries.remove(entry);
        }
    }
}
