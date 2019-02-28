package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Person;
import cyclone.otusspring.hw01.model.Question;

import java.util.List;

public interface AskService {

    Person preparePerson();

    List<Answer> askQuestions(List<Question> questions);
}
