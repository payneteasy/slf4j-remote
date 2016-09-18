package io.pne.logger.uploader.androidlog;

import android.util.Log;
import io.pne.logger.uploader.ILogUploader;
import org.slf4j.event.Level;

public class AndroidLogUploader implements ILogUploader {

    @Override
    public void upload(Level aLevel, String aTag, String aMessage) {
        int priority;
        switch (aLevel) {
            case DEBUG: priority = Log.DEBUG;   break;
            case ERROR: priority = Log.ERROR;   break;
            case INFO:  priority = Log.INFO;    break;
            case TRACE: priority = Log.VERBOSE; break;
            case WARN:  priority = Log.WARN;    break;

            default:
                priority = Log.WARN;
        }
        Log.println(priority, aTag, aMessage);
    }
}
