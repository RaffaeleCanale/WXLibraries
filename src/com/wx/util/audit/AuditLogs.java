package com.wx.util.audit;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Canale
 */
public class AuditLogs {
    
    private final List<String> infos = new LinkedList<>();
    private final List<String> warnings = new LinkedList<>();
    private final List<String> errors = new LinkedList<>();
    
    public void info(String message) {
        infos.add(message);
    }
    
    public void warning(String message) {
        warnings.add(message);
    }
    
    public void error(String message) {
        errors.add(message);
    }

    public List<String> getInfos() {
        return infos;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }
    
    public void stopped(Object ob) {
        info("Audit stopped at " + ob.getClass().getName());
    }
    
    public List<String> getAllMessages() {
        List<String> allMessages = new LinkedList<>(infos);
        allMessages.addAll(warnings);
        allMessages.addAll(errors);
        
        return allMessages;
    }
}
