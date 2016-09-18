package io.pne.logger.slf4j;

import android.util.Log;
import io.pne.logger.uploader.LogUploaderConfig;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import static org.slf4j.event.Level.TRACE;
import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.ERROR;
import static org.slf4j.event.Level.INFO;
import static org.slf4j.event.Level.WARN;


public class RemoteLogger extends MarkerIgnoringBase {

    RemoteLogger(String tag) {
        name = tag;
    }

    public boolean isTraceEnabled()                            { return isLoggable(TRACE);                  }
    public void trace(String msg)                              { log(TRACE, msg, null);                     }
    public void trace(String format, Object arg)               { formatAndLog(TRACE, format, arg);          }
    public void trace(String format, Object arg1, Object arg2) { formatAndLog(TRACE, format, arg1, arg2);   }
    public void trace(String format, Object... argArray)       { formatAndLog(TRACE, format, argArray);     }
    public void trace(String msg, Throwable t)                 { log(TRACE, msg, t);                        }

    public boolean isDebugEnabled()                            { return isLoggable(DEBUG);                  }
    public void debug(String msg)                              { log(DEBUG, msg, null);                     }
    public void debug(String format, Object arg)               { formatAndLog(DEBUG, format, arg);          }
    public void debug(String format, Object arg1, Object arg2) { formatAndLog(DEBUG, format, arg1, arg2);   }
    public void debug(String format, Object... argArray)       { formatAndLog(DEBUG, format, argArray);     }
    public void debug(String msg, Throwable t)                 { log(DEBUG, msg, t);                        }

    public boolean isInfoEnabled()                             { return isLoggable(INFO);                   }
    public void info(String msg)                               { log(INFO, msg, null);                      }
    public void info(String format, Object arg)                { formatAndLog(INFO, format, arg);           }
    public void info(String format, Object arg1, Object arg2)  { formatAndLog(INFO, format, arg1, arg2);    }
    public void info(String format, Object... argArray)        { formatAndLog(INFO, format, argArray);      }
    public void info(String msg, Throwable t)                  { log(INFO, msg, t);                         }

    public boolean isWarnEnabled()                             { return isLoggable(WARN);                   }
    public void warn(String msg)                               { log(WARN, msg, null);                      }
    public void warn(String format, Object arg)                { formatAndLog(WARN, format, arg);           }
    public void warn(String format, Object arg1, Object arg2)  { formatAndLog(WARN, format, arg1, arg2);    }
    public void warn(String format, Object... argArray)        { formatAndLog(WARN, format, argArray);      }
    public void warn(String msg, Throwable t)                  { log(WARN, msg, t);                         }

    public boolean isErrorEnabled()                            { return isLoggable(ERROR);                  }
    public void error(String msg)                              { log(ERROR, msg, null);                     }
    public void error(String format, Object arg)               { formatAndLog(ERROR, format, arg);          }
    public void error(String format, Object arg1, Object arg2) { formatAndLog(ERROR, format, arg1, arg2);   }
    public void error(String format, Object... argArray)       { formatAndLog(ERROR, format, argArray);     }
    public void error(String msg, Throwable t)                 { log(ERROR, msg, t);                        }

    private void formatAndLog(Level priority, String format, Object... argArray) {
        //noinspection ConstantConditions
        if (isLoggable(priority)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            _log(priority, ft.getMessage(), ft.getThrowable());
        }
    }

    private void log(Level priority, String message, Throwable throwable) {
        //noinspection ConstantConditions
        if (isLoggable(priority)) {
            _log(priority, message, throwable);
        }
    }

    private boolean isLoggable(@SuppressWarnings("UnusedParameters") Level priority) { return true; }

    private void _log(Level priority, String message, Throwable throwable) {
        if (throwable != null) {
            message += '\n' + Log.getStackTraceString(throwable);
        }

        LogUploaderConfig.getInstance()
                .getCurrentUploader()
                .upload(priority, name, message);
    }



}
