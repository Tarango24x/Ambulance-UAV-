package org.shaded.apache.http.impl.p005io;

import java.io.IOException;
import org.shaded.apache.http.HttpMessage;
import org.shaded.apache.http.HttpRequest;
import org.shaded.apache.http.message.LineFormatter;
import org.shaded.apache.http.p006io.SessionOutputBuffer;
import org.shaded.apache.http.params.HttpParams;

/* renamed from: org.shaded.apache.http.impl.io.HttpRequestWriter */
public class HttpRequestWriter extends AbstractMessageWriter {
    public HttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        super(buffer, formatter, params);
    }

    /* access modifiers changed from: protected */
    public void writeHeadLine(HttpMessage message) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, ((HttpRequest) message).getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
