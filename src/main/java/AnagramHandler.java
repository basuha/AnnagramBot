import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class AnagramHandler {
    private static final int ANAGRAM_LIMIT = 3;
    private Queue<String> anagramQueue = new LinkedBlockingQueue<>(ANAGRAM_LIMIT);

    private static final List<String> dictionary = new ArrayList<>();
    private static final String DICTIONARY_FILE = "src/main/resources/rus_comm_noun.txt";

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

    public String get() {
        anagramQueue.add(dictionary.get(new Random().nextInt(dictionary.size())));
        return anagramize(anagramQueue.peek());
    }

    private static String anagramize(String word) {
        List<Character> anagram = new ArrayList<>();

        for (int i = 0; i < word.length(); i++)
            anagram.add(null);

        for (Character c : word.toLowerCase().toCharArray()) {
            int index;
            do {
                index = new Random().nextInt(word.length());
            } while (anagram.get(index) != null);
            anagram.set(index, c);
        }
        return StringUtils.capitalize(Joiner.on("").join(anagram));
    }

    public static void main(String[] args) {

    }
}
