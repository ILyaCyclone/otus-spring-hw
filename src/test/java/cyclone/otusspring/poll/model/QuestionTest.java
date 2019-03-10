package cyclone.otusspring.poll.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {

    @Test
    void testQuestionWithoutAnswer() {
        assertThrows(IllegalArgumentException.class, () -> new Question("q", "ZZZ", "aa", "bb", "cc")
                , "Question without correct answer variant should fail");
    }
}