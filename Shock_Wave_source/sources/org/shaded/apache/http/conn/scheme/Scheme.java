package org.shaded.apache.http.conn.scheme;

import java.util.Locale;
import org.shaded.apache.http.annotation.Immutable;
import org.shaded.apache.http.util.LangUtils;

@Immutable
public final class Scheme {
    private final int defaultPort;
    private final boolean layered;
    private final String name;
    private final SocketFactory socketFactory;
    private String stringRep;

    public Scheme(String name2, SocketFactory factory, int port) {
        if (name2 == null) {
            throw new IllegalArgumentException("Scheme name may not be null");
        } else if (factory == null) {
            throw new IllegalArgumentException("Socket factory may not be null");
        } else if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port is invalid: " + port);
        } else {
            this.name = name2.toLowerCase(Locale.ENGLISH);
            this.socketFactory = factory;
            this.defaultPort = port;
            this.layered = factory instanceof LayeredSocketFactory;
        }
    }

    public final int getDefaultPort() {
        return this.defaultPort;
    }

    public final SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isLayered() {
        return this.layered;
    }

    public final int resolvePort(int port) {
        return port <= 0 ? this.defaultPort : port;
    }

    public final String toString() {
        if (this.stringRep == null) {
            this.stringRep = this.name + ':' + Integer.toString(this.defaultPort);
        }
        return this.stringRep;
    }

    public final boolean equals(Object obj) {
        boolean z = true;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scheme)) {
            return false;
        }
        Scheme s = (Scheme) obj;
        if (!this.name.equals(s.name) || this.defaultPort != s.defaultPort || this.layered != s.layered || !this.socketFactory.equals(s.socketFactory)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.defaultPort), (Object) this.name), this.layered), (Object) this.socketFactory);
    }
}
