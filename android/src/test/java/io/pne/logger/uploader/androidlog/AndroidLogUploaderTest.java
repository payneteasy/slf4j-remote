package io.pne.logger.uploader.androidlog;

import org.junit.Test;
import org.slf4j.event.Level;

public class AndroidLogUploaderTest {

    @Test
    public void upload() {
        AndroidLogUploader uploader = new AndroidLogUploader();
        uploader.upload(Level.INFO, "Test", "Hello");
    }
}