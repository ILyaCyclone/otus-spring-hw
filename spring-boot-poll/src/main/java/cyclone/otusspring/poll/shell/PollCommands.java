package cyclone.otusspring.poll.shell;

import cyclone.otusspring.poll.runner.PollRunner;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class PollCommands {

    private final PollRunner pollRunner;

    public PollCommands(PollRunner pollRunner) {
        this.pollRunner = pollRunner;
    }

    @ShellMethod("Runs the poll")
    public void run() {
        pollRunner.run();
    }
}
