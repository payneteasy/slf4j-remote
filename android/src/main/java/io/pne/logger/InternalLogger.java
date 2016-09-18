package io.pne.logger;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class InternalLogger {

    private final String tag;

    public InternalLogger(Class<?> aClass) {
        tag  = aClass.getSimpleName();
    }

    public void error(String aPattern, Object ... aArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(aPattern, aArguments);
        System.err.println(tag + " ERROR: " + ft.getMessage());
    }

    public void info(String aPattern, Object ... aArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(aPattern, aArguments);
        System.out.println(tag + " INFO: " + ft.getMessage());
    }
}
