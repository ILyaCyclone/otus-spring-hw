package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Answer;
import cyclone.otusspring.poll.model.Person;
import cyclone.otusspring.poll.model.Question;

import java.util.List;

public interface AskService {

    Person preparePerson();

    List<Answer> askQuestions(Person person, List<Question> questions);
}
