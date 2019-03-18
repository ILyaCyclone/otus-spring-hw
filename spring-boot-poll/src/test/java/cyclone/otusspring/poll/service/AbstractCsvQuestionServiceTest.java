package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;



abstract class AbstractCsvQuestionServiceTest {

    @Test
    void getQuestions(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language
            , @Value("${cyclone.otusspring.pollfile.separator:#{null}}") String csvSeparator
            , @Value("${cyclone.otusspring.pollfile.comment:#{null}}") String csvComment
    ) {
        CsvQuestionService service = new CsvQuestionService(filenameBase, filenameExtension, language
                , csvSeparator, csvComment);
        List<Question> questions = service.getQuestions();

        assertIterableEquals(Arrays.asList(getExpectedQuestions()), questions, "Questions incorrectly read");
    }

    protected abstract Question[] getExpectedQuestions();
}