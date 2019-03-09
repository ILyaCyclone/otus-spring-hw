package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Question;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class CsvQuestionServiceTest {

    static final Question[] TEST_QUESTIONS = new Question[]{
            new Question("First question?", "aa1", "aa1", "bb1", "cc1", "dd1")
            , new Question("Second question with | separator?", "bb2", "aa2", "bb2", "cc2", "dd2")
            , new Question("Third question after comment?", "cc3", "aa3", "bb3", "cc3", "dd3")
    };

    @Test
    void getQuestions() {
        CsvQuestionService service = new CsvQuestionService("/test-poll.csv", "|", "--");
        List<Question> questions = service.getQuestions();

        assertIterableEquals(Arrays.asList(TEST_QUESTIONS), questions, "Questions incorrectly read");
    }
}