import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AnagramHandler {
    private static final Logger log = Logger.getLogger(AnagramHandler.class);

    private static final int ANAGRAM_LIMIT = 5;
    private Map<String, String> anagramMap = new LinkedHashMap<>();

    private static final List<String> dictionary = new ArrayList<>();
    private static final String DICTIONARY_FILE = "src/main/resources/rus_comm_noun.txt";

    private static final String MESSAGE = "Угадайте следующие слова: \n ";
    private static final String OP_B_TAG = "<b>";
    private static final String CL_B_TAG = "</b>";

    private String chatID;
    private String inputText;

    private Bot bot;

    static {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(DICTIONARY_FILE));
            String temp;
            do {
                temp = reader.readLine();
                if (temp == null) break;
                dictionary.add(temp);
            } while (true);
        } catch (IOException e) {
            System.err.println("File " + DICTIONARY_FILE + "Not found");
        }
    }

    public AnagramHandler(Bot bot) {
        this.bot = bot;
        refresh();
    }

    public void process(Update update) {

    }

    public void get() {
        log.info(anagramMap.size());
        String nextWord = dictionary.get(new Random().nextInt(dictionary.size()));
        anagramMap.put(nextWord, anagramize(nextWord));
        log.info(anagramMap);
    }

    public String guess(String message, User user) {
        for (String w : message.toLowerCase()
                .replaceAll("([?!:;,.])", "")
                .replaceAll("([\\-`_])", " ")
                .split(" ")) {
            if (anagramMap.containsKey(w)) {
                anagramMap.remove(w);
                return anagramMap.get(w) + " - угадано! Победитель " + user.getUserName();
            }
        }
        return null;
    }

    private void refresh() {
        for (int i = anagramMap.size(); i < ANAGRAM_LIMIT; i++)
            get();
    }

    private static String anagramize(String word) {
        List<Character> anagram = new ArrayList<>();
        String request;

        for (int i = 0; i < word.length(); i++)
            anagram.add(null);

        do {
            for (Character c : word.toLowerCase().toCharArray()) {
                int index;
                do {
                    index = new Random().nextInt(word.length());
                } while (anagram.get(index) != null);
                anagram.set(index, c);
            }
            request = Joiner.on("").join(anagram);
        } while (request.equals(word));

        return StringUtils.capitalize(request);
    }

    public String getMessage() {
        StringBuilder message = new StringBuilder(MESSAGE + OP_B_TAG);

        for (Map.Entry<String, String> m : anagramMap.entrySet()) {
            message.append(m.getValue())
                    .append(" ");
        }
        message.append(CL_B_TAG);

        return message.toString();
    }

    public static void main(String[] args) {

    }
}
