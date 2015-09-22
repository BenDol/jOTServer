package org.jotserver.ot.model.event;

import org.apache.log4j.Logger;
import org.jotserver.ot.model.chat.ChatChannelEventEngine;
import org.jotserver.ot.model.item.ItemEventEngine;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.mozilla.javascript.*;

import java.io.*;

public class JavaScriptEventEngine implements EventEngine {
    private static final Logger logger = Logger.getLogger(JavaScriptEventEngine.class);

    private String basePath;
    private ItemEventEngine itemEngine;
    private ChatChannelEventEngine chatEngine;

    public JavaScriptEventEngine(String basePath) {
        if(!new File(basePath).exists()) {
            throw new IllegalArgumentException("Base path does not exist. (" + basePath + ")");
        }
        this.basePath = basePath;
    }

    public ItemEventEngine getItemEngine() {
        return itemEngine;
    }

    public ChatChannelEventEngine getChatEngine() {
        return chatEngine;
    }

    public void init(final LocalGameWorld world) {
        itemEngine = new ItemEventEngine();
        chatEngine = new ChatChannelEventEngine(world);

        Context cx = ContextFactory.getGlobal().enterContext();

        ModuleContext context = new ModuleContext() {
                public EventEngine getEventEngine() {
                    return JavaScriptEventEngine.this;
                }
                public LocalGameWorld getWorld() {
                    return world;
                }
            };

        String moduleList = world.getConfiguration().getString("events.javascript.modules").trim();
        String[] modules = moduleList.split(",");
        for(int i = 0; i < modules.length; i++) {
            modules[i] = modules[i].trim();
        }

        Scriptable globalScope = cx.initStandardObjects();
        String libInit = new File(this.basePath).getAbsolutePath() + File.separator + "lib.js";
        File file = new File(libInit);
        try {
            cx.evaluateReader(globalScope, new FileReader(file), file.getAbsolutePath(), 1, null);
        } catch (FileNotFoundException e1) {
            logger.error("Could not find library initialization file (" + file.getAbsolutePath() + ").", e1);
        } catch (IOException e1) {
            logger.error("Failed to load library initialization file (" + file.getAbsolutePath() + ").", e1);
        }

        for(String module : modules) {
            if(!module.isEmpty()) {
                try {
                    boolean ret = initModule(cx, globalScope, context, module);
                    if(!ret) {
                        logger.error("Failed to initialize module " + module + ".");
                    } else {
                        logger.info("Module " + module + " loaded.");
                    }
                } catch(IOException e) {
                    logger.error("Exception while loading module.", e);
                }
            }
        }
        Context.exit();
    }

    private boolean initModule(Context cx, Scriptable globalScope, ModuleContext context, String module) throws IOException {
        String basePath = new File(this.basePath).getAbsolutePath() + File.separator + module;
        File file = new File(basePath + File.separator + "init.js");
        if(file.exists()) {
            Reader reader = new FileReader(file);
            ScriptableObject moduleScope = new JavaScriptEnvironment(basePath);
            moduleScope.setParentScope(globalScope);
            cx.evaluateReader(moduleScope, reader, file.getAbsolutePath(), 1, null);

            Object initObject = moduleScope.get("init", moduleScope);
            if(initObject != null && initObject instanceof Function) {
                Function init = (Function)initObject;
                Object ret = init.call(cx, moduleScope, moduleScope, new Object[] { context });
                if(ret instanceof Boolean) {
                    return (Boolean)ret;
                } else {
                    return true;
                }
            } else {
                throw new IOException("Module init function not found.");
            }
        } else {
            throw new IOException("Module init file not found. Expected filename: " + file.getAbsolutePath() + ".");
        }
    }
}
