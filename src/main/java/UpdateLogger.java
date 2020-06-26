import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateLogger {
    private static final Logger log = Logger.getLogger(Bot.class);

    public static void log(Update update) {
        log.info("Receive new Update -> updateID: " + update.getUpdateId()
                + " Text: " + update.getMessage().getText()
                + " From User: " + update.getMessage().getFrom().getUserName()
                + " From Chat: " + update.getMessage().getChat().getTitle());
    }
}
