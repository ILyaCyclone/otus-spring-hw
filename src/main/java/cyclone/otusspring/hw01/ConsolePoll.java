package cyclone.otusspring.hw01;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class ConsolePoll {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        PollService service = context.getBean(PollService.class);
        System.out.println(service.sayHello());

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, tell us about yourself.");
        String firstName = readString(scanner, "Your first name: ");
        String lastName = readString(scanner, "Your last name: ");
        int age = readInt(scanner, "Your age: ");

        Person person = new Person(firstName, lastName, age);

    }

    private static String readString(Scanner scanner, String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    private static int readInt(Scanner scanner, String question) {
        System.out.print(question);
        return scanner.nextInt();
    }
}
