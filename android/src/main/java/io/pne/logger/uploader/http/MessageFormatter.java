package io.pne.logger.uploader.http;

import org.slf4j.event.Level;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MessageFormatter {

    public byte[] createMessage(Level aLevel, String aTag, String aMessage) {
        String text = new Date() + " " + aLevel + " " + aTag + " " + aMessage + "\n";
        return text.getBytes(StandardCharsets.UTF_8);
    }

}
