package utilities;

import org.telegram.telegrambots.meta.api.objects.Update;
import pojo.BotUser;
import repository.BotUserRepository;

import java.util.List;

public class ScoreHandler extends AbstractHandler {

    private static final String LOCAL_MESSAGE = "Рейтинговая таблица чата ";
    private static final String OVERALL_MESSAGE = "Общая рейтинговая таблица ";
    private static final String BOT_NAME = "@AnnagramBot";
    private static final int TABLE_LIMIT = 10;

    public ScoreHandler(long chatID) {
        super(chatID);
    }

    public ScoreHandler() {
    }

    public String showLocalScores(Update update) {
        initUpdate(update);

        StringBuilder localScores = new StringBuilder(LOCAL_MESSAGE + OP_B_TAG + chatName + CL_B_TAG + NEXT_LINE);
        int place = 0;
        List<BotUser> botUsers = BotUserRepository.getByChatID(chatID);

        botUsers.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));

        for (BotUser user : botUsers) {
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
            if (place == TABLE_LIMIT)
                break;
        }
        return localScores.toString();
    }

    public String showOverallScores() {

        StringBuilder overallScores = new StringBuilder(OVERALL_MESSAGE + OP_B_TAG + BOT_NAME + CL_B_TAG + NEXT_LINE);
        int place = 0;
        List<BotUser> botUsers = BotUserRepository.getAll();

        botUsers.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));

        for (BotUser user : botUsers) {
            overallScores.append(OP_CODE_TAG)
                    .append(++place)
                    .append(". ")
                    .append(user.getUserName())
                    .append(" - ")
                    .append(user.getScore())
                    .append(" ")
                    .append("points")
                    .append(CL_CODE_TAG)
                    .append(NEXT_LINE);
            if (place == TABLE_LIMIT)
                break;
        }
        return overallScores.toString();
    }
}
