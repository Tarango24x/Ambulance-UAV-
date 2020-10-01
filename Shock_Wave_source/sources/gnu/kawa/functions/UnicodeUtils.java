package gnu.kawa.functions;

import gnu.mapping.Namespace;
import gnu.mapping.Symbol;
import java.text.BreakIterator;

public class UnicodeUtils {

    /* renamed from: Cc */
    static final Symbol f49Cc;

    /* renamed from: Cf */
    static final Symbol f50Cf;

    /* renamed from: Cn */
    static final Symbol f51Cn;

    /* renamed from: Co */
    static final Symbol f52Co;

    /* renamed from: Cs */
    static final Symbol f53Cs;

    /* renamed from: Ll */
    static final Symbol f54Ll;

    /* renamed from: Lm */
    static final Symbol f55Lm;

    /* renamed from: Lo */
    static final Symbol f56Lo;

    /* renamed from: Lt */
    static final Symbol f57Lt;

    /* renamed from: Lu */
    static final Symbol f58Lu;

    /* renamed from: Mc */
    static final Symbol f59Mc;

    /* renamed from: Me */
    static final Symbol f60Me;

    /* renamed from: Mn */
    static final Symbol f61Mn;

    /* renamed from: Nd */
    static final Symbol f62Nd;

    /* renamed from: Nl */
    static final Symbol f63Nl;

    /* renamed from: No */
    static final Symbol f64No;

    /* renamed from: Pc */
    static final Symbol f65Pc;

    /* renamed from: Pd */
    static final Symbol f66Pd;

    /* renamed from: Pe */
    static final Symbol f67Pe;

    /* renamed from: Pf */
    static final Symbol f68Pf;

    /* renamed from: Pi */
    static final Symbol f69Pi;

    /* renamed from: Po */
    static final Symbol f70Po;

    /* renamed from: Ps */
    static final Symbol f71Ps;

    /* renamed from: Sc */
    static final Symbol f72Sc;

    /* renamed from: Sk */
    static final Symbol f73Sk;

    /* renamed from: Sm */
    static final Symbol f74Sm;

    /* renamed from: So */
    static final Symbol f75So;

    /* renamed from: Zl */
    static final Symbol f76Zl;

    /* renamed from: Zp */
    static final Symbol f77Zp;

    /* renamed from: Zs */
    static final Symbol f78Zs;

    public static boolean isWhitespace(int ch) {
        if (ch == 32 || (ch >= 9 && ch <= 13)) {
            return true;
        }
        if (ch < 133) {
            return false;
        }
        if (ch == 133 || ch == 160 || ch == 5760 || ch == 6158) {
            return true;
        }
        if (ch < 8192 || ch > 12288) {
            return false;
        }
        if (ch <= 8202 || ch == 8232 || ch == 8233 || ch == 8239 || ch == 8287 || ch == 12288) {
            return true;
        }
        return false;
    }

    public static String capitalize(String str) {
        StringBuilder sbuf = new StringBuilder();
        BreakIterator wb = BreakIterator.getWordInstance();
        wb.setText(str);
        int start = wb.first();
        for (int end = wb.next(); end != -1; end = wb.next()) {
            boolean isWord = false;
            int p = start;
            while (true) {
                if (p >= end) {
                    break;
                } else if (Character.isLetter(str.codePointAt(p))) {
                    isWord = true;
                    break;
                } else {
                    p++;
                }
            }
            if (!isWord) {
                sbuf.append(str, start, end);
            } else {
                sbuf.append(Character.toTitleCase(str.charAt(start)));
                sbuf.append(str.substring(start + 1, end).toLowerCase());
            }
            start = end;
        }
        return sbuf.toString();
    }

    public static String foldCase(CharSequence str) {
        int len = str.length();
        if (len == 0) {
            return "";
        }
        StringBuilder sbuf = null;
        int start = 0;
        int i = 0;
        while (true) {
            int ch = i == len ? -1 : str.charAt(i);
            boolean sigma = ch == 931 || ch == 963 || ch == 962;
            if (ch < 0 || ch == 304 || ch == 305 || sigma) {
                if (sbuf == null && ch >= 0) {
                    sbuf = new StringBuilder();
                }
                if (i > start) {
                    String converted = str.subSequence(start, i).toString().toUpperCase().toLowerCase();
                    if (sbuf == null) {
                        return converted;
                    }
                    sbuf.append(converted);
                }
                if (ch < 0) {
                    return sbuf.toString();
                }
                if (sigma) {
                    ch = 963;
                }
                sbuf.append((char) ch);
                start = i + 1;
            }
            i++;
        }
    }

    public static Symbol generalCategory(int ch) {
        switch (Character.getType(ch)) {
            case 1:
                return f58Lu;
            case 2:
                return f54Ll;
            case 3:
                return f57Lt;
            case 4:
                return f55Lm;
            case 5:
                return f56Lo;
            case 6:
                return f61Mn;
            case 7:
                return f60Me;
            case 8:
                return f59Mc;
            case 9:
                return f62Nd;
            case 10:
                return f63Nl;
            case 11:
                return f64No;
            case 12:
                return f78Zs;
            case 13:
                return f76Zl;
            case 14:
                return f77Zp;
            case 15:
                return f49Cc;
            case 16:
                return f50Cf;
            case 18:
                return f52Co;
            case 19:
                return f53Cs;
            case 20:
                return f66Pd;
            case 21:
                return f71Ps;
            case 22:
                return f67Pe;
            case 23:
                return f65Pc;
            case 24:
                return f70Po;
            case 25:
                return f74Sm;
            case 26:
                return f72Sc;
            case 27:
                return f73Sk;
            case 28:
                return f75So;
            case 29:
                return f69Pi;
            case 30:
                return f68Pf;
            default:
                return f51Cn;
        }
    }

    static {
        Namespace empty = Namespace.EmptyNamespace;
        f59Mc = empty.getSymbol("Mc");
        f65Pc = empty.getSymbol("Pc");
        f49Cc = empty.getSymbol("Cc");
        f72Sc = empty.getSymbol("Sc");
        f66Pd = empty.getSymbol("Pd");
        f62Nd = empty.getSymbol("Nd");
        f60Me = empty.getSymbol("Me");
        f67Pe = empty.getSymbol("Pe");
        f68Pf = empty.getSymbol("Pf");
        f50Cf = empty.getSymbol("Cf");
        f69Pi = empty.getSymbol("Pi");
        f63Nl = empty.getSymbol("Nl");
        f76Zl = empty.getSymbol("Zl");
        f54Ll = empty.getSymbol("Ll");
        f74Sm = empty.getSymbol("Sm");
        f55Lm = empty.getSymbol("Lm");
        f73Sk = empty.getSymbol("Sk");
        f61Mn = empty.getSymbol("Mn");
        f56Lo = empty.getSymbol("Lo");
        f64No = empty.getSymbol("No");
        f70Po = empty.getSymbol("Po");
        f75So = empty.getSymbol("So");
        f77Zp = empty.getSymbol("Zp");
        f52Co = empty.getSymbol("Co");
        f78Zs = empty.getSymbol("Zs");
        f71Ps = empty.getSymbol("Ps");
        f53Cs = empty.getSymbol("Cs");
        f57Lt = empty.getSymbol("Lt");
        f51Cn = empty.getSymbol("Cn");
        f58Lu = empty.getSymbol("Lu");
    }
}
