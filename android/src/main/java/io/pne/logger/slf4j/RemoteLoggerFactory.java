package io.pne.logger.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class RemoteLoggerFactory implements ILoggerFactory {

    public static final String PREFIX = "sdk.";

    @Override
    public Logger getLogger(String aLongName) {
        String name = createShortName(aLongName);
        return new RemoteLogger(name);
    }

    private String createShortName(String aLongName) {
        int pos = aLongName.lastIndexOf('.');
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        if (pos == -1) {
            sb.append(aLongName);
        } else {
            sb.append(aLongName.substring(pos + 1));
        }

        String name = sb.toString();
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        return name;
    }
}
