package appinventor.ai_shoaibmahmuds06.Shock_Wave;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AppInventorCompatActivity;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.FirebaseDB;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.RetValManager;
import com.google.appinventor.components.runtime.util.RuntimeErrorAlert;
import com.google.youngandroid.C0609runtime;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.Apply;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.functions.IsEqual;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.kawa.reflect.SlotSet;
import gnu.lists.Consumer;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import kawa.lang.Promise;
import kawa.lib.C0621lists;
import kawa.lib.misc;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.require;
import org.shaded.apache.http.HttpStatus;

/* compiled from: Screen1.yail */
public class Screen1 extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Screen1").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement2").readResolve());
    static final IntNum Lit101 = IntNum.make(16777215);
    static final FString Lit102 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit103 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("Button4").readResolve());
    static final IntNum Lit105;
    static final IntNum Lit106;
    static final FString Lit107 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit108 = IntNum.make(4);
    static final PairWithPosition Lit109 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 888921), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 888915);
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("Icon").readResolve());
    static final SimpleSymbol Lit110 = ((SimpleSymbol) new SimpleSymbol("Button4$Click").readResolve());
    static final FString Lit111 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("Button15").readResolve());
    static final IntNum Lit113;
    static final IntNum Lit114;
    static final FString Lit115 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit116 = IntNum.make(0);
    static final PairWithPosition Lit117 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 958553), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 958547);
    static final SimpleSymbol Lit118 = ((SimpleSymbol) new SimpleSymbol("Button15$Click").readResolve());
    static final FString Lit119 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("PrimaryColor").readResolve());
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("Button5").readResolve());
    static final IntNum Lit121;
    static final IntNum Lit122;
    static final FString Lit123 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit124 = IntNum.make(5);
    static final PairWithPosition Lit125 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1028185), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1028179);
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("Button5$Click").readResolve());
    static final FString Lit127 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit128 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement3").readResolve());
    static final IntNum Lit129 = IntNum.make(16777215);
    static final IntNum Lit13 = IntNum.make(16777215);
    static final FString Lit130 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit131 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit132 = ((SimpleSymbol) new SimpleSymbol("Button6").readResolve());
    static final IntNum Lit133;
    static final IntNum Lit134;
    static final FString Lit135 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit136 = IntNum.make(6);
    static final PairWithPosition Lit137 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1142873), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1142867);
    static final SimpleSymbol Lit138 = ((SimpleSymbol) new SimpleSymbol("Button6$Click").readResolve());
    static final FString Lit139 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final SimpleSymbol Lit140 = ((SimpleSymbol) new SimpleSymbol("Button7").readResolve());
    static final IntNum Lit141;
    static final IntNum Lit142;
    static final FString Lit143 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit144 = IntNum.make(7);
    static final PairWithPosition Lit145 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1212505), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1212499);
    static final SimpleSymbol Lit146 = ((SimpleSymbol) new SimpleSymbol("Button7$Click").readResolve());
    static final FString Lit147 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit148 = ((SimpleSymbol) new SimpleSymbol("Button8").readResolve());
    static final IntNum Lit149;
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("ShowListsAsJson").readResolve());
    static final IntNum Lit150;
    static final FString Lit151 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit152 = IntNum.make(8);
    static final PairWithPosition Lit153 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1282137), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1282131);
    static final SimpleSymbol Lit154 = ((SimpleSymbol) new SimpleSymbol("Button8$Click").readResolve());
    static final FString Lit155 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit156 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement15").readResolve());
    static final IntNum Lit157 = IntNum.make(16777215);
    static final IntNum Lit158 = IntNum.make(150);
    static final FString Lit159 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final FString Lit160 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit161 = ((SimpleSymbol) new SimpleSymbol("Button9").readResolve());
    static final IntNum Lit162;
    static final IntNum Lit163;
    static final FString Lit164 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit165 = IntNum.make(9);
    static final PairWithPosition Lit166 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1396825), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1396819);
    static final SimpleSymbol Lit167 = ((SimpleSymbol) new SimpleSymbol("Button9$Click").readResolve());
    static final FString Lit168 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit169 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement5").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("ShowStatusBar").readResolve());
    static final IntNum Lit170 = IntNum.make(16777215);
    static final FString Lit171 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit172 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit173 = ((SimpleSymbol) new SimpleSymbol("Label2").readResolve());
    static final IntNum Lit174;
    static final IntNum Lit175;
    static final FString Lit176 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit177 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit178 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement6").readResolve());
    static final IntNum Lit179 = IntNum.make(16777215);
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("Sizing").readResolve());
    static final FString Lit180 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit181 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit182 = ((SimpleSymbol) new SimpleSymbol("Button10").readResolve());
    static final IntNum Lit183;
    static final IntNum Lit184;
    static final FString Lit185 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit186 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1618009), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1618003);
    static final SimpleSymbol Lit187 = ((SimpleSymbol) new SimpleSymbol("Button10$Click").readResolve());
    static final FString Lit188 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit189 = ((SimpleSymbol) new SimpleSymbol("Button11").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final IntNum Lit190;
    static final IntNum Lit191;
    static final FString Lit192 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit193 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1687641), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1687635);
    static final SimpleSymbol Lit194 = ((SimpleSymbol) new SimpleSymbol("Button11$Click").readResolve());
    static final FString Lit195 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit196 = ((SimpleSymbol) new SimpleSymbol("Button12").readResolve());
    static final IntNum Lit197;
    static final IntNum Lit198;
    static final FString Lit199 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("TitleVisible").readResolve());
    static final PairWithPosition Lit200 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1757273), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1757267);
    static final SimpleSymbol Lit201 = ((SimpleSymbol) new SimpleSymbol("Button12$Click").readResolve());
    static final FString Lit202 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit203 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement7").readResolve());
    static final IntNum Lit204 = IntNum.make(16777215);
    static final FString Lit205 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit206 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit207 = ((SimpleSymbol) new SimpleSymbol("Button13").readResolve());
    static final IntNum Lit208;
    static final IntNum Lit209;
    static final FString Lit21 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit210 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit211 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1863769), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1863763);
    static final SimpleSymbol Lit212 = ((SimpleSymbol) new SimpleSymbol("Button13$Click").readResolve());
    static final FString Lit213 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit214 = ((SimpleSymbol) new SimpleSymbol("Button16").readResolve());
    static final IntNum Lit215;
    static final IntNum Lit216;
    static final FString Lit217 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit218 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1933401), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 1933395);
    static final SimpleSymbol Lit219 = ((SimpleSymbol) new SimpleSymbol("Button16$Click").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement14").readResolve());
    static final FString Lit220 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit221 = ((SimpleSymbol) new SimpleSymbol("Button14").readResolve());
    static final IntNum Lit222;
    static final IntNum Lit223;
    static final FString Lit224 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit225;
    static final SimpleSymbol Lit226 = ((SimpleSymbol) new SimpleSymbol("Button14$Click").readResolve());
    static final FString Lit227 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit228 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement8").readResolve());
    static final IntNum Lit229 = IntNum.make(16777215);
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final FString Lit230 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit231 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit232 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement11").readResolve());
    static final IntNum Lit233 = IntNum.make(16777215);
    static final IntNum Lit234 = IntNum.make(10);
    static final FString Lit235 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit236 = new FString("com.google.appinventor.components.runtime.FirebaseDB");
    static final SimpleSymbol Lit237 = ((SimpleSymbol) new SimpleSymbol("DefaultURL").readResolve());
    static final SimpleSymbol Lit238 = ((SimpleSymbol) new SimpleSymbol("DeveloperBucket").readResolve());
    static final SimpleSymbol Lit239 = ((SimpleSymbol) new SimpleSymbol("FirebaseToken").readResolve());
    static final IntNum Lit24 = IntNum.make(16777215);
    static final SimpleSymbol Lit240 = ((SimpleSymbol) new SimpleSymbol("FirebaseURL").readResolve());
    static final SimpleSymbol Lit241 = ((SimpleSymbol) new SimpleSymbol("ProjectBucket").readResolve());
    static final FString Lit242 = new FString("com.google.appinventor.components.runtime.FirebaseDB");
    static final SimpleSymbol Lit243 = ((SimpleSymbol) new SimpleSymbol("get-simple-name").readResolve());
    static final SimpleSymbol Lit244 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit245 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit246 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit247 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final SimpleSymbol Lit248 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit249 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final FString Lit25 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit250 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit251 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit252 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit253 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit254 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit255 = ((SimpleSymbol) new SimpleSymbol("dispatchGenericEvent").readResolve());
    static final SimpleSymbol Lit256 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit257 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final FString Lit26 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("Label5").readResolve());
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("AboutScreen").readResolve());
    static final IntNum Lit30 = IntNum.make(30);
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("FontTypeface").readResolve());
    static final IntNum Lit32 = IntNum.make(1);
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit36;
    static final FString Lit37 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit38 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit39 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement9").readResolve());
    static final SimpleSymbol Lit4;
    static final IntNum Lit40 = IntNum.make(16777215);
    static final SimpleSymbol Lit41 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit42 = IntNum.make(100);
    static final SimpleSymbol Lit43 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final IntNum Lit44 = IntNum.make((int) HttpStatus.SC_MULTIPLE_CHOICES);
    static final FString Lit45 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit46 = new FString("com.google.appinventor.components.runtime.Image");
    static final SimpleSymbol Lit47 = ((SimpleSymbol) new SimpleSymbol("Image2").readResolve());
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final IntNum Lit49 = IntNum.make(200);
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("AccentColor").readResolve());
    static final FString Lit50 = new FString("com.google.appinventor.components.runtime.Image");
    static final FString Lit51 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement10").readResolve());
    static final IntNum Lit53 = IntNum.make(16777215);
    static final FString Lit54 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit55 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit56 = ((SimpleSymbol) new SimpleSymbol("Label3").readResolve());
    static final IntNum Lit57 = IntNum.make(15);
    static final FString Lit58 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit59 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final IntNum Lit6 = IntNum.make(16777215);
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement4").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final IntNum Lit62 = IntNum.make(2);
    static final IntNum Lit63 = IntNum.make(1593774867);
    static final FString Lit64 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit65 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol("Label1").readResolve());
    static final IntNum Lit67;
    static final IntNum Lit68;
    static final FString Lit69 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("number").readResolve());
    static final FString Lit70 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement13").readResolve());
    static final IntNum Lit72 = IntNum.make(16777215);
    static final IntNum Lit73 = IntNum.make(350);
    static final FString Lit74 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit75 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("Button1").readResolve());
    static final IntNum Lit77;
    static final IntNum Lit78;
    static final FString Lit79 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final SimpleSymbol Lit80 = ((SimpleSymbol) new SimpleSymbol("FirebaseDB1").readResolve());
    static final SimpleSymbol Lit81 = ((SimpleSymbol) new SimpleSymbol("StoreValue").readResolve());
    static final PairWithPosition Lit82 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 634969), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 634963);
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("Button1$Click").readResolve());
    static final SimpleSymbol Lit84 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit85 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol("Button2").readResolve());
    static final IntNum Lit87;
    static final IntNum Lit88;
    static final FString Lit89 = new FString("com.google.appinventor.components.runtime.Button");
    static final IntNum Lit9 = IntNum.make(3);
    static final PairWithPosition Lit90 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 704601), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 704595);
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("Button2$Click").readResolve());
    static final FString Lit92 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("Button3").readResolve());
    static final IntNum Lit94;
    static final IntNum Lit95;
    static final FString Lit96 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit97 = PairWithPosition.make(Lit4, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 774233), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 774227);
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("Button3$Click").readResolve());
    static final FString Lit99 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    public static Screen1 Screen1;
    static final ModuleMethod lambda$Fn1 = null;
    static final ModuleMethod lambda$Fn10 = null;
    static final ModuleMethod lambda$Fn11 = null;
    static final ModuleMethod lambda$Fn12 = null;
    static final ModuleMethod lambda$Fn13 = null;
    static final ModuleMethod lambda$Fn14 = null;
    static final ModuleMethod lambda$Fn15 = null;
    static final ModuleMethod lambda$Fn16 = null;
    static final ModuleMethod lambda$Fn17 = null;
    static final ModuleMethod lambda$Fn18 = null;
    static final ModuleMethod lambda$Fn19 = null;
    static final ModuleMethod lambda$Fn2 = null;
    static final ModuleMethod lambda$Fn20 = null;
    static final ModuleMethod lambda$Fn21 = null;
    static final ModuleMethod lambda$Fn22 = null;
    static final ModuleMethod lambda$Fn23 = null;
    static final ModuleMethod lambda$Fn24 = null;
    static final ModuleMethod lambda$Fn25 = null;
    static final ModuleMethod lambda$Fn26 = null;
    static final ModuleMethod lambda$Fn27 = null;
    static final ModuleMethod lambda$Fn28 = null;
    static final ModuleMethod lambda$Fn29 = null;
    static final ModuleMethod lambda$Fn3 = null;
    static final ModuleMethod lambda$Fn30 = null;
    static final ModuleMethod lambda$Fn31 = null;
    static final ModuleMethod lambda$Fn32 = null;
    static final ModuleMethod lambda$Fn33 = null;
    static final ModuleMethod lambda$Fn34 = null;
    static final ModuleMethod lambda$Fn35 = null;
    static final ModuleMethod lambda$Fn36 = null;
    static final ModuleMethod lambda$Fn37 = null;
    static final ModuleMethod lambda$Fn38 = null;
    static final ModuleMethod lambda$Fn39 = null;
    static final ModuleMethod lambda$Fn4 = null;
    static final ModuleMethod lambda$Fn40 = null;
    static final ModuleMethod lambda$Fn41 = null;
    static final ModuleMethod lambda$Fn42 = null;
    static final ModuleMethod lambda$Fn43 = null;
    static final ModuleMethod lambda$Fn44 = null;
    static final ModuleMethod lambda$Fn45 = null;
    static final ModuleMethod lambda$Fn46 = null;
    static final ModuleMethod lambda$Fn47 = null;
    static final ModuleMethod lambda$Fn48 = null;
    static final ModuleMethod lambda$Fn49 = null;
    static final ModuleMethod lambda$Fn5 = null;
    static final ModuleMethod lambda$Fn50 = null;
    static final ModuleMethod lambda$Fn51 = null;
    static final ModuleMethod lambda$Fn52 = null;
    static final ModuleMethod lambda$Fn53 = null;
    static final ModuleMethod lambda$Fn54 = null;
    static final ModuleMethod lambda$Fn55 = null;
    static final ModuleMethod lambda$Fn56 = null;
    static final ModuleMethod lambda$Fn57 = null;
    static final ModuleMethod lambda$Fn58 = null;
    static final ModuleMethod lambda$Fn59 = null;
    static final ModuleMethod lambda$Fn6 = null;
    static final ModuleMethod lambda$Fn60 = null;
    static final ModuleMethod lambda$Fn61 = null;
    static final ModuleMethod lambda$Fn62 = null;
    static final ModuleMethod lambda$Fn63 = null;
    static final ModuleMethod lambda$Fn64 = null;
    static final ModuleMethod lambda$Fn65 = null;
    static final ModuleMethod lambda$Fn66 = null;
    static final ModuleMethod lambda$Fn67 = null;
    static final ModuleMethod lambda$Fn68 = null;
    static final ModuleMethod lambda$Fn69 = null;
    static final ModuleMethod lambda$Fn7 = null;
    static final ModuleMethod lambda$Fn70 = null;
    static final ModuleMethod lambda$Fn71 = null;
    static final ModuleMethod lambda$Fn72 = null;
    static final ModuleMethod lambda$Fn8 = null;
    static final ModuleMethod lambda$Fn9 = null;
    public Boolean $Stdebug$Mnform$St;
    public final ModuleMethod $define;
    public Button Button1;
    public final ModuleMethod Button1$Click;
    public Button Button10;
    public final ModuleMethod Button10$Click;
    public Button Button11;
    public final ModuleMethod Button11$Click;
    public Button Button12;
    public final ModuleMethod Button12$Click;
    public Button Button13;
    public final ModuleMethod Button13$Click;
    public Button Button14;
    public final ModuleMethod Button14$Click;
    public Button Button15;
    public final ModuleMethod Button15$Click;
    public Button Button16;
    public final ModuleMethod Button16$Click;
    public Button Button2;
    public final ModuleMethod Button2$Click;
    public Button Button3;
    public final ModuleMethod Button3$Click;
    public Button Button4;
    public final ModuleMethod Button4$Click;
    public Button Button5;
    public final ModuleMethod Button5$Click;
    public Button Button6;
    public final ModuleMethod Button6$Click;
    public Button Button7;
    public final ModuleMethod Button7$Click;
    public Button Button8;
    public final ModuleMethod Button8$Click;
    public Button Button9;
    public final ModuleMethod Button9$Click;
    public FirebaseDB FirebaseDB1;
    public HorizontalArrangement HorizontalArrangement10;
    public HorizontalArrangement HorizontalArrangement11;
    public HorizontalArrangement HorizontalArrangement13;
    public HorizontalArrangement HorizontalArrangement14;
    public HorizontalArrangement HorizontalArrangement15;
    public HorizontalArrangement HorizontalArrangement2;
    public HorizontalArrangement HorizontalArrangement3;
    public HorizontalArrangement HorizontalArrangement4;
    public HorizontalArrangement HorizontalArrangement5;
    public HorizontalArrangement HorizontalArrangement6;
    public HorizontalArrangement HorizontalArrangement7;
    public HorizontalArrangement HorizontalArrangement8;
    public HorizontalArrangement HorizontalArrangement9;
    public Image Image2;
    public Label Label1;
    public Label Label2;
    public Label Label3;
    public Label Label5;
    public final ModuleMethod add$Mnto$Mncomponents;
    public final ModuleMethod add$Mnto$Mnevents;
    public final ModuleMethod add$Mnto$Mnform$Mndo$Mnafter$Mncreation;
    public final ModuleMethod add$Mnto$Mnform$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvar$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvars;
    public final ModuleMethod android$Mnlog$Mnform;
    public LList components$Mnto$Mncreate;
    public final ModuleMethod dispatchEvent;
    public final ModuleMethod dispatchGenericEvent;
    public LList events$Mnto$Mnregister;
    public LList form$Mndo$Mnafter$Mncreation;
    public Environment form$Mnenvironment;
    public Symbol form$Mnname$Mnsymbol;
    public final ModuleMethod get$Mnsimple$Mnname;
    public Environment global$Mnvar$Mnenvironment;
    public LList global$Mnvars$Mnto$Mncreate;
    public final ModuleMethod is$Mnbound$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod lookup$Mnhandler;
    public final ModuleMethod lookup$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod onCreate;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit4 = simpleSymbol;
        Lit225 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit257, LList.Empty, "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 2003033), "/tmp/1601532711041_0.2557706268444063-0/youngandroidproject/../src/appinventor/ai_shoaibmahmuds06/Shock_Wave/Screen1.yail", 2003027);
        int[] iArr = new int[2];
        iArr[0] = -1;
        Lit223 = IntNum.make(iArr);
        int[] iArr2 = new int[2];
        iArr2[0] = -16748544;
        Lit222 = IntNum.make(iArr2);
        int[] iArr3 = new int[2];
        iArr3[0] = -1;
        Lit216 = IntNum.make(iArr3);
        int[] iArr4 = new int[2];
        iArr4[0] = -65536;
        Lit215 = IntNum.make(iArr4);
        int[] iArr5 = new int[2];
        iArr5[0] = -1;
        Lit209 = IntNum.make(iArr5);
        int[] iArr6 = new int[2];
        iArr6[0] = -16748544;
        Lit208 = IntNum.make(iArr6);
        int[] iArr7 = new int[2];
        iArr7[0] = -1;
        Lit198 = IntNum.make(iArr7);
        int[] iArr8 = new int[2];
        iArr8[0] = -16748544;
        Lit197 = IntNum.make(iArr8);
        int[] iArr9 = new int[2];
        iArr9[0] = -1;
        Lit191 = IntNum.make(iArr9);
        int[] iArr10 = new int[2];
        iArr10[0] = -16748544;
        Lit190 = IntNum.make(iArr10);
        int[] iArr11 = new int[2];
        iArr11[0] = -1;
        Lit184 = IntNum.make(iArr11);
        int[] iArr12 = new int[2];
        iArr12[0] = -16748544;
        Lit183 = IntNum.make(iArr12);
        int[] iArr13 = new int[2];
        iArr13[0] = -1;
        Lit175 = IntNum.make(iArr13);
        int[] iArr14 = new int[2];
        iArr14[0] = -65536;
        Lit174 = IntNum.make(iArr14);
        int[] iArr15 = new int[2];
        iArr15[0] = -1;
        Lit163 = IntNum.make(iArr15);
        int[] iArr16 = new int[2];
        iArr16[0] = -16748544;
        Lit162 = IntNum.make(iArr16);
        int[] iArr17 = new int[2];
        iArr17[0] = -1;
        Lit150 = IntNum.make(iArr17);
        int[] iArr18 = new int[2];
        iArr18[0] = -16748544;
        Lit149 = IntNum.make(iArr18);
        int[] iArr19 = new int[2];
        iArr19[0] = -1;
        Lit142 = IntNum.make(iArr19);
        int[] iArr20 = new int[2];
        iArr20[0] = -16748544;
        Lit141 = IntNum.make(iArr20);
        int[] iArr21 = new int[2];
        iArr21[0] = -1;
        Lit134 = IntNum.make(iArr21);
        int[] iArr22 = new int[2];
        iArr22[0] = -16748544;
        Lit133 = IntNum.make(iArr22);
        int[] iArr23 = new int[2];
        iArr23[0] = -1;
        Lit122 = IntNum.make(iArr23);
        int[] iArr24 = new int[2];
        iArr24[0] = -16748544;
        Lit121 = IntNum.make(iArr24);
        int[] iArr25 = new int[2];
        iArr25[0] = -1;
        Lit114 = IntNum.make(iArr25);
        int[] iArr26 = new int[2];
        iArr26[0] = -65536;
        Lit113 = IntNum.make(iArr26);
        int[] iArr27 = new int[2];
        iArr27[0] = -1;
        Lit106 = IntNum.make(iArr27);
        int[] iArr28 = new int[2];
        iArr28[0] = -16748544;
        Lit105 = IntNum.make(iArr28);
        int[] iArr29 = new int[2];
        iArr29[0] = -1;
        Lit95 = IntNum.make(iArr29);
        int[] iArr30 = new int[2];
        iArr30[0] = -16748544;
        Lit94 = IntNum.make(iArr30);
        int[] iArr31 = new int[2];
        iArr31[0] = -1;
        Lit88 = IntNum.make(iArr31);
        int[] iArr32 = new int[2];
        iArr32[0] = -16748544;
        Lit87 = IntNum.make(iArr32);
        int[] iArr33 = new int[2];
        iArr33[0] = -1;
        Lit78 = IntNum.make(iArr33);
        int[] iArr34 = new int[2];
        iArr34[0] = -16748544;
        Lit77 = IntNum.make(iArr34);
        int[] iArr35 = new int[2];
        iArr35[0] = -1;
        Lit68 = IntNum.make(iArr35);
        int[] iArr36 = new int[2];
        iArr36[0] = -65536;
        Lit67 = IntNum.make(iArr36);
        int[] iArr37 = new int[2];
        iArr37[0] = -65536;
        Lit36 = IntNum.make(iArr37);
    }

    public Screen1() {
        ModuleInfo.register(this);
        frame frame2 = new frame();
        frame2.$main = this;
        this.get$Mnsimple$Mnname = new ModuleMethod(frame2, 1, Lit243, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.onCreate = new ModuleMethod(frame2, 2, "onCreate", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.android$Mnlog$Mnform = new ModuleMethod(frame2, 3, Lit244, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame2, 4, Lit245, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame2, 5, Lit246, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame2, 7, Lit247, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame2, 8, Lit248, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame2, 9, Lit249, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame2, 10, Lit250, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame2, 11, Lit251, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame2, 12, Lit252, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.send$Mnerror = new ModuleMethod(frame2, 13, Lit253, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.process$Mnexception = new ModuleMethod(frame2, 14, "process-exception", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.dispatchEvent = new ModuleMethod(frame2, 15, Lit254, 16388);
        this.dispatchGenericEvent = new ModuleMethod(frame2, 16, Lit255, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame2, 17, Lit256, 8194);
        ModuleMethod moduleMethod = new ModuleMethod(frame2, 18, (Object) null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime7098639402960890708.scm:622");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame2, 19, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame2, 20, (Object) null, 0);
        lambda$Fn3 = new ModuleMethod(frame2, 21, (Object) null, 0);
        lambda$Fn4 = new ModuleMethod(frame2, 22, (Object) null, 0);
        lambda$Fn5 = new ModuleMethod(frame2, 23, (Object) null, 0);
        lambda$Fn6 = new ModuleMethod(frame2, 24, (Object) null, 0);
        lambda$Fn7 = new ModuleMethod(frame2, 25, (Object) null, 0);
        lambda$Fn8 = new ModuleMethod(frame2, 26, (Object) null, 0);
        lambda$Fn9 = new ModuleMethod(frame2, 27, (Object) null, 0);
        lambda$Fn10 = new ModuleMethod(frame2, 28, (Object) null, 0);
        lambda$Fn11 = new ModuleMethod(frame2, 29, (Object) null, 0);
        lambda$Fn12 = new ModuleMethod(frame2, 30, (Object) null, 0);
        lambda$Fn13 = new ModuleMethod(frame2, 31, (Object) null, 0);
        lambda$Fn14 = new ModuleMethod(frame2, 32, (Object) null, 0);
        lambda$Fn15 = new ModuleMethod(frame2, 33, (Object) null, 0);
        lambda$Fn16 = new ModuleMethod(frame2, 34, (Object) null, 0);
        lambda$Fn17 = new ModuleMethod(frame2, 35, (Object) null, 0);
        lambda$Fn18 = new ModuleMethod(frame2, 36, (Object) null, 0);
        lambda$Fn19 = new ModuleMethod(frame2, 37, (Object) null, 0);
        lambda$Fn20 = new ModuleMethod(frame2, 38, (Object) null, 0);
        lambda$Fn21 = new ModuleMethod(frame2, 39, (Object) null, 0);
        lambda$Fn22 = new ModuleMethod(frame2, 40, (Object) null, 0);
        this.Button1$Click = new ModuleMethod(frame2, 41, Lit83, 0);
        lambda$Fn23 = new ModuleMethod(frame2, 42, (Object) null, 0);
        lambda$Fn24 = new ModuleMethod(frame2, 43, (Object) null, 0);
        this.Button2$Click = new ModuleMethod(frame2, 44, Lit91, 0);
        lambda$Fn25 = new ModuleMethod(frame2, 45, (Object) null, 0);
        lambda$Fn26 = new ModuleMethod(frame2, 46, (Object) null, 0);
        this.Button3$Click = new ModuleMethod(frame2, 47, Lit98, 0);
        lambda$Fn27 = new ModuleMethod(frame2, 48, (Object) null, 0);
        lambda$Fn28 = new ModuleMethod(frame2, 49, (Object) null, 0);
        lambda$Fn29 = new ModuleMethod(frame2, 50, (Object) null, 0);
        lambda$Fn30 = new ModuleMethod(frame2, 51, (Object) null, 0);
        this.Button4$Click = new ModuleMethod(frame2, 52, Lit110, 0);
        lambda$Fn31 = new ModuleMethod(frame2, 53, (Object) null, 0);
        lambda$Fn32 = new ModuleMethod(frame2, 54, (Object) null, 0);
        this.Button15$Click = new ModuleMethod(frame2, 55, Lit118, 0);
        lambda$Fn33 = new ModuleMethod(frame2, 56, (Object) null, 0);
        lambda$Fn34 = new ModuleMethod(frame2, 57, (Object) null, 0);
        this.Button5$Click = new ModuleMethod(frame2, 58, Lit126, 0);
        lambda$Fn35 = new ModuleMethod(frame2, 59, (Object) null, 0);
        lambda$Fn36 = new ModuleMethod(frame2, 60, (Object) null, 0);
        lambda$Fn37 = new ModuleMethod(frame2, 61, (Object) null, 0);
        lambda$Fn38 = new ModuleMethod(frame2, 62, (Object) null, 0);
        this.Button6$Click = new ModuleMethod(frame2, 63, Lit138, 0);
        lambda$Fn39 = new ModuleMethod(frame2, 64, (Object) null, 0);
        lambda$Fn40 = new ModuleMethod(frame2, 65, (Object) null, 0);
        this.Button7$Click = new ModuleMethod(frame2, 66, Lit146, 0);
        lambda$Fn41 = new ModuleMethod(frame2, 67, (Object) null, 0);
        lambda$Fn42 = new ModuleMethod(frame2, 68, (Object) null, 0);
        this.Button8$Click = new ModuleMethod(frame2, 69, Lit154, 0);
        lambda$Fn43 = new ModuleMethod(frame2, 70, (Object) null, 0);
        lambda$Fn44 = new ModuleMethod(frame2, 71, (Object) null, 0);
        lambda$Fn45 = new ModuleMethod(frame2, 72, (Object) null, 0);
        lambda$Fn46 = new ModuleMethod(frame2, 73, (Object) null, 0);
        this.Button9$Click = new ModuleMethod(frame2, 74, Lit167, 0);
        lambda$Fn47 = new ModuleMethod(frame2, 75, (Object) null, 0);
        lambda$Fn48 = new ModuleMethod(frame2, 76, (Object) null, 0);
        lambda$Fn49 = new ModuleMethod(frame2, 77, (Object) null, 0);
        lambda$Fn50 = new ModuleMethod(frame2, 78, (Object) null, 0);
        lambda$Fn51 = new ModuleMethod(frame2, 79, (Object) null, 0);
        lambda$Fn52 = new ModuleMethod(frame2, 80, (Object) null, 0);
        lambda$Fn53 = new ModuleMethod(frame2, 81, (Object) null, 0);
        lambda$Fn54 = new ModuleMethod(frame2, 82, (Object) null, 0);
        this.Button10$Click = new ModuleMethod(frame2, 83, Lit187, 0);
        lambda$Fn55 = new ModuleMethod(frame2, 84, (Object) null, 0);
        lambda$Fn56 = new ModuleMethod(frame2, 85, (Object) null, 0);
        this.Button11$Click = new ModuleMethod(frame2, 86, Lit194, 0);
        lambda$Fn57 = new ModuleMethod(frame2, 87, (Object) null, 0);
        lambda$Fn58 = new ModuleMethod(frame2, 88, (Object) null, 0);
        this.Button12$Click = new ModuleMethod(frame2, 89, Lit201, 0);
        lambda$Fn59 = new ModuleMethod(frame2, 90, (Object) null, 0);
        lambda$Fn60 = new ModuleMethod(frame2, 91, (Object) null, 0);
        lambda$Fn61 = new ModuleMethod(frame2, 92, (Object) null, 0);
        lambda$Fn62 = new ModuleMethod(frame2, 93, (Object) null, 0);
        this.Button13$Click = new ModuleMethod(frame2, 94, Lit212, 0);
        lambda$Fn63 = new ModuleMethod(frame2, 95, (Object) null, 0);
        lambda$Fn64 = new ModuleMethod(frame2, 96, (Object) null, 0);
        this.Button16$Click = new ModuleMethod(frame2, 97, Lit219, 0);
        lambda$Fn65 = new ModuleMethod(frame2, 98, (Object) null, 0);
        lambda$Fn66 = new ModuleMethod(frame2, 99, (Object) null, 0);
        this.Button14$Click = new ModuleMethod(frame2, 100, Lit226, 0);
        lambda$Fn67 = new ModuleMethod(frame2, 101, (Object) null, 0);
        lambda$Fn68 = new ModuleMethod(frame2, 102, (Object) null, 0);
        lambda$Fn69 = new ModuleMethod(frame2, 103, (Object) null, 0);
        lambda$Fn70 = new ModuleMethod(frame2, 104, (Object) null, 0);
        lambda$Fn71 = new ModuleMethod(frame2, 105, (Object) null, 0);
        lambda$Fn72 = new ModuleMethod(frame2, 106, (Object) null, 0);
    }

    public Object lookupInFormEnvironment(Symbol symbol) {
        return lookupInFormEnvironment(symbol, Boolean.FALSE);
    }

    public void run() {
        CallContext instance = CallContext.getInstance();
        Consumer consumer = instance.consumer;
        instance.consumer = VoidConsumer.instance;
        try {
            run(instance);
            th = null;
        } catch (Throwable th) {
            th = th;
        }
        ModuleBody.runCleanup(instance, th, consumer);
    }

    public final void run(CallContext $ctx) {
        String obj;
        Consumer $result = $ctx.consumer;
        Object find = require.find("com.google.youngandroid.runtime");
        try {
            ((Runnable) find).run();
            this.$Stdebug$Mnform$St = Boolean.FALSE;
            this.form$Mnenvironment = Environment.make(misc.symbol$To$String(Lit0));
            FString stringAppend = strings.stringAppend(misc.symbol$To$String(Lit0), "-global-vars");
            if (stringAppend == null) {
                obj = null;
            } else {
                obj = stringAppend.toString();
            }
            this.global$Mnvar$Mnenvironment = Environment.make(obj);
            Screen1 = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            Object find2 = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find2).run();
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "CREATED BY TEAM SHOCK WAVE", Lit4);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit5, Lit6, Lit7);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit8, Lit9, Lit7);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "WaveVent", Lit4);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit11, "Picture1.png", Lit4);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit12, Lit13, Lit7);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit14, "portrait", Lit4);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit15, Boolean.FALSE, Lit16);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit17, Boolean.FALSE, Lit16);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Responsive", Lit4);
                    C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit19, "WaveVent", Lit4);
                    Values.writeValues(C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit20, Boolean.FALSE, Lit16), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn2));
                }
                this.HorizontalArrangement14 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit21, Lit22, lambda$Fn3), $result);
                } else {
                    addToComponents(Lit0, Lit25, Lit22, lambda$Fn4);
                }
                this.Label5 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit22, Lit26, Lit27, lambda$Fn5), $result);
                } else {
                    addToComponents(Lit22, Lit37, Lit27, lambda$Fn6);
                }
                this.HorizontalArrangement9 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit38, Lit39, lambda$Fn7), $result);
                } else {
                    addToComponents(Lit0, Lit45, Lit39, lambda$Fn8);
                }
                this.Image2 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit39, Lit46, Lit47, lambda$Fn9), $result);
                } else {
                    addToComponents(Lit39, Lit50, Lit47, lambda$Fn10);
                }
                this.HorizontalArrangement10 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit51, Lit52, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit0, Lit54, Lit52, lambda$Fn12);
                }
                this.Label3 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit52, Lit55, Lit56, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit52, Lit58, Lit56, lambda$Fn14);
                }
                this.HorizontalArrangement4 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit59, Lit60, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit0, Lit64, Lit60, lambda$Fn16);
                }
                this.Label1 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit60, Lit65, Lit66, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit60, Lit69, Lit66, lambda$Fn18);
                }
                this.HorizontalArrangement13 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit70, Lit71, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit0, Lit74, Lit71, lambda$Fn20);
                }
                this.Button1 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit71, Lit75, Lit76, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit71, Lit79, Lit76, lambda$Fn22);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit83, this.Button1$Click);
                } else {
                    addToFormEnvironment(Lit83, this.Button1$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button1", "Click");
                } else {
                    addToEvents(Lit76, Lit84);
                }
                this.Button2 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit71, Lit85, Lit86, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit71, Lit89, Lit86, lambda$Fn24);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit91, this.Button2$Click);
                } else {
                    addToFormEnvironment(Lit91, this.Button2$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button2", "Click");
                } else {
                    addToEvents(Lit86, Lit84);
                }
                this.Button3 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit71, Lit92, Lit93, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit71, Lit96, Lit93, lambda$Fn26);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit98, this.Button3$Click);
                } else {
                    addToFormEnvironment(Lit98, this.Button3$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button3", "Click");
                } else {
                    addToEvents(Lit93, Lit84);
                }
                this.HorizontalArrangement2 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit99, Lit100, lambda$Fn27), $result);
                } else {
                    addToComponents(Lit0, Lit102, Lit100, lambda$Fn28);
                }
                this.Button4 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit100, Lit103, Lit104, lambda$Fn29), $result);
                } else {
                    addToComponents(Lit100, Lit107, Lit104, lambda$Fn30);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit110, this.Button4$Click);
                } else {
                    addToFormEnvironment(Lit110, this.Button4$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button4", "Click");
                } else {
                    addToEvents(Lit104, Lit84);
                }
                this.Button15 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit100, Lit111, Lit112, lambda$Fn31), $result);
                } else {
                    addToComponents(Lit100, Lit115, Lit112, lambda$Fn32);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit118, this.Button15$Click);
                } else {
                    addToFormEnvironment(Lit118, this.Button15$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button15", "Click");
                } else {
                    addToEvents(Lit112, Lit84);
                }
                this.Button5 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit100, Lit119, Lit120, lambda$Fn33), $result);
                } else {
                    addToComponents(Lit100, Lit123, Lit120, lambda$Fn34);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit126, this.Button5$Click);
                } else {
                    addToFormEnvironment(Lit126, this.Button5$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button5", "Click");
                } else {
                    addToEvents(Lit120, Lit84);
                }
                this.HorizontalArrangement3 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit127, Lit128, lambda$Fn35), $result);
                } else {
                    addToComponents(Lit0, Lit130, Lit128, lambda$Fn36);
                }
                this.Button6 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit128, Lit131, Lit132, lambda$Fn37), $result);
                } else {
                    addToComponents(Lit128, Lit135, Lit132, lambda$Fn38);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit138, this.Button6$Click);
                } else {
                    addToFormEnvironment(Lit138, this.Button6$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button6", "Click");
                } else {
                    addToEvents(Lit132, Lit84);
                }
                this.Button7 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit128, Lit139, Lit140, lambda$Fn39), $result);
                } else {
                    addToComponents(Lit128, Lit143, Lit140, lambda$Fn40);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit146, this.Button7$Click);
                } else {
                    addToFormEnvironment(Lit146, this.Button7$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button7", "Click");
                } else {
                    addToEvents(Lit140, Lit84);
                }
                this.Button8 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit128, Lit147, Lit148, lambda$Fn41), $result);
                } else {
                    addToComponents(Lit128, Lit151, Lit148, lambda$Fn42);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit154, this.Button8$Click);
                } else {
                    addToFormEnvironment(Lit154, this.Button8$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button8", "Click");
                } else {
                    addToEvents(Lit148, Lit84);
                }
                this.HorizontalArrangement15 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit155, Lit156, lambda$Fn43), $result);
                } else {
                    addToComponents(Lit0, Lit159, Lit156, lambda$Fn44);
                }
                this.Button9 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit156, Lit160, Lit161, lambda$Fn45), $result);
                } else {
                    addToComponents(Lit156, Lit164, Lit161, lambda$Fn46);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit167, this.Button9$Click);
                } else {
                    addToFormEnvironment(Lit167, this.Button9$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button9", "Click");
                } else {
                    addToEvents(Lit161, Lit84);
                }
                this.HorizontalArrangement5 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit168, Lit169, lambda$Fn47), $result);
                } else {
                    addToComponents(Lit0, Lit171, Lit169, lambda$Fn48);
                }
                this.Label2 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit169, Lit172, Lit173, lambda$Fn49), $result);
                } else {
                    addToComponents(Lit169, Lit176, Lit173, lambda$Fn50);
                }
                this.HorizontalArrangement6 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit177, Lit178, lambda$Fn51), $result);
                } else {
                    addToComponents(Lit0, Lit180, Lit178, lambda$Fn52);
                }
                this.Button10 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit178, Lit181, Lit182, lambda$Fn53), $result);
                } else {
                    addToComponents(Lit178, Lit185, Lit182, lambda$Fn54);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit187, this.Button10$Click);
                } else {
                    addToFormEnvironment(Lit187, this.Button10$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button10", "Click");
                } else {
                    addToEvents(Lit182, Lit84);
                }
                this.Button11 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit178, Lit188, Lit189, lambda$Fn55), $result);
                } else {
                    addToComponents(Lit178, Lit192, Lit189, lambda$Fn56);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit194, this.Button11$Click);
                } else {
                    addToFormEnvironment(Lit194, this.Button11$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button11", "Click");
                } else {
                    addToEvents(Lit189, Lit84);
                }
                this.Button12 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit178, Lit195, Lit196, lambda$Fn57), $result);
                } else {
                    addToComponents(Lit178, Lit199, Lit196, lambda$Fn58);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit201, this.Button12$Click);
                } else {
                    addToFormEnvironment(Lit201, this.Button12$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button12", "Click");
                } else {
                    addToEvents(Lit196, Lit84);
                }
                this.HorizontalArrangement7 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit202, Lit203, lambda$Fn59), $result);
                } else {
                    addToComponents(Lit0, Lit205, Lit203, lambda$Fn60);
                }
                this.Button13 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit203, Lit206, Lit207, lambda$Fn61), $result);
                } else {
                    addToComponents(Lit203, Lit210, Lit207, lambda$Fn62);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit212, this.Button13$Click);
                } else {
                    addToFormEnvironment(Lit212, this.Button13$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button13", "Click");
                } else {
                    addToEvents(Lit207, Lit84);
                }
                this.Button16 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit203, Lit213, Lit214, lambda$Fn63), $result);
                } else {
                    addToComponents(Lit203, Lit217, Lit214, lambda$Fn64);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit219, this.Button16$Click);
                } else {
                    addToFormEnvironment(Lit219, this.Button16$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button16", "Click");
                } else {
                    addToEvents(Lit214, Lit84);
                }
                this.Button14 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit203, Lit220, Lit221, lambda$Fn65), $result);
                } else {
                    addToComponents(Lit203, Lit224, Lit221, lambda$Fn66);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    C0609runtime.addToCurrentFormEnvironment(Lit226, this.Button14$Click);
                } else {
                    addToFormEnvironment(Lit226, this.Button14$Click);
                }
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) C0609runtime.$Stthis$Mnform$St, "Button14", "Click");
                } else {
                    addToEvents(Lit221, Lit84);
                }
                this.HorizontalArrangement8 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit227, Lit228, lambda$Fn67), $result);
                } else {
                    addToComponents(Lit0, Lit230, Lit228, lambda$Fn68);
                }
                this.HorizontalArrangement11 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit231, Lit232, lambda$Fn69), $result);
                } else {
                    addToComponents(Lit0, Lit235, Lit232, lambda$Fn70);
                }
                this.FirebaseDB1 = null;
                if (C0609runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(C0609runtime.addComponentWithinRepl(Lit0, Lit236, Lit80, lambda$Fn71), $result);
                } else {
                    addToComponents(Lit0, Lit242, Lit80, lambda$Fn72);
                }
                C0609runtime.initRuntime();
            } catch (ClassCastException e) {
                throw new WrongType(e, "java.lang.Runnable.run()", 1, find2);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "java.lang.Runnable.run()", 1, find);
        }
    }

    static Object lambda3() {
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit3, "CREATED BY TEAM SHOCK WAVE", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit5, Lit6, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit10, "WaveVent", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit11, "Picture1.png", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit12, Lit13, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit14, "portrait", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit15, Boolean.FALSE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit17, Boolean.FALSE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Responsive", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit19, "WaveVent", Lit4);
        return C0609runtime.setAndCoerceProperty$Ex(Lit0, Lit20, Boolean.FALSE, Lit16);
    }

    static Object lambda4() {
        C0609runtime.setAndCoerceProperty$Ex(Lit22, Lit8, Lit9, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit22, Lit23, Lit24, Lit7);
    }

    static Object lambda5() {
        C0609runtime.setAndCoerceProperty$Ex(Lit22, Lit8, Lit9, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit22, Lit23, Lit24, Lit7);
    }

    static Object lambda6() {
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit29, Lit30, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit31, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit33, "WaveVent", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit34, Lit32, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit35, Lit36, Lit7);
    }

    static Object lambda7() {
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit29, Lit30, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit31, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit33, "WaveVent", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit34, Lit32, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit27, Lit35, Lit36, Lit7);
    }

    static Object lambda8() {
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit23, Lit40, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit41, Lit42, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit43, Lit44, Lit7);
    }

    static Object lambda9() {
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit23, Lit40, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit41, Lit42, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit39, Lit43, Lit44, Lit7);
    }

    static Object lambda10() {
        C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit41, Lit42, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit48, "Picture1.png", Lit4);
        return C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit43, Lit49, Lit7);
    }

    static Object lambda11() {
        C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit41, Lit42, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit48, "Picture1.png", Lit4);
        return C0609runtime.setAndCoerceProperty$Ex(Lit47, Lit43, Lit49, Lit7);
    }

    static Object lambda12() {
        C0609runtime.setAndCoerceProperty$Ex(Lit52, Lit8, Lit9, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit52, Lit23, Lit53, Lit7);
    }

    static Object lambda13() {
        C0609runtime.setAndCoerceProperty$Ex(Lit52, Lit8, Lit9, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit52, Lit23, Lit53, Lit7);
    }

    static Object lambda14() {
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit33, "Created By Team Shock Wave", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit34, Lit32, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit43, Lit44, Lit7);
    }

    static Object lambda15() {
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit33, "Created By Team Shock Wave", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit34, Lit32, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit56, Lit43, Lit44, Lit7);
    }

    static Object lambda16() {
        C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit61, Lit62, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit23, Lit63, Lit7);
    }

    static Object lambda17() {
        C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit61, Lit62, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit60, Lit23, Lit63, Lit7);
    }

    static Object lambda18() {
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit23, Lit67, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit33, "Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit34, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit35, Lit68, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit43, Lit42, Lit7);
    }

    static Object lambda19() {
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit23, Lit67, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit33, "Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit34, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit35, Lit68, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit66, Lit43, Lit42, Lit7);
    }

    static Object lambda20() {
        C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit23, Lit72, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit43, Lit73, Lit7);
    }

    static Object lambda21() {
        C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit23, Lit72, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit71, Lit43, Lit73, Lit7);
    }

    static Object lambda22() {
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit23, Lit77, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit33, "6 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit35, Lit78, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit43, Lit42, Lit7);
    }

    static Object lambda23() {
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit23, Lit77, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit33, "6 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit35, Lit78, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit76, Lit43, Lit42, Lit7);
    }

    public Object Button1$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit32), Lit82);
    }

    static Object lambda24() {
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit23, Lit87, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit33, "8 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit35, Lit88, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit43, Lit42, Lit7);
    }

    static Object lambda25() {
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit23, Lit87, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit33, "8 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit35, Lit88, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit86, Lit43, Lit42, Lit7);
    }

    public Object Button2$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit62), Lit90);
    }

    static Object lambda26() {
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit23, Lit94, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit33, "10 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit35, Lit95, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit43, Lit42, Lit7);
    }

    static Object lambda27() {
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit23, Lit94, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit33, "10 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit35, Lit95, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit93, Lit43, Lit42, Lit7);
    }

    public Object Button3$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit9), Lit97);
    }

    static Object lambda28() {
        C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit23, Lit101, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit43, Lit73, Lit7);
    }

    static Object lambda29() {
        C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit23, Lit101, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit100, Lit43, Lit73, Lit7);
    }

    static Object lambda30() {
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit23, Lit105, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit33, "12 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit35, Lit106, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit43, Lit42, Lit7);
    }

    static Object lambda31() {
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit23, Lit105, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit33, "12 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit35, Lit106, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit104, Lit43, Lit42, Lit7);
    }

    public Object Button4$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit108), Lit109);
    }

    static Object lambda32() {
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit23, Lit113, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit33, "Stop", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit35, Lit114, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit43, Lit42, Lit7);
    }

    static Object lambda33() {
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit23, Lit113, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit33, "Stop", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit35, Lit114, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit112, Lit43, Lit42, Lit7);
    }

    public Object Button15$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit116), Lit117);
    }

    static Object lambda34() {
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit23, Lit121, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit33, "14 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit35, Lit122, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit43, Lit42, Lit7);
    }

    static Object lambda35() {
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit23, Lit121, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit33, "14 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit35, Lit122, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit120, Lit43, Lit42, Lit7);
    }

    public Object Button5$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit124), Lit125);
    }

    static Object lambda36() {
        C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit23, Lit129, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit43, Lit73, Lit7);
    }

    static Object lambda37() {
        C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit23, Lit129, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit128, Lit43, Lit73, Lit7);
    }

    static Object lambda38() {
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit23, Lit133, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit33, "18 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit35, Lit134, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit43, Lit42, Lit7);
    }

    static Object lambda39() {
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit23, Lit133, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit33, "18 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit35, Lit134, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit132, Lit43, Lit42, Lit7);
    }

    public Object Button6$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit136), Lit137);
    }

    static Object lambda40() {
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit23, Lit141, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit33, "20 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit35, Lit142, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit43, Lit42, Lit7);
    }

    static Object lambda41() {
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit23, Lit141, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit33, "20 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit35, Lit142, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit140, Lit43, Lit42, Lit7);
    }

    public Object Button7$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit144), Lit145);
    }

    static Object lambda42() {
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit23, Lit149, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit33, "22 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit35, Lit150, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit43, Lit42, Lit7);
    }

    static Object lambda43() {
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit23, Lit149, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit33, "22 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit35, Lit150, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit148, Lit43, Lit42, Lit7);
    }

    /* compiled from: Screen1.yail */
    public class frame extends ModuleBody {
        Screen1 $main;

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 1:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                case 2:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
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
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                case 7:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                case 12:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                case 13:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                case 14:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 4:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                case 5:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                case 8:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                case 9:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                case 11:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                case 17:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 10:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 4;
                    return 0;
                case 15:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    if (!(obj4 instanceof String)) {
                        return -786428;
                    }
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 4;
                    return 0;
                case 16:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 4;
                    return 0;
                default:
                    return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 1:
                    return this.$main.getSimpleName(obj);
                case 2:
                    try {
                        this.$main.onCreate((Bundle) obj);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "onCreate", 1, obj);
                    }
                case 3:
                    this.$main.androidLogForm(obj);
                    return Values.empty;
                case 5:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 7:
                    try {
                        return this.$main.isBoundInFormEnvironment((Symbol) obj) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "is-bound-in-form-environment", 1, obj);
                    }
                case 12:
                    this.$main.addToFormDoAfterCreation(obj);
                    return Values.empty;
                case 13:
                    this.$main.sendError(obj);
                    return Values.empty;
                case 14:
                    this.$main.processException(obj);
                    return Values.empty;
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            boolean z = true;
            switch (moduleMethod.selector) {
                case 10:
                    this.$main.addToComponents(obj, obj2, obj3, obj4);
                    return Values.empty;
                case 15:
                    try {
                        try {
                            try {
                                try {
                                    return this.$main.dispatchEvent((Component) obj, (String) obj2, (String) obj3, (Object[]) obj4) ? Boolean.TRUE : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "dispatchEvent", 4, obj4);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "dispatchEvent", 3, obj3);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "dispatchEvent", 2, obj2);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "dispatchEvent", 1, obj);
                    }
                case 16:
                    Screen1 screen1 = this.$main;
                    try {
                        Component component = (Component) obj;
                        try {
                            String str = (String) obj2;
                            try {
                                if (obj3 == Boolean.FALSE) {
                                    z = false;
                                }
                                try {
                                    screen1.dispatchGenericEvent(component, str, z, (Object[]) obj4);
                                    return Values.empty;
                                } catch (ClassCastException e5) {
                                    throw new WrongType(e5, "dispatchGenericEvent", 4, obj4);
                                }
                            } catch (ClassCastException e6) {
                                throw new WrongType(e6, "dispatchGenericEvent", 3, obj3);
                            }
                        } catch (ClassCastException e7) {
                            throw new WrongType(e7, "dispatchGenericEvent", 2, obj2);
                        }
                    } catch (ClassCastException e8) {
                        throw new WrongType(e8, "dispatchGenericEvent", 1, obj);
                    }
                default:
                    return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 4:
                    try {
                        this.$main.addToFormEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "add-to-form-environment", 1, obj);
                    }
                case 5:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj, obj2);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 8:
                    try {
                        this.$main.addToGlobalVarEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "add-to-global-var-environment", 1, obj);
                    }
                case 9:
                    this.$main.addToEvents(obj, obj2);
                    return Values.empty;
                case 11:
                    this.$main.addToGlobalVars(obj, obj2);
                    return Values.empty;
                case 17:
                    return this.$main.lookupHandler(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 18:
                    return Screen1.lambda2();
                case 19:
                    this.$main.$define();
                    return Values.empty;
                case 20:
                    return Screen1.lambda3();
                case 21:
                    return Screen1.lambda4();
                case 22:
                    return Screen1.lambda5();
                case 23:
                    return Screen1.lambda6();
                case 24:
                    return Screen1.lambda7();
                case 25:
                    return Screen1.lambda8();
                case 26:
                    return Screen1.lambda9();
                case 27:
                    return Screen1.lambda10();
                case 28:
                    return Screen1.lambda11();
                case 29:
                    return Screen1.lambda12();
                case 30:
                    return Screen1.lambda13();
                case 31:
                    return Screen1.lambda14();
                case 32:
                    return Screen1.lambda15();
                case 33:
                    return Screen1.lambda16();
                case 34:
                    return Screen1.lambda17();
                case 35:
                    return Screen1.lambda18();
                case 36:
                    return Screen1.lambda19();
                case 37:
                    return Screen1.lambda20();
                case 38:
                    return Screen1.lambda21();
                case 39:
                    return Screen1.lambda22();
                case 40:
                    return Screen1.lambda23();
                case 41:
                    return this.$main.Button1$Click();
                case 42:
                    return Screen1.lambda24();
                case 43:
                    return Screen1.lambda25();
                case 44:
                    return this.$main.Button2$Click();
                case 45:
                    return Screen1.lambda26();
                case 46:
                    return Screen1.lambda27();
                case 47:
                    return this.$main.Button3$Click();
                case 48:
                    return Screen1.lambda28();
                case 49:
                    return Screen1.lambda29();
                case 50:
                    return Screen1.lambda30();
                case 51:
                    return Screen1.lambda31();
                case 52:
                    return this.$main.Button4$Click();
                case 53:
                    return Screen1.lambda32();
                case 54:
                    return Screen1.lambda33();
                case 55:
                    return this.$main.Button15$Click();
                case 56:
                    return Screen1.lambda34();
                case 57:
                    return Screen1.lambda35();
                case 58:
                    return this.$main.Button5$Click();
                case 59:
                    return Screen1.lambda36();
                case 60:
                    return Screen1.lambda37();
                case 61:
                    return Screen1.lambda38();
                case 62:
                    return Screen1.lambda39();
                case 63:
                    return this.$main.Button6$Click();
                case 64:
                    return Screen1.lambda40();
                case 65:
                    return Screen1.lambda41();
                case 66:
                    return this.$main.Button7$Click();
                case 67:
                    return Screen1.lambda42();
                case 68:
                    return Screen1.lambda43();
                case 69:
                    return this.$main.Button8$Click();
                case 70:
                    return Screen1.lambda44();
                case 71:
                    return Screen1.lambda45();
                case 72:
                    return Screen1.lambda46();
                case 73:
                    return Screen1.lambda47();
                case 74:
                    return this.$main.Button9$Click();
                case 75:
                    return Screen1.lambda48();
                case 76:
                    return Screen1.lambda49();
                case 77:
                    return Screen1.lambda50();
                case 78:
                    return Screen1.lambda51();
                case 79:
                    return Screen1.lambda52();
                case 80:
                    return Screen1.lambda53();
                case 81:
                    return Screen1.lambda54();
                case 82:
                    return Screen1.lambda55();
                case 83:
                    return this.$main.Button10$Click();
                case 84:
                    return Screen1.lambda56();
                case 85:
                    return Screen1.lambda57();
                case 86:
                    return this.$main.Button11$Click();
                case 87:
                    return Screen1.lambda58();
                case 88:
                    return Screen1.lambda59();
                case 89:
                    return this.$main.Button12$Click();
                case 90:
                    return Screen1.lambda60();
                case 91:
                    return Screen1.lambda61();
                case 92:
                    return Screen1.lambda62();
                case 93:
                    return Screen1.lambda63();
                case 94:
                    return this.$main.Button13$Click();
                case 95:
                    return Screen1.lambda64();
                case 96:
                    return Screen1.lambda65();
                case 97:
                    return this.$main.Button16$Click();
                case 98:
                    return Screen1.lambda66();
                case 99:
                    return Screen1.lambda67();
                case 100:
                    return this.$main.Button14$Click();
                case 101:
                    return Screen1.lambda68();
                case 102:
                    return Screen1.lambda69();
                case 103:
                    return Screen1.lambda70();
                case 104:
                    return Screen1.lambda71();
                case 105:
                    return Screen1.lambda72();
                case 106:
                    return Screen1.lambda73();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 18:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 19:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 20:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 21:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 22:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 23:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 24:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 25:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 26:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 27:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 28:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 29:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 30:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 31:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 32:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 33:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 34:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 35:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 36:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 37:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 38:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 39:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 40:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 41:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 42:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 43:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 44:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 45:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 46:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 47:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 48:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 49:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 50:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 51:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 52:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 53:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 54:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 55:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 56:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 57:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 58:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 59:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 60:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 61:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 62:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 63:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 64:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 65:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 66:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 67:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 68:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 69:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 70:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 71:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 72:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 73:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 74:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 75:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 76:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 77:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 78:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 79:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 80:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 81:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 82:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 83:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 84:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 85:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 86:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 87:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 88:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 89:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 90:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 91:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 92:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 93:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 94:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 95:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 96:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 97:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 98:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 99:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 100:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 101:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 102:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 103:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 104:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 105:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                case 106:
                    callContext.proc = moduleMethod;
                    callContext.f221pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }
    }

    public Object Button8$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit152), Lit153);
    }

    static Object lambda44() {
        C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit23, Lit157, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit43, Lit158, Lit7);
    }

    static Object lambda45() {
        C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit23, Lit157, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit156, Lit43, Lit158, Lit7);
    }

    static Object lambda46() {
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit23, Lit162, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit33, "24 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit35, Lit163, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit43, Lit42, Lit7);
    }

    static Object lambda47() {
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit23, Lit162, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit33, "24 Beats/M", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit35, Lit163, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit161, Lit43, Lit42, Lit7);
    }

    public Object Button9$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S1", Lit165), Lit166);
    }

    static Object lambda48() {
        return C0609runtime.setAndCoerceProperty$Ex(Lit169, Lit23, Lit170, Lit7);
    }

    static Object lambda49() {
        return C0609runtime.setAndCoerceProperty$Ex(Lit169, Lit23, Lit170, Lit7);
    }

    static Object lambda50() {
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit23, Lit174, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit33, "Volume", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit34, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit35, Lit175, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit43, Lit42, Lit7);
    }

    static Object lambda51() {
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit23, Lit174, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit29, Lit57, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit33, "Volume", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit34, Lit32, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit35, Lit175, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit173, Lit43, Lit42, Lit7);
    }

    static Object lambda52() {
        C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit23, Lit179, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit43, Lit73, Lit7);
    }

    static Object lambda53() {
        C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit23, Lit179, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit178, Lit43, Lit73, Lit7);
    }

    static Object lambda54() {
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit23, Lit183, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit33, "Volume 1", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit35, Lit184, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit43, Lit42, Lit7);
    }

    static Object lambda55() {
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit23, Lit183, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit33, "Volume 1", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit35, Lit184, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit182, Lit43, Lit42, Lit7);
    }

    public Object Button10$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit32), Lit186);
    }

    static Object lambda56() {
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit23, Lit190, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit33, "Volume 2", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit35, Lit191, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit43, Lit42, Lit7);
    }

    static Object lambda57() {
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit23, Lit190, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit33, "Volume 2", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit35, Lit191, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit189, Lit43, Lit42, Lit7);
    }

    public Object Button11$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit62), Lit193);
    }

    static Object lambda58() {
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit23, Lit197, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit33, "Volume 3", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit35, Lit198, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit43, Lit42, Lit7);
    }

    static Object lambda59() {
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit23, Lit197, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit33, "Volume 3", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit35, Lit198, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit196, Lit43, Lit42, Lit7);
    }

    public Object Button12$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit9), Lit200);
    }

    static Object lambda60() {
        C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit23, Lit204, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit43, Lit73, Lit7);
    }

    static Object lambda61() {
        C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit23, Lit204, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit203, Lit43, Lit73, Lit7);
    }

    static Object lambda62() {
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit23, Lit208, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit33, "Volume 4", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit35, Lit209, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit43, Lit42, Lit7);
    }

    static Object lambda63() {
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit23, Lit208, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit33, "Volume 4", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit35, Lit209, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit207, Lit43, Lit42, Lit7);
    }

    public Object Button13$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit108), Lit211);
    }

    static Object lambda64() {
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit23, Lit215, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit33, "Stop", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit35, Lit216, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit43, Lit42, Lit7);
    }

    static Object lambda65() {
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit23, Lit215, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit33, "Stop", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit35, Lit216, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit214, Lit43, Lit42, Lit7);
    }

    public Object Button16$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit116), Lit218);
    }

    static Object lambda66() {
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit23, Lit222, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit33, "Volume 5", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit35, Lit223, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit43, Lit42, Lit7);
    }

    static Object lambda67() {
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit23, Lit222, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit28, Boolean.TRUE, Lit16);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit33, "Volume 5", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit35, Lit223, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit221, Lit43, Lit42, Lit7);
    }

    public Object Button14$Click() {
        C0609runtime.setThisForm();
        return C0609runtime.callComponentMethod(Lit80, Lit81, LList.list2("S2", Lit124), Lit225);
    }

    static Object lambda68() {
        C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit23, Lit229, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit43, Lit42, Lit7);
    }

    static Object lambda69() {
        C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit8, Lit9, Lit7);
        C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit23, Lit229, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit228, Lit43, Lit42, Lit7);
    }

    static Object lambda70() {
        C0609runtime.setAndCoerceProperty$Ex(Lit232, Lit23, Lit233, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit232, Lit41, Lit234, Lit7);
    }

    static Object lambda71() {
        C0609runtime.setAndCoerceProperty$Ex(Lit232, Lit23, Lit233, Lit7);
        return C0609runtime.setAndCoerceProperty$Ex(Lit232, Lit41, Lit234, Lit7);
    }

    static Object lambda72() {
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit237, "https://dazzling-fire-7140.firebaseio.com/", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit238, "shoaibmahmuds06@gmail:com/", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit239, "zen9z1C4ff09yvZz08Pejo9zRPbWwqk4ebsoXSFD", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit240, "https://shockwave-1a259.firebaseio.com/", Lit4);
        return C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit241, "Shock_Wave", Lit4);
    }

    static Object lambda73() {
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit237, "https://dazzling-fire-7140.firebaseio.com/", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit238, "shoaibmahmuds06@gmail:com/", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit239, "zen9z1C4ff09yvZz08Pejo9zRPbWwqk4ebsoXSFD", Lit4);
        C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit240, "https://shockwave-1a259.firebaseio.com/", Lit4);
        return C0609runtime.setAndCoerceProperty$Ex(Lit80, Lit241, "Shock_Wave", Lit4);
    }

    public String getSimpleName(Object object) {
        return object.getClass().getSimpleName();
    }

    public void onCreate(Bundle icicle) {
        AppInventorCompatActivity.setClassicModeFromYail(true);
        super.onCreate(icicle);
    }

    public void androidLogForm(Object message) {
    }

    public void addToFormEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.form$Mnenvironment, object));
        this.form$Mnenvironment.put(name, object);
    }

    public Object lookupInFormEnvironment(Symbol name, Object default$Mnvalue) {
        boolean x = ((this.form$Mnenvironment == null ? 1 : 0) + 1) & true;
        if (x) {
            if (!this.form$Mnenvironment.isBound(name)) {
                return default$Mnvalue;
            }
        } else if (!x) {
            return default$Mnvalue;
        }
        return this.form$Mnenvironment.get(name);
    }

    public boolean isBoundInFormEnvironment(Symbol name) {
        return this.form$Mnenvironment.isBound(name);
    }

    public void addToGlobalVarEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.global$Mnvar$Mnenvironment, object));
        this.global$Mnvar$Mnenvironment.put(name, object);
    }

    public void addToEvents(Object component$Mnname, Object event$Mnname) {
        this.events$Mnto$Mnregister = C0621lists.cons(C0621lists.cons(component$Mnname, event$Mnname), this.events$Mnto$Mnregister);
    }

    public void addToComponents(Object container$Mnname, Object component$Mntype, Object component$Mnname, Object init$Mnthunk) {
        this.components$Mnto$Mncreate = C0621lists.cons(LList.list4(container$Mnname, component$Mntype, component$Mnname, init$Mnthunk), this.components$Mnto$Mncreate);
    }

    public void addToGlobalVars(Object var, Object val$Mnthunk) {
        this.global$Mnvars$Mnto$Mncreate = C0621lists.cons(LList.list2(var, val$Mnthunk), this.global$Mnvars$Mnto$Mncreate);
    }

    public void addToFormDoAfterCreation(Object thunk) {
        this.form$Mndo$Mnafter$Mncreation = C0621lists.cons(thunk, this.form$Mndo$Mnafter$Mncreation);
    }

    public void sendError(Object error) {
        RetValManager.sendError(error == null ? null : error.toString());
    }

    public void processException(Object ex) {
        Object apply1 = Scheme.applyToArgs.apply1(GetNamedPart.getNamedPart.apply2(ex, Lit1));
        RuntimeErrorAlert.alert(this, apply1 == null ? null : apply1.toString(), ex instanceof YailRuntimeError ? ((YailRuntimeError) ex).getErrorType() : "Runtime Error", "End Application");
    }

    public boolean dispatchEvent(Component componentObject, String registeredComponentName, String eventName, Object[] args) {
        boolean x;
        SimpleSymbol registeredObject = misc.string$To$Symbol(registeredComponentName);
        if (!isBoundInFormEnvironment(registeredObject)) {
            EventDispatcher.unregisterEventForDelegation(this, registeredComponentName, eventName);
            return false;
        } else if (lookupInFormEnvironment(registeredObject) != componentObject) {
            return false;
        } else {
            try {
                Scheme.apply.apply2(lookupHandler(registeredComponentName, eventName), LList.makeList(args, 0));
                return true;
            } catch (PermissionException exception) {
                exception.printStackTrace();
                if (this == componentObject) {
                    x = true;
                } else {
                    x = false;
                }
                if (!x ? x : IsEqual.apply(eventName, "PermissionNeeded")) {
                    processException(exception);
                } else {
                    PermissionDenied(componentObject, eventName, exception.getPermissionNeeded());
                }
                return false;
            } catch (Throwable exception2) {
                androidLogForm(exception2.getMessage());
                exception2.printStackTrace();
                processException(exception2);
                return false;
            }
        }
    }

    public void dispatchGenericEvent(Component componentObject, String eventName, boolean notAlreadyHandled, Object[] args) {
        Boolean bool;
        boolean x = true;
        Object handler = lookupInFormEnvironment(misc.string$To$Symbol(strings.stringAppend("any$", getSimpleName(componentObject), "$", eventName)));
        if (handler != Boolean.FALSE) {
            try {
                Apply apply = Scheme.apply;
                if (notAlreadyHandled) {
                    bool = Boolean.TRUE;
                } else {
                    bool = Boolean.FALSE;
                }
                apply.apply2(handler, C0621lists.cons(componentObject, C0621lists.cons(bool, LList.makeList(args, 0))));
            } catch (PermissionException exception) {
                exception.printStackTrace();
                if (this != componentObject) {
                    x = false;
                }
                if (!x ? x : IsEqual.apply(eventName, "PermissionNeeded")) {
                    processException(exception);
                } else {
                    PermissionDenied(componentObject, eventName, exception.getPermissionNeeded());
                }
            } catch (Throwable exception2) {
                androidLogForm(exception2.getMessage());
                exception2.printStackTrace();
                processException(exception2);
            }
        }
    }

    public Object lookupHandler(Object componentName, Object eventName) {
        String str = null;
        String obj = componentName == null ? null : componentName.toString();
        if (eventName != null) {
            str = eventName.toString();
        }
        return lookupInFormEnvironment(misc.string$To$Symbol(EventDispatcher.makeFullEventName(obj, str)));
    }

    public void $define() {
        Object reverse;
        Object obj;
        Object reverse2;
        Object obj2;
        Object obj3;
        Object var;
        Object component$Mnname;
        Object obj4;
        Language.setDefaults(Scheme.getInstance());
        try {
            run();
        } catch (Exception exception) {
            androidLogForm(exception.getMessage());
            processException(exception);
        }
        Screen1 = this;
        addToFormEnvironment(Lit0, this);
        Object obj5 = this.events$Mnto$Mnregister;
        while (obj5 != LList.Empty) {
            try {
                Pair arg0 = (Pair) obj5;
                Object event$Mninfo = arg0.getCar();
                Object apply1 = C0621lists.car.apply1(event$Mninfo);
                String obj6 = apply1 == null ? null : apply1.toString();
                Object apply12 = C0621lists.cdr.apply1(event$Mninfo);
                EventDispatcher.registerEventForDelegation(this, obj6, apply12 == null ? null : apply12.toString());
                obj5 = arg0.getCdr();
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, obj5);
            }
        }
        try {
            LList components = C0621lists.reverse(this.components$Mnto$Mncreate);
            addToGlobalVars(Lit2, lambda$Fn1);
            reverse = C0621lists.reverse(this.form$Mndo$Mnafter$Mncreation);
            while (reverse != LList.Empty) {
                Pair arg02 = (Pair) reverse;
                misc.force(arg02.getCar());
                reverse = arg02.getCdr();
            }
            obj = components;
            while (obj != LList.Empty) {
                Pair arg03 = (Pair) obj;
                Object component$Mninfo = arg03.getCar();
                component$Mnname = C0621lists.caddr.apply1(component$Mninfo);
                C0621lists.cadddr.apply1(component$Mninfo);
                Object component$Mnobject = Invoke.make.apply2(C0621lists.cadr.apply1(component$Mninfo), lookupInFormEnvironment((Symbol) C0621lists.car.apply1(component$Mninfo)));
                SlotSet.set$Mnfield$Ex.apply3(this, component$Mnname, component$Mnobject);
                addToFormEnvironment((Symbol) component$Mnname, component$Mnobject);
                obj = arg03.getCdr();
            }
            reverse2 = C0621lists.reverse(this.global$Mnvars$Mnto$Mncreate);
            while (reverse2 != LList.Empty) {
                Pair arg04 = (Pair) reverse2;
                Object var$Mnval = arg04.getCar();
                var = C0621lists.car.apply1(var$Mnval);
                addToGlobalVarEnvironment((Symbol) var, Scheme.applyToArgs.apply1(C0621lists.cadr.apply1(var$Mnval)));
                reverse2 = arg04.getCdr();
            }
            LList component$Mndescriptors = components;
            obj2 = component$Mndescriptors;
            while (obj2 != LList.Empty) {
                Pair arg05 = (Pair) obj2;
                Object component$Mninfo2 = arg05.getCar();
                C0621lists.caddr.apply1(component$Mninfo2);
                Object init$Mnthunk = C0621lists.cadddr.apply1(component$Mninfo2);
                if (init$Mnthunk != Boolean.FALSE) {
                    Scheme.applyToArgs.apply1(init$Mnthunk);
                }
                obj2 = arg05.getCdr();
            }
            obj3 = component$Mndescriptors;
            while (obj3 != LList.Empty) {
                Pair arg06 = (Pair) obj3;
                Object component$Mninfo3 = arg06.getCar();
                Object component$Mnname2 = C0621lists.caddr.apply1(component$Mninfo3);
                C0621lists.cadddr.apply1(component$Mninfo3);
                callInitialize(SlotGet.field.apply2(this, component$Mnname2));
                obj3 = arg06.getCdr();
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "arg0", -2, obj3);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "arg0", -2, obj2);
        } catch (ClassCastException e4) {
            throw new WrongType(e4, "add-to-global-var-environment", 0, var);
        } catch (ClassCastException e5) {
            throw new WrongType(e5, "arg0", -2, reverse2);
        } catch (ClassCastException e6) {
            throw new WrongType(e6, "add-to-form-environment", 0, component$Mnname);
        } catch (ClassCastException e7) {
            throw new WrongType(e7, "lookup-in-form-environment", 0, obj4);
        } catch (ClassCastException e8) {
            throw new WrongType(e8, "arg0", -2, obj);
        } catch (ClassCastException e9) {
            throw new WrongType(e9, "arg0", -2, reverse);
        } catch (YailRuntimeError exception2) {
            processException(exception2);
        }
    }

    public static SimpleSymbol lambda1symbolAppend$V(Object[] argsArray) {
        LList symbols = LList.makeList(argsArray, 0);
        Apply apply = Scheme.apply;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Object obj = LList.Empty;
        LList lList = symbols;
        while (lList != LList.Empty) {
            try {
                Pair arg0 = (Pair) lList;
                Object arg02 = arg0.getCdr();
                Object car = arg0.getCar();
                try {
                    obj = Pair.make(misc.symbol$To$String((Symbol) car), obj);
                    lList = arg02;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "symbol->string", 1, car);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, lList);
            }
        }
        Object apply2 = apply.apply2(moduleMethod, LList.reverseInPlace(obj));
        try {
            return misc.string$To$Symbol((CharSequence) apply2);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "string->symbol", 1, apply2);
        }
    }

    static Object lambda2() {
        return null;
    }
}
