package io.pne.logger.uploader.compositor;

import io.pne.logger.uploader.ILogUploader;
import org.slf4j.event.Level;

public class CompositorLogUploader implements ILogUploader {

    private final ILogUploader[] uploaders;

    public CompositorLogUploader(ILogUploader ... aUploaders) {
        uploaders = aUploaders;
    }

    @Override
    public void upload(Level aLevel, String aTag, String aMessage) {
        for (ILogUploader uploader : uploaders) {
            uploader.upload(aLevel, aTag, aMessage);
        }
    }
}
