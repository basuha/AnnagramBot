package utilities.loggers;

import app.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractLogger {
    protected static final Logger log = Logger.getLogger(Bot.class);

    public static void log(Update update) {}
}
