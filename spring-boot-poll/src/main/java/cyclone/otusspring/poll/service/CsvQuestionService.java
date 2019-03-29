package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.config.CsvProperties;
import cyclone.otusspring.poll.model.Question;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
 * Get poll data from CSV file according to {@code CsvProperties}.
 * By default CSV separator is "," symbol and lines starting with "#" are treated as comments.
 * Customize this behavior by providing {@code csvSeparator} or {@code csvComment} as CsvProperties or using setters.
 * Separator may be escaped using "\" symbol.
 */
@Service
public class CsvQuestionService implements QuestionService {
    private static final String DEFAULT_CSV_SEPARATOR = ",";
    private static final String DEFAULT_CSV_COMMENT = "#";

    private final CsvProperties csvProperties;

    private String csvSeparator = DEFAULT_CSV_SEPARATOR;
    private String csvComment = DEFAULT_CSV_COMMENT;

    public CsvQuestionService(CsvProperties csvProperties) {
        this.csvProperties = csvProperties;

        if (StringUtils.hasText(csvProperties.getSeparator())) {
            this.csvSeparator = csvProperties.getSeparator();
        }
        if (StringUtils.hasText(csvProperties.getComment())) {
            this.csvComment = csvProperties.getComment();
        }
    }



    public List<Question> getQuestions() {
        String localizedFilename = getLocalizedFilename(csvProperties.getBasename(), csvProperties.getLocale());
        return readCsvQuestions(localizedFilename);
    }

    private List<Question> readCsvQuestions(String filename) {
        URL pollFileURL = this.getClass().getResource(filename);
        try (Stream<String> stream = Files.lines(Paths.get(pollFileURL.toURI()))) {
            return stream
                    .filter(line -> !line.startsWith(csvComment)) // skip comments
                    .map(line -> {
                        String[] parts = line.split("(?<!\\\\)" + Pattern.quote(csvSeparator)); // separator not lead by slash
                        // unescape separator
                        parts = Arrays.stream(parts)
                                .map(part -> part.replaceAll(Pattern.quote("\\" + csvSeparator), csvSeparator))
                                .toArray(String[]::new);
                        String text = parts[0];
                        // unescape first comment
                        if (text.startsWith("\\" + csvComment)) {
                            text = text.replaceFirst(Pattern.quote("\\" + csvComment), csvComment);
                        }
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


    String getLocalizedFilename(String csvBasename, String locale) {
        if(locale == null) {
            return csvBasename+".csv";
        } else {
            return csvBasename+"_"+locale+".csv";
        }
    }
}
