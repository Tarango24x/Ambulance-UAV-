package com.shaded.fasterxml.jackson.core.json;

import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.FullScreenVideoUtil;
import com.shaded.fasterxml.jackson.core.Base64Variant;
import com.shaded.fasterxml.jackson.core.JsonParseException;
import com.shaded.fasterxml.jackson.core.JsonParser;
import com.shaded.fasterxml.jackson.core.JsonToken;
import com.shaded.fasterxml.jackson.core.ObjectCodec;
import com.shaded.fasterxml.jackson.core.SerializableString;
import com.shaded.fasterxml.jackson.core.base.ParserBase;
import com.shaded.fasterxml.jackson.core.p004io.CharTypes;
import com.shaded.fasterxml.jackson.core.p004io.IOContext;
import com.shaded.fasterxml.jackson.core.sym.BytesToNameCanonicalizer;
import com.shaded.fasterxml.jackson.core.sym.Name;
import com.shaded.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class UTF8StreamJsonParser extends ParserBase {
    static final byte BYTE_LF = 10;
    private static final int[] sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
    private static final int[] sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
    protected boolean _bufferRecyclable;
    protected byte[] _inputBuffer;
    protected InputStream _inputStream;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer = new int[16];
    protected final BytesToNameCanonicalizer _symbols;
    protected boolean _tokenIncomplete = false;

    public UTF8StreamJsonParser(IOContext iOContext, int i, InputStream inputStream, ObjectCodec objectCodec, BytesToNameCanonicalizer bytesToNameCanonicalizer, byte[] bArr, int i2, int i3, boolean z) {
        super(iOContext, i);
        this._inputStream = inputStream;
        this._objectCodec = objectCodec;
        this._symbols = bytesToNameCanonicalizer;
        this._inputBuffer = bArr;
        this._inputPtr = i2;
        this._inputEnd = i3;
        this._bufferRecyclable = z;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec objectCodec) {
        this._objectCodec = objectCodec;
    }

    public int releaseBuffered(OutputStream outputStream) throws IOException {
        int i = this._inputEnd - this._inputPtr;
        if (i < 1) {
            return 0;
        }
        outputStream.write(this._inputBuffer, this._inputPtr, i);
        return i;
    }

    public Object getInputSource() {
        return this._inputStream;
    }

    /* access modifiers changed from: protected */
    public boolean loadMore() throws IOException {
        this._currInputProcessed += (long) this._inputEnd;
        this._currInputRowStart -= this._inputEnd;
        if (this._inputStream == null) {
            return false;
        }
        int read = this._inputStream.read(this._inputBuffer, 0, this._inputBuffer.length);
        if (read > 0) {
            this._inputPtr = 0;
            this._inputEnd = read;
            return true;
        }
        _closeInput();
        if (read != 0) {
            return false;
        }
        throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
    }

    /* access modifiers changed from: protected */
    public boolean _loadToHaveAtLeast(int i) throws IOException {
        if (this._inputStream == null) {
            return false;
        }
        int i2 = this._inputEnd - this._inputPtr;
        if (i2 <= 0 || this._inputPtr <= 0) {
            this._inputEnd = 0;
        } else {
            this._currInputProcessed += (long) this._inputPtr;
            this._currInputRowStart -= this._inputPtr;
            System.arraycopy(this._inputBuffer, this._inputPtr, this._inputBuffer, 0, i2);
            this._inputEnd = i2;
        }
        this._inputPtr = 0;
        while (this._inputEnd < i) {
            int read = this._inputStream.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            if (read < 1) {
                _closeInput();
                if (read != 0) {
                    return false;
                }
                throw new IOException("InputStream.read() returned 0 characters when trying to read " + i2 + " bytes");
            }
            this._inputEnd = read + this._inputEnd;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void _closeInput() throws IOException {
        if (this._inputStream != null) {
            if (this._ioContext.isResourceManaged() || isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)) {
                this._inputStream.close();
            }
            this._inputStream = null;
        }
    }

    /* access modifiers changed from: protected */
    public void _releaseBuffers() throws IOException {
        byte[] bArr;
        super._releaseBuffers();
        if (this._bufferRecyclable && (bArr = this._inputBuffer) != null) {
            this._inputBuffer = null;
            this._ioContext.releaseReadIOBuffer(bArr);
        }
    }

    public String getText() throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            return _getText2(this._currToken);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    public String getValueAsString() throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            return super.getValueAsString((String) null);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    public String getValueAsString(String str) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            return super.getValueAsString(str);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
        return this._textBuffer.contentsAsString();
    }

    /* access modifiers changed from: protected */
    public String _getText2(JsonToken jsonToken) {
        if (jsonToken == null) {
            return null;
        }
        switch (jsonToken) {
            case FIELD_NAME:
                return this._parsingContext.getCurrentName();
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                return this._textBuffer.contentsAsString();
            default:
                return jsonToken.asString();
        }
    }

    public char[] getTextCharacters() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return null;
        }
        switch (this._currToken) {
            case FIELD_NAME:
                if (!this._nameCopied) {
                    String currentName = this._parsingContext.getCurrentName();
                    int length = currentName.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(length);
                    } else if (this._nameCopyBuffer.length < length) {
                        this._nameCopyBuffer = new char[length];
                    }
                    currentName.getChars(0, length, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            case VALUE_STRING:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                break;
            default:
                return this._currToken.asCharArray();
        }
        return this._textBuffer.getTextBuffer();
    }

    public int getTextLength() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken) {
            case FIELD_NAME:
                return this._parsingContext.getCurrentName().length();
            case VALUE_STRING:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                break;
            default:
                return this._currToken.asCharArray().length;
        }
        return this._textBuffer.size();
    }

    public int getTextOffset() throws IOException, JsonParseException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken) {
            case VALUE_STRING:
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
                break;
            default:
                return 0;
        }
        return this._textBuffer.getTextOffset();
    }

    public byte[] getBinaryValue(Base64Variant base64Variant) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(base64Variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException e) {
                throw _constructError("Failed to decode VALUE_STRING as base64 (" + base64Variant + "): " + e.getMessage());
            }
        } else if (this._binaryValue == null) {
            ByteArrayBuilder _getByteArrayBuilder = _getByteArrayBuilder();
            _decodeBase64(getText(), _getByteArrayBuilder, base64Variant);
            this._binaryValue = _getByteArrayBuilder.toByteArray();
        }
        return this._binaryValue;
    }

    public int readBinaryValue(Base64Variant base64Variant, OutputStream outputStream) throws IOException, JsonParseException {
        if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
            byte[] binaryValue = getBinaryValue(base64Variant);
            outputStream.write(binaryValue);
            return binaryValue.length;
        }
        byte[] allocBase64Buffer = this._ioContext.allocBase64Buffer();
        try {
            return _readBinary(base64Variant, outputStream, allocBase64Buffer);
        } finally {
            this._ioContext.releaseBase64Buffer(allocBase64Buffer);
        }
    }

    /* access modifiers changed from: protected */
    public int _readBinary(Base64Variant base64Variant, OutputStream outputStream, byte[] bArr) throws IOException, JsonParseException {
        int i;
        int length = bArr.length - 3;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr2 = this._inputBuffer;
            int i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            byte b = bArr2[i4] & Ev3Constants.Opcode.TST;
            if (b > 32) {
                int decodeBase64Char = base64Variant.decodeBase64Char((int) b);
                if (decodeBase64Char < 0) {
                    if (b == 34) {
                        break;
                    }
                    decodeBase64Char = _decodeBase64Escape(base64Variant, (int) b, 0);
                    if (decodeBase64Char < 0) {
                        continue;
                    }
                }
                int i5 = decodeBase64Char;
                if (i3 > length) {
                    i2 += i3;
                    outputStream.write(bArr, 0, i3);
                    i = 0;
                } else {
                    i = i3;
                }
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i6 = this._inputPtr;
                this._inputPtr = i6 + 1;
                byte b2 = bArr3[i6] & Ev3Constants.Opcode.TST;
                int decodeBase64Char2 = base64Variant.decodeBase64Char((int) b2);
                if (decodeBase64Char2 < 0) {
                    decodeBase64Char2 = _decodeBase64Escape(base64Variant, (int) b2, 1);
                }
                int i7 = (i5 << 6) | decodeBase64Char2;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr4 = this._inputBuffer;
                int i8 = this._inputPtr;
                this._inputPtr = i8 + 1;
                byte b3 = bArr4[i8] & Ev3Constants.Opcode.TST;
                int decodeBase64Char3 = base64Variant.decodeBase64Char((int) b3);
                if (decodeBase64Char3 < 0) {
                    if (decodeBase64Char3 != -2) {
                        if (b3 == 34 && !base64Variant.usesPadding()) {
                            i3 = i + 1;
                            bArr[i] = (byte) (i7 >> 4);
                            break;
                        }
                        decodeBase64Char3 = _decodeBase64Escape(base64Variant, (int) b3, 2);
                    }
                    if (decodeBase64Char3 == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr5 = this._inputBuffer;
                        int i9 = this._inputPtr;
                        this._inputPtr = i9 + 1;
                        byte b4 = bArr5[i9] & Ev3Constants.Opcode.TST;
                        if (!base64Variant.usesPaddingChar((int) b4)) {
                            throw reportInvalidBase64Char(base64Variant, b4, 3, "expected padding character '" + base64Variant.getPaddingChar() + "'");
                        }
                        i3 = i + 1;
                        bArr[i] = (byte) (i7 >> 4);
                    }
                }
                int i10 = (i7 << 6) | decodeBase64Char3;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr6 = this._inputBuffer;
                int i11 = this._inputPtr;
                this._inputPtr = i11 + 1;
                byte b5 = bArr6[i11] & Ev3Constants.Opcode.TST;
                int decodeBase64Char4 = base64Variant.decodeBase64Char((int) b5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        if (b5 == 34 && !base64Variant.usesPadding()) {
                            int i12 = i10 >> 2;
                            int i13 = i + 1;
                            bArr[i] = (byte) (i12 >> 8);
                            i3 = i13 + 1;
                            bArr[i13] = (byte) i12;
                            break;
                        }
                        decodeBase64Char4 = _decodeBase64Escape(base64Variant, (int) b5, 3);
                    }
                    if (decodeBase64Char4 == -2) {
                        int i14 = i10 >> 2;
                        int i15 = i + 1;
                        bArr[i] = (byte) (i14 >> 8);
                        i3 = i15 + 1;
                        bArr[i15] = (byte) i14;
                    }
                }
                int i16 = (i10 << 6) | decodeBase64Char4;
                int i17 = i + 1;
                bArr[i] = (byte) (i16 >> 16);
                int i18 = i17 + 1;
                bArr[i17] = (byte) (i16 >> 8);
                i3 = i18 + 1;
                bArr[i18] = (byte) i16;
            }
        }
        this._tokenIncomplete = false;
        if (i3 <= 0) {
            return i2;
        }
        int i19 = i2 + i3;
        outputStream.write(bArr, 0, i3);
        return i19;
    }

    public JsonToken nextToken() throws IOException, JsonParseException {
        JsonToken parseNumberText;
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int _skipWSOrEnd = _skipWSOrEnd();
        if (_skipWSOrEnd < 0) {
            close();
            this._currToken = null;
            return null;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        if (_skipWSOrEnd == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(_skipWSOrEnd, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            JsonToken jsonToken = JsonToken.END_ARRAY;
            this._currToken = jsonToken;
            return jsonToken;
        } else if (_skipWSOrEnd == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(_skipWSOrEnd, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            JsonToken jsonToken2 = JsonToken.END_OBJECT;
            this._currToken = jsonToken2;
            return jsonToken2;
        } else {
            if (this._parsingContext.expectComma()) {
                if (_skipWSOrEnd != 44) {
                    _reportUnexpectedChar(_skipWSOrEnd, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                }
                _skipWSOrEnd = _skipWS();
            }
            if (!this._parsingContext.inObject()) {
                return _nextTokenNotInObject(_skipWSOrEnd);
            }
            this._parsingContext.setCurrentName(_parseFieldName(_skipWSOrEnd).getName());
            this._currToken = JsonToken.FIELD_NAME;
            int _skipWS = _skipWS();
            if (_skipWS != 58) {
                _reportUnexpectedChar(_skipWS, "was expecting a colon to separate field name and value");
            }
            int _skipWS2 = _skipWS();
            if (_skipWS2 == 34) {
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
                return this._currToken;
            }
            switch (_skipWS2) {
                case 45:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    parseNumberText = parseNumberText(_skipWS2);
                    break;
                case 91:
                    parseNumberText = JsonToken.START_ARRAY;
                    break;
                case 93:
                case 125:
                    _reportUnexpectedChar(_skipWS2, "expected a value");
                    break;
                case 102:
                    _matchToken("false", 1);
                    parseNumberText = JsonToken.VALUE_FALSE;
                    break;
                case 110:
                    _matchToken("null", 1);
                    parseNumberText = JsonToken.VALUE_NULL;
                    break;
                case 116:
                    break;
                case 123:
                    parseNumberText = JsonToken.START_OBJECT;
                    break;
                default:
                    parseNumberText = _handleUnexpectedValue(_skipWS2);
                    break;
            }
            _matchToken("true", 1);
            parseNumberText = JsonToken.VALUE_TRUE;
            this._nextToken = parseNumberText;
            return this._currToken;
        }
    }

    private JsonToken _nextTokenNotInObject(int i) throws IOException, JsonParseException {
        if (i == 34) {
            this._tokenIncomplete = true;
            JsonToken jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        }
        switch (i) {
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                JsonToken parseNumberText = parseNumberText(i);
                this._currToken = parseNumberText;
                return parseNumberText;
            case 91:
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                JsonToken jsonToken2 = JsonToken.START_ARRAY;
                this._currToken = jsonToken2;
                return jsonToken2;
            case 93:
            case 125:
                _reportUnexpectedChar(i, "expected a value");
                break;
            case 102:
                _matchToken("false", 1);
                JsonToken jsonToken3 = JsonToken.VALUE_FALSE;
                this._currToken = jsonToken3;
                return jsonToken3;
            case 110:
                _matchToken("null", 1);
                JsonToken jsonToken4 = JsonToken.VALUE_NULL;
                this._currToken = jsonToken4;
                return jsonToken4;
            case 116:
                break;
            case 123:
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                JsonToken jsonToken5 = JsonToken.START_OBJECT;
                this._currToken = jsonToken5;
                return jsonToken5;
            default:
                JsonToken _handleUnexpectedValue = _handleUnexpectedValue(i);
                this._currToken = _handleUnexpectedValue;
                return _handleUnexpectedValue;
        }
        _matchToken("true", 1);
        JsonToken jsonToken6 = JsonToken.VALUE_TRUE;
        this._currToken = jsonToken6;
        return jsonToken6;
    }

    private JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken jsonToken = this._nextToken;
        this._nextToken = null;
        if (jsonToken == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (jsonToken == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = jsonToken;
        return jsonToken;
    }

    public void close() throws IOException {
        super.close();
        this._symbols.release();
    }

    public boolean nextFieldName(SerializableString serializableString) throws IOException, JsonParseException {
        int i = 0;
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            _nextAfterName();
            return false;
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int _skipWSOrEnd = _skipWSOrEnd();
        if (_skipWSOrEnd < 0) {
            close();
            this._currToken = null;
            return false;
        }
        this._tokenInputTotal = (this._currInputProcessed + ((long) this._inputPtr)) - 1;
        this._tokenInputRow = this._currInputRow;
        this._tokenInputCol = (this._inputPtr - this._currInputRowStart) - 1;
        this._binaryValue = null;
        if (_skipWSOrEnd == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(_skipWSOrEnd, '}');
            }
            this._parsingContext = this._parsingContext.getParent();
            this._currToken = JsonToken.END_ARRAY;
            return false;
        } else if (_skipWSOrEnd == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(_skipWSOrEnd, ']');
            }
            this._parsingContext = this._parsingContext.getParent();
            this._currToken = JsonToken.END_OBJECT;
            return false;
        } else {
            if (this._parsingContext.expectComma()) {
                if (_skipWSOrEnd != 44) {
                    _reportUnexpectedChar(_skipWSOrEnd, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
                }
                _skipWSOrEnd = _skipWS();
            }
            if (!this._parsingContext.inObject()) {
                _nextTokenNotInObject(_skipWSOrEnd);
                return false;
            }
            if (_skipWSOrEnd == 34) {
                byte[] asQuotedUTF8 = serializableString.asQuotedUTF8();
                int length = asQuotedUTF8.length;
                if (this._inputPtr + length < this._inputEnd) {
                    int i2 = this._inputPtr + length;
                    if (this._inputBuffer[i2] == 34) {
                        int i3 = this._inputPtr;
                        while (i != length) {
                            if (asQuotedUTF8[i] == this._inputBuffer[i3 + i]) {
                                i++;
                            }
                        }
                        this._inputPtr = i2 + 1;
                        this._parsingContext.setCurrentName(serializableString.getValue());
                        this._currToken = JsonToken.FIELD_NAME;
                        _isNextTokenNameYes();
                        return true;
                    }
                }
            }
            return _isNextTokenNameMaybe(_skipWSOrEnd, serializableString);
        }
    }

    private void _isNextTokenNameYes() throws IOException, JsonParseException {
        int _skipColon;
        if (this._inputPtr >= this._inputEnd - 1 || this._inputBuffer[this._inputPtr] != 58) {
            _skipColon = _skipColon();
        } else {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr + 1;
            this._inputPtr = i;
            byte b = bArr[i];
            this._inputPtr++;
            if (b == 34) {
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
                return;
            } else if (b == 123) {
                this._nextToken = JsonToken.START_OBJECT;
                return;
            } else if (b == 91) {
                this._nextToken = JsonToken.START_ARRAY;
                return;
            } else {
                _skipColon = b & Ev3Constants.Opcode.TST;
                if (_skipColon <= 32 || _skipColon == 47) {
                    this._inputPtr--;
                    _skipColon = _skipWS();
                }
            }
        }
        switch (_skipColon) {
            case 34:
                this._tokenIncomplete = true;
                this._nextToken = JsonToken.VALUE_STRING;
                return;
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                this._nextToken = parseNumberText(_skipColon);
                return;
            case 91:
                this._nextToken = JsonToken.START_ARRAY;
                return;
            case 93:
            case 125:
                _reportUnexpectedChar(_skipColon, "expected a value");
                break;
            case 102:
                _matchToken("false", 1);
                this._nextToken = JsonToken.VALUE_FALSE;
                return;
            case 110:
                _matchToken("null", 1);
                this._nextToken = JsonToken.VALUE_NULL;
                return;
            case 116:
                break;
            case 123:
                this._nextToken = JsonToken.START_OBJECT;
                return;
            default:
                this._nextToken = _handleUnexpectedValue(_skipColon);
                return;
        }
        _matchToken("true", 1);
        this._nextToken = JsonToken.VALUE_TRUE;
    }

    private boolean _isNextTokenNameMaybe(int i, SerializableString serializableString) throws IOException, JsonParseException {
        JsonToken parseNumberText;
        String name = _parseFieldName(i).getName();
        this._parsingContext.setCurrentName(name);
        boolean equals = name.equals(serializableString.getValue());
        this._currToken = JsonToken.FIELD_NAME;
        int _skipWS = _skipWS();
        if (_skipWS != 58) {
            _reportUnexpectedChar(_skipWS, "was expecting a colon to separate field name and value");
        }
        int _skipWS2 = _skipWS();
        if (_skipWS2 == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
            return equals;
        }
        switch (_skipWS2) {
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                parseNumberText = parseNumberText(_skipWS2);
                break;
            case 91:
                parseNumberText = JsonToken.START_ARRAY;
                break;
            case 93:
            case 125:
                _reportUnexpectedChar(_skipWS2, "expected a value");
                break;
            case 102:
                _matchToken("false", 1);
                parseNumberText = JsonToken.VALUE_FALSE;
                break;
            case 110:
                _matchToken("null", 1);
                parseNumberText = JsonToken.VALUE_NULL;
                break;
            case 116:
                break;
            case 123:
                parseNumberText = JsonToken.START_OBJECT;
                break;
            default:
                parseNumberText = _handleUnexpectedValue(_skipWS2);
                break;
        }
        _matchToken("true", 1);
        parseNumberText = JsonToken.VALUE_TRUE;
        this._nextToken = parseNumberText;
        return equals;
    }

    public String nextTextValue() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken == JsonToken.VALUE_STRING) {
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                }
                return this._textBuffer.contentsAsString();
            } else if (jsonToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            } else if (jsonToken != JsonToken.START_OBJECT) {
                return null;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            }
        } else if (nextToken() == JsonToken.VALUE_STRING) {
            return getText();
        } else {
            return null;
        }
    }

    public int nextIntValue(int i) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.FIELD_NAME) {
            return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : i;
        }
        this._nameCopied = false;
        JsonToken jsonToken = this._nextToken;
        this._nextToken = null;
        this._currToken = jsonToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
            return getIntValue();
        }
        if (jsonToken == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            return i;
        } else if (jsonToken != JsonToken.START_OBJECT) {
            return i;
        } else {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            return i;
        }
    }

    public long nextLongValue(long j) throws IOException, JsonParseException {
        if (this._currToken != JsonToken.FIELD_NAME) {
            return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : j;
        }
        this._nameCopied = false;
        JsonToken jsonToken = this._nextToken;
        this._nextToken = null;
        this._currToken = jsonToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
            return getLongValue();
        }
        if (jsonToken == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            return j;
        } else if (jsonToken != JsonToken.START_OBJECT) {
            return j;
        } else {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            return j;
        }
    }

    public Boolean nextBooleanValue() throws IOException, JsonParseException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken == JsonToken.VALUE_TRUE) {
                return Boolean.TRUE;
            }
            if (jsonToken == JsonToken.VALUE_FALSE) {
                return Boolean.FALSE;
            }
            if (jsonToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            } else if (jsonToken != JsonToken.START_OBJECT) {
                return null;
            } else {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                return null;
            }
        } else {
            switch (nextToken()) {
                case VALUE_TRUE:
                    return Boolean.TRUE;
                case VALUE_FALSE:
                    return Boolean.FALSE;
                default:
                    return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public JsonToken parseNumberText(int i) throws IOException, JsonParseException {
        int i2;
        int i3;
        int i4 = 1;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        boolean z = i == 45;
        if (z) {
            emptyAndGetCurrentSegment[0] = '-';
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i5 = this._inputPtr;
            this._inputPtr = i5 + 1;
            i3 = bArr[i5] & Ev3Constants.Opcode.TST;
            if (i3 < 48 || i3 > 57) {
                return _handleInvalidNumberStart(i3, true);
            }
            i2 = 1;
        } else {
            i2 = 0;
            i3 = i;
        }
        if (i3 == 48) {
            i3 = _verifyNoLeadingZeroes();
        }
        int i6 = i2 + 1;
        emptyAndGetCurrentSegment[i2] = (char) i3;
        int length = this._inputPtr + emptyAndGetCurrentSegment.length;
        if (length > this._inputEnd) {
            length = this._inputEnd;
        }
        while (this._inputPtr < length) {
            byte[] bArr2 = this._inputBuffer;
            int i7 = this._inputPtr;
            this._inputPtr = i7 + 1;
            byte b = bArr2[i7] & Ev3Constants.Opcode.TST;
            if (b >= 48 && b <= 57) {
                i4++;
                emptyAndGetCurrentSegment[i6] = (char) b;
                i6++;
            } else if (b == 46 || b == 101 || b == 69) {
                return _parseFloatText(emptyAndGetCurrentSegment, i6, b, z, i4);
            } else {
                this._inputPtr--;
                this._textBuffer.setCurrentLength(i6);
                return resetInt(z, i4);
            }
        }
        return _parserNumber2(emptyAndGetCurrentSegment, i6, z, i4);
    }

    private JsonToken _parserNumber2(char[] cArr, int i, boolean z, int i2) throws IOException, JsonParseException {
        byte b;
        int i3 = i2;
        int i4 = i;
        char[] cArr2 = cArr;
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i5 = this._inputPtr;
                this._inputPtr = i5 + 1;
                b = bArr[i5] & Ev3Constants.Opcode.TST;
                if (b <= 57 && b >= 48) {
                    if (i4 >= cArr2.length) {
                        cArr2 = this._textBuffer.finishCurrentSegment();
                        i4 = 0;
                    }
                    int i6 = i4;
                    i4 = i6 + 1;
                    cArr2[i6] = (char) b;
                    i3++;
                }
            } else {
                this._textBuffer.setCurrentLength(i4);
                return resetInt(z, i3);
            }
        }
        if (b == 46 || b == 101 || b == 69) {
            return _parseFloatText(cArr2, i4, b, z, i3);
        }
        this._inputPtr--;
        this._textBuffer.setCurrentLength(i4);
        return resetInt(z, i3);
    }

    private int _verifyNoLeadingZeroes() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            return 48;
        }
        byte b = this._inputBuffer[this._inputPtr] & Ev3Constants.Opcode.TST;
        if (b < 48 || b > 57) {
            return 48;
        }
        if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        this._inputPtr++;
        if (b != 48) {
            return b;
        }
        do {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                return b;
            }
            b = this._inputBuffer[this._inputPtr] & Ev3Constants.Opcode.TST;
            if (b < 48 || b > 57) {
                return 48;
            }
            this._inputPtr++;
        } while (b == 48);
        return b;
    }

    private JsonToken _parseFloatText(char[] cArr, int i, int i2, boolean z, int i3) throws IOException, JsonParseException {
        int i4;
        char[] cArr2;
        int i5;
        int i6;
        boolean z2;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11 = 0;
        boolean z3 = false;
        if (i2 == 46) {
            int i12 = i + 1;
            cArr[i] = (char) i2;
            while (true) {
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    z3 = true;
                    break;
                }
                byte[] bArr = this._inputBuffer;
                int i13 = this._inputPtr;
                this._inputPtr = i13 + 1;
                i2 = bArr[i13] & Ev3Constants.Opcode.TST;
                if (i2 < 48 || i2 > 57) {
                    break;
                }
                i11++;
                if (i12 >= cArr.length) {
                    cArr = this._textBuffer.finishCurrentSegment();
                    i12 = 0;
                }
                int i14 = i12;
                i12 = i14 + 1;
                cArr[i14] = (char) i2;
            }
            if (i11 == 0) {
                reportUnexpectedNumberChar(i2, "Decimal point not followed by a digit");
            }
            i4 = i11;
            i5 = i12;
            cArr2 = cArr;
        } else {
            i4 = 0;
            cArr2 = cArr;
            i5 = i;
        }
        if (i2 == 101 || i2 == 69) {
            if (i5 >= cArr2.length) {
                cArr2 = this._textBuffer.finishCurrentSegment();
                i5 = 0;
            }
            int i15 = i5 + 1;
            cArr2[i5] = (char) i2;
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr2 = this._inputBuffer;
            int i16 = this._inputPtr;
            this._inputPtr = i16 + 1;
            byte b = bArr2[i16] & Ev3Constants.Opcode.TST;
            if (b == 45 || b == 43) {
                if (i15 >= cArr2.length) {
                    cArr2 = this._textBuffer.finishCurrentSegment();
                    i10 = 0;
                } else {
                    i10 = i15;
                }
                int i17 = i10 + 1;
                cArr2[i10] = (char) b;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i18 = this._inputPtr;
                this._inputPtr = i18 + 1;
                b = bArr3[i18] & Ev3Constants.Opcode.TST;
                i9 = i17;
                i8 = 0;
            } else {
                i9 = i15;
                i8 = 0;
            }
            while (true) {
                if (b <= 57 && b >= 48) {
                    i8++;
                    if (i9 >= cArr2.length) {
                        cArr2 = this._textBuffer.finishCurrentSegment();
                        i9 = 0;
                    }
                    int i19 = i9 + 1;
                    cArr2[i9] = (char) b;
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        i7 = i8;
                        z2 = true;
                        i6 = i19;
                        break;
                    }
                    byte[] bArr4 = this._inputBuffer;
                    int i20 = this._inputPtr;
                    this._inputPtr = i20 + 1;
                    b = bArr4[i20] & Ev3Constants.Opcode.TST;
                    i9 = i19;
                } else {
                    z2 = z3;
                    int i21 = i8;
                    i6 = i9;
                    i7 = i21;
                }
            }
            z2 = z3;
            int i212 = i8;
            i6 = i9;
            i7 = i212;
            if (i7 == 0) {
                reportUnexpectedNumberChar(b, "Exponent indicator not followed by a digit");
            }
        } else {
            z2 = z3;
            i6 = i5;
            i7 = 0;
        }
        if (!z2) {
            this._inputPtr--;
        }
        this._textBuffer.setCurrentLength(i6);
        return resetFloat(z, i3, i4, i7);
    }

    /* access modifiers changed from: protected */
    public Name _parseFieldName(int i) throws IOException, JsonParseException {
        if (i != 34) {
            return _handleUnusualFieldName(i);
        }
        if (this._inputPtr + 9 > this._inputEnd) {
            return slowParseFieldName();
        }
        byte[] bArr = this._inputBuffer;
        int[] iArr = sInputCodesLatin1;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2] & Ev3Constants.Opcode.TST;
        if (iArr[b] == 0) {
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            byte b2 = bArr[i3] & Ev3Constants.Opcode.TST;
            if (iArr[b2] == 0) {
                byte b3 = (b << 8) | b2;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                byte b4 = bArr[i4] & Ev3Constants.Opcode.TST;
                if (iArr[b4] == 0) {
                    byte b5 = (b3 << 8) | b4;
                    int i5 = this._inputPtr;
                    this._inputPtr = i5 + 1;
                    byte b6 = bArr[i5] & Ev3Constants.Opcode.TST;
                    if (iArr[b6] == 0) {
                        byte b7 = (b5 << 8) | b6;
                        int i6 = this._inputPtr;
                        this._inputPtr = i6 + 1;
                        byte b8 = bArr[i6] & Ev3Constants.Opcode.TST;
                        if (iArr[b8] == 0) {
                            this._quad1 = b7;
                            return parseMediumFieldName(b8, iArr);
                        } else if (b8 == 34) {
                            return findName(b7, 4);
                        } else {
                            return parseFieldName(b7, b8, 4);
                        }
                    } else if (b6 == 34) {
                        return findName(b5, 3);
                    } else {
                        return parseFieldName(b5, b6, 3);
                    }
                } else if (b4 == 34) {
                    return findName(b3, 2);
                } else {
                    return parseFieldName(b3, b4, 2);
                }
            } else if (b2 == 34) {
                return findName(b, 1);
            } else {
                return parseFieldName(b, b2, 1);
            }
        } else if (b == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        } else {
            return parseFieldName(0, b, 0);
        }
    }

    /* access modifiers changed from: protected */
    public Name parseMediumFieldName(int i, int[] iArr) throws IOException, JsonParseException {
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2] & Ev3Constants.Opcode.TST;
        if (iArr[b] == 0) {
            byte b2 = b | (i << 8);
            byte[] bArr2 = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            byte b3 = bArr2[i3] & Ev3Constants.Opcode.TST;
            if (iArr[b3] == 0) {
                byte b4 = (b2 << 8) | b3;
                byte[] bArr3 = this._inputBuffer;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                byte b5 = bArr3[i4] & Ev3Constants.Opcode.TST;
                if (iArr[b5] == 0) {
                    int i5 = (b4 << 8) | b5;
                    byte[] bArr4 = this._inputBuffer;
                    int i6 = this._inputPtr;
                    this._inputPtr = i6 + 1;
                    byte b6 = bArr4[i6] & Ev3Constants.Opcode.TST;
                    if (iArr[b6] == 0) {
                        this._quadBuffer[0] = this._quad1;
                        this._quadBuffer[1] = i5;
                        return parseLongFieldName(b6);
                    } else if (b6 == 34) {
                        return findName(this._quad1, i5, 4);
                    } else {
                        return parseFieldName(this._quad1, i5, b6, 4);
                    }
                } else if (b5 == 34) {
                    return findName(this._quad1, b4, 3);
                } else {
                    return parseFieldName(this._quad1, b4, b5, 3);
                }
            } else if (b3 == 34) {
                return findName(this._quad1, b2, 2);
            } else {
                return parseFieldName(this._quad1, b2, b3, 2);
            }
        } else if (b == 34) {
            return findName(this._quad1, i, 1);
        } else {
            return parseFieldName(this._quad1, i, b, 1);
        }
    }

    /* access modifiers changed from: protected */
    public Name parseLongFieldName(int i) throws IOException, JsonParseException {
        int[] iArr = sInputCodesLatin1;
        int i2 = 2;
        byte b = i;
        while (this._inputEnd - this._inputPtr >= 4) {
            byte[] bArr = this._inputBuffer;
            int i3 = this._inputPtr;
            this._inputPtr = i3 + 1;
            byte b2 = bArr[i3] & Ev3Constants.Opcode.TST;
            if (iArr[b2] == 0) {
                byte b3 = (b << 8) | b2;
                byte[] bArr2 = this._inputBuffer;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                byte b4 = bArr2[i4] & Ev3Constants.Opcode.TST;
                if (iArr[b4] == 0) {
                    byte b5 = (b3 << 8) | b4;
                    byte[] bArr3 = this._inputBuffer;
                    int i5 = this._inputPtr;
                    this._inputPtr = i5 + 1;
                    byte b6 = bArr3[i5] & Ev3Constants.Opcode.TST;
                    if (iArr[b6] == 0) {
                        int i6 = (b5 << 8) | b6;
                        byte[] bArr4 = this._inputBuffer;
                        int i7 = this._inputPtr;
                        this._inputPtr = i7 + 1;
                        b = bArr4[i7] & Ev3Constants.Opcode.TST;
                        if (iArr[b] == 0) {
                            if (i2 >= this._quadBuffer.length) {
                                this._quadBuffer = growArrayBy(this._quadBuffer, i2);
                            }
                            this._quadBuffer[i2] = i6;
                            i2++;
                        } else if (b == 34) {
                            return findName(this._quadBuffer, i2, i6, 4);
                        } else {
                            return parseEscapedFieldName(this._quadBuffer, i2, i6, b, 4);
                        }
                    } else if (b6 == 34) {
                        return findName(this._quadBuffer, i2, b5, 3);
                    } else {
                        return parseEscapedFieldName(this._quadBuffer, i2, b5, b6, 3);
                    }
                } else if (b4 == 34) {
                    return findName(this._quadBuffer, i2, b3, 2);
                } else {
                    return parseEscapedFieldName(this._quadBuffer, i2, b3, b4, 2);
                }
            } else if (b2 == 34) {
                return findName(this._quadBuffer, i2, b, 1);
            } else {
                return parseEscapedFieldName(this._quadBuffer, i2, b, b2, 1);
            }
        }
        return parseEscapedFieldName(this._quadBuffer, i2, 0, b, 0);
    }

    /* access modifiers changed from: protected */
    public Name slowParseFieldName() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(": was expecting closing '\"' for name");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        byte b = bArr[i] & Ev3Constants.Opcode.TST;
        if (b == 34) {
            return BytesToNameCanonicalizer.getEmptyName();
        }
        return parseEscapedFieldName(this._quadBuffer, 0, 0, b, 0);
    }

    private Name parseFieldName(int i, int i2, int i3) throws IOException, JsonParseException {
        return parseEscapedFieldName(this._quadBuffer, 0, i, i2, i3);
    }

    private Name parseFieldName(int i, int i2, int i3, int i4) throws IOException, JsonParseException {
        this._quadBuffer[0] = i;
        return parseEscapedFieldName(this._quadBuffer, 1, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.core.sym.Name parseEscapedFieldName(int[] r10, int r11, int r12, int r13, int r14) throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonParseException {
        /*
            r9 = this;
            r7 = 4
            r1 = 0
            int[] r5 = sInputCodesLatin1
        L_0x0004:
            r0 = r5[r13]
            if (r0 == 0) goto L_0x00d7
            r0 = 34
            if (r13 != r0) goto L_0x002a
            if (r14 <= 0) goto L_0x001d
            int r0 = r10.length
            if (r11 < r0) goto L_0x0018
            int r0 = r10.length
            int[] r10 = growArrayBy(r10, r0)
            r9._quadBuffer = r10
        L_0x0018:
            int r0 = r11 + 1
            r10[r11] = r12
            r11 = r0
        L_0x001d:
            com.shaded.fasterxml.jackson.core.sym.BytesToNameCanonicalizer r0 = r9._symbols
            com.shaded.fasterxml.jackson.core.sym.Name r0 = r0.findName((int[]) r10, (int) r11)
            if (r0 != 0) goto L_0x0029
            com.shaded.fasterxml.jackson.core.sym.Name r0 = r9.addName(r10, r11, r14)
        L_0x0029:
            return r0
        L_0x002a:
            r0 = 92
            if (r13 == r0) goto L_0x008a
            java.lang.String r0 = "name"
            r9._throwUnquotedSpace(r13, r0)
        L_0x0033:
            r0 = 127(0x7f, float:1.78E-43)
            if (r13 <= r0) goto L_0x00d7
            if (r14 < r7) goto L_0x00d3
            int r0 = r10.length
            if (r11 < r0) goto L_0x0043
            int r0 = r10.length
            int[] r10 = growArrayBy(r10, r0)
            r9._quadBuffer = r10
        L_0x0043:
            int r4 = r11 + 1
            r10[r11] = r12
            r14 = r1
            r12 = r1
            r0 = r10
        L_0x004a:
            r2 = 2048(0x800, float:2.87E-42)
            if (r13 >= r2) goto L_0x008f
            int r2 = r12 << 8
            int r3 = r13 >> 6
            r3 = r3 | 192(0xc0, float:2.69E-43)
            r3 = r3 | r2
            int r2 = r14 + 1
            r8 = r2
            r2 = r3
            r3 = r0
            r0 = r8
        L_0x005b:
            r6 = r13 & 63
            r12 = r6 | 128(0x80, float:1.794E-43)
            r14 = r0
            r11 = r4
            r0 = r3
            r3 = r2
        L_0x0063:
            if (r14 >= r7) goto L_0x00bb
            int r14 = r14 + 1
            int r2 = r3 << 8
            r12 = r12 | r2
            r10 = r0
        L_0x006b:
            int r0 = r9._inputPtr
            int r2 = r9._inputEnd
            if (r0 < r2) goto L_0x007c
            boolean r0 = r9.loadMore()
            if (r0 != 0) goto L_0x007c
            java.lang.String r0 = " in field name"
            r9._reportInvalidEOF(r0)
        L_0x007c:
            byte[] r0 = r9._inputBuffer
            int r2 = r9._inputPtr
            int r3 = r2 + 1
            r9._inputPtr = r3
            byte r0 = r0[r2]
            r13 = r0 & 255(0xff, float:3.57E-43)
            goto L_0x0004
        L_0x008a:
            char r13 = r9._decodeEscaped()
            goto L_0x0033
        L_0x008f:
            int r2 = r12 << 8
            int r3 = r13 >> 12
            r3 = r3 | 224(0xe0, float:3.14E-43)
            r3 = r3 | r2
            int r2 = r14 + 1
            if (r2 < r7) goto L_0x00cd
            int r2 = r0.length
            if (r4 < r2) goto L_0x00a4
            int r2 = r0.length
            int[] r0 = growArrayBy(r0, r2)
            r9._quadBuffer = r0
        L_0x00a4:
            int r2 = r4 + 1
            r0[r4] = r3
            r3 = r2
            r4 = r0
            r0 = r1
            r2 = r1
        L_0x00ac:
            int r2 = r2 << 8
            int r6 = r13 >> 6
            r6 = r6 & 63
            r6 = r6 | 128(0x80, float:1.794E-43)
            r2 = r2 | r6
            int r0 = r0 + 1
            r8 = r3
            r3 = r4
            r4 = r8
            goto L_0x005b
        L_0x00bb:
            int r2 = r0.length
            if (r11 < r2) goto L_0x00c5
            int r2 = r0.length
            int[] r0 = growArrayBy(r0, r2)
            r9._quadBuffer = r0
        L_0x00c5:
            int r2 = r11 + 1
            r0[r11] = r3
            r14 = 1
            r11 = r2
            r10 = r0
            goto L_0x006b
        L_0x00cd:
            r8 = r2
            r2 = r3
            r3 = r4
            r4 = r0
            r0 = r8
            goto L_0x00ac
        L_0x00d3:
            r4 = r11
            r0 = r10
            goto L_0x004a
        L_0x00d7:
            r3 = r12
            r0 = r10
            r12 = r13
            goto L_0x0063
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.UTF8StreamJsonParser.parseEscapedFieldName(int[], int, int, int, int):com.shaded.fasterxml.jackson.core.sym.Name");
    }

    /* access modifiers changed from: protected */
    public Name _handleUnusualFieldName(int i) throws IOException, JsonParseException {
        int[] iArr;
        int i2;
        int i3;
        int i4;
        if (i == 39 && isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseApostropheFieldName();
        }
        if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar(i, "was expecting double-quote to start field name");
        }
        int[] inputCodeUtf8JsNames = CharTypes.getInputCodeUtf8JsNames();
        if (inputCodeUtf8JsNames[i] != 0) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int i5 = 0;
        int i6 = 0;
        byte b = i;
        int i7 = 0;
        int[] iArr2 = this._quadBuffer;
        while (true) {
            if (i5 < 4) {
                int i8 = i5 + 1;
                i3 = b | (i6 << 8);
                i4 = i7;
                iArr = iArr2;
                i2 = i8;
            } else {
                if (i7 >= iArr2.length) {
                    iArr2 = growArrayBy(iArr2, iArr2.length);
                    this._quadBuffer = iArr2;
                }
                int i9 = i7 + 1;
                iArr2[i7] = i6;
                iArr = iArr2;
                i2 = 1;
                i3 = b;
                i4 = i9;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOF(" in field name");
            }
            byte b2 = this._inputBuffer[this._inputPtr] & Ev3Constants.Opcode.TST;
            if (inputCodeUtf8JsNames[b2] != 0) {
                break;
            }
            this._inputPtr++;
            i6 = i3;
            i5 = i2;
            iArr2 = iArr;
            i7 = i4;
            b = b2;
        }
        if (i2 > 0) {
            if (i4 >= iArr.length) {
                iArr = growArrayBy(iArr, iArr.length);
                this._quadBuffer = iArr;
            }
            iArr[i4] = i3;
            i4++;
        }
        Name findName = this._symbols.findName(iArr, i4);
        if (findName == null) {
            return addName(iArr, i4, i2);
        }
        return findName;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00f3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.core.sym.Name _parseApostropheFieldName() throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonParseException {
        /*
            r12 = this;
            r10 = 39
            r9 = 4
            r1 = 0
            int r0 = r12._inputPtr
            int r2 = r12._inputEnd
            if (r0 < r2) goto L_0x0015
            boolean r0 = r12.loadMore()
            if (r0 != 0) goto L_0x0015
            java.lang.String r0 = ": was expecting closing ''' for name"
            r12._reportInvalidEOF(r0)
        L_0x0015:
            byte[] r0 = r12._inputBuffer
            int r2 = r12._inputPtr
            int r3 = r2 + 1
            r12._inputPtr = r3
            byte r0 = r0[r2]
            r5 = r0 & 255(0xff, float:3.57E-43)
            if (r5 != r10) goto L_0x0028
            com.shaded.fasterxml.jackson.core.sym.Name r0 = com.shaded.fasterxml.jackson.core.sym.BytesToNameCanonicalizer.getEmptyName()
        L_0x0027:
            return r0
        L_0x0028:
            int[] r0 = r12._quadBuffer
            int[] r7 = sInputCodesLatin1
            r3 = r1
            r4 = r1
            r2 = r1
        L_0x002f:
            if (r5 != r10) goto L_0x0051
            if (r3 <= 0) goto L_0x010a
            int r1 = r0.length
            if (r2 < r1) goto L_0x003d
            int r1 = r0.length
            int[] r0 = growArrayBy(r0, r1)
            r12._quadBuffer = r0
        L_0x003d:
            int r1 = r2 + 1
            r0[r2] = r4
            r11 = r1
            r1 = r0
            r0 = r11
        L_0x0044:
            com.shaded.fasterxml.jackson.core.sym.BytesToNameCanonicalizer r2 = r12._symbols
            com.shaded.fasterxml.jackson.core.sym.Name r2 = r2.findName((int[]) r1, (int) r0)
            if (r2 != 0) goto L_0x0107
            com.shaded.fasterxml.jackson.core.sym.Name r0 = r12.addName(r1, r0, r3)
            goto L_0x0027
        L_0x0051:
            r6 = 34
            if (r5 == r6) goto L_0x011a
            r6 = r7[r5]
            if (r6 == 0) goto L_0x011a
            r6 = 92
            if (r5 == r6) goto L_0x00c2
            java.lang.String r6 = "name"
            r12._throwUnquotedSpace(r5, r6)
        L_0x0062:
            r6 = 127(0x7f, float:1.78E-43)
            if (r5 <= r6) goto L_0x011a
            if (r3 < r9) goto L_0x0114
            int r3 = r0.length
            if (r2 < r3) goto L_0x0072
            int r3 = r0.length
            int[] r0 = growArrayBy(r0, r3)
            r12._quadBuffer = r0
        L_0x0072:
            int r3 = r2 + 1
            r0[r2] = r4
            r2 = r1
            r4 = r3
            r3 = r1
        L_0x0079:
            r6 = 2048(0x800, float:2.87E-42)
            if (r5 >= r6) goto L_0x00c7
            int r3 = r3 << 8
            int r6 = r5 >> 6
            r6 = r6 | 192(0xc0, float:2.69E-43)
            r3 = r3 | r6
            int r2 = r2 + 1
            r11 = r2
            r2 = r3
            r3 = r0
            r0 = r11
        L_0x008a:
            r5 = r5 & 63
            r5 = r5 | 128(0x80, float:1.794E-43)
            r6 = r2
            r2 = r0
            r0 = r3
            r3 = r5
        L_0x0092:
            if (r2 >= r9) goto L_0x00f3
            int r2 = r2 + 1
            int r5 = r6 << 8
            r3 = r3 | r5
            r11 = r2
            r2 = r3
            r3 = r4
            r4 = r0
            r0 = r11
        L_0x009e:
            int r5 = r12._inputPtr
            int r6 = r12._inputEnd
            if (r5 < r6) goto L_0x00af
            boolean r5 = r12.loadMore()
            if (r5 != 0) goto L_0x00af
            java.lang.String r5 = " in field name"
            r12._reportInvalidEOF(r5)
        L_0x00af:
            byte[] r5 = r12._inputBuffer
            int r6 = r12._inputPtr
            int r8 = r6 + 1
            r12._inputPtr = r8
            byte r5 = r5[r6]
            r5 = r5 & 255(0xff, float:3.57E-43)
            r11 = r0
            r0 = r4
            r4 = r2
            r2 = r3
            r3 = r11
            goto L_0x002f
        L_0x00c2:
            char r5 = r12._decodeEscaped()
            goto L_0x0062
        L_0x00c7:
            int r3 = r3 << 8
            int r6 = r5 >> 12
            r6 = r6 | 224(0xe0, float:3.14E-43)
            r3 = r3 | r6
            int r2 = r2 + 1
            if (r2 < r9) goto L_0x010e
            int r2 = r0.length
            if (r4 < r2) goto L_0x00dc
            int r2 = r0.length
            int[] r0 = growArrayBy(r0, r2)
            r12._quadBuffer = r0
        L_0x00dc:
            int r2 = r4 + 1
            r0[r4] = r3
            r3 = r2
            r4 = r0
            r0 = r1
            r2 = r1
        L_0x00e4:
            int r2 = r2 << 8
            int r6 = r5 >> 6
            r6 = r6 & 63
            r6 = r6 | 128(0x80, float:1.794E-43)
            r2 = r2 | r6
            int r0 = r0 + 1
            r11 = r3
            r3 = r4
            r4 = r11
            goto L_0x008a
        L_0x00f3:
            int r2 = r0.length
            if (r4 < r2) goto L_0x00fd
            int r2 = r0.length
            int[] r0 = growArrayBy(r0, r2)
            r12._quadBuffer = r0
        L_0x00fd:
            int r5 = r4 + 1
            r0[r4] = r6
            r2 = 1
            r4 = r0
            r0 = r2
            r2 = r3
            r3 = r5
            goto L_0x009e
        L_0x0107:
            r0 = r2
            goto L_0x0027
        L_0x010a:
            r1 = r0
            r0 = r2
            goto L_0x0044
        L_0x010e:
            r11 = r2
            r2 = r3
            r3 = r4
            r4 = r0
            r0 = r11
            goto L_0x00e4
        L_0x0114:
            r11 = r3
            r3 = r4
            r4 = r2
            r2 = r11
            goto L_0x0079
        L_0x011a:
            r6 = r4
            r4 = r2
            r2 = r3
            r3 = r5
            goto L_0x0092
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.UTF8StreamJsonParser._parseApostropheFieldName():com.shaded.fasterxml.jackson.core.sym.Name");
    }

    private Name findName(int i, int i2) throws JsonParseException {
        Name findName = this._symbols.findName(i);
        if (findName != null) {
            return findName;
        }
        this._quadBuffer[0] = i;
        return addName(this._quadBuffer, 1, i2);
    }

    private Name findName(int i, int i2, int i3) throws JsonParseException {
        Name findName = this._symbols.findName(i, i2);
        if (findName != null) {
            return findName;
        }
        this._quadBuffer[0] = i;
        this._quadBuffer[1] = i2;
        return addName(this._quadBuffer, 2, i3);
    }

    private Name findName(int[] iArr, int i, int i2, int i3) throws JsonParseException {
        if (i >= iArr.length) {
            iArr = growArrayBy(iArr, iArr.length);
            this._quadBuffer = iArr;
        }
        int i4 = i + 1;
        iArr[i] = i2;
        Name findName = this._symbols.findName(iArr, i4);
        if (findName == null) {
            return addName(iArr, i4, i3);
        }
        return findName;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00cb  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d1 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.shaded.fasterxml.jackson.core.sym.Name addName(int[] r12, int r13, int r14) throws com.shaded.fasterxml.jackson.core.JsonParseException {
        /*
            r11 = this;
            int r0 = r13 << 2
            int r0 = r0 + -4
            int r6 = r0 + r14
            r0 = 4
            if (r14 >= r0) goto L_0x00da
            int r0 = r13 + -1
            r0 = r12[r0]
            int r1 = r13 + -1
            int r2 = 4 - r14
            int r2 = r2 << 3
            int r2 = r0 << r2
            r12[r1] = r2
        L_0x0017:
            com.shaded.fasterxml.jackson.core.util.TextBuffer r1 = r11._textBuffer
            char[] r1 = r1.emptyAndGetCurrentSegment()
            r5 = 0
            r2 = 0
            r3 = r2
        L_0x0020:
            if (r3 >= r6) goto L_0x0100
            int r2 = r3 >> 2
            r2 = r12[r2]
            r4 = r3 & 3
            int r4 = 3 - r4
            int r4 = r4 << 3
            int r2 = r2 >> r4
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r3 = r3 + 1
            r4 = 127(0x7f, float:1.78E-43)
            if (r2 <= r4) goto L_0x0114
            r4 = r2 & 224(0xe0, float:3.14E-43)
            r7 = 192(0xc0, float:2.69E-43)
            if (r4 != r7) goto L_0x00dd
            r4 = r2 & 31
            r2 = 1
            r10 = r2
            r2 = r4
            r4 = r10
        L_0x0041:
            int r7 = r3 + r4
            if (r7 <= r6) goto L_0x004a
            java.lang.String r7 = " in field name"
            r11._reportInvalidEOF(r7)
        L_0x004a:
            int r7 = r3 >> 2
            r7 = r12[r7]
            r8 = r3 & 3
            int r8 = 3 - r8
            int r8 = r8 << 3
            int r7 = r7 >> r8
            int r3 = r3 + 1
            r8 = r7 & 192(0xc0, float:2.69E-43)
            r9 = 128(0x80, float:1.794E-43)
            if (r8 == r9) goto L_0x0060
            r11._reportInvalidOther(r7)
        L_0x0060:
            int r2 = r2 << 6
            r7 = r7 & 63
            r2 = r2 | r7
            r7 = 1
            if (r4 <= r7) goto L_0x00a3
            int r7 = r3 >> 2
            r7 = r12[r7]
            r8 = r3 & 3
            int r8 = 3 - r8
            int r8 = r8 << 3
            int r7 = r7 >> r8
            int r3 = r3 + 1
            r8 = r7 & 192(0xc0, float:2.69E-43)
            r9 = 128(0x80, float:1.794E-43)
            if (r8 == r9) goto L_0x007e
            r11._reportInvalidOther(r7)
        L_0x007e:
            int r2 = r2 << 6
            r7 = r7 & 63
            r2 = r2 | r7
            r7 = 2
            if (r4 <= r7) goto L_0x00a3
            int r7 = r3 >> 2
            r7 = r12[r7]
            r8 = r3 & 3
            int r8 = 3 - r8
            int r8 = r8 << 3
            int r7 = r7 >> r8
            int r3 = r3 + 1
            r8 = r7 & 192(0xc0, float:2.69E-43)
            r9 = 128(0x80, float:1.794E-43)
            if (r8 == r9) goto L_0x009e
            r8 = r7 & 255(0xff, float:3.57E-43)
            r11._reportInvalidOther(r8)
        L_0x009e:
            int r2 = r2 << 6
            r7 = r7 & 63
            r2 = r2 | r7
        L_0x00a3:
            r7 = 2
            if (r4 <= r7) goto L_0x0114
            r4 = 65536(0x10000, float:9.18355E-41)
            int r2 = r2 - r4
            int r4 = r1.length
            if (r5 < r4) goto L_0x00b2
            com.shaded.fasterxml.jackson.core.util.TextBuffer r1 = r11._textBuffer
            char[] r1 = r1.expandCurrentSegment()
        L_0x00b2:
            int r4 = r5 + 1
            r7 = 55296(0xd800, float:7.7486E-41)
            int r8 = r2 >> 10
            int r7 = r7 + r8
            char r7 = (char) r7
            r1[r5] = r7
            r5 = 56320(0xdc00, float:7.8921E-41)
            r2 = r2 & 1023(0x3ff, float:1.434E-42)
            r2 = r2 | r5
            r10 = r2
            r2 = r3
            r3 = r4
            r4 = r1
            r1 = r10
        L_0x00c8:
            int r5 = r4.length
            if (r3 < r5) goto L_0x00d1
            com.shaded.fasterxml.jackson.core.util.TextBuffer r4 = r11._textBuffer
            char[] r4 = r4.expandCurrentSegment()
        L_0x00d1:
            int r5 = r3 + 1
            char r1 = (char) r1
            r4[r3] = r1
            r3 = r2
            r1 = r4
            goto L_0x0020
        L_0x00da:
            r0 = 0
            goto L_0x0017
        L_0x00dd:
            r4 = r2 & 240(0xf0, float:3.36E-43)
            r7 = 224(0xe0, float:3.14E-43)
            if (r4 != r7) goto L_0x00eb
            r4 = r2 & 15
            r2 = 2
            r10 = r2
            r2 = r4
            r4 = r10
            goto L_0x0041
        L_0x00eb:
            r4 = r2 & 248(0xf8, float:3.48E-43)
            r7 = 240(0xf0, float:3.36E-43)
            if (r4 != r7) goto L_0x00f9
            r4 = r2 & 7
            r2 = 3
            r10 = r2
            r2 = r4
            r4 = r10
            goto L_0x0041
        L_0x00f9:
            r11._reportInvalidInitial(r2)
            r2 = 1
            r4 = r2
            goto L_0x0041
        L_0x0100:
            java.lang.String r2 = new java.lang.String
            r3 = 0
            r2.<init>(r1, r3, r5)
            r1 = 4
            if (r14 >= r1) goto L_0x010d
            int r1 = r13 + -1
            r12[r1] = r0
        L_0x010d:
            com.shaded.fasterxml.jackson.core.sym.BytesToNameCanonicalizer r0 = r11._symbols
            com.shaded.fasterxml.jackson.core.sym.Name r0 = r0.addName((java.lang.String) r2, (int[]) r12, (int) r13)
            return r0
        L_0x0114:
            r4 = r1
            r1 = r2
            r2 = r3
            r3 = r5
            goto L_0x00c8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.UTF8StreamJsonParser.addName(int[], int, int):com.shaded.fasterxml.jackson.core.sym.Name");
    }

    /* access modifiers changed from: protected */
    public void _finishString() throws IOException, JsonParseException {
        int i = this._inputPtr;
        if (i >= this._inputEnd) {
            loadMoreGuaranteed();
            i = this._inputPtr;
        }
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = sInputCodesUtf8;
        int min = Math.min(this._inputEnd, emptyAndGetCurrentSegment.length + i);
        byte[] bArr = this._inputBuffer;
        int i2 = i;
        int i3 = 0;
        while (true) {
            if (i2 >= min) {
                break;
            }
            byte b = bArr[i2] & Ev3Constants.Opcode.TST;
            if (iArr[b] == 0) {
                emptyAndGetCurrentSegment[i3] = (char) b;
                i3++;
                i2++;
            } else if (b == 34) {
                this._inputPtr = i2 + 1;
                this._textBuffer.setCurrentLength(i3);
                return;
            }
        }
        this._inputPtr = i2;
        _finishString2(emptyAndGetCurrentSegment, i3);
    }

    private void _finishString2(char[] cArr, int i) throws IOException, JsonParseException {
        int i2;
        int[] iArr = sInputCodesUtf8;
        byte[] bArr = this._inputBuffer;
        while (true) {
            int i3 = this._inputPtr;
            if (i3 >= this._inputEnd) {
                loadMoreGuaranteed();
                i3 = this._inputPtr;
            }
            if (i >= cArr.length) {
                cArr = this._textBuffer.finishCurrentSegment();
                i = 0;
            }
            int min = Math.min(this._inputEnd, (cArr.length - i) + i3);
            while (true) {
                if (i3 < min) {
                    int i4 = i3 + 1;
                    int i5 = bArr[i3] & Ev3Constants.Opcode.TST;
                    if (iArr[i5] != 0) {
                        this._inputPtr = i4;
                        if (i5 == 34) {
                            this._textBuffer.setCurrentLength(i);
                            return;
                        }
                        switch (iArr[i5]) {
                            case 1:
                                i5 = _decodeEscaped();
                                break;
                            case 2:
                                i5 = _decodeUtf8_2(i5);
                                break;
                            case 3:
                                if (this._inputEnd - this._inputPtr < 2) {
                                    i5 = _decodeUtf8_3(i5);
                                    break;
                                } else {
                                    i5 = _decodeUtf8_3fast(i5);
                                    break;
                                }
                            case 4:
                                int _decodeUtf8_4 = _decodeUtf8_4(i5);
                                int i6 = i + 1;
                                cArr[i] = (char) (55296 | (_decodeUtf8_4 >> 10));
                                if (i6 >= cArr.length) {
                                    cArr = this._textBuffer.finishCurrentSegment();
                                    i6 = 0;
                                }
                                i = i6;
                                i5 = (_decodeUtf8_4 & 1023) | 56320;
                                break;
                            default:
                                if (i5 >= 32) {
                                    _reportInvalidChar(i5);
                                    break;
                                } else {
                                    _throwUnquotedSpace(i5, "string value");
                                    break;
                                }
                        }
                        if (i >= cArr.length) {
                            cArr = this._textBuffer.finishCurrentSegment();
                            i2 = 0;
                        } else {
                            i2 = i;
                        }
                        i = i2 + 1;
                        cArr[i2] = (char) i5;
                    } else {
                        cArr[i] = (char) i5;
                        i3 = i4;
                        i++;
                    }
                } else {
                    this._inputPtr = i3;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void _skipString() throws IOException, JsonParseException {
        this._tokenIncomplete = false;
        int[] iArr = sInputCodesUtf8;
        byte[] bArr = this._inputBuffer;
        while (true) {
            int i = this._inputPtr;
            int i2 = this._inputEnd;
            if (i >= i2) {
                loadMoreGuaranteed();
                i = this._inputPtr;
                i2 = this._inputEnd;
            }
            while (true) {
                if (i < i2) {
                    int i3 = i + 1;
                    byte b = bArr[i] & Ev3Constants.Opcode.TST;
                    if (iArr[b] != 0) {
                        this._inputPtr = i3;
                        if (b != 34) {
                            switch (iArr[b]) {
                                case 1:
                                    _decodeEscaped();
                                    break;
                                case 2:
                                    _skipUtf8_2(b);
                                    break;
                                case 3:
                                    _skipUtf8_3(b);
                                    break;
                                case 4:
                                    _skipUtf8_4(b);
                                    break;
                                default:
                                    if (b >= 32) {
                                        _reportInvalidChar(b);
                                        break;
                                    } else {
                                        _throwUnquotedSpace(b, "string value");
                                        break;
                                    }
                            }
                        } else {
                            return;
                        }
                    } else {
                        i = i3;
                    }
                } else {
                    this._inputPtr = i;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public JsonToken _handleUnexpectedValue(int i) throws IOException, JsonParseException {
        switch (i) {
            case 39:
                if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApostropheValue();
                }
                break;
            case 43:
                if (this._inputPtr >= this._inputEnd && !loadMore()) {
                    _reportInvalidEOFInValue();
                }
                byte[] bArr = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                return _handleInvalidNumberStart(bArr[i2] & Ev3Constants.Opcode.TST, false);
            case 78:
                _matchToken("NaN", 1);
                if (!isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                    break;
                } else {
                    return resetAsNaN("NaN", Double.NaN);
                }
        }
        _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.core.JsonToken _handleApostropheValue() throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonParseException {
        /*
            r10 = this;
            r9 = 39
            r2 = 0
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r10._textBuffer
            char[] r0 = r0.emptyAndGetCurrentSegment()
            int[] r6 = sInputCodesUtf8
            byte[] r7 = r10._inputBuffer
            r1 = r2
        L_0x000e:
            int r3 = r10._inputPtr
            int r4 = r10._inputEnd
            if (r3 < r4) goto L_0x0017
            r10.loadMoreGuaranteed()
        L_0x0017:
            int r3 = r0.length
            if (r1 < r3) goto L_0x0021
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r10._textBuffer
            char[] r0 = r0.finishCurrentSegment()
            r1 = r2
        L_0x0021:
            int r4 = r10._inputEnd
            int r3 = r10._inputPtr
            int r5 = r0.length
            int r5 = r5 - r1
            int r3 = r3 + r5
            if (r3 >= r4) goto L_0x00b5
        L_0x002a:
            int r4 = r10._inputPtr
            if (r4 >= r3) goto L_0x000e
            int r4 = r10._inputPtr
            int r5 = r4 + 1
            r10._inputPtr = r5
            byte r4 = r7[r4]
            r5 = r4 & 255(0xff, float:3.57E-43)
            if (r5 == r9) goto L_0x003e
            r4 = r6[r5]
            if (r4 == 0) goto L_0x0048
        L_0x003e:
            if (r5 != r9) goto L_0x004f
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r10._textBuffer
            r0.setCurrentLength(r1)
            com.shaded.fasterxml.jackson.core.JsonToken r0 = com.shaded.fasterxml.jackson.core.JsonToken.VALUE_STRING
            return r0
        L_0x0048:
            int r4 = r1 + 1
            char r5 = (char) r5
            r0[r1] = r5
            r1 = r4
            goto L_0x002a
        L_0x004f:
            r3 = r6[r5]
            switch(r3) {
                case 1: goto L_0x0071;
                case 2: goto L_0x007a;
                case 3: goto L_0x007f;
                case 4: goto L_0x0091;
                default: goto L_0x0054;
            }
        L_0x0054:
            r3 = 32
            if (r5 >= r3) goto L_0x005d
            java.lang.String r3 = "string value"
            r10._throwUnquotedSpace(r5, r3)
        L_0x005d:
            r10._reportInvalidChar(r5)
        L_0x0060:
            r3 = r5
        L_0x0061:
            int r4 = r0.length
            if (r1 < r4) goto L_0x00b1
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r10._textBuffer
            char[] r0 = r0.finishCurrentSegment()
            r4 = r2
        L_0x006b:
            int r1 = r4 + 1
            char r3 = (char) r3
            r0[r4] = r3
            goto L_0x000e
        L_0x0071:
            r3 = 34
            if (r5 == r3) goto L_0x0060
            char r3 = r10._decodeEscaped()
            goto L_0x0061
        L_0x007a:
            int r3 = r10._decodeUtf8_2(r5)
            goto L_0x0061
        L_0x007f:
            int r3 = r10._inputEnd
            int r4 = r10._inputPtr
            int r3 = r3 - r4
            r4 = 2
            if (r3 < r4) goto L_0x008c
            int r3 = r10._decodeUtf8_3fast(r5)
            goto L_0x0061
        L_0x008c:
            int r3 = r10._decodeUtf8_3(r5)
            goto L_0x0061
        L_0x0091:
            int r4 = r10._decodeUtf8_4(r5)
            int r3 = r1 + 1
            r5 = 55296(0xd800, float:7.7486E-41)
            int r8 = r4 >> 10
            r5 = r5 | r8
            char r5 = (char) r5
            r0[r1] = r5
            int r1 = r0.length
            if (r3 < r1) goto L_0x00b3
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r10._textBuffer
            char[] r0 = r0.finishCurrentSegment()
            r1 = r2
        L_0x00aa:
            r3 = 56320(0xdc00, float:7.8921E-41)
            r4 = r4 & 1023(0x3ff, float:1.434E-42)
            r3 = r3 | r4
            goto L_0x0061
        L_0x00b1:
            r4 = r1
            goto L_0x006b
        L_0x00b3:
            r1 = r3
            goto L_0x00aa
        L_0x00b5:
            r3 = r4
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.UTF8StreamJsonParser._handleApostropheValue():com.shaded.fasterxml.jackson.core.JsonToken");
    }

    /* access modifiers changed from: protected */
    public JsonToken _handleInvalidNumberStart(int i, boolean z) throws IOException, JsonParseException {
        String str;
        byte b = i;
        while (true) {
            if (b != 73) {
                break;
            }
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                _reportInvalidEOFInValue();
            }
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b2 = bArr[i2];
            if (b2 != 78) {
                if (b2 != 110) {
                    b = b2;
                    break;
                }
                str = z ? "-Infinity" : "+Infinity";
            } else {
                str = z ? "-INF" : "+INF";
            }
            _matchToken(str, 3);
            if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                return resetAsNaN(str, z ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            _reportError("Non-standard token '" + str + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            b = b2;
        }
        reportUnexpectedNumberChar(b, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    /* access modifiers changed from: protected */
    public void _matchToken(String str, int i) throws IOException, JsonParseException {
        byte b;
        int length = str.length();
        do {
            if ((this._inputPtr >= this._inputEnd && !loadMore()) || this._inputBuffer[this._inputPtr] != str.charAt(i)) {
                _reportInvalidToken(str.substring(0, i));
            }
            this._inputPtr++;
            i++;
        } while (i < length);
        if ((this._inputPtr < this._inputEnd || loadMore()) && (b = this._inputBuffer[this._inputPtr] & Ev3Constants.Opcode.TST) >= 48 && b != 93 && b != 125 && Character.isJavaIdentifierPart((char) _decodeCharForError(b))) {
            _reportInvalidToken(str.substring(0, i));
        }
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidToken(String str) throws IOException, JsonParseException {
        _reportInvalidToken(str, "'null', 'true', 'false' or NaN");
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidToken(String str, String str2) throws IOException, JsonParseException {
        StringBuilder sb = new StringBuilder(str);
        while (true) {
            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                break;
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char _decodeCharForError = (char) _decodeCharForError(bArr[i]);
            if (!Character.isJavaIdentifierPart(_decodeCharForError)) {
                break;
            }
            sb.append(_decodeCharForError);
        }
        _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + str2);
    }

    private int _skipWS() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                byte b = bArr[i] & Ev3Constants.Opcode.TST;
                if (b > 32) {
                    if (b != 47) {
                        return b;
                    }
                    _skipComment();
                } else if (b != 32) {
                    if (b == 10) {
                        _skipLF();
                    } else if (b == 13) {
                        _skipCR();
                    } else if (b != 9) {
                        _throwInvalidSpace(b);
                    }
                }
            } else {
                throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
            }
        }
    }

    private int _skipWSOrEnd() throws IOException, JsonParseException {
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                byte b = bArr[i] & Ev3Constants.Opcode.TST;
                if (b > 32) {
                    if (b != 47) {
                        return b;
                    }
                    _skipComment();
                } else if (b != 32) {
                    if (b == 10) {
                        _skipLF();
                    } else if (b == 13) {
                        _skipCR();
                    } else if (b != 9) {
                        _throwInvalidSpace(b);
                    }
                }
            } else {
                _handleEOF();
                return -1;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006c, code lost:
        if (r6._inputPtr < r6._inputEnd) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006e, code lost:
        loadMoreGuaranteed();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int _skipColon() throws java.io.IOException, com.shaded.fasterxml.jackson.core.JsonParseException {
        /*
            r6 = this;
            r5 = 58
            r4 = 47
            r3 = 32
            int r0 = r6._inputPtr
            int r1 = r6._inputEnd
            if (r0 < r1) goto L_0x000f
            r6.loadMoreGuaranteed()
        L_0x000f:
            byte[] r0 = r6._inputBuffer
            int r1 = r6._inputPtr
            int r2 = r1 + 1
            r6._inputPtr = r2
            byte r0 = r0[r1]
            if (r0 != r5) goto L_0x0034
            int r0 = r6._inputPtr
            int r1 = r6._inputEnd
            if (r0 >= r1) goto L_0x0045
            byte[] r0 = r6._inputBuffer
            int r1 = r6._inputPtr
            byte r0 = r0[r1]
            r0 = r0 & 255(0xff, float:3.57E-43)
            if (r0 <= r3) goto L_0x0045
            if (r0 == r4) goto L_0x0045
            int r1 = r6._inputPtr
            int r1 = r1 + 1
            r6._inputPtr = r1
        L_0x0033:
            return r0
        L_0x0034:
            r0 = r0 & 255(0xff, float:3.57E-43)
        L_0x0036:
            switch(r0) {
                case 9: goto L_0x0068;
                case 10: goto L_0x007e;
                case 13: goto L_0x0065;
                case 32: goto L_0x0068;
                case 47: goto L_0x0082;
                default: goto L_0x0039;
            }
        L_0x0039:
            if (r0 >= r3) goto L_0x003e
            r6._throwInvalidSpace(r0)
        L_0x003e:
            if (r0 == r5) goto L_0x0045
            java.lang.String r1 = "was expecting a colon to separate field name and value"
            r6._reportUnexpectedChar(r0, r1)
        L_0x0045:
            int r0 = r6._inputPtr
            int r1 = r6._inputEnd
            if (r0 < r1) goto L_0x0051
            boolean r0 = r6.loadMore()
            if (r0 == 0) goto L_0x00a0
        L_0x0051:
            byte[] r0 = r6._inputBuffer
            int r1 = r6._inputPtr
            int r2 = r1 + 1
            r6._inputPtr = r2
            byte r0 = r0[r1]
            r0 = r0 & 255(0xff, float:3.57E-43)
            if (r0 <= r3) goto L_0x0086
            if (r0 != r4) goto L_0x0033
            r6._skipComment()
            goto L_0x0045
        L_0x0065:
            r6._skipCR()
        L_0x0068:
            int r0 = r6._inputPtr
            int r1 = r6._inputEnd
            if (r0 < r1) goto L_0x0071
            r6.loadMoreGuaranteed()
        L_0x0071:
            byte[] r0 = r6._inputBuffer
            int r1 = r6._inputPtr
            int r2 = r1 + 1
            r6._inputPtr = r2
            byte r0 = r0[r1]
            r0 = r0 & 255(0xff, float:3.57E-43)
            goto L_0x0036
        L_0x007e:
            r6._skipLF()
            goto L_0x0068
        L_0x0082:
            r6._skipComment()
            goto L_0x0068
        L_0x0086:
            if (r0 == r3) goto L_0x0045
            r1 = 10
            if (r0 != r1) goto L_0x0090
            r6._skipLF()
            goto L_0x0045
        L_0x0090:
            r1 = 13
            if (r0 != r1) goto L_0x0098
            r6._skipCR()
            goto L_0x0045
        L_0x0098:
            r1 = 9
            if (r0 == r1) goto L_0x0045
            r6._throwInvalidSpace(r0)
            goto L_0x0045
        L_0x00a0:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Unexpected end-of-input within/between "
            java.lang.StringBuilder r0 = r0.append(r1)
            com.shaded.fasterxml.jackson.core.json.JsonReadContext r1 = r6._parsingContext
            java.lang.String r1 = r1.getTypeDesc()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " entries"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.shaded.fasterxml.jackson.core.JsonParseException r0 = r6._constructError(r0)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.json.UTF8StreamJsonParser._skipColon():int");
    }

    private void _skipComment() throws IOException, JsonParseException {
        if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in a comment");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        byte b = bArr[i] & Ev3Constants.Opcode.TST;
        if (b == 47) {
            _skipCppComment();
        } else if (b == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(b, "was expecting either '*' or '/' for a comment");
        }
    }

    private void _skipCComment() throws IOException, JsonParseException {
        int[] inputCodeComment = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                byte b = bArr[i] & Ev3Constants.Opcode.TST;
                int i2 = inputCodeComment[b];
                if (i2 != 0) {
                    switch (i2) {
                        case 2:
                            _skipUtf8_2(b);
                            continue;
                        case 3:
                            _skipUtf8_3(b);
                            continue;
                        case 4:
                            _skipUtf8_4(b);
                            continue;
                        case 10:
                            _skipLF();
                            continue;
                        case 13:
                            _skipCR();
                            continue;
                        case 42:
                            if (this._inputPtr >= this._inputEnd && !loadMore()) {
                                break;
                            } else if (this._inputBuffer[this._inputPtr] == 47) {
                                this._inputPtr++;
                                return;
                            } else {
                                continue;
                            }
                        default:
                            _reportInvalidChar(b);
                            continue;
                    }
                }
            }
        }
        _reportInvalidEOF(" in a comment");
    }

    private void _skipCppComment() throws IOException, JsonParseException {
        int[] inputCodeComment = CharTypes.getInputCodeComment();
        while (true) {
            if (this._inputPtr < this._inputEnd || loadMore()) {
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                byte b = bArr[i] & Ev3Constants.Opcode.TST;
                int i2 = inputCodeComment[b];
                if (i2 != 0) {
                    switch (i2) {
                        case 2:
                            _skipUtf8_2(b);
                            break;
                        case 3:
                            _skipUtf8_3(b);
                            break;
                        case 4:
                            _skipUtf8_4(b);
                            break;
                        case 10:
                            _skipLF();
                            return;
                        case 13:
                            _skipCR();
                            return;
                        case 42:
                            break;
                        default:
                            _reportInvalidChar(b);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public char _decodeEscaped() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd && !loadMore()) {
            _reportInvalidEOF(" in character escape sequence");
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        byte b = bArr[i];
        switch (b) {
            case 34:
            case 47:
            case 92:
                return (char) b;
            case 98:
                return 8;
            case 102:
                return 12;
            case 110:
                return 10;
            case 114:
                return 13;
            case 116:
                return 9;
            case 117:
                int i2 = 0;
                for (int i3 = 0; i3 < 4; i3++) {
                    if (this._inputPtr >= this._inputEnd && !loadMore()) {
                        _reportInvalidEOF(" in character escape sequence");
                    }
                    byte[] bArr2 = this._inputBuffer;
                    int i4 = this._inputPtr;
                    this._inputPtr = i4 + 1;
                    byte b2 = bArr2[i4];
                    int charToHex = CharTypes.charToHex(b2);
                    if (charToHex < 0) {
                        _reportUnexpectedChar(b2, "expected a hex-digit for character escape sequence");
                    }
                    i2 = (i2 << 4) | charToHex;
                }
                return (char) i2;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(b));
        }
    }

    /* access modifiers changed from: protected */
    public int _decodeCharForError(int i) throws IOException, JsonParseException {
        char c;
        if (i >= 0) {
            return i;
        }
        if ((i & 224) == 192) {
            i &= 31;
            c = 1;
        } else if ((i & 240) == 224) {
            i &= 15;
            c = 2;
        } else if ((i & 248) == 240) {
            i &= 7;
            c = 3;
        } else {
            _reportInvalidInitial(i & 255);
            c = 1;
        }
        int nextByte = nextByte();
        if ((nextByte & FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE) != 128) {
            _reportInvalidOther(nextByte & 255);
        }
        int i2 = (i << 6) | (nextByte & 63);
        if (c <= 1) {
            return i2;
        }
        int nextByte2 = nextByte();
        if ((nextByte2 & FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE) != 128) {
            _reportInvalidOther(nextByte2 & 255);
        }
        int i3 = (i2 << 6) | (nextByte2 & 63);
        if (c <= 2) {
            return i3;
        }
        int nextByte3 = nextByte();
        if ((nextByte3 & FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE) != 128) {
            _reportInvalidOther(nextByte3 & 255);
        }
        return (i3 << 6) | (nextByte3 & 63);
    }

    private int _decodeUtf8_2(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        return (b & Ev3Constants.Opcode.MOVEF_F) | ((i & 31) << 6);
    }

    private int _decodeUtf8_3(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        int i2 = i & 15;
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b = bArr[i3];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        byte b2 = (i2 << 6) | (b & Ev3Constants.Opcode.MOVEF_F);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        byte b3 = bArr2[i4];
        if ((b3 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b3 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        return (b2 << 6) | (b3 & Ev3Constants.Opcode.MOVEF_F);
    }

    private int _decodeUtf8_3fast(int i) throws IOException, JsonParseException {
        int i2 = i & 15;
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b = bArr[i3];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        byte b2 = (i2 << 6) | (b & Ev3Constants.Opcode.MOVEF_F);
        byte[] bArr2 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        byte b3 = bArr2[i4];
        if ((b3 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b3 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        return (b2 << 6) | (b3 & Ev3Constants.Opcode.MOVEF_F);
    }

    private int _decodeUtf8_4(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        byte b2 = (b & Ev3Constants.Opcode.MOVEF_F) | ((i & 7) << 6);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b3 = bArr2[i3];
        if ((b3 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b3 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        byte b4 = (b2 << 6) | (b3 & Ev3Constants.Opcode.MOVEF_F);
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr3 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        byte b5 = bArr3[i4];
        if ((b5 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b5 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        return ((b4 << 6) | (b5 & Ev3Constants.Opcode.MOVEF_F)) - 65536;
    }

    private void _skipUtf8_2(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
    }

    private void _skipUtf8_3(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b2 = bArr2[i3];
        if ((b2 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b2 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
    }

    private void _skipUtf8_4(int i) throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if ((b & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr2 = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b2 = bArr2[i3];
        if ((b2 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b2 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr3 = this._inputBuffer;
        int i4 = this._inputPtr;
        this._inputPtr = i4 + 1;
        byte b3 = bArr3[i4];
        if ((b3 & Ev3Constants.Opcode.FILE) != 128) {
            _reportInvalidOther(b3 & Ev3Constants.Opcode.TST, this._inputPtr);
        }
    }

    /* access modifiers changed from: protected */
    public void _skipCR() throws IOException {
        if ((this._inputPtr < this._inputEnd || loadMore()) && this._inputBuffer[this._inputPtr] == 10) {
            this._inputPtr++;
        }
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    /* access modifiers changed from: protected */
    public void _skipLF() throws IOException {
        this._currInputRow++;
        this._currInputRowStart = this._inputPtr;
    }

    private int nextByte() throws IOException, JsonParseException {
        if (this._inputPtr >= this._inputEnd) {
            loadMoreGuaranteed();
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        return bArr[i] & Ev3Constants.Opcode.TST;
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidChar(int i) throws JsonParseException {
        if (i < 32) {
            _throwInvalidSpace(i);
        }
        _reportInvalidInitial(i);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidInitial(int i) throws JsonParseException {
        _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(i));
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidOther(int i) throws JsonParseException {
        _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(i));
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidOther(int i, int i2) throws JsonParseException {
        this._inputPtr = i2;
        _reportInvalidOther(i);
    }

    public static int[] growArrayBy(int[] iArr, int i) {
        if (iArr == null) {
            return new int[i];
        }
        int length = iArr.length;
        int[] iArr2 = new int[(length + i)];
        System.arraycopy(iArr, 0, iArr2, 0, length);
        return iArr2;
    }

    /* access modifiers changed from: protected */
    public byte[] _decodeBase64(Base64Variant base64Variant) throws IOException, JsonParseException {
        ByteArrayBuilder _getByteArrayBuilder = _getByteArrayBuilder();
        while (true) {
            if (this._inputPtr >= this._inputEnd) {
                loadMoreGuaranteed();
            }
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = bArr[i] & Ev3Constants.Opcode.TST;
            if (b > 32) {
                int decodeBase64Char = base64Variant.decodeBase64Char((int) b);
                if (decodeBase64Char < 0) {
                    if (b == 34) {
                        return _getByteArrayBuilder.toByteArray();
                    }
                    decodeBase64Char = _decodeBase64Escape(base64Variant, (int) b, 0);
                    if (decodeBase64Char < 0) {
                        continue;
                    }
                }
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr2 = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                byte b2 = bArr2[i2] & Ev3Constants.Opcode.TST;
                int decodeBase64Char2 = base64Variant.decodeBase64Char((int) b2);
                if (decodeBase64Char2 < 0) {
                    decodeBase64Char2 = _decodeBase64Escape(base64Variant, (int) b2, 1);
                }
                int i3 = decodeBase64Char2 | (decodeBase64Char << 6);
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr3 = this._inputBuffer;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                byte b3 = bArr3[i4] & Ev3Constants.Opcode.TST;
                int decodeBase64Char3 = base64Variant.decodeBase64Char((int) b3);
                if (decodeBase64Char3 < 0) {
                    if (decodeBase64Char3 != -2) {
                        if (b3 != 34 || base64Variant.usesPadding()) {
                            decodeBase64Char3 = _decodeBase64Escape(base64Variant, (int) b3, 2);
                        } else {
                            _getByteArrayBuilder.append(i3 >> 4);
                            return _getByteArrayBuilder.toByteArray();
                        }
                    }
                    if (decodeBase64Char3 == -2) {
                        if (this._inputPtr >= this._inputEnd) {
                            loadMoreGuaranteed();
                        }
                        byte[] bArr4 = this._inputBuffer;
                        int i5 = this._inputPtr;
                        this._inputPtr = i5 + 1;
                        byte b4 = bArr4[i5] & Ev3Constants.Opcode.TST;
                        if (!base64Variant.usesPaddingChar((int) b4)) {
                            throw reportInvalidBase64Char(base64Variant, b4, 3, "expected padding character '" + base64Variant.getPaddingChar() + "'");
                        }
                        _getByteArrayBuilder.append(i3 >> 4);
                    }
                }
                int i6 = (i3 << 6) | decodeBase64Char3;
                if (this._inputPtr >= this._inputEnd) {
                    loadMoreGuaranteed();
                }
                byte[] bArr5 = this._inputBuffer;
                int i7 = this._inputPtr;
                this._inputPtr = i7 + 1;
                byte b5 = bArr5[i7] & Ev3Constants.Opcode.TST;
                int decodeBase64Char4 = base64Variant.decodeBase64Char((int) b5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        if (b5 != 34 || base64Variant.usesPadding()) {
                            decodeBase64Char4 = _decodeBase64Escape(base64Variant, (int) b5, 3);
                        } else {
                            _getByteArrayBuilder.appendTwoBytes(i6 >> 2);
                            return _getByteArrayBuilder.toByteArray();
                        }
                    }
                    if (decodeBase64Char4 == -2) {
                        _getByteArrayBuilder.appendTwoBytes(i6 >> 2);
                    }
                }
                _getByteArrayBuilder.appendThreeBytes(decodeBase64Char4 | (i6 << 6));
            }
        }
    }
}
