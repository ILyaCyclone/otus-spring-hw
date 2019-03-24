package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Question;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;



abstract class AbstractCsvQuestionServiceTest {

    final CsvQuestionService csvQuestionService;

    public AbstractCsvQuestionServiceTest(CsvQuestionService csvQuestionService) {
        this.csvQuestionService = csvQuestionService;
    }

    @Test
    void getQuestions() {
        List<Question> questions = csvQuestionService.getQuestions();

        assertIterableEquals(Arrays.asList(getExpectedQuestions()), questions, "Questions incorrectly read");
    }

    protected abstract Question[] getExpectedQuestions();
}