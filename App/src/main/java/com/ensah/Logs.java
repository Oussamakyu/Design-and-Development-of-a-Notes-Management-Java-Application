package com.ensah;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Oussama BENABBOU
 * I've designed this class to be thread-safe
 *
 * **/
public class Logs {
    private final static Logger LOG = Logger.getLogger("Logs");

    static {
        // Set the logging level to INFO
        LOG.setLevel(Level.INFO);

        // Create a console handler and set its formatter
        ConsoleHandler handler = new ConsoleHandler();
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        LOG.addHandler(handler);
    }

    public static void info(String msg, String iduser) {
        LOG.log(Level.INFO, msg, new Object[]{iduser});
    }

    public static void warning(String msg, String iduser) {
        LOG.log(Level.WARNING, msg, new Object[]{iduser});
    }

    public static void severe(String msg, String iduser) {
        LOG.log(Level.SEVERE, msg, new Object[]{iduser});
    }

    private static class MyFormatter extends SimpleFormatter {
        private static final String FORMAT = "[%1$tF %1$tT] [%2$-7s] %3$s (User: %4$s)%n";

        @Override
        public synchronized String format(LogRecord record) {
            return String.format(FORMAT,
                    record.getMillis(),
                    record.getLevel().getLocalizedName(),
                    formatMessage(record),
                    record.getParameters()[0]
            );

        }
    }
}

