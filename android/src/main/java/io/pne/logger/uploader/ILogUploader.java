package io.pne.logger.uploader;

import org.slf4j.event.Level;

public interface ILogUploader {
    void upload(Level aLevel, String aTag, String aMessage);
}
