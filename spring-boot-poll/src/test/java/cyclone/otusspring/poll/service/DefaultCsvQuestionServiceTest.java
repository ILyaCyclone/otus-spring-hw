package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Question;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DefaultCsvQuestionServiceTest.class
        , properties = {"spring.config.location=classpath:/default-csv.yml"}) // or @TestPropertySource(properties={....})
class DefaultCsvQuestionServiceTest extends AbstractCsvQuestionServiceTest {

    static final Question[] TEST_QUESTIONS = new Question[]{
            new Question("First question?", "aa1", "aa1", "bb1", "cc1", "dd1")
            , new Question("Second question with , separator?", "bb2", "aa2", "bb2", "cc2", "dd2")
            , new Question("Third question after comment?", "cc3", "aa3", "bb3", "cc3", "dd3")
            , new Question("#Fourth starts with comment symbol?", "dd4", "aa4", "bb4", "cc4", "dd4")
            , new Question("Fifth question with separator in answers?", "c,c5", "aa5", "bb5", "c,c5", "dd5")
    };

    @Override
    protected Question[] getExpectedQuestions() {
        return TEST_QUESTIONS;
    }
}
