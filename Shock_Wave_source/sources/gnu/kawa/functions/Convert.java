package gnu.kawa.functions;

import gnu.bytecode.Type;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure2;

public class Convert extends Procedure2 {

    /* renamed from: as */
    public static final Convert f45as = new Convert();

    static {
        f45as.setName("as");
        f45as.setProperty(Procedure.validateApplyKey, "gnu.kawa.functions.CompileMisc:validateApplyConvert");
        Procedure.compilerKey.set(f45as, "*gnu.kawa.functions.CompileMisc:forConvert");
    }

    public static Convert getInstance() {
        return f45as;
    }

    public Object apply2(Object arg1, Object arg2) {
        Type type;
        if (arg1 instanceof Class) {
            type = Type.make((Class) arg1);
        } else {
            type = (Type) arg1;
        }
        return type.coerceFromObject(arg2);
    }
}
