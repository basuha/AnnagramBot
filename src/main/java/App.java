import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;

public class App {
    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        log.trace("test");
        ApiContextInitializer.init();
        Bot annagramBot = new Bot("AnnagramBot", "1268361427:AAEBI07yEkQ5PbDkczeq0V9stcZ0GmRe5-E");
        annagramBot.botConnect();
    }
}
