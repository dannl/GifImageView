package com.felipecsl.gifimageview.library;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Created by DanLiu
 * Nov 09 2015
 */
public class FileInputStreamWrapper {

    private static final String TAG = "FileInputStreamWrapper";

    private FileChannel mChannel;
    private FileInputStream mData;

    /**
     * Constructs a new DataInputStream on the InputStream {@code in}. All
     * reads are then filtered through this stream. Note that data read by this
     * stream is not in a human readable format and was most likely created by a
     * DataOutputStream.
     * <p/>
     * <p><strong>Warning:</strong> passing a null source creates an invalid
     * {@code DataInputStream}. All operations on such a stream will fail.
     *
     * @param in the source InputStream the filter reads from.
     */
    public FileInputStreamWrapper(FileInputStream in) {
        mData = in;
        mChannel = in.getChannel();
    }

    public long position() throws IOException {
        return mChannel.position();
    }

    public void position(final long skipped) throws IOException {
        final long position = position();
        if (position > skipped) {
            reset();
            mData.skip(skipped);
        } else {
            mData.skip(skipped - position);
        }
    }

    public void reset() throws IOException {
        mChannel.position(0);
    }

    public void get(final byte[] buffer) throws IOException {
        mData.read(buffer);
    }

    public void get(byte[] block, int n, int count) throws IOException {
        mData.read(block, n, count);
    }

    public int get() throws IOException {
        return mData.read();
    }

    private byte[] mShortBuffer = new byte[2];

    public int getShort() throws IOException {
        mData.read(mShortBuffer);
        short s = ByteBuffer.wrap(mShortBuffer).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return s;
    }

    public int remaining() {
        try {
            return (int) (mChannel.size() - mChannel.position());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int limit() {
        return 0;
    }
}
