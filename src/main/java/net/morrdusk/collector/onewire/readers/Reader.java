package net.morrdusk.collector.onewire.readers;

import java.nio.charset.Charset;

public abstract class Reader {
    public final Charset charset = Charset.forName("US-ASCII");
}
