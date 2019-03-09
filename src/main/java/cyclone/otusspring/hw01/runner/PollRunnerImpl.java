package cyclone.otusspring.hw01.runner;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;
import cyclone.otusspring.hw01.model.Result;
import cyclone.otusspring.hw01.presenter.PollPresenter;
import cyclone.otusspring.hw01.service.AskService;
import cyclone.otusspring.hw01.service.PollDataService;

import java.util.List;

public class PollRunnerImpl implements PollRunner {

    private final PollDataService pollDataService;
    private final AskService askService;

    public PollRunnerImpl(PollDataService pollDataService, AskService askService) {
        this.pollDataService = pollDataService;
        this.askService = askService;
    }

    public void run() {
        List<Question> questions = pollDataService.getQuestions();
        Person person = askService.preparePerson();

        System.out.println("Beginning poll...");
        List<Answer> answers = askService.askQuestions(questions);
        Result result = pollDataService.getResult(answers);

        System.out.println("\n--- RESULT ---");
        System.out.println(person.getFirstName() + " " + person.getLastName() + ", " + person.getAge());
        answers.forEach(answer -> {
            System.out.println("Question: " + answer.getQuestion().getText());
            System.out.println("Your answer: " + answer.getText());
            boolean correct = answer.getText().equals(answer.getQuestion().getCorrectAnswer());
            System.out.println(correct ? "Correct" : "Incorrect. The correct answer is: " + answer.getQuestion().getCorrectAnswer());
            System.out.println();
        });

        System.out.println("Correct answers: " + result.asFraction() + " (" + result.asPercent() + "%)");
    }
}
