package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Answer;
import cyclone.otusspring.poll.model.Question;
import cyclone.otusspring.poll.model.Result;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultServiceImplTest {

    @Test
    void getResult_allCorrect() {
        String[] variants = {"aa", "bb", "cc"};
        String[] correctAnswers = {variants[2], variants[0], variants[1]};

        List<Answer> answers = Arrays.asList(new Answer(new Question("q1", correctAnswers[0], variants), correctAnswers[0])
                , new Answer(new Question("q2", correctAnswers[1], variants), correctAnswers[1])
                , new Answer(new Question("q3", correctAnswers[2], variants), correctAnswers[2])
        );

        ResultServiceImpl service = new ResultServiceImpl();

        Result result = service.getResult(answers);

        assertEquals(100, result.asPercent());
        int questionsCount = answers.size();
        assertEquals(questionsCount + "/" + questionsCount, result.asFraction());
    }

    @Test
    void getResult_partiallyCorrect() {
        String[] variants = {"aa", "bb", "cc"};
        String[] correctAnswers = {variants[2], variants[0], variants[1]};
        String[] userAnswers = {variants[2], variants[0], variants[0]}; // should be 2/3 correct answers

        List<Answer> answers = Arrays.asList(new Answer(new Question("q1", correctAnswers[0], variants), userAnswers[0])
                , new Answer(new Question("q2", correctAnswers[1], variants), userAnswers[1])
                , new Answer(new Question("q3", correctAnswers[2], variants), userAnswers[2])
        );

        ResultServiceImpl service = new ResultServiceImpl();

        Result result = service.getResult(answers);

        assertEquals(66, result.asPercent());
        int questionsCount = answers.size();
        assertEquals(2 + "/" + questionsCount, result.asFraction());
    }
}