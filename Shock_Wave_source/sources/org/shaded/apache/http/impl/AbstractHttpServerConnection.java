package org.shaded.apache.http.impl;

import java.io.IOException;
import org.shaded.apache.http.HttpConnectionMetrics;
import org.shaded.apache.http.HttpEntityEnclosingRequest;
import org.shaded.apache.http.HttpException;
import org.shaded.apache.http.HttpRequest;
import org.shaded.apache.http.HttpRequestFactory;
import org.shaded.apache.http.HttpResponse;
import org.shaded.apache.http.HttpServerConnection;
import org.shaded.apache.http.impl.entity.EntityDeserializer;
import org.shaded.apache.http.impl.entity.EntitySerializer;
import org.shaded.apache.http.impl.entity.LaxContentLengthStrategy;
import org.shaded.apache.http.impl.entity.StrictContentLengthStrategy;
import org.shaded.apache.http.impl.p005io.HttpRequestParser;
import org.shaded.apache.http.impl.p005io.HttpResponseWriter;
import org.shaded.apache.http.message.LineFormatter;
import org.shaded.apache.http.message.LineParser;
import org.shaded.apache.http.p006io.EofSensor;
import org.shaded.apache.http.p006io.HttpMessageParser;
import org.shaded.apache.http.p006io.HttpMessageWriter;
import org.shaded.apache.http.p006io.SessionInputBuffer;
import org.shaded.apache.http.p006io.SessionOutputBuffer;
import org.shaded.apache.http.params.HttpParams;

public abstract class AbstractHttpServerConnection implements HttpServerConnection {
    private final EntityDeserializer entitydeserializer = createEntityDeserializer();
    private final EntitySerializer entityserializer = createEntitySerializer();
    private EofSensor eofSensor = null;
    private SessionInputBuffer inbuffer = null;
    private HttpConnectionMetricsImpl metrics = null;
    private SessionOutputBuffer outbuffer = null;
    private HttpMessageParser requestParser = null;
    private HttpMessageWriter responseWriter = null;

    /* access modifiers changed from: protected */
    public abstract void assertOpen() throws IllegalStateException;

    /* access modifiers changed from: protected */
    public EntityDeserializer createEntityDeserializer() {
        return new EntityDeserializer(new LaxContentLengthStrategy());
    }

    /* access modifiers changed from: protected */
    public EntitySerializer createEntitySerializer() {
        return new EntitySerializer(new StrictContentLengthStrategy());
    }

    /* access modifiers changed from: protected */
    public HttpRequestFactory createHttpRequestFactory() {
        return new DefaultHttpRequestFactory();
    }

    /* access modifiers changed from: protected */
    public HttpMessageParser createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
        return new HttpRequestParser(buffer, (LineParser) null, requestFactory, params);
    }

    /* access modifiers changed from: protected */
    public HttpMessageWriter createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
        return new HttpResponseWriter(buffer, (LineFormatter) null, params);
    }

    /* access modifiers changed from: protected */
    public void init(SessionInputBuffer inbuffer2, SessionOutputBuffer outbuffer2, HttpParams params) {
        if (inbuffer2 == null) {
            throw new IllegalArgumentException("Input session buffer may not be null");
        } else if (outbuffer2 == null) {
            throw new IllegalArgumentException("Output session buffer may not be null");
        } else {
            this.inbuffer = inbuffer2;
            this.outbuffer = outbuffer2;
            if (inbuffer2 instanceof EofSensor) {
                this.eofSensor = (EofSensor) inbuffer2;
            }
            this.requestParser = createRequestParser(inbuffer2, createHttpRequestFactory(), params);
            this.responseWriter = createResponseWriter(outbuffer2, params);
            this.metrics = new HttpConnectionMetricsImpl(inbuffer2.getMetrics(), outbuffer2.getMetrics());
        }
    }

    public HttpRequest receiveRequestHeader() throws HttpException, IOException {
        assertOpen();
        HttpRequest request = (HttpRequest) this.requestParser.parse();
        this.metrics.incrementRequestCount();
        return request;
    }

    public void receiveRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        assertOpen();
        request.setEntity(this.entitydeserializer.deserialize(this.inbuffer, request));
    }

    /* access modifiers changed from: protected */
    public void doFlush() throws IOException {
        this.outbuffer.flush();
    }

    public void flush() throws IOException {
        assertOpen();
        doFlush();
    }

    public void sendResponseHeader(HttpResponse response) throws HttpException, IOException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        assertOpen();
        this.responseWriter.write(response);
        if (response.getStatusLine().getStatusCode() >= 200) {
            this.metrics.incrementResponseCount();
        }
    }

    public void sendResponseEntity(HttpResponse response) throws HttpException, IOException {
        if (response.getEntity() != null) {
            this.entityserializer.serialize(this.outbuffer, response, response.getEntity());
        }
    }

    /* access modifiers changed from: protected */
    public boolean isEof() {
        return this.eofSensor != null && this.eofSensor.isEof();
    }

    public boolean isStale() {
        if (!isOpen() || isEof()) {
            return true;
        }
        try {
            this.inbuffer.isDataAvailable(1);
            return isEof();
        } catch (IOException e) {
            return true;
        }
    }

    public HttpConnectionMetrics getMetrics() {
        return this.metrics;
    }
}
