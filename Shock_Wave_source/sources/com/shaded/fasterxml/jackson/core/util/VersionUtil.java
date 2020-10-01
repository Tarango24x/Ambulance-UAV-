package com.shaded.fasterxml.jackson.core.util;

import com.shaded.fasterxml.jackson.core.Version;
import com.shaded.fasterxml.jackson.core.Versioned;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;
import org.shaded.apache.http.cookie.ClientCookie;
import org.shaded.apache.http.protocol.HTTP;

public class VersionUtil {
    public static final String PACKAGE_VERSION_CLASS_NAME = "PackageVersion";
    @Deprecated
    public static final String VERSION_FILE = "VERSION.txt";
    private static final Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");
    private final Version _version;

    protected VersionUtil() {
        Version version = null;
        try {
            version = versionFor(getClass());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load Version information for bundle (via " + getClass().getName() + ").");
        }
        this._version = version == null ? Version.unknownVersion() : version;
    }

    public Version version() {
        return this._version;
    }

    public static Version versionFor(Class<?> cls) {
        InputStreamReader inputStreamReader;
        Version packageVersionFor = packageVersionFor(cls);
        if (packageVersionFor != null) {
            return packageVersionFor;
        }
        InputStream resourceAsStream = cls.getResourceAsStream(VERSION_FILE);
        if (resourceAsStream == null) {
            return Version.unknownVersion();
        }
        try {
            inputStreamReader = new InputStreamReader(resourceAsStream, HTTP.UTF_8);
            Version doReadVersion = doReadVersion(inputStreamReader);
            try {
                inputStreamReader.close();
            } catch (IOException e) {
            }
            try {
                resourceAsStream.close();
                return doReadVersion;
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        } catch (UnsupportedEncodingException e3) {
            try {
                try {
                    return Version.unknownVersion();
                } catch (IOException e4) {
                    throw new RuntimeException(e4);
                }
            } finally {
                try {
                    resourceAsStream.close();
                } catch (IOException e5) {
                    throw new RuntimeException(e5);
                }
            }
        } catch (Throwable th) {
            try {
                inputStreamReader.close();
            } catch (IOException e6) {
            }
            throw th;
        }
    }

    public static Version packageVersionFor(Class<?> cls) {
        try {
            Class<?> cls2 = Class.forName(cls.getPackage().getName() + "." + PACKAGE_VERSION_CLASS_NAME, true, cls.getClassLoader());
            if (cls2 == null) {
                return null;
            }
            try {
                Object newInstance = cls2.newInstance();
                if (newInstance instanceof Versioned) {
                    return ((Versioned) newInstance).version();
                }
                throw new IllegalArgumentException("Bad version class " + cls2.getName() + ": does not implement " + Versioned.class.getName());
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new IllegalArgumentException("Failed to instantiate " + cls2.getName() + " to find version information, problem: " + e2.getMessage(), e2);
            }
        } catch (Exception e3) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0041, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0049, code lost:
        r0 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x001e  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0041 A[ExcHandler: all (r0v0 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0006] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.shaded.fasterxml.jackson.core.Version doReadVersion(java.io.Reader r6) {
        /*
            r1 = 0
            java.io.BufferedReader r3 = new java.io.BufferedReader
            r3.<init>(r6)
            java.lang.String r2 = r3.readLine()     // Catch:{ IOException -> 0x0032, all -> 0x0041 }
            if (r2 == 0) goto L_0x004d
            java.lang.String r0 = r3.readLine()     // Catch:{ IOException -> 0x0048, all -> 0x0041 }
            if (r0 == 0) goto L_0x0016
            java.lang.String r1 = r3.readLine()     // Catch:{ IOException -> 0x004b, all -> 0x0041 }
        L_0x0016:
            r3.close()     // Catch:{ IOException -> 0x002d }
            r5 = r1
            r1 = r0
            r0 = r5
        L_0x001c:
            if (r1 == 0) goto L_0x0022
            java.lang.String r1 = r1.trim()
        L_0x0022:
            if (r0 == 0) goto L_0x0028
            java.lang.String r0 = r0.trim()
        L_0x0028:
            com.shaded.fasterxml.jackson.core.Version r0 = parseVersion(r2, r1, r0)
            return r0
        L_0x002d:
            r3 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x001c
        L_0x0032:
            r0 = move-exception
            r0 = r1
            r2 = r1
        L_0x0035:
            r3.close()     // Catch:{ IOException -> 0x003c }
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x001c
        L_0x003c:
            r3 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L_0x001c
        L_0x0041:
            r0 = move-exception
            r3.close()     // Catch:{ IOException -> 0x0046 }
        L_0x0045:
            throw r0
        L_0x0046:
            r1 = move-exception
            goto L_0x0045
        L_0x0048:
            r0 = move-exception
            r0 = r1
            goto L_0x0035
        L_0x004b:
            r4 = move-exception
            goto L_0x0035
        L_0x004d:
            r0 = r1
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.util.VersionUtil.doReadVersion(java.io.Reader):com.shaded.fasterxml.jackson.core.Version");
    }

    public static Version mavenVersionFor(ClassLoader classLoader, String str, String str2) {
        InputStream resourceAsStream = classLoader.getResourceAsStream("META-INF/maven/" + str.replaceAll("\\.", "/") + "/" + str2 + "/pom.properties");
        if (resourceAsStream != null) {
            try {
                Properties properties = new Properties();
                properties.load(resourceAsStream);
                Version parseVersion = parseVersion(properties.getProperty(ClientCookie.VERSION_ATTR), properties.getProperty("groupId"), properties.getProperty("artifactId"));
                try {
                    resourceAsStream.close();
                    return parseVersion;
                } catch (IOException e) {
                    return parseVersion;
                }
            } catch (IOException e2) {
                try {
                    resourceAsStream.close();
                } catch (IOException e3) {
                }
            } catch (Throwable th) {
                try {
                    resourceAsStream.close();
                } catch (IOException e4) {
                }
                throw th;
            }
        }
        return Version.unknownVersion();
    }

    @Deprecated
    public static Version parseVersion(String str) {
        return parseVersion(str, (String) null, (String) null);
    }

    public static Version parseVersion(String str, String str2, String str3) {
        int i;
        int i2;
        String str4 = null;
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.length() == 0) {
            return null;
        }
        String[] split = VERSION_SEPARATOR.split(trim);
        int parseVersionPart = parseVersionPart(split[0]);
        if (split.length > 1) {
            i = parseVersionPart(split[1]);
        } else {
            i = 0;
        }
        if (split.length > 2) {
            i2 = parseVersionPart(split[2]);
        } else {
            i2 = 0;
        }
        if (split.length > 3) {
            str4 = split[3];
        }
        return new Version(parseVersionPart, i, i2, str4, str2, str3);
    }

    protected static int parseVersionPart(String str) {
        String str2 = str.toString();
        int length = str2.length();
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str2.charAt(i2);
            if (charAt > '9' || charAt < '0') {
                break;
            }
            i = (i * 10) + (charAt - '0');
        }
        return i;
    }

    public static final void throwInternal() {
        throw new RuntimeException("Internal error: this code path should never get executed");
    }
}
