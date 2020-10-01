package org.shaded.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.shaded.apache.commons.logging.Log;
import org.shaded.apache.commons.logging.LogConfigurationException;
import org.shaded.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory {
    public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.shaded.apache.commons.logging.Log.allowFlawedContext";
    public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.shaded.apache.commons.logging.Log.allowFlawedDiscovery";
    public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.shaded.apache.commons.logging.Log.allowFlawedHierarchy";
    private static final String LOGGING_IMPL_JDK14_LOGGER = "org.shaded.apache.commons.logging.impl.Jdk14Logger";
    private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.shaded.apache.commons.logging.impl.Log4JLogger";
    private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.shaded.apache.commons.logging.impl.Jdk13LumberjackLogger";
    private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.shaded.apache.commons.logging.impl.SimpleLog";
    public static final String LOG_PROPERTY = "org.shaded.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.shaded.apache.commons.logging.log";
    private static final String PKG_IMPL = "org.shaded.apache.commons.logging.impl.";
    private static final int PKG_LEN = PKG_IMPL.length();
    static Class class$java$lang$String;
    static Class class$org$apache$commons$logging$Log;
    static Class class$org$apache$commons$logging$LogFactory;
    static Class class$org$apache$commons$logging$impl$LogFactoryImpl;
    private static final String[] classesToDiscover = {LOGGING_IMPL_LOG4J_LOGGER, LOGGING_IMPL_JDK14_LOGGER, LOGGING_IMPL_LUMBERJACK_LOGGER, LOGGING_IMPL_SIMPLE_LOGGER};
    private boolean allowFlawedContext;
    private boolean allowFlawedDiscovery;
    private boolean allowFlawedHierarchy;
    protected Hashtable attributes = new Hashtable();
    private String diagnosticPrefix;
    protected Hashtable instances = new Hashtable();
    private String logClassName;
    protected Constructor logConstructor = null;
    protected Class[] logConstructorSignature;
    protected Method logMethod;
    protected Class[] logMethodSignature;
    private boolean useTCCL = true;

    static ClassLoader access$000() throws LogConfigurationException {
        return LogFactory.directGetContextClassLoader();
    }

    public LogFactoryImpl() {
        Class cls;
        Class cls2;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        clsArr[0] = cls;
        this.logConstructorSignature = clsArr;
        this.logMethod = null;
        Class[] clsArr2 = new Class[1];
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls2 = class$(LogFactory.FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls2;
        } else {
            cls2 = class$org$apache$commons$logging$LogFactory;
        }
        clsArr2[0] = cls2;
        this.logMethodSignature = clsArr2;
        initDiagnostics();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Instance created.");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public String[] getAttributeNames() {
        Vector names = new Vector();
        Enumeration keys = this.attributes.keys();
        while (keys.hasMoreElements()) {
            names.addElement((String) keys.nextElement());
        }
        String[] results = new String[names.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = (String) names.elementAt(i);
        }
        return results;
    }

    public Log getInstance(Class clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    public Log getInstance(String name) throws LogConfigurationException {
        Log instance = (Log) this.instances.get(name);
        if (instance != null) {
            return instance;
        }
        Log instance2 = newInstance(name);
        this.instances.put(name, instance2);
        return instance2;
    }

    public void release() {
        logDiagnostic("Releasing all known loggers");
        this.instances.clear();
    }

    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        if (this.logConstructor != null) {
            logDiagnostic("setAttribute: call too late; configuration already performed.");
        }
        if (value == null) {
            this.attributes.remove(name);
        } else {
            this.attributes.put(name, value);
        }
        if (name.equals(LogFactory.TCCL_KEY)) {
            this.useTCCL = Boolean.valueOf(value.toString()).booleanValue();
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return LogFactory.getContextClassLoader();
    }

    protected static boolean isDiagnosticsEnabled() {
        return LogFactory.isDiagnosticsEnabled();
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        return LogFactory.getClassLoader(clazz);
    }

    private void initDiagnostics() {
        String classLoaderName;
        ClassLoader classLoader = getClassLoader(getClass());
        if (classLoader == null) {
            classLoaderName = "BOOTLOADER";
        } else {
            try {
                classLoaderName = LogFactory.objectId(classLoader);
            } catch (SecurityException e) {
                classLoaderName = "UNKNOWN";
            }
        }
        this.diagnosticPrefix = new StringBuffer().append("[LogFactoryImpl@").append(System.identityHashCode(this)).append(" from ").append(classLoaderName).append("] ").toString();
    }

    /* access modifiers changed from: protected */
    public void logDiagnostic(String msg) {
        if (isDiagnosticsEnabled()) {
            LogFactory.logRawDiagnostic(new StringBuffer().append(this.diagnosticPrefix).append(msg).toString());
        }
    }

    /* access modifiers changed from: protected */
    public String getLogClassName() {
        if (this.logClassName == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logClassName;
    }

    /* access modifiers changed from: protected */
    public Constructor getLogConstructor() throws LogConfigurationException {
        if (this.logConstructor == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logConstructor;
    }

    /* access modifiers changed from: protected */
    public boolean isJdk13LumberjackAvailable() {
        return isLogLibraryAvailable("Jdk13Lumberjack", LOGGING_IMPL_LUMBERJACK_LOGGER);
    }

    /* access modifiers changed from: protected */
    public boolean isJdk14Available() {
        return isLogLibraryAvailable("Jdk14", LOGGING_IMPL_JDK14_LOGGER);
    }

    /* access modifiers changed from: protected */
    public boolean isLog4JAvailable() {
        return isLogLibraryAvailable("Log4J", LOGGING_IMPL_LOG4J_LOGGER);
    }

    /* access modifiers changed from: protected */
    public Log newInstance(String name) throws LogConfigurationException {
        Log instance;
        try {
            if (this.logConstructor == null) {
                instance = discoverLogImplementation(name);
            } else {
                instance = (Log) this.logConstructor.newInstance(new Object[]{name});
            }
            if (this.logMethod != null) {
                this.logMethod.invoke(instance, new Object[]{this});
            }
            return instance;
        } catch (LogConfigurationException lce) {
            throw lce;
        } catch (InvocationTargetException e) {
            Throwable c = e.getTargetException();
            if (c != null) {
                throw new LogConfigurationException(c);
            }
            throw new LogConfigurationException((Throwable) e);
        } catch (Throwable t) {
            throw new LogConfigurationException(t);
        }
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return LogFactoryImpl.access$000();
            }
        });
    }

    private static String getSystemProperty(String key, String def) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction(key, def) {
            private final String val$def;
            private final String val$key;

            {
                this.val$key = val$key;
                this.val$def = val$def;
            }

            public Object run() {
                return System.getProperty(this.val$key, this.val$def);
            }
        });
    }

    private ClassLoader getParentClassLoader(ClassLoader cl) {
        try {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction(this, cl) {
                private final LogFactoryImpl this$0;
                private final ClassLoader val$cl;

                {
                    this.this$0 = this$0;
                    this.val$cl = val$cl;
                }

                public Object run() {
                    return this.val$cl.getParent();
                }
            });
        } catch (SecurityException e) {
            logDiagnostic("[SECURITY] Unable to obtain parent classloader");
            return null;
        }
    }

    private boolean isLogLibraryAvailable(String name, String classname) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Checking for '").append(name).append("'.").toString());
        }
        try {
            if (createLogFromClass(classname, getClass().getName(), false) != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Found '").append(name).append("'.").toString());
                }
                return true;
            } else if (!isDiagnosticsEnabled()) {
                return false;
            } else {
                logDiagnostic(new StringBuffer().append("Did not find '").append(name).append("'.").toString());
                return false;
            }
        } catch (LogConfigurationException e) {
            if (!isDiagnosticsEnabled()) {
                return false;
            }
            logDiagnostic(new StringBuffer().append("Logging system '").append(name).append("' is available but not useable.").toString());
            return false;
        }
    }

    private String getConfigurationValue(String property) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] Trying to get configuration for item ").append(property).toString());
        }
        Object valueObj = getAttribute(property);
        if (valueObj != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Found LogFactory attribute [").append(valueObj).append("] for ").append(property).toString());
            }
            return valueObj.toString();
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] No LogFactory attribute found for ").append(property).toString());
        }
        try {
            String value = getSystemProperty(property, (String) null);
            if (value == null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[ENV] No system property found for property ").append(property).toString());
                }
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[ENV] No configuration defined for item ").append(property).toString());
                }
                return null;
            } else if (!isDiagnosticsEnabled()) {
                return value;
            } else {
                logDiagnostic(new StringBuffer().append("[ENV] Found system property [").append(value).append("] for ").append(property).toString());
                return value;
            }
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Security prevented reading system property ").append(property).toString());
            }
        }
    }

    private boolean getBooleanConfiguration(String key, boolean dflt) {
        String val = getConfigurationValue(key);
        return val == null ? dflt : Boolean.valueOf(val).booleanValue();
    }

    private void initConfiguration() {
        this.allowFlawedContext = getBooleanConfiguration(ALLOW_FLAWED_CONTEXT_PROPERTY, true);
        this.allowFlawedDiscovery = getBooleanConfiguration(ALLOW_FLAWED_DISCOVERY_PROPERTY, true);
        this.allowFlawedHierarchy = getBooleanConfiguration(ALLOW_FLAWED_HIERARCHY_PROPERTY, true);
    }

    private Log discoverLogImplementation(String logCategory) throws LogConfigurationException {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Discovering a Log implementation...");
        }
        initConfiguration();
        Log result = null;
        String specifiedLogClassName = findUserSpecifiedLogClassName();
        if (specifiedLogClassName != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Attempting to load user-specified log class '").append(specifiedLogClassName).append("'...").toString());
            }
            Log result2 = createLogFromClass(specifiedLogClassName, logCategory, true);
            if (result2 != null) {
                return result2;
            }
            StringBuffer messageBuffer = new StringBuffer("User-specified log class '");
            messageBuffer.append(specifiedLogClassName);
            messageBuffer.append("' cannot be found or is not useable.");
            if (specifiedLogClassName != null) {
                informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LOG4J_LOGGER);
                informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_JDK14_LOGGER);
                informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LUMBERJACK_LOGGER);
                informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_SIMPLE_LOGGER);
            }
            throw new LogConfigurationException(messageBuffer.toString());
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
        }
        for (int i = 0; i < classesToDiscover.length && result == null; i++) {
            result = createLogFromClass(classesToDiscover[i], logCategory, true);
        }
        if (result != null) {
            return result;
        }
        throw new LogConfigurationException("No suitable Log implementation");
    }

    private void informUponSimilarName(StringBuffer messageBuffer, String name, String candidate) {
        if (!name.equals(candidate)) {
            if (name.regionMatches(true, 0, candidate, 0, PKG_LEN + 5)) {
                messageBuffer.append(" Did you mean '");
                messageBuffer.append(candidate);
                messageBuffer.append("'?");
            }
        }
    }

    private String findUserSpecifiedLogClassName() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
        }
        String specifiedClass = (String) getAttribute(LOG_PROPERTY);
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
            }
            specifiedClass = (String) getAttribute(LOG_PROPERTY_OLD);
        }
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
            }
            try {
                specifiedClass = getSystemProperty(LOG_PROPERTY, (String) null);
            } catch (SecurityException e) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.Log' - ").append(e.getMessage()).toString());
                }
            }
        }
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
            }
            try {
                specifiedClass = getSystemProperty(LOG_PROPERTY_OLD, (String) null);
            } catch (SecurityException e2) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.log' - ").append(e2.getMessage()).toString());
                }
            }
        }
        if (specifiedClass != null) {
            return specifiedClass.trim();
        }
        return specifiedClass;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v31, resolved type: org.shaded.apache.commons.logging.Log} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.shaded.apache.commons.logging.Log createLogFromClass(java.lang.String r21, java.lang.String r22, boolean r23) throws org.shaded.apache.commons.logging.LogConfigurationException {
        /*
            r20 = this;
            boolean r17 = isDiagnosticsEnabled()
            if (r17 == 0) goto L_0x002a
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "Attempting to instantiate '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "'"
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
        L_0x002a:
            r17 = 1
            r0 = r17
            java.lang.Object[] r12 = new java.lang.Object[r0]
            r17 = 0
            r12[r17] = r22
            r7 = 0
            r4 = 0
            r8 = 0
            java.lang.ClassLoader r5 = r20.getBaseClassLoader()
        L_0x003b:
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "Trying to load '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "' from classloader "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r5)
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
            boolean r17 = isDiagnosticsEnabled()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            if (r17 == 0) goto L_0x00c6
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r18 = 46
            r19 = 47
            r0 = r21
            r1 = r18
            r2 = r19
            java.lang.String r18 = r0.replace(r1, r2)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = ".class"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r13 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            if (r5 == 0) goto L_0x016b
            java.net.URL r16 = r5.getResource(r13)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
        L_0x0094:
            if (r16 != 0) goto L_0x0186
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "Class '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "' ["
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            java.lang.StringBuffer r17 = r0.append(r13)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "] cannot be found."
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r17 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
        L_0x00c6:
            r3 = 0
            r17 = 1
            r0 = r21
            r1 = r17
            java.lang.Class r3 = java.lang.Class.forName(r0, r1, r5)     // Catch:{ ClassNotFoundException -> 0x020e }
        L_0x00d1:
            r0 = r20
            java.lang.Class[] r0 = r0.logConstructorSignature     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17 = r0
            r0 = r17
            java.lang.reflect.Constructor r4 = r3.getConstructor(r0)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.Object r10 = r4.newInstance(r12)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            boolean r0 = r10 instanceof org.shaded.apache.commons.logging.Log     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17 = r0
            if (r17 == 0) goto L_0x0300
            r8 = r3
            r0 = r10
            org.shaded.apache.commons.logging.Log r0 = (org.shaded.apache.commons.logging.Log) r0     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r7 = r0
        L_0x00ec:
            if (r7 == 0) goto L_0x016a
            if (r23 == 0) goto L_0x016a
            r0 = r21
            r1 = r20
            r1.logClassName = r0
            r0 = r20
            r0.logConstructor = r4
            java.lang.String r17 = "setLogFactory"
            r0 = r20
            java.lang.Class[] r0 = r0.logMethodSignature     // Catch:{ Throwable -> 0x031a }
            r18 = r0
            r0 = r17
            r1 = r18
            java.lang.reflect.Method r17 = r8.getMethod(r0, r1)     // Catch:{ Throwable -> 0x031a }
            r0 = r17
            r1 = r20
            r1.logMethod = r0     // Catch:{ Throwable -> 0x031a }
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ Throwable -> 0x031a }
            r17.<init>()     // Catch:{ Throwable -> 0x031a }
            java.lang.String r18 = "Found method setLogFactory(LogFactory) in '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ Throwable -> 0x031a }
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ Throwable -> 0x031a }
            java.lang.String r18 = "'"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ Throwable -> 0x031a }
            java.lang.String r17 = r17.toString()     // Catch:{ Throwable -> 0x031a }
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)     // Catch:{ Throwable -> 0x031a }
        L_0x0134:
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "Log adapter '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "' from classloader "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.ClassLoader r18 = r8.getClassLoader()
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r18)
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = " has been selected for use."
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
        L_0x016a:
            return r7
        L_0x016b:
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            java.lang.StringBuffer r17 = r0.append(r13)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = ".class"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r17 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.net.URL r16 = java.lang.ClassLoader.getSystemResource(r17)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            goto L_0x0094
        L_0x0186:
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "Class '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "' was found at '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            r1 = r16
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "'"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r17 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            goto L_0x00c6
        L_0x01ba:
            r6 = move-exception
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = ""
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = r6.getMessage()
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r9 = r17.toString()
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "The log adapter '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "' is missing dependencies when loaded via classloader "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r5)
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = ": "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = r9.trim()
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
            goto L_0x00ec
        L_0x020e:
            r11 = move-exception
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = ""
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = r11.getMessage()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r9 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "The log adapter '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "' is not available via classloader "
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r5)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = ": "
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = r9.trim()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r17 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.Class r3 = java.lang.Class.forName(r21)     // Catch:{ ClassNotFoundException -> 0x0266 }
            goto L_0x00d1
        L_0x0266:
            r14 = move-exception
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = ""
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = r14.getMessage()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r9 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = new java.lang.StringBuffer     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r17.<init>()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "The log adapter '"
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = "' is not available via the LogFactoryImpl class classloader: "
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r18 = r9.trim()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.StringBuffer r17 = r17.append(r18)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            java.lang.String r17 = r17.toString()     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
            goto L_0x00ec
        L_0x02ac:
            r6 = move-exception
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = ""
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = r6.getMessage()
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r9 = r17.toString()
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "The log adapter '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "' is unable to initialize itself when loaded via classloader "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r5)
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = ": "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = r9.trim()
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
            goto L_0x00ec
        L_0x0300:
            r0 = r20
            r0.handleFlawedHierarchy(r5, r3)     // Catch:{ NoClassDefFoundError -> 0x01ba, ExceptionInInitializerError -> 0x02ac, LogConfigurationException -> 0x030f, Throwable -> 0x0311 }
        L_0x0305:
            if (r5 == 0) goto L_0x00ec
            r0 = r20
            java.lang.ClassLoader r5 = r0.getParentClassLoader(r5)
            goto L_0x003b
        L_0x030f:
            r6 = move-exception
            throw r6
        L_0x0311:
            r15 = move-exception
            r0 = r20
            r1 = r21
            r0.handleFlawedDiscovery(r1, r5, r15)
            goto L_0x0305
        L_0x031a:
            r15 = move-exception
            r17 = 0
            r0 = r17
            r1 = r20
            r1.logMethod = r0
            java.lang.StringBuffer r17 = new java.lang.StringBuffer
            r17.<init>()
            java.lang.String r18 = "[INFO] '"
            java.lang.StringBuffer r17 = r17.append(r18)
            r0 = r17
            r1 = r21
            java.lang.StringBuffer r17 = r0.append(r1)
            java.lang.String r18 = "' from classloader "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = org.shaded.apache.commons.logging.LogFactory.objectId(r5)
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = " does not declare optional method "
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r18 = "setLogFactory(LogFactory)"
            java.lang.StringBuffer r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r20
            r1 = r17
            r0.logDiagnostic(r1)
            goto L_0x0134
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.commons.logging.impl.LogFactoryImpl.createLogFromClass(java.lang.String, java.lang.String, boolean):org.shaded.apache.commons.logging.Log");
    }

    private ClassLoader getBaseClassLoader() throws LogConfigurationException {
        Class cls;
        if (class$org$apache$commons$logging$impl$LogFactoryImpl == null) {
            cls = class$(LogFactory.FACTORY_DEFAULT);
            class$org$apache$commons$logging$impl$LogFactoryImpl = cls;
        } else {
            cls = class$org$apache$commons$logging$impl$LogFactoryImpl;
        }
        ClassLoader thisClassLoader = getClassLoader(cls);
        if (!this.useTCCL) {
            return thisClassLoader;
        }
        ClassLoader contextClassLoader = getContextClassLoaderInternal();
        ClassLoader baseClassLoader = getLowestClassLoader(contextClassLoader, thisClassLoader);
        if (baseClassLoader != null) {
            if (baseClassLoader != contextClassLoader) {
                if (!this.allowFlawedContext) {
                    throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
                }
            }
            return baseClassLoader;
        } else if (this.allowFlawedContext) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
            }
            return contextClassLoader;
        } else {
            throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
        }
    }

    private ClassLoader getLowestClassLoader(ClassLoader c1, ClassLoader c2) {
        if (c1 == null) {
            return c2;
        }
        if (c2 == null) {
            return c1;
        }
        for (ClassLoader current = c1; current != null; current = current.getParent()) {
            if (current == c2) {
                return c1;
            }
        }
        for (ClassLoader current2 = c2; current2 != null; current2 = current2.getParent()) {
            if (current2 == c1) {
                return c2;
            }
        }
        return null;
    }

    private void handleFlawedDiscovery(String logAdapterClassName, ClassLoader classLoader, Throwable discoveryFlaw) {
        Throwable cause;
        Throwable cause2;
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Could not instantiate Log '").append(logAdapterClassName).append("' -- ").append(discoveryFlaw.getClass().getName()).append(": ").append(discoveryFlaw.getLocalizedMessage()).toString());
            if ((discoveryFlaw instanceof InvocationTargetException) && (cause = ((InvocationTargetException) discoveryFlaw).getTargetException()) != null) {
                logDiagnostic(new StringBuffer().append("... InvocationTargetException: ").append(cause.getClass().getName()).append(": ").append(cause.getLocalizedMessage()).toString());
                if ((cause instanceof ExceptionInInitializerError) && (cause2 = ((ExceptionInInitializerError) cause).getException()) != null) {
                    logDiagnostic(new StringBuffer().append("... ExceptionInInitializerError: ").append(cause2.getClass().getName()).append(": ").append(cause2.getLocalizedMessage()).toString());
                }
            }
        }
        if (!this.allowFlawedDiscovery) {
            throw new LogConfigurationException(discoveryFlaw);
        }
    }

    private void handleFlawedHierarchy(ClassLoader badClassLoader, Class badClass) throws LogConfigurationException {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean implementsLog = false;
        if (class$org$apache$commons$logging$Log == null) {
            cls = class$(LOG_PROPERTY);
            class$org$apache$commons$logging$Log = cls;
        } else {
            cls = class$org$apache$commons$logging$Log;
        }
        String logInterfaceName = cls.getName();
        Class[] interfaces = badClass.getInterfaces();
        int i = 0;
        while (true) {
            if (i >= interfaces.length) {
                break;
            } else if (logInterfaceName.equals(interfaces[i].getName())) {
                implementsLog = true;
                break;
            } else {
                i++;
            }
        }
        if (implementsLog) {
            if (isDiagnosticsEnabled()) {
                try {
                    if (class$org$apache$commons$logging$Log == null) {
                        cls4 = class$(LOG_PROPERTY);
                        class$org$apache$commons$logging$Log = cls4;
                    } else {
                        cls4 = class$org$apache$commons$logging$Log;
                    }
                    logDiagnostic(new StringBuffer().append("Class '").append(badClass.getName()).append("' was found in classloader ").append(LogFactory.objectId(badClassLoader)).append(". It is bound to a Log interface which is not").append(" the one loaded from classloader ").append(LogFactory.objectId(getClassLoader(cls4))).toString());
                } catch (Throwable th) {
                    logDiagnostic(new StringBuffer().append("Error while trying to output diagnostics about bad class '").append(badClass).append("'").toString());
                }
            }
            if (!this.allowFlawedHierarchy) {
                StringBuffer msg = new StringBuffer();
                msg.append("Terminating logging for this context ");
                msg.append("due to bad log hierarchy. ");
                msg.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    cls3 = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = cls3;
                } else {
                    cls3 = class$org$apache$commons$logging$Log;
                }
                msg.append(cls3.getName());
                msg.append("' visible.");
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(msg.toString());
                }
                throw new LogConfigurationException(msg.toString());
            } else if (isDiagnosticsEnabled()) {
                StringBuffer msg2 = new StringBuffer();
                msg2.append("Warning: bad log hierarchy. ");
                msg2.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    cls2 = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = cls2;
                } else {
                    cls2 = class$org$apache$commons$logging$Log;
                }
                msg2.append(cls2.getName());
                msg2.append("' visible.");
                logDiagnostic(msg2.toString());
            }
        } else if (!this.allowFlawedDiscovery) {
            StringBuffer msg3 = new StringBuffer();
            msg3.append("Terminating logging for this context. ");
            msg3.append("Log class '");
            msg3.append(badClass.getName());
            msg3.append("' does not implement the Log interface.");
            if (isDiagnosticsEnabled()) {
                logDiagnostic(msg3.toString());
            }
            throw new LogConfigurationException(msg3.toString());
        } else if (isDiagnosticsEnabled()) {
            StringBuffer msg4 = new StringBuffer();
            msg4.append("[WARNING] Log class '");
            msg4.append(badClass.getName());
            msg4.append("' does not implement the Log interface.");
            logDiagnostic(msg4.toString());
        }
    }
}
