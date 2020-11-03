package com.dermacon.securewebapp.logger;


import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerSingleton {

    private static Logger logger;

    private static final Level LOGGING_lEVEL = Level.ALL;
    private static final String FILE_NAME = "flat-organizer.log";
    private static final String LOGGER_NAME = "flat-organizer.log";

    public static Logger getInstance() {
        if (logger == null) {
            logger = init();
        }
        return logger;
    }

    /**
     * https://de.wikibooks.org/wiki/Muster:_Java:_Singleton
     * https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
     * @return
     */
    private static Logger init() {
        Logger new_logger = Logger.getLogger(LOGGER_NAME);
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler(FILE_NAME);
            new_logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            new_logger.setLevel(LOGGING_lEVEL);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new_logger;
    }
}
