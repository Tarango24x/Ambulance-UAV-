package kawa.lib;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.expr.QuoteExp;
import gnu.expr.Symbols;
import gnu.kawa.functions.AddOp;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.kawa.reflect.StaticFieldLocation;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Location;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import kawa.lang.Eval;
import kawa.lang.Macro;
import kawa.lang.Quote;
import kawa.lang.SyntaxForm;
import kawa.lang.SyntaxForms;
import kawa.lang.SyntaxPattern;
import kawa.lang.SyntaxRule;
import kawa.lang.SyntaxRules;
import kawa.lang.SyntaxTemplate;
import kawa.lang.TemplateScope;
import kawa.lang.Translator;
import kawa.standard.Scheme;
import kawa.standard.syntax_case;

/* compiled from: std_syntax.scm */
public class std_syntax extends ModuleBody {
    public static final Macro $Prvt$$Pccase = Macro.make(Lit6, Lit7, $instance);
    public static final Macro $Prvt$$Pccase$Mnmatch = Macro.make(Lit8, Lit9, $instance);
    public static final Macro $Prvt$$Pcdo$Mninit = Macro.make(Lit34, Lit35, $instance);
    public static final Macro $Prvt$$Pcdo$Mnlambda1 = Macro.make(Lit36, Lit37, $instance);
    public static final Macro $Prvt$$Pcdo$Mnlambda2 = Macro.make(Lit38, Lit39, $instance);
    public static final Macro $Prvt$$Pcdo$Mnstep = Macro.make(Lit32, Lit33, $instance);
    public static final Macro $Prvt$$Pclet$Mninit = Macro.make(Lit26, Lit27, $instance);
    public static final Macro $Prvt$$Pclet$Mnlambda1 = Macro.make(Lit22, Lit23, $instance);
    public static final Macro $Prvt$$Pclet$Mnlambda2 = Macro.make(Lit24, Lit25, $instance);
    public static final Location $Prvt$define = StaticFieldLocation.make("kawa.lib.prim_syntax", "define");
    public static final Location $Prvt$define$Mnconstant = StaticFieldLocation.make("kawa.lib.prim_syntax", "define$Mnconstant");
    public static final Location $Prvt$if = StaticFieldLocation.make("kawa.lib.prim_syntax", "if");
    public static final Location $Prvt$letrec = StaticFieldLocation.make("kawa.lib.prim_syntax", "letrec");
    public static final std_syntax $instance = new std_syntax();
    static final IntNum Lit0 = IntNum.make(0);
    static final IntNum Lit1 = IntNum.make(1);
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("and").readResolve());
    static final SyntaxPattern Lit11 = new SyntaxPattern("\f\u0007\b", new Object[0], 1);
    static final SyntaxPattern Lit12 = new SyntaxPattern("\f\u0007\f\u000f\b", new Object[0], 2);
    static final SyntaxTemplate Lit13 = new SyntaxTemplate("\u0001\u0001", "\u000b", new Object[0], 0);
    static final SyntaxPattern Lit14 = new SyntaxPattern("\f\u0007\f\u000f\r\u0017\u0010\b\b", new Object[0], 3);
    static final SyntaxTemplate Lit15 = new SyntaxTemplate("\u0001\u0001\u0003", "\u0011\u0018\u00041\b\u0011\u0018\f\b\u000b\b\u0011\u0018\u0014\u0011\u0018\f)\t\u0003\b\u0015\u0013\u0018\u001c", new Object[]{Lit70, Lit73, Lit72, PairWithPosition.make(Lit73, LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 385052)}, 1);
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("or").readResolve());
    static final SyntaxPattern Lit17 = new SyntaxPattern("\f\u0007\b", new Object[0], 1);
    static final SyntaxPattern Lit18 = new SyntaxPattern("\f\u0007\f\u000f\b", new Object[0], 2);
    static final SyntaxTemplate Lit19 = new SyntaxTemplate("\u0001\u0001", "\u000b", new Object[0], 0);
    static final SimpleSymbol Lit2;
    static final SyntaxPattern Lit20 = new SyntaxPattern("\f\u0007\f\u000f\r\u0017\u0010\b\b", new Object[0], 3);
    static final SyntaxTemplate Lit21 = new SyntaxTemplate("\u0001\u0001\u0003", "\u0011\u0018\u00041\b\u0011\u0018\f\b\u000b\b\u0011\u0018\u0014\u0011\u0018\f\u0011\u0018\f\b\t\u0003\b\u0015\u0013", new Object[]{Lit70, Lit73, Lit72}, 1);
    static final SimpleSymbol Lit22;
    static final SyntaxRules Lit23 = new SyntaxRules(new Object[]{Lit22, Lit64}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018L<\f\u0007\f\u000f\f\u0017\b\u001b\f'\f/\b", new Object[0], 6), "\u0001\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u001a1!\t\u0003\b\u000b#\b+", new Object[]{Lit22}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\\L\f\u0007\f\u0002\f\u000f\f\u0017\b\u001b\f'\f/\b", new Object[]{Lit64}, 6), "\u0001\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u001a1!\t\u0003\b\u000b#\b+", new Object[]{Lit22}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018<,\f\u0007\f\u000f\b\u0013\f\u001f\f'\b", new Object[0], 5), "\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u0012\u0019\t\u0003\u001b\b#", new Object[]{Lit22}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\b\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0011\u0018\u0004\t\u0003\t\u0010\b\u000b", new Object[]{Lit24}, 0)}, 6);
    static final SimpleSymbol Lit24;
    static final SyntaxRules Lit25;
    static final SimpleSymbol Lit26;
    static final SyntaxRules Lit27 = new SyntaxRules(new Object[]{Lit26, Lit64}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018,\f\u0007\f\u000f\b\b", new Object[0], 2), "\u0001\u0001", "\u000b", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u0002\f\u000f\f\u0017\b\b", new Object[]{Lit64}, 3), "\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u000f\f\u0017\b\b", new Object[0], 3), "\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\b\b", new Object[0], 1), "\u0001", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("let binding with no value", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 552979), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 552964)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u000f\f\u0017\f\u001f\b\b", new Object[0], 4), "\u0001\u0001\u0001\u0001", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("let binding must have syntax: (var [type] init)", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 565253), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 561156)}, 0)}, 4);
    static final SimpleSymbol Lit28;
    static final SyntaxRules Lit29;
    static final SyntaxRules Lit3;
    static final SimpleSymbol Lit30;
    static final SyntaxRules Lit31;
    static final SimpleSymbol Lit32;
    static final SyntaxRules Lit33 = new SyntaxRules(new Object[]{Lit32, Lit64}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u0002\f\u000f\f\u0017\f\u001f\b", new Object[]{Lit64}, 4), "\u0001\u0001\u0001\u0001", "\u001b", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u0002\f\u000f\f\u0017\b", new Object[]{Lit64}, 3), "\u0001\u0001\u0001", "\u0003", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u000f\f\u0017\b", new Object[0], 3), "\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0003", new Object[0], 0)}, 4);
    static final SimpleSymbol Lit34;
    static final SyntaxRules Lit35 = new SyntaxRules(new Object[]{Lit34, Lit64}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\\\f\u0007\f\u0002\f\u000f\f\u0017\f\u001f\b\b", new Object[]{Lit64}, 4), "\u0001\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u0002\f\u000f\f\u0017\b\b", new Object[]{Lit64}, 3), "\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u000f\f\u0017\b\b", new Object[0], 3), "\u0001\u0001\u0001", "\u000b", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018,\f\u0007\f\u000f\b\b", new Object[0], 2), "\u0001\u0001", "\u000b", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u000f\f\u0017\b\b", new Object[0], 3), "\u0001\u0001\u0001", "\u0013", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\b\b", new Object[0], 1), "\u0001", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("do binding with no value", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 794643), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 794628)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u000f\f\u0017\f\u001f\b\b", new Object[0], 4), "\u0001\u0001\u0001\u0001", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("do binding must have syntax: (var [:: type] init [step])", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 806917), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 802820)}, 0)}, 4);
    static final SimpleSymbol Lit36;
    static final SyntaxRules Lit37 = new SyntaxRules(new Object[]{Lit36, Lit64}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018l\\\f\u0007\f\u0002\f\u000f\f\u0017\f\u001f\b#\f/\f7\b", new Object[]{Lit64}, 7), "\u0001\u0001\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\"I9\t\u0003\u0011\u0018\f\b\u000b+\b3", new Object[]{Lit36, Lit64}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\\L\f\u0007\f\u0002\f\u000f\f\u0017\b\u001b\f'\f/\b", new Object[]{Lit64}, 6), "\u0001\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u001aI9\t\u0003\u0011\u0018\f\b\u000b#\b+", new Object[]{Lit36, Lit64}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018L<\f\u0007\f\u000f\f\u0017\b\u001b\f'\f/\b", new Object[0], 6), "\u0001\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u001a\u0019\t\u0003#\b+", new Object[]{Lit36}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018<,\f\u0007\f\u000f\b\u0013\f\u001f\f'\b", new Object[0], 5), "\u0001\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\u0012\u0019\t\u0003\u001b\b#", new Object[]{Lit36}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\b\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0011\u0018\u0004\t\u0003\t\u0010\b\u000b", new Object[]{Lit38}, 0)}, 7);
    static final SimpleSymbol Lit38;
    static final SyntaxRules Lit39;
    static final SimpleSymbol Lit4;
    static final SimpleSymbol Lit40;
    static final SyntaxRules Lit41;
    static final SimpleSymbol Lit42;
    static final SyntaxRules Lit43;
    static final SimpleSymbol Lit44;
    static final SyntaxRules Lit45;
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("syntax-object->datum").readResolve());
    static final SimpleSymbol Lit47 = ((SimpleSymbol) new SimpleSymbol("datum->syntax-object").readResolve());
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("generate-temporaries").readResolve());
    static final SimpleSymbol Lit49 = ((SimpleSymbol) new SimpleSymbol("identifier?").readResolve());
    static final SyntaxRules Lit5;
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("free-identifier=?").readResolve());
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("syntax-source").readResolve());
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("syntax-line").readResolve());
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("syntax-column").readResolve());
    static final SimpleSymbol Lit54;
    static final SyntaxPattern Lit55 = new SyntaxPattern("\f\u0007\u000b", new Object[0], 2);
    static final SimpleSymbol Lit56;
    static final SyntaxTemplate Lit57 = new SyntaxTemplate("\u0001\u0000", "\n", new Object[0], 0);
    static final SyntaxTemplate Lit58 = new SyntaxTemplate("\u0001\u0000", "\u0018\u0004", new Object[]{Values.empty}, 0);
    static final SimpleSymbol Lit59;
    static final SimpleSymbol Lit6;
    static final SyntaxRules Lit60;
    static final SimpleSymbol Lit61;
    static final SyntaxRules Lit62;
    static final SimpleSymbol Lit63 = ((SimpleSymbol) new SimpleSymbol("syntax-case").readResolve());
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("::").readResolve());
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("<gnu.expr.GenericProc>").readResolve());
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol(LispLanguage.quote_sym).readResolve());
    static final SimpleSymbol Lit67 = ((SimpleSymbol) new SimpleSymbol("make").readResolve());
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("lambda").readResolve());
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("%syntax-error").readResolve());
    static final SyntaxRules Lit7;
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("%let").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("letrec").readResolve());
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("if").readResolve());
    static final SimpleSymbol Lit73 = ((SimpleSymbol) new SimpleSymbol("x").readResolve());
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("eqv?").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("else").readResolve());
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("=>").readResolve());
    static final SimpleSymbol Lit77 = ((SimpleSymbol) new SimpleSymbol("temp").readResolve());
    static final SimpleSymbol Lit8;
    static final SyntaxRules Lit9;
    public static final Macro and;
    public static final Macro begin$Mnfor$Mnsyntax;

    /* renamed from: case  reason: not valid java name */
    public static final Macro f288case = Macro.make(Lit4, Lit5, $instance);
    public static final Macro cond = Macro.make(Lit2, Lit3, $instance);
    public static final ModuleMethod datum$Mn$Grsyntax$Mnobject;
    public static final Macro define$Mnfor$Mnsyntax = Macro.make(Lit59, Lit60, $instance);
    public static final Macro define$Mnprocedure = Macro.make(Lit44, Lit45, $instance);
    public static final Macro delay = Macro.make(Lit42, Lit43, $instance);

    /* renamed from: do */
    public static final Macro f246do = Macro.make(Lit40, Lit41, $instance);
    public static final ModuleMethod free$Mnidentifier$Eq$Qu;
    public static final ModuleMethod generate$Mntemporaries;
    public static final ModuleMethod identifier$Qu;
    public static final Macro let = Macro.make(Lit28, Lit29, $instance);
    public static final Macro let$St = Macro.make(Lit30, Lit31, $instance);

    /* renamed from: or */
    public static final Macro f247or;
    public static final ModuleMethod syntax$Mncolumn;
    public static final ModuleMethod syntax$Mnline;
    public static final ModuleMethod syntax$Mnobject$Mn$Grdatum;
    public static final ModuleMethod syntax$Mnsource;
    public static final Macro with$Mnsyntax = Macro.make(Lit61, Lit62, $instance);

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol("with-syntax").readResolve();
        Lit61 = simpleSymbol;
        SyntaxPattern syntaxPattern = new SyntaxPattern("\f\u0018\f\b\f\u0007\r\u000f\b\b\b", new Object[0], 2);
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol("begin").readResolve();
        Lit56 = simpleSymbol2;
        Lit62 = new SyntaxRules(new Object[]{simpleSymbol}, new SyntaxRule[]{new SyntaxRule(syntaxPattern, "\u0001\u0003", "\u0011\u0018\u0004\t\u0003\b\r\u000b", new Object[]{simpleSymbol2}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018<,\f\u0007\f\u000f\b\b\f\u0017\r\u001f\u0018\b\b", new Object[0], 4), "\u0001\u0001\u0001\u0003", "\u0011\u0018\u0004\t\u000b\t\u0010\b\t\u0003\b\u0011\u0018\f\t\u0013\b\u001d\u001b", new Object[]{Lit63, Lit56}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018L-\f\u0007\f\u000f\b\u0000\u0010\b\f\u0017\r\u001f\u0018\b\b", new Object[0], 4), "\u0003\u0003\u0001\u0003", "\u0011\u0018\u00041\u0011\u0018\f\b\r\u000b\t\u0010\b\u0019\b\u0005\u0003\b\u0011\u0018\u0014\t\u0013\b\u001d\u001b", new Object[]{Lit63, (SimpleSymbol) new SimpleSymbol("list").readResolve(), Lit56}, 1)}, 4);
        SimpleSymbol simpleSymbol3 = (SimpleSymbol) new SimpleSymbol("define-for-syntax").readResolve();
        Lit59 = simpleSymbol3;
        SyntaxPattern syntaxPattern2 = new SyntaxPattern("\f\u0018\u0003", new Object[0], 1);
        SimpleSymbol simpleSymbol4 = (SimpleSymbol) new SimpleSymbol("begin-for-syntax").readResolve();
        Lit54 = simpleSymbol4;
        Lit60 = new SyntaxRules(new Object[]{simpleSymbol3}, new SyntaxRule[]{new SyntaxRule(syntaxPattern2, "\u0000", "\u0011\u0018\u0004\b\u0011\u0018\f\u0002", new Object[]{simpleSymbol4, (SimpleSymbol) new SimpleSymbol("define").readResolve()}, 0)}, 1);
        SimpleSymbol simpleSymbol5 = (SimpleSymbol) new SimpleSymbol("define-procedure").readResolve();
        Lit44 = simpleSymbol5;
        Lit45 = new SyntaxRules(new Object[]{simpleSymbol5, Lit64, Lit65}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\r\u000f\b\b\b", new Object[0], 2), "\u0001\u0003", "\u0011\u0018\u0004Á\u0011\u0018\f\t\u0003\u0011\u0018\u0014\u0011\u0018\u001c\b\u0011\u0018$\u0011\u0018\u001c\b\u0011\u0018,\b\u0003\b\u0011\u00184\t\u0003\u0011\u0018<\b\u0011\u0018D\b\r\u000b", new Object[]{Lit56, (SimpleSymbol) new SimpleSymbol("define-constant").readResolve(), Lit64, Lit65, Lit67, Lit66, (SimpleSymbol) new SimpleSymbol("invoke").readResolve(), PairWithPosition.make(Lit66, PairWithPosition.make((SimpleSymbol) new SimpleSymbol("setProperties").readResolve(), LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 1024020), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 1024020), (SimpleSymbol) new SimpleSymbol("java.lang.Object[]").readResolve()}, 1)}, 2);
        SimpleSymbol simpleSymbol6 = (SimpleSymbol) new SimpleSymbol("delay").readResolve();
        Lit42 = simpleSymbol6;
        Lit43 = new SyntaxRules(new Object[]{simpleSymbol6}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\b", new Object[0], 1), "\u0001", "\u0011\u0018\u0004\u0011\u0018\f\b\u0011\u0018\u0014\t\u0010\b\u0003", new Object[]{Lit67, (SimpleSymbol) new SimpleSymbol("<kawa.lang.Promise>").readResolve(), Lit68}, 0)}, 1);
        SimpleSymbol simpleSymbol7 = (SimpleSymbol) new SimpleSymbol("do").readResolve();
        Lit40 = simpleSymbol7;
        Object[] objArr = {simpleSymbol7, Lit64};
        SyntaxPattern syntaxPattern3 = new SyntaxPattern("\f\u0018,\r\u0007\u0000\b\b\u001c\f\u000f\u0013\r\u001f\u0018\b\b", new Object[0], 4);
        SimpleSymbol simpleSymbol8 = (SimpleSymbol) new SimpleSymbol("%do-lambda1").readResolve();
        Lit36 = simpleSymbol8;
        SimpleSymbol simpleSymbol9 = (SimpleSymbol) new SimpleSymbol("%do-step").readResolve();
        Lit32 = simpleSymbol9;
        SimpleSymbol simpleSymbol10 = (SimpleSymbol) new SimpleSymbol("%do-init").readResolve();
        Lit34 = simpleSymbol10;
        Lit41 = new SyntaxRules(objArr, new SyntaxRule[]{new SyntaxRule(syntaxPattern3, "\u0003\u0001\u0000\u0003", "\u0011\u0018\u0004Ɖ\b\u0011\u0018\f\b\u0011\u0018\u0014\u0019\b\u0005\u0003\t\u0010\b\u0011\u0018\u001c)\u0011\u0018$\b\u000b\u0011\u0018,\u0011\u001d\u001b\b\u0011\u0018\f\b\u0005\u0011\u00184\u0003\b\u0011\u0018,\u0011\u0018<\u0012\b\u0011\u0018\f\b\u0005\u0011\u0018D\b\u0003", new Object[]{Lit71, (SimpleSymbol) new SimpleSymbol("%do%loop").readResolve(), simpleSymbol8, Lit72, (SimpleSymbol) new SimpleSymbol("not").readResolve(), Lit56, simpleSymbol9, Values.empty, simpleSymbol10}, 1)}, 4);
        SimpleSymbol simpleSymbol11 = (SimpleSymbol) new SimpleSymbol("%do-lambda2").readResolve();
        Lit38 = simpleSymbol11;
        Lit39 = new SyntaxRules(new Object[]{simpleSymbol11}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\u000b\f\u0017\f\u001f\b", new Object[0], 4), "\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\n\u0019\t\u0003\u0013\b\u001b", new Object[]{Lit38}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\b\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0011\u0018\u0004\t\u0003\b\u000b", new Object[]{Lit68}, 0)}, 4);
        SimpleSymbol simpleSymbol12 = (SimpleSymbol) new SimpleSymbol("let*").readResolve();
        Lit30 = simpleSymbol12;
        Lit31 = new SyntaxRules(new Object[]{simpleSymbol12}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\b\u0003", new Object[0], 1), "\u0000", "\u0011\u0018\u0004\t\u0010\u0002", new Object[]{Lit70}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\b\u000b", new Object[0], 2), "\u0001\u0000", "\u0011\u0018\u0004\u0011\b\u0003\n", new Object[]{Lit70}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\u000b\u0013", new Object[0], 3), "\u0001\u0000\u0000", "\u0011\u0018\u0004\u0011\b\u0003\b\u0011\u0018\f\t\n\u0012", new Object[]{Lit70, Lit30}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\u000b", new Object[0], 2), "\u0001\u0000", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("invalid bindings list in let*", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 679943), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 675846)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u0003", new Object[0], 1), "\u0000", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("missing bindings list in let*", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 692231), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 688134)}, 0)}, 3);
        SimpleSymbol simpleSymbol13 = (SimpleSymbol) new SimpleSymbol("let").readResolve();
        Lit28 = simpleSymbol13;
        SyntaxPattern syntaxPattern4 = new SyntaxPattern("\f\u0018,\r\u0007\u0000\b\b\u000b", new Object[0], 2);
        Object[] objArr2 = {Lit70};
        SyntaxPattern syntaxPattern5 = new SyntaxPattern("\f\u0018\f\u0007,\r\u000f\b\b\b\u0013", new Object[0], 3);
        SimpleSymbol simpleSymbol14 = (SimpleSymbol) new SimpleSymbol("%let-lambda1").readResolve();
        Lit22 = simpleSymbol14;
        SimpleSymbol simpleSymbol15 = (SimpleSymbol) new SimpleSymbol("%let-init").readResolve();
        Lit26 = simpleSymbol15;
        Lit29 = new SyntaxRules(new Object[]{simpleSymbol13}, new SyntaxRule[]{new SyntaxRule(syntaxPattern4, "\u0003\u0000", "\u0011\u0018\u0004\u0019\b\u0005\u0003\n", objArr2, 1), new SyntaxRule(syntaxPattern5, "\u0001\u0003\u0000", "©\u0011\u0018\u0004y\b\t\u0003\b\u0011\u0018\f\u0019\b\r\u000b\t\u0010\b\u0012\b\u0003\b\r\u0011\u0018\u0014\b\u000b", new Object[]{Lit71, simpleSymbol14, simpleSymbol15}, 1)}, 3);
        SimpleSymbol simpleSymbol16 = (SimpleSymbol) new SimpleSymbol("%let-lambda2").readResolve();
        Lit24 = simpleSymbol16;
        Lit25 = new SyntaxRules(new Object[]{simpleSymbol16}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\u000b\f\u0017\f\u001f\b", new Object[0], 4), "\u0001\u0000\u0001\u0001", "\u0011\u0018\u0004\t\n\u0019\t\u0003\u0013\b\u001b", new Object[]{Lit24}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\b\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0011\u0018\u0004\t\u0003\u000b", new Object[]{Lit68}, 0)}, 4);
        SimpleSymbol simpleSymbol17 = (SimpleSymbol) new SimpleSymbol("%case-match").readResolve();
        Lit8 = simpleSymbol17;
        Lit9 = new SyntaxRules(new Object[]{simpleSymbol17}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u000f\b", new Object[0], 2), "\u0001\u0001", "\u0011\u0018\u0004\t\u0003\b\u0011\u0018\f\b\u000b", new Object[]{Lit74, Lit66}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u000f\r\u0017\u0010\b\b", new Object[0], 3), "\u0001\u0001\u0003", "\u0011\u0018\u0004Y\u0011\u0018\f\t\u0003\b\u0011\u0018\u0014\b\u000b\b\u0011\u0018\u001c\t\u0003\b\u0015\u0013", new Object[]{Lit16, Lit74, Lit66, Lit8}, 1)}, 3);
        SimpleSymbol simpleSymbol18 = (SimpleSymbol) new SimpleSymbol("%case").readResolve();
        Lit6 = simpleSymbol18;
        Lit7 = new SyntaxRules(new Object[]{simpleSymbol18, Lit75}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007<\f\u0002\r\u000f\b\b\b\b", new Object[]{Lit75}, 2), "\u0001\u0003", "\u0011\u0018\u0004\b\r\u000b", new Object[]{Lit56}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007<\f\u0002\r\u000f\b\b\b\u0013", new Object[]{Lit75}, 3), "\u0001\u0003\u0000", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("junk following else (in case)", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 241674), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 237577)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\\,\r\u000f\b\b\b\r\u0017\u0010\b\b\b", new Object[0], 3), "\u0001\u0003\u0003", "\u0011\u0018\u0004A\u0011\u0018\f\t\u0003\b\r\u000b\b\u0011\u0018\u0014\b\u0015\u0013", new Object[]{Lit72, Lit8, Lit56}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\\,\r\u000f\b\b\b\r\u0017\u0010\b\b\f\u001f\r' \b\b", new Object[0], 5), "\u0001\u0003\u0003\u0001\u0003", "\u0011\u0018\u0004A\u0011\u0018\f\t\u0003\b\r\u000b1\u0011\u0018\u0014\b\u0015\u0013\b\u0011\u0018\u001c\t\u0003\t\u001b\b%#", new Object[]{Lit72, Lit8, Lit56, Lit6}, 1)}, 5);
        SimpleSymbol simpleSymbol19 = (SimpleSymbol) new SimpleSymbol("case").readResolve();
        Lit4 = simpleSymbol19;
        Lit5 = new SyntaxRules(new Object[]{simpleSymbol19}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\r\u000f\b\b\b", new Object[0], 2), "\u0001\u0003", "\u0011\u0018\u00041\b\u0011\u0018\f\b\u0003\b\u0011\u0018\u0014\u0011\u0018\f\b\r\u000b", new Object[]{Lit70, (SimpleSymbol) new SimpleSymbol("tmp").readResolve(), Lit6}, 1)}, 2);
        SimpleSymbol simpleSymbol20 = (SimpleSymbol) new SimpleSymbol("cond").readResolve();
        Lit2 = simpleSymbol20;
        Lit3 = new SyntaxRules(new Object[]{simpleSymbol20, Lit75, Lit76}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0002\f\u0007\r\u000f\b\b\b\b", new Object[]{Lit75}, 2), "\u0001\u0003", "\u0011\u0018\u0004\t\u0003\b\r\u000b", new Object[]{Lit56}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0002\f\u0007\r\u000f\b\b\b\r\u0017\u0010\b\b", new Object[]{Lit75}, 3), "\u0001\u0003\u0003", "\u0018\u0004", new Object[]{PairWithPosition.make(Lit69, PairWithPosition.make("else clause must be last clause of cond", LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 86035), "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 86020)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u0002\f\u000f\b\b", new Object[]{Lit76}, 2), "\u0001\u0001", "\u0011\u0018\u00041\b\u0011\u0018\f\b\u0003\b\u0011\u0018\u0014\u0011\u0018\f\b\t\u000b\u0018\u001c", new Object[]{Lit70, Lit77, Lit72, PairWithPosition.make(Lit77, LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 102423)}, 0), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u0002\f\u000f\b\f\u0017\r\u001f\u0018\b\b", new Object[]{Lit76}, 4), "\u0001\u0001\u0001\u0003", "\u0011\u0018\u00041\b\u0011\u0018\f\b\u0003\b\u0011\u0018\u0014\u0011\u0018\f!\t\u000b\u0018\u001c\b\u0011\u0018$\t\u0013\b\u001d\u001b", new Object[]{Lit70, Lit77, Lit72, PairWithPosition.make(Lit77, LList.Empty, "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm", 122898), Lit2}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\b\b", new Object[0], 1), "\u0001", "\u0003", new Object[0], 0), new SyntaxRule(new SyntaxPattern("\f\u0018\u001c\f\u0007\b\f\u000f\r\u0017\u0010\b\b", new Object[0], 3), "\u0001\u0001\u0003", "\u0011\u0018\u0004\t\u0003\b\u0011\u0018\f\t\u000b\b\u0015\u0013", new Object[]{Lit16, Lit2}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u000f\r\u0017\u0010\b\b\b", new Object[0], 3), "\u0001\u0001\u0003", "\u0011\u0018\u0004\t\u0003\b\u0011\u0018\f\t\u000b\b\u0015\u0013", new Object[]{Lit72, Lit56}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018L\f\u0007\f\u000f\r\u0017\u0010\b\b\f\u001f\r' \b\b", new Object[0], 5), "\u0001\u0001\u0003\u0001\u0003", "\u0011\u0018\u0004\t\u0003A\u0011\u0018\f\t\u000b\b\u0015\u0013\b\u0011\u0018\u0014\t\u001b\b%#", new Object[]{Lit72, Lit56, Lit2}, 1)}, 5);
        SimpleSymbol simpleSymbol21 = Lit10;
        std_syntax std_syntax = $instance;
        and = Macro.make(simpleSymbol21, new ModuleMethod(std_syntax, 1, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN), $instance);
        f247or = Macro.make(Lit16, new ModuleMethod(std_syntax, 2, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN), $instance);
        syntax$Mnobject$Mn$Grdatum = new ModuleMethod(std_syntax, 3, Lit46, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        datum$Mn$Grsyntax$Mnobject = new ModuleMethod(std_syntax, 4, Lit47, 8194);
        generate$Mntemporaries = new ModuleMethod(std_syntax, 5, Lit48, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        identifier$Qu = new ModuleMethod(std_syntax, 6, Lit49, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        free$Mnidentifier$Eq$Qu = new ModuleMethod(std_syntax, 7, Lit50, 8194);
        syntax$Mnsource = new ModuleMethod(std_syntax, 8, Lit51, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        syntax$Mnline = new ModuleMethod(std_syntax, 9, Lit52, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        syntax$Mncolumn = new ModuleMethod(std_syntax, 10, Lit53, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        SimpleSymbol simpleSymbol22 = Lit54;
        ModuleMethod moduleMethod = new ModuleMethod(std_syntax, 11, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/kawa/lib/std_syntax.scm:298");
        begin$Mnfor$Mnsyntax = Macro.make(simpleSymbol22, moduleMethod, $instance);
        $instance.run();
    }

    public std_syntax() {
        ModuleInfo.register(this);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    static Object lambda1(Object f) {
        Object[] allocVars = SyntaxPattern.allocVars(3, (Object[]) null);
        if (Lit11.match(f, allocVars, 0)) {
            return new QuoteExp(Language.getDefaultLanguage().booleanObject(true));
        }
        if (Lit12.match(f, allocVars, 0)) {
            return Lit13.execute(allocVars, TemplateScope.make());
        } else if (!Lit14.match(f, allocVars, 0)) {
            return syntax_case.error("syntax-case", f);
        } else {
            return Lit15.execute(allocVars, TemplateScope.make());
        }
    }

    static Object lambda2(Object f) {
        Object[] allocVars = SyntaxPattern.allocVars(3, (Object[]) null);
        if (Lit17.match(f, allocVars, 0)) {
            return new QuoteExp(Language.getDefaultLanguage().booleanObject(false));
        }
        if (Lit18.match(f, allocVars, 0)) {
            return Lit19.execute(allocVars, TemplateScope.make());
        } else if (!Lit20.match(f, allocVars, 0)) {
            return syntax_case.error("syntax-case", f);
        } else {
            return Lit21.execute(allocVars, TemplateScope.make());
        }
    }

    public static Object syntaxObject$To$Datum(Object obj) {
        return Quote.quote(obj);
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 1:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 2:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 3:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 5:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 6:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 8:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 9:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 10:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            case 11:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.f221pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object datum$To$SyntaxObject(Object template$Mnidentifier, Object obj) {
        return SyntaxForms.makeWithTemplate(template$Mnidentifier, obj);
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 4:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.f221pc = 2;
                return 0;
            case 7:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.f221pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public static Object generateTemporaries(Object list) {
        Object valueOf = Integer.valueOf(Translator.listLength(list));
        Object obj = LList.Empty;
        while (true) {
            Object obj2 = obj;
            if (Scheme.numEqu.apply2(valueOf, Lit0) != Boolean.FALSE) {
                return obj2;
            }
            valueOf = AddOp.$Mn.apply2(valueOf, Lit1);
            obj = new Pair(datum$To$SyntaxObject(list, Symbols.gentemp()), obj2);
        }
    }

    public static boolean isIdentifier(Object form) {
        boolean x = form instanceof Symbol;
        if (x) {
            return x;
        }
        boolean x2 = form instanceof SyntaxForm;
        if (!x2) {
            return x2;
        }
        try {
            return SyntaxForms.isIdentifier((SyntaxForm) form);
        } catch (ClassCastException e) {
            throw new WrongType(e, "kawa.lang.SyntaxForms.isIdentifier(kawa.lang.SyntaxForm)", 1, form);
        }
    }

    public static boolean isFreeIdentifier$Eq(Object id1, Object id2) {
        try {
            try {
                return SyntaxForms.freeIdentifierEquals((SyntaxForm) id1, (SyntaxForm) id2);
            } catch (ClassCastException e) {
                throw new WrongType(e, "kawa.lang.SyntaxForms.freeIdentifierEquals(kawa.lang.SyntaxForm,kawa.lang.SyntaxForm)", 2, id2);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "kawa.lang.SyntaxForms.freeIdentifierEquals(kawa.lang.SyntaxForm,kawa.lang.SyntaxForm)", 1, id1);
        }
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        switch (moduleMethod.selector) {
            case 4:
                return datum$To$SyntaxObject(obj, obj2);
            case 7:
                return isFreeIdentifier$Eq(obj, obj2) ? Boolean.TRUE : Boolean.FALSE;
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static Object syntaxSource(Object form) {
        if (form instanceof SyntaxForm) {
            return syntaxSource(((SyntaxForm) form).getDatum());
        }
        if (!(form instanceof PairWithPosition)) {
            return Boolean.FALSE;
        }
        String str = ((PairWithPosition) form).getFileName();
        return str == null ? Boolean.FALSE : str;
    }

    public static Object syntaxLine(Object form) {
        if (form instanceof SyntaxForm) {
            return syntaxLine(((SyntaxForm) form).getDatum());
        }
        return form instanceof PairWithPosition ? Integer.valueOf(((PairWithPosition) form).getLineNumber()) : Boolean.FALSE;
    }

    public static Object syntaxColumn(Object form) {
        if (form instanceof SyntaxForm) {
            return syntaxLine(((SyntaxForm) form).getDatum());
        }
        return form instanceof PairWithPosition ? Integer.valueOf(((PairWithPosition) form).getColumnNumber() + 0) : Boolean.FALSE;
    }

    static Object lambda3(Object form) {
        Object[] allocVars = SyntaxPattern.allocVars(2, (Object[]) null);
        if (Lit55.match(form, allocVars, 0)) {
            if (Eval.eval.apply1(syntaxObject$To$Datum(new Pair(Lit56, Lit57.execute(allocVars, TemplateScope.make())))) != Boolean.FALSE) {
                return Lit58.execute(allocVars, TemplateScope.make());
            }
        }
        return syntax_case.error("syntax-case", form);
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 1:
                return lambda1(obj);
            case 2:
                return lambda2(obj);
            case 3:
                return syntaxObject$To$Datum(obj);
            case 5:
                return generateTemporaries(obj);
            case 6:
                return isIdentifier(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 8:
                return syntaxSource(obj);
            case 9:
                return syntaxLine(obj);
            case 10:
                return syntaxColumn(obj);
            case 11:
                return lambda3(obj);
            default:
                return super.apply1(moduleMethod, obj);
        }
    }
}
