/**
 * 
 */
package com.cloud.utils;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Journal is used to kept what has happened during a process so someone can track 
 * what happens during a process.
 *
 */
public class Journal {
    String _name;
    ArrayList<Pair<String, Object[]>> _entries;
    
    public Journal(String name) {
        _name = name;
        _entries = new ArrayList<Pair<String, Object[]>>();
    }
    
    
    final private void log(String msg, Object... params) {
        Pair<String, Object[]> entry = new Pair<String, Object[]>(msg, params);
        assert msg != null : "Message can not be null or else it's useless!";
        _entries.add(entry);
    }
    
    public void record(String msg, Object... params) {
        log(msg, params);
    }
    
    public void record(Logger logger, Level p, String msg, Object... params) {
        if (logger.isEnabledFor(p)) {
            StringBuilder buf = new StringBuilder();
            toString(buf, msg, params);
            String entry = buf.toString();
            log(entry);
            logger.log(p, entry);
        } else {
            log(msg, params);
        }
    }
    
    protected void toString(StringBuilder buf, String msg, Object[] params) {
        buf.append(msg);
        if (params != null) {
            buf.append(" - ");
            int i = 0;
            for (Object obj : params) {
                buf.append('P').append(i).append('=');
                buf.append(obj != null ? obj.toString() : "null");
                buf.append(", ");
            }
            buf.delete(buf.length() - 2, buf.length());
        }
    }
    
    public String toString(String separator) {
        StringBuilder buf = new StringBuilder(_name).append(": ");
        for (Pair<String, Object[]> entry : _entries) {
            toString(buf, entry.first(), entry.second());
            buf.append(separator);
        }
        return buf.toString();
    }
    
    @Override
    public String toString() {
        return toString("; ");
    }
    
    public static class LogJournal extends Journal {
        Logger _logger;
        public LogJournal(String name, Logger logger) {
            super(name);
            _logger = logger;
        }
        
        @Override
        public void record(String msg, Object... params) {
            record(_logger, Level.DEBUG, msg, params);
        }
    }
}
