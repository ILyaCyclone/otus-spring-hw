package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Question;
import org.springframework.beans.factory.annotation.Value;

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
public class CsvQuestionService implements QuestionService {
    private String csvSeparator = ",";
    private String csvComment = "#";

    private final List<Question> questions;

    public CsvQuestionService(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language
    ) {
        String localizedFilename = getLocalizedFilename(filenameBase, filenameExtension, language);
        // csv file is in classpath and shouldn't change on runtime, so read it right away
        this.questions = readCsvQuestions(localizedFilename);
    }

    String getLocalizedFilename(String filenameBase, String extension, String language) {
        return filenameBase + "_" + language + "." + extension;
    }

    public CsvQuestionService(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language
            , String csvSeparator
            , String csvComment) {
        this.csvSeparator = csvSeparator;
        this.csvComment = csvComment;

        String localizedFilename = getLocalizedFilename(filenameBase, filenameExtension, language);
        // csv file is in classpath and shouldn't change on runtime, so read it right away
        this.questions = readCsvQuestions(localizedFilename);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    private List<Question> readCsvQuestions(String filename) {
        URL pollFileURL = this.getClass().getResource(filename);
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
