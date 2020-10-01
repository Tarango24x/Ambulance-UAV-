package com.shaded.fasterxml.jackson.core.json;

import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.shaded.fasterxml.jackson.core.Base64Variant;
import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.core.ObjectCodec;
import com.shaded.fasterxml.jackson.core.SerializableString;
import com.shaded.fasterxml.jackson.core.p004io.CharTypes;
import com.shaded.fasterxml.jackson.core.p004io.IOContext;
import com.shaded.fasterxml.jackson.core.p004io.NumberOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class WriterBasedJsonGenerator extends JsonGeneratorImpl {
    protected static final char[] HEX_CHARS = CharTypes.copyHexChars();
    protected static final int SHORT_WRITE = 32;
    protected SerializableString _currentEscape;
    protected char[] _entityBuffer;
    protected char[] _outputBuffer;
    protected int _outputEnd;
    protected int _outputHead = 0;
    protected int _outputTail = 0;
    protected final Writer _writer;

    public WriterBasedJsonGenerator(IOContext iOContext, int i, ObjectCodec objectCodec, Writer writer) {
        super(iOContext, i, objectCodec);
        this._writer = writer;
        this._outputBuffer = iOContext.allocConcatBuffer();
        this._outputEnd = this._outputBuffer.length;
    }

    public Object getOutputTarget() {
        return this._writer;
    }

    public void writeFieldName(String str) throws IOException, JsonGenerationException {
        boolean z = true;
        int writeFieldName = this._writeContext.writeFieldName(str);
        if (writeFieldName == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (writeFieldName != 1) {
            z = false;
        }
        _writeFieldName(str, z);
    }

    public void writeFieldName(SerializableString serializableString) throws IOException, JsonGenerationException {
        boolean z = true;
        int writeFieldName = this._writeContext.writeFieldName(serializableString.getValue());
        if (writeFieldName == 4) {
            _reportError("Can not write a field name, expecting a value");
        }
        if (writeFieldName != 1) {
            z = false;
        }
        _writeFieldName(serializableString, z);
    }

    public void writeStartArray() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an array");
        this._writeContext = this._writeContext.createChildArrayContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartArray(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '[';
    }

    public void writeEndArray() throws IOException, JsonGenerationException {
        if (!this._writeContext.inArray()) {
            _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = ']';
        }
        this._writeContext = this._writeContext.getParent();
    }

    public void writeStartObject() throws IOException, JsonGenerationException {
        _verifyValueWrite("start an object");
        this._writeContext = this._writeContext.createChildObjectContext();
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeStartObject(this);
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '{';
    }

    public void writeEndObject() throws IOException, JsonGenerationException {
        if (!this._writeContext.inObject()) {
            _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
        }
        if (this._cfgPrettyPrinter != null) {
            this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        } else {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '}';
        }
        this._writeContext = this._writeContext.getParent();
    }

    /* access modifiers changed from: protected */
    public void _writeFieldName(String str, boolean z) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(str, z);
            return;
        }
        if (this._outputTail + 1 >= this._outputEnd) {
            _flushBuffer();
        }
        if (z) {
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = ',';
        }
        if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            _writeString(str);
            return;
        }
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
        _writeString(str);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr3 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        cArr3[i3] = '\"';
    }

    public void _writeFieldName(SerializableString serializableString, boolean z) throws IOException, JsonGenerationException {
        if (this._cfgPrettyPrinter != null) {
            _writePPFieldName(serializableString, z);
            return;
        }
        if (this._outputTail + 1 >= this._outputEnd) {
            _flushBuffer();
        }
        if (z) {
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = ',';
        }
        char[] asQuotedChars = serializableString.asQuotedChars();
        if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            writeRaw(asQuotedChars, 0, asQuotedChars.length);
            return;
        }
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
        int length = asQuotedChars.length;
        if (this._outputTail + length + 1 >= this._outputEnd) {
            writeRaw(asQuotedChars, 0, length);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr3 = this._outputBuffer;
            int i3 = this._outputTail;
            this._outputTail = i3 + 1;
            cArr3[i3] = '\"';
            return;
        }
        System.arraycopy(asQuotedChars, 0, this._outputBuffer, this._outputTail, length);
        this._outputTail += length;
        char[] cArr4 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        cArr4[i4] = '\"';
    }

    /* access modifiers changed from: protected */
    public void _writePPFieldName(String str, boolean z) throws IOException, JsonGenerationException {
        if (z) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
            _writeString(str);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            cArr2[i2] = '\"';
            return;
        }
        _writeString(str);
    }

    /* access modifiers changed from: protected */
    public void _writePPFieldName(SerializableString serializableString, boolean z) throws IOException, JsonGenerationException {
        if (z) {
            this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        } else {
            this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        char[] asQuotedChars = serializableString.asQuotedChars();
        if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i = this._outputTail;
            this._outputTail = i + 1;
            cArr[i] = '\"';
            writeRaw(asQuotedChars, 0, asQuotedChars.length);
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr2 = this._outputBuffer;
            int i2 = this._outputTail;
            this._outputTail = i2 + 1;
            cArr2[i2] = '\"';
            return;
        }
        writeRaw(asQuotedChars, 0, asQuotedChars.length);
    }

    public void writeString(String str) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (str == null) {
            _writeNull();
            return;
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        _writeString(str);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
    }

    public void writeString(char[] cArr, int i, int i2) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        cArr2[i3] = '\"';
        _writeString(cArr, i, i2);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr3 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        cArr3[i4] = '\"';
    }

    public void writeString(SerializableString serializableString) throws IOException, JsonGenerationException {
        _verifyValueWrite("write text value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        char[] asQuotedChars = serializableString.asQuotedChars();
        int length = asQuotedChars.length;
        if (length < 32) {
            if (length > this._outputEnd - this._outputTail) {
                _flushBuffer();
            }
            System.arraycopy(asQuotedChars, 0, this._outputBuffer, this._outputTail, length);
            this._outputTail += length;
        } else {
            _flushBuffer();
            this._writer.write(asQuotedChars, 0, length);
        }
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
    }

    public void writeRawUTF8String(byte[] bArr, int i, int i2) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeUTF8String(byte[] bArr, int i, int i2) throws IOException, JsonGenerationException {
        _reportUnsupportedOperation();
    }

    public void writeRaw(String str) throws IOException, JsonGenerationException {
        int length = str.length();
        int i = this._outputEnd - this._outputTail;
        if (i == 0) {
            _flushBuffer();
            i = this._outputEnd - this._outputTail;
        }
        if (i >= length) {
            str.getChars(0, length, this._outputBuffer, this._outputTail);
            this._outputTail += length;
            return;
        }
        writeRawLong(str);
    }

    public void writeRaw(String str, int i, int i2) throws IOException, JsonGenerationException {
        int i3 = this._outputEnd - this._outputTail;
        if (i3 < i2) {
            _flushBuffer();
            i3 = this._outputEnd - this._outputTail;
        }
        if (i3 >= i2) {
            str.getChars(i, i + i2, this._outputBuffer, this._outputTail);
            this._outputTail += i2;
            return;
        }
        writeRawLong(str.substring(i, i + i2));
    }

    public void writeRaw(SerializableString serializableString) throws IOException, JsonGenerationException {
        writeRaw(serializableString.getValue());
    }

    public void writeRaw(char[] cArr, int i, int i2) throws IOException, JsonGenerationException {
        if (i2 < 32) {
            if (i2 > this._outputEnd - this._outputTail) {
                _flushBuffer();
            }
            System.arraycopy(cArr, i, this._outputBuffer, this._outputTail, i2);
            this._outputTail += i2;
            return;
        }
        _flushBuffer();
        this._writer.write(cArr, i, i2);
    }

    public void writeRaw(char c) throws IOException, JsonGenerationException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = c;
    }

    private void writeRawLong(String str) throws IOException, JsonGenerationException {
        int i = this._outputEnd - this._outputTail;
        str.getChars(0, i, this._outputBuffer, this._outputTail);
        this._outputTail += i;
        _flushBuffer();
        int length = str.length() - i;
        while (length > this._outputEnd) {
            int i2 = this._outputEnd;
            str.getChars(i, i + i2, this._outputBuffer, 0);
            this._outputHead = 0;
            this._outputTail = i2;
            _flushBuffer();
            i += i2;
            length -= i2;
        }
        str.getChars(i, i + length, this._outputBuffer, 0);
        this._outputHead = 0;
        this._outputTail = length;
    }

    public void writeBinary(Base64Variant base64Variant, byte[] bArr, int i, int i2) throws IOException, JsonGenerationException {
        _verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        cArr[i3] = '\"';
        _writeBinary(base64Variant, bArr, i, i + i2);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i4 = this._outputTail;
        this._outputTail = i4 + 1;
        cArr2[i4] = '\"';
    }

    public int writeBinary(Base64Variant base64Variant, InputStream inputStream, int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write binary value");
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr[i2] = '\"';
        byte[] allocBase64Buffer = this._ioContext.allocBase64Buffer();
        if (i < 0) {
            try {
                i = _writeBinary(base64Variant, inputStream, allocBase64Buffer);
            } catch (Throwable th) {
                this._ioContext.releaseBase64Buffer(allocBase64Buffer);
                throw th;
            }
        } else {
            int _writeBinary = _writeBinary(base64Variant, inputStream, allocBase64Buffer, i);
            if (_writeBinary > 0) {
                _reportError("Too few bytes available: missing " + _writeBinary + " bytes (out of " + i + ")");
            }
        }
        this._ioContext.releaseBase64Buffer(allocBase64Buffer);
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        cArr2[i3] = '\"';
        return i;
    }

    public void writeNumber(short s) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedShort(s);
            return;
        }
        if (this._outputTail + 6 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputInt((int) s, this._outputBuffer, this._outputTail);
    }

    private void _writeQuotedShort(short s) throws IOException {
        if (this._outputTail + 8 >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        this._outputTail = NumberOutput.outputInt((int) s, this._outputBuffer, this._outputTail);
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
    }

    public void writeNumber(int i) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedInt(i);
            return;
        }
        if (this._outputTail + 11 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
    }

    private void _writeQuotedInt(int i) throws IOException {
        if (this._outputTail + 13 >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr[i2] = '\"';
        this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
        char[] cArr2 = this._outputBuffer;
        int i3 = this._outputTail;
        this._outputTail = i3 + 1;
        cArr2[i3] = '\"';
    }

    public void writeNumber(long j) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedLong(j);
            return;
        }
        if (this._outputTail + 21 >= this._outputEnd) {
            _flushBuffer();
        }
        this._outputTail = NumberOutput.outputLong(j, this._outputBuffer, this._outputTail);
    }

    private void _writeQuotedLong(long j) throws IOException {
        if (this._outputTail + 23 >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        this._outputTail = NumberOutput.outputLong(j, this._outputBuffer, this._outputTail);
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
    }

    public void writeNumber(BigInteger bigInteger) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (bigInteger == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(bigInteger);
        } else {
            writeRaw(bigInteger.toString());
        }
    }

    public void writeNumber(double d) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Double.isNaN(d) || Double.isInfinite(d)) && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(d));
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(d));
    }

    public void writeNumber(float f) throws IOException, JsonGenerationException {
        if (this._cfgNumbersAsStrings || ((Float.isNaN(f) || Float.isInfinite(f)) && isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))) {
            writeString(String.valueOf(f));
            return;
        }
        _verifyValueWrite("write number");
        writeRaw(String.valueOf(f));
    }

    public void writeNumber(BigDecimal bigDecimal) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (bigDecimal == null) {
            _writeNull();
        } else if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(bigDecimal);
        } else {
            writeRaw(bigDecimal.toString());
        }
    }

    public void writeNumber(String str) throws IOException, JsonGenerationException {
        _verifyValueWrite("write number");
        if (this._cfgNumbersAsStrings) {
            _writeQuotedRaw(str);
        } else {
            writeRaw(str);
        }
    }

    private void _writeQuotedRaw(Object obj) throws IOException {
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr = this._outputBuffer;
        int i = this._outputTail;
        this._outputTail = i + 1;
        cArr[i] = '\"';
        writeRaw(obj.toString());
        if (this._outputTail >= this._outputEnd) {
            _flushBuffer();
        }
        char[] cArr2 = this._outputBuffer;
        int i2 = this._outputTail;
        this._outputTail = i2 + 1;
        cArr2[i2] = '\"';
    }

    public void writeBoolean(boolean z) throws IOException, JsonGenerationException {
        int i;
        _verifyValueWrite("write boolean value");
        if (this._outputTail + 5 >= this._outputEnd) {
            _flushBuffer();
        }
        int i2 = this._outputTail;
        char[] cArr = this._outputBuffer;
        if (z) {
            cArr[i2] = 't';
            int i3 = i2 + 1;
            cArr[i3] = 'r';
            int i4 = i3 + 1;
            cArr[i4] = 'u';
            i = i4 + 1;
            cArr[i] = 'e';
        } else {
            cArr[i2] = 'f';
            int i5 = i2 + 1;
            cArr[i5] = 'a';
            int i6 = i5 + 1;
            cArr[i6] = 'l';
            int i7 = i6 + 1;
            cArr[i7] = 's';
            i = i7 + 1;
            cArr[i] = 'e';
        }
        this._outputTail = i + 1;
    }

    public void writeNull() throws IOException, JsonGenerationException {
        _verifyValueWrite("write null value");
        _writeNull();
    }

    /* access modifiers changed from: protected */
    public void _verifyValueWrite(String str) throws IOException, JsonGenerationException {
        char c;
        int writeValue = this._writeContext.writeValue();
        if (writeValue == 5) {
            _reportError("Can not " + str + ", expecting field name");
        }
        if (this._cfgPrettyPrinter == null) {
            switch (writeValue) {
                case 1:
                    c = ',';
                    break;
                case 2:
                    c = ':';
                    break;
                case 3:
                    if (this._rootValueSeparator != null) {
                        writeRaw(this._rootValueSeparator.getValue());
                        return;
                    }
                    return;
                default:
                    return;
            }
            if (this._outputTail >= this._outputEnd) {
                _flushBuffer();
            }
            this._outputBuffer[this._outputTail] = c;
            this._outputTail++;
            return;
        }
        _verifyPrettyValueWrite(str, writeValue);
    }

    /* access modifiers changed from: protected */
    public void _verifyPrettyValueWrite(String str, int i) throws IOException, JsonGenerationException {
        switch (i) {
            case 0:
                if (this._writeContext.inArray()) {
                    this._cfgPrettyPrinter.beforeArrayValues(this);
                    return;
                } else if (this._writeContext.inObject()) {
                    this._cfgPrettyPrinter.beforeObjectEntries(this);
                    return;
                } else {
                    return;
                }
            case 1:
                this._cfgPrettyPrinter.writeArrayValueSeparator(this);
                return;
            case 2:
                this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
                return;
            case 3:
                this._cfgPrettyPrinter.writeRootValueSeparator(this);
                return;
            default:
                _throwInternal();
                return;
        }
    }

    public void flush() throws IOException {
        _flushBuffer();
        if (this._writer != null && isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
            this._writer.flush();
        }
    }

    public void close() throws IOException {
        super.close();
        if (this._outputBuffer != null && isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                JsonWriteContext outputContext = getOutputContext();
                if (!outputContext.inArray()) {
                    if (!outputContext.inObject()) {
                        break;
                    }
                    writeEndObject();
                } else {
                    writeEndArray();
                }
            }
        }
        _flushBuffer();
        if (this._writer != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {
                this._writer.close();
            } else if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)) {
                this._writer.flush();
            }
        }
        _releaseBuffers();
    }

    /* access modifiers changed from: protected */
    public void _releaseBuffers() {
        char[] cArr = this._outputBuffer;
        if (cArr != null) {
            this._outputBuffer = null;
            this._ioContext.releaseConcatBuffer(cArr);
        }
    }

    private void _writeString(String str) throws IOException, JsonGenerationException {
        int length = str.length();
        if (length > this._outputEnd) {
            _writeLongString(str);
            return;
        }
        if (this._outputTail + length > this._outputEnd) {
            _flushBuffer();
        }
        str.getChars(0, length, this._outputBuffer, this._outputTail);
        if (this._characterEscapes != null) {
            _writeStringCustom(length);
        } else if (this._maximumNonEscapedChar != 0) {
            _writeStringASCII(length, this._maximumNonEscapedChar);
        } else {
            _writeString2(length);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0026, code lost:
        r3 = r7._outputBuffer;
        r4 = r7._outputTail;
        r7._outputTail = r4 + 1;
        r3 = r3[r4];
        _prependOrWriteCharacterEscape(r3, r1[r3]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0016, code lost:
        r3 = r7._outputTail - r7._outputHead;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        if (r3 <= 0) goto L_0x0026;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        r7._writer.write(r7._outputBuffer, r7._outputHead, r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeString2(int r8) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r7 = this;
            int r0 = r7._outputTail
            int r0 = r0 + r8
            int[] r1 = r7._outputEscapes
            int r2 = r1.length
        L_0x0006:
            int r3 = r7._outputTail
            if (r3 >= r0) goto L_0x003e
        L_0x000a:
            char[] r3 = r7._outputBuffer
            int r4 = r7._outputTail
            char r3 = r3[r4]
            if (r3 >= r2) goto L_0x0036
            r3 = r1[r3]
            if (r3 == 0) goto L_0x0036
            int r3 = r7._outputTail
            int r4 = r7._outputHead
            int r3 = r3 - r4
            if (r3 <= 0) goto L_0x0026
            java.io.Writer r4 = r7._writer
            char[] r5 = r7._outputBuffer
            int r6 = r7._outputHead
            r4.write(r5, r6, r3)
        L_0x0026:
            char[] r3 = r7._outputBuffer
            int r4 = r7._outputTail
            int r5 = r4 + 1
            r7._outputTail = r5
            char r3 = r3[r4]
            r4 = r1[r3]
            r7._prependOrWriteCharacterEscape(r3, r4)
            goto L_0x0006
        L_0x0036:
            int r3 = r7._outputTail
            int r3 = r3 + 1
            r7._outputTail = r3
            if (r3 < r0) goto L_0x000a
        L_0x003e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeString2(int):void");
    }

    private void _writeLongString(String str) throws IOException, JsonGenerationException {
        _flushBuffer();
        int length = str.length();
        int i = 0;
        do {
            int i2 = this._outputEnd;
            if (i + i2 > length) {
                i2 = length - i;
            }
            str.getChars(i, i + i2, this._outputBuffer, 0);
            if (this._characterEscapes != null) {
                _writeSegmentCustom(i2);
            } else if (this._maximumNonEscapedChar != 0) {
                _writeSegmentASCII(i2, this._maximumNonEscapedChar);
            } else {
                _writeSegment(i2);
            }
            i += i2;
        } while (i < length);
    }

    private void _writeSegment(int i) throws IOException, JsonGenerationException {
        char c;
        int[] iArr = this._outputEscapes;
        int length = iArr.length;
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            do {
                c = this._outputBuffer[i2];
                if ((c < length && iArr[c] != 0) || (i2 = i2 + 1) >= i) {
                    int i4 = i2 - i3;
                }
                c = this._outputBuffer[i2];
                break;
            } while ((i2 = i2 + 1) >= i);
            int i42 = i2 - i3;
            if (i42 > 0) {
                this._writer.write(this._outputBuffer, i3, i42);
                if (i2 >= i) {
                    return;
                }
            }
            int i5 = i2 + 1;
            i3 = _prependOrWriteCharacterEscape(this._outputBuffer, i5, i, c, iArr[c]);
            i2 = i5;
        }
    }

    private void _writeString(char[] cArr, int i, int i2) throws IOException, JsonGenerationException {
        if (this._characterEscapes != null) {
            _writeStringCustom(cArr, i, i2);
        } else if (this._maximumNonEscapedChar != 0) {
            _writeStringASCII(cArr, i, i2, this._maximumNonEscapedChar);
        } else {
            int i3 = i2 + i;
            int[] iArr = this._outputEscapes;
            int length = iArr.length;
            int i4 = i;
            while (i4 < i3) {
                int i5 = i4;
                do {
                    char c = cArr[i5];
                    if ((c < length && iArr[c] != 0) || (i5 = i5 + 1) >= i3) {
                        int i6 = i5 - i4;
                    }
                    char c2 = cArr[i5];
                    break;
                } while ((i5 = i5 + 1) >= i3);
                int i62 = i5 - i4;
                if (i62 < 32) {
                    if (this._outputTail + i62 > this._outputEnd) {
                        _flushBuffer();
                    }
                    if (i62 > 0) {
                        System.arraycopy(cArr, i4, this._outputBuffer, this._outputTail, i62);
                        this._outputTail += i62;
                    }
                } else {
                    _flushBuffer();
                    this._writer.write(cArr, i4, i62);
                }
                if (i5 < i3) {
                    i4 = i5 + 1;
                    char c3 = cArr[i5];
                    _appendCharacterEscape(c3, iArr[c3]);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeStringASCII(int r10, int r11) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r9 = this;
            int r0 = r9._outputTail
            int r1 = r0 + r10
            int[] r2 = r9._outputEscapes
            int r0 = r2.length
            int r3 = r11 + 1
            int r3 = java.lang.Math.min(r0, r3)
        L_0x000d:
            int r0 = r9._outputTail
            if (r0 >= r1) goto L_0x0043
        L_0x0011:
            char[] r0 = r9._outputBuffer
            int r4 = r9._outputTail
            char r4 = r0[r4]
            if (r4 >= r3) goto L_0x0037
            r0 = r2[r4]
            if (r0 == 0) goto L_0x003b
        L_0x001d:
            int r5 = r9._outputTail
            int r6 = r9._outputHead
            int r5 = r5 - r6
            if (r5 <= 0) goto L_0x002d
            java.io.Writer r6 = r9._writer
            char[] r7 = r9._outputBuffer
            int r8 = r9._outputHead
            r6.write(r7, r8, r5)
        L_0x002d:
            int r5 = r9._outputTail
            int r5 = r5 + 1
            r9._outputTail = r5
            r9._prependOrWriteCharacterEscape(r4, r0)
            goto L_0x000d
        L_0x0037:
            if (r4 <= r11) goto L_0x003b
            r0 = -1
            goto L_0x001d
        L_0x003b:
            int r0 = r9._outputTail
            int r0 = r0 + 1
            r9._outputTail = r0
            if (r0 < r1) goto L_0x0011
        L_0x0043:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeStringASCII(int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x002f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeSegmentASCII(int r10, int r11) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r9 = this;
            r0 = 0
            int[] r6 = r9._outputEscapes
            int r1 = r6.length
            int r2 = r11 + 1
            int r7 = java.lang.Math.min(r1, r2)
            r2 = r0
            r1 = r0
        L_0x000c:
            if (r1 >= r10) goto L_0x0025
        L_0x000e:
            char[] r3 = r9._outputBuffer
            char r4 = r3[r1]
            if (r4 >= r7) goto L_0x0026
            r5 = r6[r4]
            if (r5 == 0) goto L_0x002a
        L_0x0018:
            int r0 = r1 - r2
            if (r0 <= 0) goto L_0x0031
            java.io.Writer r3 = r9._writer
            char[] r8 = r9._outputBuffer
            r3.write(r8, r2, r0)
            if (r1 < r10) goto L_0x0031
        L_0x0025:
            return
        L_0x0026:
            if (r4 <= r11) goto L_0x002b
            r5 = -1
            goto L_0x0018
        L_0x002a:
            r0 = r5
        L_0x002b:
            int r1 = r1 + 1
            if (r1 < r10) goto L_0x000e
            r5 = r0
            goto L_0x0018
        L_0x0031:
            int r2 = r1 + 1
            char[] r1 = r9._outputBuffer
            r0 = r9
            r3 = r10
            int r0 = r0._prependOrWriteCharacterEscape(r1, r2, r3, r4, r5)
            r1 = r2
            r2 = r0
            r0 = r5
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeSegmentASCII(int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0018 A[EDGE_INSN: B:25:0x0018->B:7:0x0018 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeStringASCII(char[] r11, int r12, int r13, int r14) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r10 = this;
            int r3 = r13 + r12
            int[] r4 = r10._outputEscapes
            int r0 = r4.length
            int r1 = r14 + 1
            int r5 = java.lang.Math.min(r0, r1)
            r0 = 0
            r2 = r12
        L_0x000d:
            if (r2 >= r3) goto L_0x0038
            r1 = r2
        L_0x0010:
            char r6 = r11[r1]
            if (r6 >= r5) goto L_0x0039
            r0 = r4[r6]
            if (r0 == 0) goto L_0x003d
        L_0x0018:
            int r7 = r1 - r2
            r8 = 32
            if (r7 >= r8) goto L_0x0042
            int r8 = r10._outputTail
            int r8 = r8 + r7
            int r9 = r10._outputEnd
            if (r8 <= r9) goto L_0x0028
            r10._flushBuffer()
        L_0x0028:
            if (r7 <= 0) goto L_0x0036
            char[] r8 = r10._outputBuffer
            int r9 = r10._outputTail
            java.lang.System.arraycopy(r11, r2, r8, r9, r7)
            int r2 = r10._outputTail
            int r2 = r2 + r7
            r10._outputTail = r2
        L_0x0036:
            if (r1 < r3) goto L_0x004b
        L_0x0038:
            return
        L_0x0039:
            if (r6 <= r14) goto L_0x003d
            r0 = -1
            goto L_0x0018
        L_0x003d:
            int r1 = r1 + 1
            if (r1 < r3) goto L_0x0010
            goto L_0x0018
        L_0x0042:
            r10._flushBuffer()
            java.io.Writer r8 = r10._writer
            r8.write(r11, r2, r7)
            goto L_0x0036
        L_0x004b:
            int r2 = r1 + 1
            r10._appendCharacterEscape(r6, r0)
            goto L_0x000d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeStringASCII(char[], int, int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeStringCustom(int r12) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r11 = this;
            int r0 = r11._outputTail
            int r2 = r0 + r12
            int[] r3 = r11._outputEscapes
            int r0 = r11._maximumNonEscapedChar
            r1 = 1
            if (r0 >= r1) goto L_0x0041
            r0 = 65535(0xffff, float:9.1834E-41)
        L_0x000e:
            int r1 = r3.length
            int r4 = r0 + 1
            int r4 = java.lang.Math.min(r1, r4)
            com.shaded.fasterxml.jackson.core.io.CharacterEscapes r5 = r11._characterEscapes
        L_0x0017:
            int r1 = r11._outputTail
            if (r1 >= r2) goto L_0x005a
        L_0x001b:
            char[] r1 = r11._outputBuffer
            int r6 = r11._outputTail
            char r6 = r1[r6]
            if (r6 >= r4) goto L_0x0044
            r1 = r3[r6]
            if (r1 == 0) goto L_0x0052
        L_0x0027:
            int r7 = r11._outputTail
            int r8 = r11._outputHead
            int r7 = r7 - r8
            if (r7 <= 0) goto L_0x0037
            java.io.Writer r8 = r11._writer
            char[] r9 = r11._outputBuffer
            int r10 = r11._outputHead
            r8.write(r9, r10, r7)
        L_0x0037:
            int r7 = r11._outputTail
            int r7 = r7 + 1
            r11._outputTail = r7
            r11._prependOrWriteCharacterEscape(r6, r1)
            goto L_0x0017
        L_0x0041:
            int r0 = r11._maximumNonEscapedChar
            goto L_0x000e
        L_0x0044:
            if (r6 <= r0) goto L_0x0048
            r1 = -1
            goto L_0x0027
        L_0x0048:
            com.shaded.fasterxml.jackson.core.SerializableString r1 = r5.getEscapeSequence(r6)
            r11._currentEscape = r1
            if (r1 == 0) goto L_0x0052
            r1 = -2
            goto L_0x0027
        L_0x0052:
            int r1 = r11._outputTail
            int r1 = r1 + 1
            r11._outputTail = r1
            if (r1 < r2) goto L_0x001b
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeStringCustom(int):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: char} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0048 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeSegmentCustom(int r12) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r11 = this;
            r1 = 0
            int[] r7 = r11._outputEscapes
            int r0 = r11._maximumNonEscapedChar
            r2 = 1
            if (r0 >= r2) goto L_0x0031
            r0 = 65535(0xffff, float:9.1834E-41)
            r6 = r0
        L_0x000c:
            int r0 = r7.length
            int r2 = r6 + 1
            int r8 = java.lang.Math.min(r0, r2)
            com.shaded.fasterxml.jackson.core.io.CharacterEscapes r9 = r11._characterEscapes
            r2 = r1
            r0 = r1
        L_0x0017:
            if (r1 >= r12) goto L_0x0030
        L_0x0019:
            char[] r3 = r11._outputBuffer
            char r4 = r3[r1]
            if (r4 >= r8) goto L_0x0035
            r5 = r7[r4]
            if (r5 == 0) goto L_0x0043
        L_0x0023:
            int r0 = r1 - r2
            if (r0 <= 0) goto L_0x004a
            java.io.Writer r3 = r11._writer
            char[] r10 = r11._outputBuffer
            r3.write(r10, r2, r0)
            if (r1 < r12) goto L_0x004a
        L_0x0030:
            return
        L_0x0031:
            int r0 = r11._maximumNonEscapedChar
            r6 = r0
            goto L_0x000c
        L_0x0035:
            if (r4 <= r6) goto L_0x0039
            r5 = -1
            goto L_0x0023
        L_0x0039:
            com.shaded.fasterxml.jackson.core.SerializableString r3 = r9.getEscapeSequence(r4)
            r11._currentEscape = r3
            if (r3 == 0) goto L_0x0044
            r5 = -2
            goto L_0x0023
        L_0x0043:
            r0 = r5
        L_0x0044:
            int r1 = r1 + 1
            if (r1 < r12) goto L_0x0019
            r5 = r0
            goto L_0x0023
        L_0x004a:
            int r2 = r1 + 1
            char[] r1 = r11._outputBuffer
            r0 = r11
            r3 = r12
            int r0 = r0._prependOrWriteCharacterEscape(r1, r2, r3, r4, r5)
            r1 = r2
            r2 = r0
            r0 = r5
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeSegmentCustom(int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0022 A[EDGE_INSN: B:32:0x0022->B:10:0x0022 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void _writeStringCustom(char[] r13, int r14, int r15) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonGenerationException {
        /*
            r12 = this;
            int r4 = r15 + r14
            int[] r5 = r12._outputEscapes
            int r0 = r12._maximumNonEscapedChar
            r1 = 1
            if (r0 >= r1) goto L_0x0043
            r0 = 65535(0xffff, float:9.1834E-41)
        L_0x000c:
            int r1 = r5.length
            int r2 = r0 + 1
            int r6 = java.lang.Math.min(r1, r2)
            com.shaded.fasterxml.jackson.core.io.CharacterEscapes r7 = r12._characterEscapes
            r1 = 0
            r3 = r14
        L_0x0017:
            if (r3 >= r4) goto L_0x0042
            r2 = r3
        L_0x001a:
            char r8 = r13[r2]
            if (r8 >= r6) goto L_0x0046
            r1 = r5[r8]
            if (r1 == 0) goto L_0x0054
        L_0x0022:
            int r9 = r2 - r3
            r10 = 32
            if (r9 >= r10) goto L_0x0059
            int r10 = r12._outputTail
            int r10 = r10 + r9
            int r11 = r12._outputEnd
            if (r10 <= r11) goto L_0x0032
            r12._flushBuffer()
        L_0x0032:
            if (r9 <= 0) goto L_0x0040
            char[] r10 = r12._outputBuffer
            int r11 = r12._outputTail
            java.lang.System.arraycopy(r13, r3, r10, r11, r9)
            int r3 = r12._outputTail
            int r3 = r3 + r9
            r12._outputTail = r3
        L_0x0040:
            if (r2 < r4) goto L_0x0062
        L_0x0042:
            return
        L_0x0043:
            int r0 = r12._maximumNonEscapedChar
            goto L_0x000c
        L_0x0046:
            if (r8 <= r0) goto L_0x004a
            r1 = -1
            goto L_0x0022
        L_0x004a:
            com.shaded.fasterxml.jackson.core.SerializableString r9 = r7.getEscapeSequence(r8)
            r12._currentEscape = r9
            if (r9 == 0) goto L_0x0054
            r1 = -2
            goto L_0x0022
        L_0x0054:
            int r2 = r2 + 1
            if (r2 < r4) goto L_0x001a
            goto L_0x0022
        L_0x0059:
            r12._flushBuffer()
            java.io.Writer r10 = r12._writer
            r10.write(r13, r3, r9)
            goto L_0x0040
        L_0x0062:
            int r3 = r2 + 1
            r12._appendCharacterEscape(r8, r1)
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.WriterBasedJsonGenerator._writeStringCustom(char[], int, int):void");
    }

    /* access modifiers changed from: protected */
    public void _writeBinary(Base64Variant base64Variant, byte[] bArr, int i, int i2) throws IOException, JsonGenerationException {
        int i3 = i2 - 3;
        int i4 = this._outputEnd - 6;
        int maxLineLength = base64Variant.getMaxLineLength() >> 2;
        while (i <= i3) {
            if (this._outputTail > i4) {
                _flushBuffer();
            }
            int i5 = i + 1;
            int i6 = i5 + 1;
            i = i6 + 1;
            this._outputTail = base64Variant.encodeBase64Chunk((int) (((bArr[i5] & Ev3Constants.Opcode.TST) | (bArr[i] << 8)) << 8) | (bArr[i6] & Ev3Constants.Opcode.TST), this._outputBuffer, this._outputTail);
            maxLineLength--;
            if (maxLineLength <= 0) {
                char[] cArr = this._outputBuffer;
                int i7 = this._outputTail;
                this._outputTail = i7 + 1;
                cArr[i7] = '\\';
                char[] cArr2 = this._outputBuffer;
                int i8 = this._outputTail;
                this._outputTail = i8 + 1;
                cArr2[i8] = 'n';
                maxLineLength = base64Variant.getMaxLineLength() >> 2;
            }
        }
        int i9 = i2 - i;
        if (i9 > 0) {
            if (this._outputTail > i4) {
                _flushBuffer();
            }
            int i10 = i + 1;
            int i11 = bArr[i] << 16;
            if (i9 == 2) {
                int i12 = i10 + 1;
                i11 |= (bArr[i10] & Ev3Constants.Opcode.TST) << 8;
            }
            this._outputTail = base64Variant.encodeBase64Partial(i11, i9, this._outputBuffer, this._outputTail);
        }
    }

    /* access modifiers changed from: protected */
    public int _writeBinary(Base64Variant base64Variant, InputStream inputStream, byte[] bArr, int i) throws IOException, JsonGenerationException {
        int _readMore;
        int i2;
        int i3 = 0;
        int i4 = 0;
        int i5 = -3;
        int i6 = this._outputEnd - 6;
        int maxLineLength = base64Variant.getMaxLineLength() >> 2;
        int i7 = i;
        while (i7 > 2) {
            if (i3 > i5) {
                i4 = _readMore(inputStream, bArr, i3, i4, i7);
                i3 = 0;
                if (i4 < 3) {
                    break;
                }
                i5 = i4 - 3;
            }
            if (this._outputTail > i6) {
                _flushBuffer();
            }
            int i8 = i3 + 1;
            int i9 = i8 + 1;
            i3 = i9 + 1;
            i7 -= 3;
            this._outputTail = base64Variant.encodeBase64Chunk((int) (((bArr[i8] & Ev3Constants.Opcode.TST) | (bArr[i3] << 8)) << 8) | (bArr[i9] & Ev3Constants.Opcode.TST), this._outputBuffer, this._outputTail);
            int i10 = maxLineLength - 1;
            if (i10 <= 0) {
                char[] cArr = this._outputBuffer;
                int i11 = this._outputTail;
                this._outputTail = i11 + 1;
                cArr[i11] = '\\';
                char[] cArr2 = this._outputBuffer;
                int i12 = this._outputTail;
                this._outputTail = i12 + 1;
                cArr2[i12] = 'n';
                i10 = base64Variant.getMaxLineLength() >> 2;
            }
            maxLineLength = i10;
        }
        if (i7 <= 0 || (_readMore = _readMore(inputStream, bArr, i3, i4, i7)) <= 0) {
            return i7;
        }
        if (this._outputTail > i6) {
            _flushBuffer();
        }
        int i13 = bArr[0] << 16;
        if (1 < _readMore) {
            i13 |= (bArr[1] & Ev3Constants.Opcode.TST) << 8;
            i2 = 2;
        } else {
            i2 = 1;
        }
        this._outputTail = base64Variant.encodeBase64Partial(i13, i2, this._outputBuffer, this._outputTail);
        return i7 - i2;
    }

    /* access modifiers changed from: protected */
    public int _writeBinary(Base64Variant base64Variant, InputStream inputStream, byte[] bArr) throws IOException, JsonGenerationException {
        int i;
        int i2;
        int i3 = -3;
        int i4 = this._outputEnd - 6;
        int maxLineLength = base64Variant.getMaxLineLength() >> 2;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            if (i7 > i3) {
                i6 = _readMore(inputStream, bArr, i7, i6, bArr.length);
                if (i6 < 3) {
                    break;
                }
                i3 = i6 - 3;
                i7 = 0;
            }
            if (this._outputTail > i4) {
                _flushBuffer();
            }
            int i8 = i7 + 1;
            int i9 = i8 + 1;
            i7 = i9 + 1;
            i5 += 3;
            this._outputTail = base64Variant.encodeBase64Chunk((int) (((bArr[i8] & Ev3Constants.Opcode.TST) | (bArr[i7] << 8)) << 8) | (bArr[i9] & Ev3Constants.Opcode.TST), this._outputBuffer, this._outputTail);
            int i10 = maxLineLength - 1;
            if (i10 <= 0) {
                char[] cArr = this._outputBuffer;
                int i11 = this._outputTail;
                this._outputTail = i11 + 1;
                cArr[i11] = '\\';
                char[] cArr2 = this._outputBuffer;
                int i12 = this._outputTail;
                this._outputTail = i12 + 1;
                cArr2[i12] = 'n';
                i10 = base64Variant.getMaxLineLength() >> 2;
            }
            maxLineLength = i10;
        }
        if (0 >= i6) {
            return i5;
        }
        if (this._outputTail > i4) {
            _flushBuffer();
        }
        int i13 = bArr[0] << 16;
        if (1 < i6) {
            i = ((bArr[1] & Ev3Constants.Opcode.TST) << 8) | i13;
            i2 = 2;
        } else {
            i = i13;
            i2 = 1;
        }
        int i14 = i5 + i2;
        this._outputTail = base64Variant.encodeBase64Partial(i, i2, this._outputBuffer, this._outputTail);
        return i14;
    }

    private int _readMore(InputStream inputStream, byte[] bArr, int i, int i2, int i3) throws IOException {
        int read;
        int i4 = 0;
        while (i < i2) {
            bArr[i4] = bArr[i];
            i4++;
            i++;
        }
        int min = Math.min(i3, bArr.length);
        do {
            int i5 = min - i4;
            if (i5 == 0 || (read = inputStream.read(bArr, i4, i5)) < 0 || (i4 = i4 + read) >= 3) {
                return i4;
            }
            int i52 = min - i4;
            break;
        } while ((i4 = i4 + read) >= 3);
        return i4;
    }

    private void _writeNull() throws IOException {
        if (this._outputTail + 4 >= this._outputEnd) {
            _flushBuffer();
        }
        int i = this._outputTail;
        char[] cArr = this._outputBuffer;
        cArr[i] = 'n';
        int i2 = i + 1;
        cArr[i2] = 'u';
        int i3 = i2 + 1;
        cArr[i3] = 'l';
        int i4 = i3 + 1;
        cArr[i4] = 'l';
        this._outputTail = i4 + 1;
    }

    private void _prependOrWriteCharacterEscape(char c, int i) throws IOException, JsonGenerationException {
        String value;
        int i2;
        if (i >= 0) {
            if (this._outputTail >= 2) {
                int i3 = this._outputTail - 2;
                this._outputHead = i3;
                this._outputBuffer[i3] = '\\';
                this._outputBuffer[i3 + 1] = (char) i;
                return;
            }
            char[] cArr = this._entityBuffer;
            if (cArr == null) {
                cArr = _allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            cArr[1] = (char) i;
            this._writer.write(cArr, 0, 2);
        } else if (i == -2) {
            if (this._currentEscape == null) {
                value = this._characterEscapes.getEscapeSequence(c).getValue();
            } else {
                value = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            int length = value.length();
            if (this._outputTail >= length) {
                int i4 = this._outputTail - length;
                this._outputHead = i4;
                value.getChars(0, length, this._outputBuffer, i4);
                return;
            }
            this._outputHead = this._outputTail;
            this._writer.write(value);
        } else if (this._outputTail >= 6) {
            char[] cArr2 = this._outputBuffer;
            int i5 = this._outputTail - 6;
            this._outputHead = i5;
            cArr2[i5] = '\\';
            int i6 = i5 + 1;
            cArr2[i6] = 'u';
            if (c > 255) {
                int i7 = (c >> 8) & 255;
                int i8 = i6 + 1;
                cArr2[i8] = HEX_CHARS[i7 >> 4];
                i2 = i8 + 1;
                cArr2[i2] = HEX_CHARS[i7 & 15];
                c = (char) (c & 255);
            } else {
                int i9 = i6 + 1;
                cArr2[i9] = '0';
                i2 = i9 + 1;
                cArr2[i2] = '0';
            }
            int i10 = i2 + 1;
            cArr2[i10] = HEX_CHARS[c >> 4];
            cArr2[i10 + 1] = HEX_CHARS[c & 15];
        } else {
            char[] cArr3 = this._entityBuffer;
            if (cArr3 == null) {
                cArr3 = _allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            if (c > 255) {
                int i11 = (c >> 8) & 255;
                char c2 = c & 255;
                cArr3[10] = HEX_CHARS[i11 >> 4];
                cArr3[11] = HEX_CHARS[i11 & 15];
                cArr3[12] = HEX_CHARS[c2 >> 4];
                cArr3[13] = HEX_CHARS[c2 & 15];
                this._writer.write(cArr3, 8, 6);
                return;
            }
            cArr3[6] = HEX_CHARS[c >> 4];
            cArr3[7] = HEX_CHARS[c & 15];
            this._writer.write(cArr3, 2, 6);
        }
    }

    private int _prependOrWriteCharacterEscape(char[] cArr, int i, int i2, char c, int i3) throws IOException, JsonGenerationException {
        String value;
        int i4;
        if (i3 >= 0) {
            if (i <= 1 || i >= i2) {
                char[] cArr2 = this._entityBuffer;
                if (cArr2 == null) {
                    cArr2 = _allocateEntityBuffer();
                }
                cArr2[1] = (char) i3;
                this._writer.write(cArr2, 0, 2);
                return i;
            }
            int i5 = i - 2;
            cArr[i5] = '\\';
            cArr[i5 + 1] = (char) i3;
            return i5;
        } else if (i3 == -2) {
            if (this._currentEscape == null) {
                value = this._characterEscapes.getEscapeSequence(c).getValue();
            } else {
                value = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            int length = value.length();
            if (i < length || i >= i2) {
                this._writer.write(value);
                return i;
            }
            int i6 = i - length;
            value.getChars(0, length, cArr, i6);
            return i6;
        } else if (i <= 5 || i >= i2) {
            char[] cArr3 = this._entityBuffer;
            if (cArr3 == null) {
                cArr3 = _allocateEntityBuffer();
            }
            this._outputHead = this._outputTail;
            if (c > 255) {
                int i7 = (c >> 8) & 255;
                char c2 = c & 255;
                cArr3[10] = HEX_CHARS[i7 >> 4];
                cArr3[11] = HEX_CHARS[i7 & 15];
                cArr3[12] = HEX_CHARS[c2 >> 4];
                cArr3[13] = HEX_CHARS[c2 & 15];
                this._writer.write(cArr3, 8, 6);
                return i;
            }
            cArr3[6] = HEX_CHARS[c >> 4];
            cArr3[7] = HEX_CHARS[c & 15];
            this._writer.write(cArr3, 2, 6);
            return i;
        } else {
            int i8 = i - 6;
            int i9 = i8 + 1;
            cArr[i8] = '\\';
            int i10 = i9 + 1;
            cArr[i9] = 'u';
            if (c > 255) {
                int i11 = (c >> 8) & 255;
                int i12 = i10 + 1;
                cArr[i10] = HEX_CHARS[i11 >> 4];
                i4 = i12 + 1;
                cArr[i12] = HEX_CHARS[i11 & 15];
                c = (char) (c & 255);
            } else {
                int i13 = i10 + 1;
                cArr[i10] = '0';
                i4 = i13 + 1;
                cArr[i13] = '0';
            }
            int i14 = i4 + 1;
            cArr[i4] = HEX_CHARS[c >> 4];
            cArr[i14] = HEX_CHARS[c & 15];
            return i14 - 5;
        }
    }

    private void _appendCharacterEscape(char c, int i) throws IOException, JsonGenerationException {
        String value;
        int i2;
        if (i >= 0) {
            if (this._outputTail + 2 > this._outputEnd) {
                _flushBuffer();
            }
            char[] cArr = this._outputBuffer;
            int i3 = this._outputTail;
            this._outputTail = i3 + 1;
            cArr[i3] = '\\';
            char[] cArr2 = this._outputBuffer;
            int i4 = this._outputTail;
            this._outputTail = i4 + 1;
            cArr2[i4] = (char) i;
        } else if (i != -2) {
            if (this._outputTail + 2 > this._outputEnd) {
                _flushBuffer();
            }
            int i5 = this._outputTail;
            char[] cArr3 = this._outputBuffer;
            int i6 = i5 + 1;
            cArr3[i5] = '\\';
            int i7 = i6 + 1;
            cArr3[i6] = 'u';
            if (c > 255) {
                int i8 = (c >> 8) & 255;
                int i9 = i7 + 1;
                cArr3[i7] = HEX_CHARS[i8 >> 4];
                i2 = i9 + 1;
                cArr3[i9] = HEX_CHARS[i8 & 15];
                c = (char) (c & 255);
            } else {
                int i10 = i7 + 1;
                cArr3[i7] = '0';
                i2 = i10 + 1;
                cArr3[i10] = '0';
            }
            int i11 = i2 + 1;
            cArr3[i2] = HEX_CHARS[c >> 4];
            cArr3[i11] = HEX_CHARS[c & 15];
            this._outputTail = i11;
        } else {
            if (this._currentEscape == null) {
                value = this._characterEscapes.getEscapeSequence(c).getValue();
            } else {
                value = this._currentEscape.getValue();
                this._currentEscape = null;
            }
            int length = value.length();
            if (this._outputTail + length > this._outputEnd) {
                _flushBuffer();
                if (length > this._outputEnd) {
                    this._writer.write(value);
                    return;
                }
            }
            value.getChars(0, length, this._outputBuffer, this._outputTail);
            this._outputTail += length;
        }
    }

    private char[] _allocateEntityBuffer() {
        char[] cArr = new char[14];
        cArr[0] = '\\';
        cArr[2] = '\\';
        cArr[3] = 'u';
        cArr[4] = '0';
        cArr[5] = '0';
        cArr[8] = '\\';
        cArr[9] = 'u';
        this._entityBuffer = cArr;
        return cArr;
    }

    /* access modifiers changed from: protected */
    public void _flushBuffer() throws IOException {
        int i = this._outputTail - this._outputHead;
        if (i > 0) {
            int i2 = this._outputHead;
            this._outputHead = 0;
            this._outputTail = 0;
            this._writer.write(this._outputBuffer, i2, i);
        }
    }
}
