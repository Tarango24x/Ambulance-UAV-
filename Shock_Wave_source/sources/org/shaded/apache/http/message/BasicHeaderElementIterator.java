package org.shaded.apache.http.message;

import java.util.NoSuchElementException;
import org.shaded.apache.http.FormattedHeader;
import org.shaded.apache.http.Header;
import org.shaded.apache.http.HeaderElement;
import org.shaded.apache.http.HeaderElementIterator;
import org.shaded.apache.http.HeaderIterator;
import org.shaded.apache.http.util.CharArrayBuffer;

public class BasicHeaderElementIterator implements HeaderElementIterator {
    private CharArrayBuffer buffer;
    private HeaderElement currentElement;
    private ParserCursor cursor;
    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;

    public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser2) {
        this.currentElement = null;
        this.buffer = null;
        this.cursor = null;
        if (headerIterator == null) {
            throw new IllegalArgumentException("Header iterator may not be null");
        } else if (parser2 == null) {
            throw new IllegalArgumentException("Parser may not be null");
        } else {
            this.headerIt = headerIterator;
            this.parser = parser2;
        }
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.DEFAULT);
    }

    private void bufferHeaderValue() {
        this.cursor = null;
        this.buffer = null;
        while (this.headerIt.hasNext()) {
            Header h = this.headerIt.nextHeader();
            if (h instanceof FormattedHeader) {
                this.buffer = ((FormattedHeader) h).getBuffer();
                this.cursor = new ParserCursor(0, this.buffer.length());
                this.cursor.updatePos(((FormattedHeader) h).getValuePos());
                return;
            }
            String value = h.getValue();
            if (value != null) {
                this.buffer = new CharArrayBuffer(value.length());
                this.buffer.append(value);
                this.cursor = new ParserCursor(0, this.buffer.length());
                return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0028  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseNextElement() {
        /*
            r5 = this;
            r4 = 0
        L_0x0001:
            org.shaded.apache.http.HeaderIterator r1 = r5.headerIt
            boolean r1 = r1.hasNext()
            if (r1 != 0) goto L_0x000d
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            if (r1 == 0) goto L_0x0044
        L_0x000d:
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            if (r1 == 0) goto L_0x0019
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            boolean r1 = r1.atEnd()
            if (r1 == 0) goto L_0x001c
        L_0x0019:
            r5.bufferHeaderValue()
        L_0x001c:
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            if (r1 == 0) goto L_0x0001
        L_0x0020:
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            boolean r1 = r1.atEnd()
            if (r1 != 0) goto L_0x0045
            org.shaded.apache.http.message.HeaderValueParser r1 = r5.parser
            org.shaded.apache.http.util.CharArrayBuffer r2 = r5.buffer
            org.shaded.apache.http.message.ParserCursor r3 = r5.cursor
            org.shaded.apache.http.HeaderElement r0 = r1.parseHeaderElement(r2, r3)
            java.lang.String r1 = r0.getName()
            int r1 = r1.length()
            if (r1 != 0) goto L_0x0042
            java.lang.String r1 = r0.getValue()
            if (r1 == 0) goto L_0x0020
        L_0x0042:
            r5.currentElement = r0
        L_0x0044:
            return
        L_0x0045:
            org.shaded.apache.http.message.ParserCursor r1 = r5.cursor
            boolean r1 = r1.atEnd()
            if (r1 == 0) goto L_0x0001
            r5.cursor = r4
            r5.buffer = r4
            goto L_0x0001
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.message.BasicHeaderElementIterator.parseNextElement():void");
    }

    public boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    public HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }
        if (this.currentElement == null) {
            throw new NoSuchElementException("No more header elements available");
        }
        HeaderElement element = this.currentElement;
        this.currentElement = null;
        return element;
    }

    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
