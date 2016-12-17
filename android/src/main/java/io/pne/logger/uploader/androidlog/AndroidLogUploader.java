package io.pne.logger.uploader.androidlog;

import android.util.Log;
import io.pne.logger.uploader.ILogUploader;
import org.slf4j.event.Level;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AndroidLogUploader implements ILogUploader {

    private static final boolean IS_ANDROID = detectAndroid();

    private static boolean detectAndroid() {
        try {
            Class<?>  logClass = Class.forName("android.util.Log");
            Method logMethod   = logClass.getMethod("v", String.class, String.class);
            logMethod.invoke(null, "AndroidLogUploader", "init");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void upload(Level aLevel, String aTag, String aMessage) {
        if(IS_ANDROID) {
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
        } else {
            String dateString = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
            PrintStream out = aLevel == Level.ERROR ? System.err : System.out;
            out.println(dateString + " " + aLevel+" " + aTag + " " + aMessage);
        }
    }
}
