package utilities;

import app.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;
import pojo.BotUser;
import repository.BotUserRepository;

public class ScoreHandler extends AbstractHandler {

    private static final String MESSAGE = "Рейтинговая таблица чата ";

    public ScoreHandler(long chatID) {
        super(chatID);
    }

    public String showLocalScores(Update update) {
        initUpdate(update);

        StringBuilder localScores = new StringBuilder(MESSAGE + OP_B_TAG + chatName + CL_B_TAG + NEXT_LINE);
        int place = 0;
        for (BotUser user : BotUserRepository.getByChatID(chatID)) {
            localScores.append(OP_CODE_TAG)
                    .append(++place)
                    .append(". ")
                    .append(user.getUserName())
                    .append(" - ")
                    .append(user.getScore())
                    .append(" ")
                    .append("points")
                    .append(CL_CODE_TAG)
                    .append(NEXT_LINE);

        }

        return localScores.toString();
    }

//    public String showOverallScores() {
//
//    }
}
