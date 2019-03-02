package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cyclone.otusspring.hw01.service.ConsoleAskService.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsoleAskServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testPreparePerson() {
        // arrange
        final String testFirstName = "firstName";
        final String testLastName = "lastName";
        final int testAge = 10;

        ConsoleAskService service = mock(ConsoleAskService.class);
        when(service.readString(ASK_FIRST_NAME)).thenReturn(testFirstName);
        when(service.readString(ASK_LAST_NAME)).thenReturn(testLastName);
        when(service.readInt(ASK_AGE)).thenReturn(testAge);
        when(service.preparePerson()).thenCallRealMethod();

        // act
        Person person = service.preparePerson();

        // assert
        // better use separate asserts or add equals to Person?
        Assertions.assertEquals(testFirstName, person.getFirstName(), "First name do not match");
        Assertions.assertEquals(testLastName, person.getLastName(), "Last name do not match");
        Assertions.assertEquals(testAge, person.getAge(), "Age do not match");
    }

    @Test
    void testAskQuestions() {

        final String[] variants = {"aa", "bb", "cc"};

        List<Question> questions = Arrays.asList(new Question("q1", variants[0], variants)
                , new Question("q2", variants[1], variants)
                , new Question("q3", variants[2], variants)
        );
        int[] userAnswerNumbers = {2, 1, 3};


        ConsoleAskService service = mock(ConsoleAskService.class);
        when(service.readInt(anyString()))
                .thenReturn(userAnswerNumbers[0])
                .thenReturn(userAnswerNumbers[1])
                .thenReturn(userAnswerNumbers[2]);
        when(service.askQuestions(anyList())).thenCallRealMethod();

        // act
        List<Answer> answers = service.askQuestions(questions);

        // assert
        Assertions.assertEquals(questions.size(), answers.size());
        for (int i = 0; i < answers.size(); i++) {
            Assertions.assertEquals(questions.get(i).getText(), answers.get(i).getQuestion().getText()
                    , "Answered question text doesn't match asked question text");
            Assertions.assertEquals(questions.get(i).getVariants().get(userAnswerNumbers[i] - 1), answers.get(i).getText()
                    , "User answer doesn't match expected answer");
        }
    }


    @Test
    void testAskQuestions_outOfBounds() {
        final String[] variants = {"aa", "bb", "cc"};
        final int userAnswerNumber = 10;
        List<Question> questions = Arrays.asList(new Question("q1", variants[0], variants));

        ConsoleAskService service = mock(ConsoleAskService.class);
        when(service.readInt(anyString())).thenReturn(userAnswerNumber);
        when(service.askQuestions(anyList())).thenCallRealMethod();

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> service.askQuestions(questions));
    }
}