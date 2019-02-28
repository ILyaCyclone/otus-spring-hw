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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvPollDao implements PollDao {
    private static final String CSV_SEPARATOR = ",";
    private static final String SCV_FILE_PATH = "/poll.csv";

    private List<Question> questions;

    public CsvPollDao() {
        // csv file is in classpath and shouldn't change on runtime, so read it right away
        this.questions = readCsvQuestions(SCV_FILE_PATH);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    private List<Question> readCsvQuestions(String filename) {
        URL pollFileURL = ConsolePollMain.class.getResource(filename);
        try (Stream<String> stream = Files.lines(Paths.get(pollFileURL.toURI()))) {
            return stream.map(line -> {
                String[] parts = line.split("(?<!\\\\)" + CSV_SEPARATOR); // comma not trailed by slash
                String text = parts[0].replace("\\" + CSV_SEPARATOR, CSV_SEPARATOR); // unescape comma
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
