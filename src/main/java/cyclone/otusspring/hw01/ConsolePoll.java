package cyclone.otusspring.hw01;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsolePoll {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        PollService service = context.getBean(PollService.class);
        System.out.println(service.sayHello());

        List<Question> questions = initPoll();
        questions.stream().forEach(System.out::println);
        if (true) return;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please, tell us about yourself.");
            String firstName = readString(scanner, "Your first name: ");
            String lastName = readString(scanner, "Your last name: ");
            int age = readInt(scanner, "Your age: ");

            Person person = new Person(firstName, lastName, age);

        }

    }

    private static String readString(Scanner scanner, String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    private static int readInt(Scanner scanner, String question) {
        System.out.print(question);
        return scanner.nextInt();
    }


    private static List<Question> initPoll() {
        String fileName = "/poll.csv";
        return initPollFromCsv(fileName);
    }

    private static List<Question> initPollFromCsv(String fileName) {
        URL pollFileURL = ConsolePoll.class.getResource(fileName);
        try (Stream<String> stream = Files.lines(Paths.get(pollFileURL.toURI()))) {
            return stream.map(line -> {
                String[] parts = line.split("(?<!\\\\),"); // comma not trailed by slash
                String text = parts[0].replace("\\,", ","); // unescape comma
                String answer = parts[1];
                return new Question(text, answer);
            })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
