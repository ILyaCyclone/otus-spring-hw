package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@PropertySource("/application-test.properties")
@ContextConfiguration(classes = CsvQuestionServiceTest.class)
class CsvQuestionServiceTest {

    static final Question[] TEST_QUESTIONS = new Question[]{
            new Question("First question?", "aa1", "aa1", "bb1", "cc1", "dd1")
            , new Question("Second question with | separator?", "bb2", "aa2", "bb2", "cc2", "dd2")
            , new Question("Third question after comment?", "cc3", "aa3", "bb3", "cc3", "dd3")
            , new Question("--Fourth starts with comment symbol?", "dd4", "aa4", "bb4", "cc4", "dd4")
            , new Question("Fifth question with separator in answers?", "c|c5", "aa5", "bb5", "c|c5", "dd5")
    };

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Test
    void getQuestions(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language
            , @Value("${cyclone.otusspring.pollfile.separator}") String csvSeparator
            , @Value("${cyclone.otusspring.pollfile.comment}") String csvComment
    ) {
        CsvQuestionService service = new CsvQuestionService(filenameBase, filenameExtension, language
                , csvSeparator, csvComment);
        List<Question> questions = service.getQuestions();

        assertIterableEquals(Arrays.asList(TEST_QUESTIONS), questions, "Questions incorrectly read");
    }
}
