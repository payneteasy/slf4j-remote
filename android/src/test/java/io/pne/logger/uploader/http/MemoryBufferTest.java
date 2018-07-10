package io.pne.logger.uploader.http;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MemoryBufferTest {

    @Test
    public void append_11_of_10() {
        MemoryBuffer buffer = new MemoryBuffer(10);
        buffer.append(new byte[11]);
        assertFalse("Buffer should not have any space", buffer.hasSpace(1));
        assertEquals(10, buffer.position);
        CurrentFile file = new CurrentFile(new File("/tmp"), 100);
        try {
            buffer.clearToFile(file);
            assertEquals(10, file.activeFile.length());
        } finally {
            assertTrue("Could not delete active file", file.activeFile.delete());
        }
    }

    @Test
    public void append_7_of_10() {
        MemoryBuffer buffer = new MemoryBuffer(10);

        buffer.append(new byte[5]);
        assertEquals(5, buffer.position);

        buffer.append(new byte[2]);
        assertEquals(7, buffer.position);

        assertTrue("Buffer must have a space for additional 3 bytes", buffer.hasSpace(3));
        assertFalse("Buffer must not have space for additional 4 bytes", buffer.hasSpace(4));
        CurrentFile file = new CurrentFile(new File("/tmp"), 100);
        try {
            buffer.clearToFile(file);
            assertEquals(7, file.activeFile.length());
            assertEquals(0, buffer.position);
        } finally {
            assertTrue("Could not delete active file", file.activeFile.delete());
        }
    }

    @Test
    public void append_10_of_10() {
        MemoryBuffer buffer = new MemoryBuffer(10);

        buffer.append(new byte[5]);
        assertEquals(5, buffer.position);

        buffer.append(new byte[10]);
        assertEquals(10, buffer.position);

        assertTrue("Buffer must have a space for additional 0 bytes", buffer.hasSpace(0));
        CurrentFile file = new CurrentFile(new File("/tmp"), 100);
        try {
            buffer.clearToFile(file);
            assertEquals(10, file.activeFile.length());
        } finally {
            assertTrue("Could not delete active file", file.activeFile.delete());
        }
    }


    @Test
    public void append_overflow_check() {
        MemoryBuffer buffer = new MemoryBuffer(10);
        buffer.append(new byte[5]);
        buffer.append(new byte[5]);
        buffer.append(new byte[5]);
    }




}
