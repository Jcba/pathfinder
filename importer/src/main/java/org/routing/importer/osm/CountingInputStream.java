package org.routing.importer.osm;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Decorating class which counts the number of bytes read from an input stream
 */
public class CountingInputStream extends InputStream {

    private final InputStream inputStream;
    private final long fileSizeInBytes;
    private long readBytes;
    private int lastProgressDebugMsg = 0;


    public CountingInputStream(InputStream inputStream, long fileSizeInBytes) {
        this.inputStream = inputStream;
        this.fileSizeInBytes = fileSizeInBytes;
    }


    @Override
    public int read() throws IOException {
        updateReadBytes(1);

        return inputStream.read();
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        updateReadBytes(1);

        return inputStream.read(b);
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        updateReadBytes(len);

        return inputStream.read(b, off, len);
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return inputStream.readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        updateReadBytes(len);

        return inputStream.readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        updateReadBytes(len);

        return inputStream.readNBytes(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        updateReadBytes(n);

        return inputStream.skip(n);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        updateReadBytes(n);

        inputStream.skipNBytes(n);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return inputStream.transferTo(out);
    }

    private void updateReadBytes(long deltaInBytes) {
        readBytes += deltaInBytes;

        int progress = (int) ((readBytes * 100) / fileSizeInBytes);

        if (progress != lastProgressDebugMsg) {
            System.out.printf("Read %s percent of file%n", progress);
        }

        lastProgressDebugMsg = progress;
    }
}
