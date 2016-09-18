package io.pne.logger.uploader.http;

public class MemoryBuffer {
    private final byte buffer[];
    private       int  position;

    public MemoryBuffer(int aMemoryBufferSize) {
        position = 0;
        buffer = new byte[aMemoryBufferSize];
    }

    public boolean hasSpace(int aMessageLength) {
        return buffer.length < position + aMessageLength;
    }

    /**
     * if message is larger then buffer then cut message
     *
     * @param aMessage message to append
     */
    public void append(byte[] aMessage) {
        int count = aMessage.length;
        System.arraycopy(aMessage, 0, buffer, position, count);
        position += count;
    }

    public void clearToFile(CurrentFile aFile) {
        if(position != 0) {
            aFile.write(buffer, 0, position);
        }
        position = 0;
    }
}
