package utilities.loggers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageLogger extends AbstractLogger {
    public static void log(Update update) {
        log.info("Message from: " + update.getMessage().getFrom().getUserName()
                + "(id: " + update.getMessage().getFrom().getId() + ")"
                + " text: " + update.getMessage().getText());
    }
}
