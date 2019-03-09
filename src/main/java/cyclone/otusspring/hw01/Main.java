package cyclone.otusspring.hw01;

import cyclone.otusspring.hw01.runner.PollRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        PollRunner runner = context.getBean(PollRunner.class);
        runner.run();
    }
}
