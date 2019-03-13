package cyclone.otusspring.poll;

import cyclone.otusspring.poll.runner.PollRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        PollRunner runner = context.getBean(PollRunner.class);
        runner.run();
    }
}