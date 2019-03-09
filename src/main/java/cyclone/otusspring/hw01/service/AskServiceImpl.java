package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.io.UserIO;
import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AskServiceImpl implements AskService {

    private final UserIO userIO;
    private final MessageService messageService;

    public AskServiceImpl(UserIO userIO, MessageService messageService) {
        this.userIO = userIO;
        this.messageService = messageService;
    }


    public Person preparePerson() {
        userIO.println(messageService.getMessage("poll.before.person"));

        String firstName = askFirstName();
        String lastName = askLastName();
        int age = askAge();

        Person person = new Person(firstName, lastName, age);
        userIO.println(messageService.getMessage("poll.after.person", person.getFirstName()));
        userIO.emptyLine();

        return person;
    }

    String askFirstName() {
        return userIO.readString(messageService.getMessage("poll.ask.person.firstname"));
    }

    String askLastName() {
        return userIO.readString(messageService.getMessage("poll.ask.person.lastname"));
    }

    int askAge() {
        return userIO.readInt(messageService.getMessage("poll.ask.person.age"));
    }


    @Override
    public List<Answer> askQuestions(Person person, List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    userIO.println(question.getText());
                    AtomicInteger variantIndex = new AtomicInteger(1);
                    question.getVariants().forEach(variant -> userIO.println(variantIndex.getAndAdd(1) + ". " + variant));
                    int answerNumber = userIO.readInt(messageService.getMessage("poll.ask.answer"));

                    userIO.emptyLine();
                    String chosenVariant = question.getVariants().get(answerNumber - 1);
                    return new Answer(question, chosenVariant);
                })
                .collect(Collectors.toList());
    }

}
