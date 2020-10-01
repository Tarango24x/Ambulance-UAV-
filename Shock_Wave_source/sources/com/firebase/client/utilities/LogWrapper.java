package com.firebase.client.utilities;

import com.firebase.client.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogWrapper {
    static final /* synthetic */ boolean $assertionsDisabled = (!LogWrapper.class.desiredAssertionStatus());
    private final String component;
    private final Logger logger;
    private final String prefix;

    private static String exceptionStacktrace(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public LogWrapper(Logger logger2, String component2) {
        this(logger2, component2, (String) null);
    }

    public LogWrapper(Logger logger2, String component2, String prefix2) {
        this.logger = logger2;
        this.component = component2;
        this.prefix = prefix2;
    }

    public void error(String message, Throwable e) {
        this.logger.onLogMessage(Logger.Level.ERROR, this.component, toLog(message) + "\n" + exceptionStacktrace(e), now());
    }

    public void warn(String message) {
        warn(message, (Throwable) null);
    }

    public void warn(String message, Throwable e) {
        String logMsg = toLog(message);
        if (e != null) {
            logMsg = logMsg + "\n" + exceptionStacktrace(e);
        }
        this.logger.onLogMessage(Logger.Level.WARN, this.component, logMsg, now());
    }

    public void info(String message) {
        this.logger.onLogMessage(Logger.Level.INFO, this.component, toLog(message), now());
    }

    public void debug(String message) {
        debug(message, (Throwable) null);
    }

    public boolean logsDebug() {
        return this.logger.getLogLevel().ordinal() <= Logger.Level.DEBUG.ordinal();
    }

    public void debug(String message, Throwable e) {
        String logMsg = toLog(message);
        if (e != null) {
            logMsg = logMsg + "\n" + exceptionStacktrace(e);
        }
        if ($assertionsDisabled || logsDebug()) {
            this.logger.onLogMessage(Logger.Level.DEBUG, this.component, logMsg, now());
            return;
        }
        throw new AssertionError();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private String toLog(String message) {
        return this.prefix == null ? message : this.prefix + " - " + message;
    }
}
