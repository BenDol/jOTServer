package org.jotserver.ot.model.event;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JavaScriptEnvironment extends ScriptableObject {

    private String basePath;

    public JavaScriptEnvironment(String basePath) {
        this.basePath = basePath;
        init();
    }

    public void init() {
        defineFunctionProperties(new String[] {"load"}, JavaScriptEnvironment.class, ScriptableObject.DONTENUM);
    }

    @Override
    public String getClassName() {
        return "global";
    }

    public static void load(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
        Scriptable scope = thisObj;
        while (!(scope instanceof JavaScriptEnvironment)) {
            scope = scope.getParentScope();
            if (scope == null) {
                throw new IllegalStateException("Module environment not found.");
            }
        }
        JavaScriptEnvironment env = (JavaScriptEnvironment) scope;
        for (Object arg : args) {
            String fileName = arg.toString();
            File file = new File(env.basePath + File.separator + fileName);
            cx.evaluateReader(thisObj, new FileReader(file), file.getAbsolutePath(), 1, null);
        }
    }
}
