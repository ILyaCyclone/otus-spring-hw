package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.io.UserIO;
import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AskServiceImpl implements AskService {
    static final String ASK_FIRST_NAME = "Your first name: ";
    static final String ASK_LAST_NAME = "Your last name: ";
    static final String ASK_AGE = "Your age: ";

    private final UserIO userIO;

    public AskServiceImpl(UserIO userIO) {
        this.userIO = userIO;
    }


    public Person preparePerson() {
        userIO.println("Please, tell us about yourself.");

        String firstName = userIO.readString(ASK_FIRST_NAME);
        String lastName = userIO.readString(ASK_LAST_NAME);
        int age = userIO.readInt(ASK_AGE);
        userIO.println("Nice to meet you, " + firstName);
        userIO.emptyLine();

        return new Person(firstName, lastName, age);
    }

    @Override
    public List<Answer> askQuestions(Person person, List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    userIO.println(question.getText());
                    AtomicInteger variantIndex = new AtomicInteger(1);
                    question.getVariants().forEach(variant -> userIO.println(variantIndex.getAndAdd(1) + ". " + variant));
                    int answerNumber = userIO.readInt("Choose answer: ");

                    userIO.emptyLine();
                    String chosenVariant = question.getVariants().get(answerNumber - 1);
                    return new Answer(question, chosenVariant);
                })
                .collect(Collectors.toList());
    }

}
