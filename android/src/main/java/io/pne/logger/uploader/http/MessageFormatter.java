package io.pne.logger.uploader.http;

import org.slf4j.event.Level;

import java.nio.charset.Charset;
import java.util.Date;

public class MessageFormatter {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public byte[] createMessage(Level aLevel, String aTag, String aMessage) {
        String text = new Date() + " " + aLevel + " " + aTag + " " + aMessage + "\n";
        return text.getBytes(UTF_8);
    }

}
