package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ConsoleAskService implements AskService {
    private final Scanner scanner;

    static final String ASK_FIRST_NAME = "Your first name: ";
    static final String ASK_LAST_NAME = "Your last name: ";
    static final String ASK_AGE = "Your age: ";

    public ConsoleAskService() {
        scanner = new Scanner(System.in);
    }


    public Person preparePerson() {
        System.out.println("Please, tell us about yourself.");
        String firstName = readString(ASK_FIRST_NAME);
        String lastName = readString(ASK_LAST_NAME);
        int age = readInt(ASK_AGE);
        System.out.println("Nice to meet you, "+firstName+".\n");

        return new Person(firstName, lastName, age);
    }

    @Override
    public List<Answer> askQuestions(List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    System.out.println(question.getText());
                    AtomicInteger variantIndex = new AtomicInteger(1);
                    question.getVariants().forEach(variant -> System.out.println(variantIndex.getAndAdd(1) + ". " + variant));
                    int answerNumber = readInt("Choose answer:");
                    System.out.println();
                    String chosenVariant = question.getVariants().get(answerNumber - 1);
                    return new Answer(question, chosenVariant);
                })
                .collect(Collectors.toList());
    }

    String readString(String question) {
        System.out.print(ensureEndsWithSpace(question));
        return scanner.nextLine();
    }


    int readInt(String question) {
        System.out.print(ensureEndsWithSpace(question));
        int scannedInt = scanner.nextInt();
        scanner.nextLine(); // this consumes new line character left after entering a number
        return scannedInt;
    }

    private String ensureEndsWithSpace(String s) {
        if (!s.endsWith(" ")) {
            return s + " ";
        } else {
            return s;
        }
    }


    @PreDestroy
    public void preDestroy() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
