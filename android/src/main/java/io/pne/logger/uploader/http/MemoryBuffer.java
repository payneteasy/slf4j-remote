package io.pne.logger.uploader.http;

public class MemoryBuffer {
    final byte buffer[];
          int  position; // can be accessed from tests

    public MemoryBuffer(int aMemoryBufferSize) {
        position = 0;
        buffer = new byte[aMemoryBufferSize];
    }

    public boolean hasSpace(int aMessageLength) {
        return buffer.length <= position + aMessageLength;
    }

    /**
     * Append message to buffer.
     *
     * if message is larger then buffer then cut message
     *
     * @param aMessage message to append
     */
    public void append(byte[] aMessage) {
        int count = aMessage.length + position > buffer.length ? buffer.length - position: aMessage.length;

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
