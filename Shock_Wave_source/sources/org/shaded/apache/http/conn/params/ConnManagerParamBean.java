package org.shaded.apache.http.conn.params;

import org.shaded.apache.http.annotation.NotThreadSafe;
import org.shaded.apache.http.params.HttpAbstractParamBean;
import org.shaded.apache.http.params.HttpParams;

@NotThreadSafe
public class ConnManagerParamBean extends HttpAbstractParamBean {
    public ConnManagerParamBean(HttpParams params) {
        super(params);
    }

    public void setTimeout(long timeout) {
        this.params.setLongParameter(ConnManagerPNames.TIMEOUT, timeout);
    }

    public void setMaxTotalConnections(int maxConnections) {
        this.params.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, maxConnections);
    }

    public void setConnectionsPerRoute(ConnPerRouteBean connPerRoute) {
        this.params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, connPerRoute);
    }
}
