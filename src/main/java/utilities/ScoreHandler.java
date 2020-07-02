package utilities;

import org.telegram.telegrambots.meta.api.objects.Update;
import pojo.BotUser;
import repository.BotUserRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreHandler extends AbstractHandler {

    private static final String LOCAL_MESSAGE = "Рейтинговая таблица чата ";
    private static final String OVERALL_MESSAGE = "Общая рейтинговая таблица ";
    private static final String BOT_NAME = "@AnnagramBot";
    private static final String STAR_EMOJI = "\u2b50\ufe0f";
    private static final String LOCK_EMOJI = "\ud83d\udd12";
    private static final String SUPER_STAR_EMOJI = "\ud83c\udf1f";
    private static final int TABLE_LIMIT = 10;
    private StringBuilder scores;

    public ScoreHandler(long chatID) {
        super(chatID);
    }

    public ScoreHandler() {
    }

    public String showLocalScores(Update update) {
        initUpdate(update);
        scores = new StringBuilder(LOCAL_MESSAGE + OP_B_TAG + chatName + CL_B_TAG + NEXT_LINE);
        List<BotUser> botUsers = BotUserRepository.getByChatID(chatID);
        botUsers.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
        generateTable(botUsers);
        return scores.toString();
    }

    public String showOverallScores() {
        scores = new StringBuilder(OVERALL_MESSAGE + OP_B_TAG + BOT_NAME + CL_B_TAG + NEXT_LINE);
        List<BotUser> botUsers = BotUserRepository
                .getAll()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        for (int i = 0; i < botUsers.size(); i++) {
            var user = botUsers.get(i);
            user.setScore((int) BotUserRepository.getSumScoresOfUser(user.getUserID()));
        }
        botUsers.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
        generateTable(botUsers);
        return scores.toString();
    }

    private void generateTable(List<BotUser> botUsers) {
        int place = 0;
        for (BotUser user : botUsers) {
            scores.append(OP_CODE_TAG)
                    .append(++place)
                    .append(". ")
                    .append(user.getUserName() == null ? LOCK_EMOJI : user.getUserName())
                    .append(" - ")
                    .append(user.getScore())
                    .append(" ")
                    .append("points ")
                    .append(user.getScore() < 10000
                            ? STAR_EMOJI.repeat(user.getScore() / 1000)
                            : SUPER_STAR_EMOJI.repeat(user.getScore() / 10000))
                    .append(CL_CODE_TAG)
                    .append(NEXT_LINE);
            if (place == TABLE_LIMIT)
                break;
        }
    }
}
