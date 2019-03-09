package cyclone.otusspring.hw01.runner;

import cyclone.otusspring.hw01.io.UserIO;
import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;
import cyclone.otusspring.hw01.model.Result;
import cyclone.otusspring.hw01.service.AskService;
import cyclone.otusspring.hw01.service.QuestionService;
import cyclone.otusspring.hw01.service.ResultService;

import java.util.List;

public class PollRunnerImpl implements PollRunner {

    private final UserIO userIO;
    private final ResultService resultService;
    private final AskService askService;
    private final QuestionService questionService;

    public PollRunnerImpl(UserIO userIO, ResultService resultService, AskService askService, QuestionService questionService) {
        this.userIO = userIO;
        this.resultService = resultService;
        this.askService = askService;
        this.questionService = questionService;
    }

    public void run() {

        List<Question> questions = getQuestions();

        Person person = getPerson();

        beforeQuestions();
        List<Answer> answers = askQuestions(person, questions);

        showResult(person, answers);
    }

    private void showResult(Person person, List<Answer> answers) {
        Result result = getResult(answers);

        userIO.emptyLine();
        userIO.println("--- RESULT ---");
        userIO.println(person.getFirstName() + " " + person.getLastName() + ", " + person.getAge());
        answers.forEach(answer -> {
            userIO.println("Question: " + answer.getQuestion().getText());
            userIO.println("Your answer: " + answer.getText());
            boolean correct = answer.getText().equals(answer.getQuestion().getCorrectAnswer());
            userIO.println(correct ? "Correct" : "Incorrect. The correct answer is: " + answer.getQuestion().getCorrectAnswer() + "\n");
        });

        userIO.println("Correct answers: " + result.asFraction() + " (" + result.asPercent() + "%)");
    }


    void beforeQuestions() {
        userIO.println("Beginning poll...");
    }

    List<Question> getQuestions() {
        return questionService.getQuestions();
    }


    Person getPerson() {
        return askService.preparePerson();
    }

    private List<Answer> askQuestions(Person person, List<Question> questions) {
        return askService.askQuestions(person, questions);
    }

    private Result getResult(List<Answer> answers) {
        return resultService.getResult(answers);
    }
}
