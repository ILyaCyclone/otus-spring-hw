package cyclone.otusspring.hw01.dao;

import cyclone.otusspring.hw01.ConsolePollMain;
import cyclone.otusspring.hw01.model.Question;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Get poll data from CSV file with provided {@code filename}.
 * By default CSV separator is "," symbol and lines starting with "#" are treated as comments.
 * Customize this behavior using constructor with additional {@code csvSeparator} and {@code csvComment} parameters.
 * Separator may be escaped using "\" symbol.
 */
public class CsvPollDao implements PollDao {
    private String csvSeparator = ",";
    private String csvComment = "#";

    private final List<Question> questions;

    public CsvPollDao(String filename) {
        // csv file is in classpath and shouldn't change on runtime, so read it right away
        this.questions = readCsvQuestions(filename);
    }

    public CsvPollDao(String filename, String csvSeparator, String csvComment) {
        this.csvSeparator = csvSeparator;
        this.csvComment = csvComment;
        this.questions = readCsvQuestions(filename);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    private List<Question> readCsvQuestions(String filename) {
        URL pollFileURL = ConsolePollMain.class.getResource(filename);
        try (Stream<String> stream = Files.lines(Paths.get(pollFileURL.toURI()))) {
            return stream
                    .filter(line -> !line.startsWith(csvComment)) // skip comments
                    .map(line -> {
//                        String[] parts = line.split("(?<!\\\\)" + csvSeparator); // separator not trailed by slash
                        String[] parts = line.split("(?<!\\\\)" + Pattern.quote(csvSeparator)); // separator not trailed by slash
                        String text = parts[0].replace("\\" + csvSeparator, csvSeparator); // unescape comma
                        String answer = parts[1];
                        String[] variants = Arrays.copyOfRange(parts, 2, parts.length);

                        return new Question(text, answer, variants);
                    })
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read questions from CSV file", e);
        }
    }
}
