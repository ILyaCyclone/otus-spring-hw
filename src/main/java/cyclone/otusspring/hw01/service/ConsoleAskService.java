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
    private Scanner scanner;

    public ConsoleAskService() {
        scanner = new Scanner(System.in);
    }


    public Person preparePerson() {
        System.out.println("Please, tell us about yourself.");
        String firstName = readString(scanner, "Your first name: ");
        String lastName = readString(scanner, "Your last name: ");
        int age = readInt(scanner, "Your age: ");
        System.out.println("Nice to meet you, "+firstName+".\n");

        return new Person(firstName, lastName, age);
    }

    @Override
    public List<Answer> askQuestions(List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    System.out.println(question.getText());
                    AtomicInteger variantIndex = new AtomicInteger(0);
                    question.getVariants().stream().forEach(variant -> {
                        System.out.println(variantIndex.getAndAdd(1) + 1 + ". " + variant);
                    });
                    int answerNumber = readInt(scanner, "Choose answer:");
                    System.out.println();
                    return new Answer(question, question.getVariants().get(answerNumber - 1));
                })
                .collect(Collectors.toList());
    }

    private String readString(Scanner scanner, String question) {
        System.out.print(ensureEndsWithSpace(question));
        return scanner.nextLine();
    }


    private int readInt(Scanner scanner, String question) {
        System.out.print(ensureEndsWithSpace(question));
        int scannedInt = scanner.nextInt();
        scanner.nextLine(); // new line character
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
