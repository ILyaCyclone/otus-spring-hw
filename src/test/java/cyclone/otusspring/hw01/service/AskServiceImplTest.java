package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.io.UserIO;
import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AskServiceImplTest {

    private UserIO userIO;
    private MessageService messageService;

    static final Person TEST_PERSON = new Person("firstName", "lastName", 10);

    @BeforeEach
    void setUp() {
        // suppress output
        userIO = mock(UserIO.class);
        doNothing().when(userIO).println(anyString());
        doNothing().when(userIO).print(anyString());

        messageService = mock(MessageService.class);
        final String mockedString = "mocked string";
        when(messageService.getMessage(anyString())).thenReturn(mockedString);
        when(messageService.getMessage(anyString(), any())).thenReturn(mockedString);
    }

    @Test
    void testPreparePerson() {
        // arrange
        AskServiceImpl service = spy(new AskServiceImpl(userIO, messageService));
        doReturn(TEST_PERSON.getFirstName()).when(service).askFirstName();
        doReturn(TEST_PERSON.getLastName()).when(service).askLastName();
        doReturn(TEST_PERSON.getAge()).when(service).askAge();

        // act
        Person person = service.preparePerson();

        // assert
        // better use separate asserts or add equals to Person?
        Assertions.assertEquals(TEST_PERSON.getFirstName(), person.getFirstName(), "First name do not match");
        Assertions.assertEquals(TEST_PERSON.getLastName(), person.getLastName(), "Last name do not match");
        Assertions.assertEquals(TEST_PERSON.getAge(), person.getAge(), "Age do not match");
    }

    @Test
    void testAskQuestions() {
        final String[] variants = {"aa", "bb", "cc"};

        List<Question> questions = Arrays.asList(new Question("q1", variants[0], variants)
                , new Question("q2", variants[1], variants)
                , new Question("q3", variants[2], variants)
        );
        int[] userAnswerNumbers = {2, 1, 3};

        when(userIO.readInt(anyString()))
                .thenReturn(userAnswerNumbers[0])
                .thenReturn(userAnswerNumbers[1])
                .thenReturn(userAnswerNumbers[2]);

        AskServiceImpl service = new AskServiceImpl(userIO, messageService);

        // act
        List<Answer> answers = service.askQuestions(TEST_PERSON, questions);

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
        final int userAnswerNumber = variants.length + 1;
        List<Question> questions = Arrays.asList(new Question("q1", variants[0], variants));

        when(userIO.readInt(anyString())).thenReturn(userAnswerNumber);

        AskServiceImpl service = new AskServiceImpl(userIO, messageService);

        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> service.askQuestions(TEST_PERSON, questions));
    }
}