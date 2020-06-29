package utilities.loggers;

import app.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateLogger extends AbstractLogger {

    public static void log(Update update) {
        log.info("Receive new Update -> updateID: " + update.getUpdateId()
                + " Text: " + update.getMessage().getText()
                + " From User: " + update.getMessage().getFrom().getUserName()
                + " From Chat: " + update.getMessage().getChat().getTitle()
                + " ChatID: " + update.getMessage().getChat().getId());
    }
}
