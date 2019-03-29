package cyclone.otusspring.poll;

import cyclone.otusspring.poll.runner.PollRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    final PollRunner pollRunner;

    public Application(PollRunner pollRunner) {
        this.pollRunner = pollRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
