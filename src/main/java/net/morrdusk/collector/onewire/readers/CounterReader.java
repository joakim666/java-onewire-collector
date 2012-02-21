package net.morrdusk.collector.onewire.readers;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CounterReader extends Reader {

    public CounterReader() {
    }

    public ImmutableList<Long> readValues(File deviceDirectory) throws ParseException {
        try {
            return readFile(new File(deviceDirectory, "counters.ALL"));
        } catch (IOException e) {
            throw new ParseException("Failed to read values from counter", e);
        } catch (NumberFormatException e) {
            throw new ParseException("Failed to read values from counter", e);
        }
    }
    
    protected ImmutableList<Long> readFile(File file) throws IOException {
        final String str = Files.toString(file, charset);
        return ImmutableList.copyOf(parseValueString(str));
    }

    protected List<Long> parseValueString(String str) {
        List<Long> res = new ArrayList<Long>();

        for (String value : str.split(",")) {
            res.add(Long.valueOf(value.trim()));
        }
        return res;
    }
}
