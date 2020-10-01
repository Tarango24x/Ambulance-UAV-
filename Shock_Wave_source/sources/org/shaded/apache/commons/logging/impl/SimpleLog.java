package org.shaded.apache.commons.logging.impl;

import com.shaded.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.shaded.apache.commons.logging.Log;

public class SimpleLog implements Log, Serializable {
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_OFF = 7;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_WARN = 4;
    static Class class$java$lang$Thread = null;
    static Class class$org$apache$commons$logging$impl$SimpleLog = null;
    protected static DateFormat dateFormatter = null;
    protected static String dateTimeFormat = null;
    protected static boolean showDateTime = false;
    protected static boolean showLogName = false;
    protected static boolean showShortName = false;
    protected static final Properties simpleLogProps = new Properties();
    protected static final String systemPrefix = "org.shaded.apache.commons.logging.simplelog.";
    protected int currentLogLevel;
    protected String logName = null;
    private String shortLogName = null;

    static ClassLoader access$000() {
        return getContextClassLoader();
    }

    static {
        showLogName = false;
        showShortName = true;
        showDateTime = false;
        dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
        dateFormatter = null;
        InputStream in = getResourceAsStream("simplelog.properties");
        if (in != null) {
            try {
                simpleLogProps.load(in);
                in.close();
            } catch (IOException e) {
            }
        }
        showLogName = getBooleanProperty("org.shaded.apache.commons.logging.simplelog.showlogname", showLogName);
        showShortName = getBooleanProperty("org.shaded.apache.commons.logging.simplelog.showShortLogname", showShortName);
        showDateTime = getBooleanProperty("org.shaded.apache.commons.logging.simplelog.showdatetime", showDateTime);
        if (showDateTime) {
            dateTimeFormat = getStringProperty("org.shaded.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (IllegalArgumentException e2) {
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
        }
        return prop == null ? simpleLogProps.getProperty(name) : prop;
    }

    private static String getStringProperty(String name, String dephault) {
        String prop = getStringProperty(name);
        return prop == null ? dephault : prop;
    }

    private static boolean getBooleanProperty(String name, boolean dephault) {
        String prop = getStringProperty(name);
        return prop == null ? dephault : "true".equalsIgnoreCase(prop);
    }

    public SimpleLog(String name) {
        this.logName = name;
        setLevel(3);
        String lvl = getStringProperty(new StringBuffer().append("org.shaded.apache.commons.logging.simplelog.log.").append(this.logName).toString());
        int i = String.valueOf(name).lastIndexOf(".");
        while (lvl == null && i > -1) {
            name = name.substring(0, i);
            lvl = getStringProperty(new StringBuffer().append("org.shaded.apache.commons.logging.simplelog.log.").append(name).toString());
            i = String.valueOf(name).lastIndexOf(".");
        }
        lvl = lvl == null ? getStringProperty("org.shaded.apache.commons.logging.simplelog.defaultlog") : lvl;
        if ("all".equalsIgnoreCase(lvl)) {
            setLevel(0);
        } else if ("trace".equalsIgnoreCase(lvl)) {
            setLevel(1);
        } else if ("debug".equalsIgnoreCase(lvl)) {
            setLevel(2);
        } else if ("info".equalsIgnoreCase(lvl)) {
            setLevel(3);
        } else if ("warn".equalsIgnoreCase(lvl)) {
            setLevel(4);
        } else if ("error".equalsIgnoreCase(lvl)) {
            setLevel(5);
        } else if ("fatal".equalsIgnoreCase(lvl)) {
            setLevel(6);
        } else if ("off".equalsIgnoreCase(lvl)) {
            setLevel(7);
        }
    }

    public void setLevel(int currentLogLevel2) {
        this.currentLogLevel = currentLogLevel2;
    }

    public int getLevel() {
        return this.currentLogLevel;
    }

    /* access modifiers changed from: protected */
    public void log(int type, Object message, Throwable t) {
        String dateText;
        StringBuffer buf = new StringBuffer();
        if (showDateTime) {
            Date now = new Date();
            synchronized (dateFormatter) {
                dateText = dateFormatter.format(now);
            }
            buf.append(dateText);
            buf.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        switch (type) {
            case 1:
                buf.append("[TRACE] ");
                break;
            case 2:
                buf.append("[DEBUG] ");
                break;
            case 3:
                buf.append("[INFO] ");
                break;
            case 4:
                buf.append("[WARN] ");
                break;
            case 5:
                buf.append("[ERROR] ");
                break;
            case 6:
                buf.append("[FATAL] ");
                break;
        }
        if (showShortName) {
            if (this.shortLogName == null) {
                this.shortLogName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
                this.shortLogName = this.shortLogName.substring(this.shortLogName.lastIndexOf("/") + 1);
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        } else if (showLogName) {
            buf.append(String.valueOf(this.logName)).append(" - ");
        }
        buf.append(String.valueOf(message));
        if (t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
        }
        write(buf);
    }

    /* access modifiers changed from: protected */
    public void write(StringBuffer buffer) {
        System.err.println(buffer.toString());
    }

    /* access modifiers changed from: protected */
    public boolean isLevelEnabled(int logLevel) {
        return logLevel >= this.currentLogLevel;
    }

    public final void debug(Object message) {
        if (isLevelEnabled(2)) {
            log(2, message, (Throwable) null);
        }
    }

    public final void debug(Object message, Throwable t) {
        if (isLevelEnabled(2)) {
            log(2, message, t);
        }
    }

    public final void trace(Object message) {
        if (isLevelEnabled(1)) {
            log(1, message, (Throwable) null);
        }
    }

    public final void trace(Object message, Throwable t) {
        if (isLevelEnabled(1)) {
            log(1, message, t);
        }
    }

    public final void info(Object message) {
        if (isLevelEnabled(3)) {
            log(3, message, (Throwable) null);
        }
    }

    public final void info(Object message, Throwable t) {
        if (isLevelEnabled(3)) {
            log(3, message, t);
        }
    }

    public final void warn(Object message) {
        if (isLevelEnabled(4)) {
            log(4, message, (Throwable) null);
        }
    }

    public final void warn(Object message, Throwable t) {
        if (isLevelEnabled(4)) {
            log(4, message, t);
        }
    }

    public final void error(Object message) {
        if (isLevelEnabled(5)) {
            log(5, message, (Throwable) null);
        }
    }

    public final void error(Object message, Throwable t) {
        if (isLevelEnabled(5)) {
            log(5, message, t);
        }
    }

    public final void fatal(Object message) {
        if (isLevelEnabled(6)) {
            log(6, message, (Throwable) null);
        }
    }

    public final void fatal(Object message, Throwable t) {
        if (isLevelEnabled(6)) {
            log(6, message, t);
        }
    }

    public final boolean isDebugEnabled() {
        return isLevelEnabled(2);
    }

    public final boolean isErrorEnabled() {
        return isLevelEnabled(5);
    }

    public final boolean isFatalEnabled() {
        return isLevelEnabled(6);
    }

    public final boolean isInfoEnabled() {
        return isLevelEnabled(3);
    }

    public final boolean isTraceEnabled() {
        return isLevelEnabled(1);
    }

    public final boolean isWarnEnabled() {
        return isLevelEnabled(4);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v16, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.ClassLoader} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.ClassLoader getContextClassLoader() {
        /*
            r1 = 0
            if (r1 != 0) goto L_0x0028
            java.lang.Class r4 = class$java$lang$Thread     // Catch:{ NoSuchMethodException -> 0x0054 }
            if (r4 != 0) goto L_0x003b
            java.lang.String r4 = "java.lang.Thread"
            java.lang.Class r4 = class$(r4)     // Catch:{ NoSuchMethodException -> 0x0054 }
            class$java$lang$Thread = r4     // Catch:{ NoSuchMethodException -> 0x0054 }
            r5 = r4
        L_0x0010:
            java.lang.String r6 = "getContextClassLoader"
            r4 = 0
            java.lang.Class[] r4 = (java.lang.Class[]) r4     // Catch:{ NoSuchMethodException -> 0x0054 }
            java.lang.reflect.Method r3 = r5.getMethod(r6, r4)     // Catch:{ NoSuchMethodException -> 0x0054 }
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ IllegalAccessException -> 0x0059, InvocationTargetException -> 0x003f }
            r4 = 0
            java.lang.Class[] r4 = (java.lang.Class[]) r4     // Catch:{ IllegalAccessException -> 0x0059, InvocationTargetException -> 0x003f }
            java.lang.Object r4 = r3.invoke(r5, r4)     // Catch:{ IllegalAccessException -> 0x0059, InvocationTargetException -> 0x003f }
            r0 = r4
            java.lang.ClassLoader r0 = (java.lang.ClassLoader) r0     // Catch:{ IllegalAccessException -> 0x0059, InvocationTargetException -> 0x003f }
            r1 = r0
        L_0x0028:
            if (r1 != 0) goto L_0x003a
            java.lang.Class r4 = class$org$apache$commons$logging$impl$SimpleLog
            if (r4 != 0) goto L_0x0056
            java.lang.String r4 = "org.shaded.apache.commons.logging.impl.SimpleLog"
            java.lang.Class r4 = class$(r4)
            class$org$apache$commons$logging$impl$SimpleLog = r4
        L_0x0036:
            java.lang.ClassLoader r1 = r4.getClassLoader()
        L_0x003a:
            return r1
        L_0x003b:
            java.lang.Class r4 = class$java$lang$Thread     // Catch:{ NoSuchMethodException -> 0x0054 }
            r5 = r4
            goto L_0x0010
        L_0x003f:
            r2 = move-exception
            java.lang.Throwable r4 = r2.getTargetException()     // Catch:{ NoSuchMethodException -> 0x0054 }
            boolean r4 = r4 instanceof java.lang.SecurityException     // Catch:{ NoSuchMethodException -> 0x0054 }
            if (r4 != 0) goto L_0x0028
            org.shaded.apache.commons.logging.LogConfigurationException r4 = new org.shaded.apache.commons.logging.LogConfigurationException     // Catch:{ NoSuchMethodException -> 0x0054 }
            java.lang.String r5 = "Unexpected InvocationTargetException"
            java.lang.Throwable r6 = r2.getTargetException()     // Catch:{ NoSuchMethodException -> 0x0054 }
            r4.<init>(r5, r6)     // Catch:{ NoSuchMethodException -> 0x0054 }
            throw r4     // Catch:{ NoSuchMethodException -> 0x0054 }
        L_0x0054:
            r4 = move-exception
            goto L_0x0028
        L_0x0056:
            java.lang.Class r4 = class$org$apache$commons$logging$impl$SimpleLog
            goto L_0x0036
        L_0x0059:
            r4 = move-exception
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.commons.logging.impl.SimpleLog.getContextClassLoader():java.lang.ClassLoader");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static InputStream getResourceAsStream(String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction(name) {
            private final String val$name;

            {
                this.val$name = val$name;
            }

            public Object run() {
                ClassLoader threadCL = SimpleLog.access$000();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(this.val$name);
                }
                return ClassLoader.getSystemResourceAsStream(this.val$name);
            }
        });
    }
}
