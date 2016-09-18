package io.pne.logger.uploader;

import io.pne.logger.uploader.androidlog.AndroidLogUploader;

public class LogUploaderConfig {

    private static final LogUploaderConfig INSTANCE = new LogUploaderConfig();

    private volatile ILogUploader uploader;

    public LogUploaderConfig() {
        uploader = new AndroidLogUploader();
    }

    public static LogUploaderConfig getInstance() {
        return INSTANCE;
    }

    public  ILogUploader getCurrentUploader() {
        return uploader;
    }

    public void setCurrentUploader(ILogUploader aUploader) {
        uploader = aUploader;
    }
}
