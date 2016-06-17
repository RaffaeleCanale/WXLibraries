package com.wx.io.save;

import com.wx.crypto.CryptoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an error handler that will be triggered when a {@link SaveableConfig}
 * encounters an error while saving.
 * <br><br>
 * This class also provides static methods to allow class handlers (ie: handlers
 * that will be automatically linked to any of its instances), but it is
 * preferable to use object handlers instead.
 * @author Canale
 */
public abstract class SaveHandler {
    private static final Map<String, SaveHandler> handlers = new HashMap<>();

    public static void addHandler(Class<?> targetClass, 
            SaveHandler handler) {
        handlers.put(targetClass.getName(), handler);
    }
    
    public static SaveHandler getHandler(Class<?> targetClass) {
        return handlers.get(targetClass.getName());
    }
    
    public abstract void handle(SaveableConfig config, FileNotFoundException ex);
    public abstract void handle(SaveableConfig config, IOException ex);
    public abstract void handle(SaveableConfig config, CryptoException ex);
    
}
