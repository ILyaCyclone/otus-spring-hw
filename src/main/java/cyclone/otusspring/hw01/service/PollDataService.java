package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Question;
import cyclone.otusspring.hw01.model.Result;

import java.util.List;

public interface PollDataService {

    List<Question> getQuestions();

    Result getResult(List<Answer> answers);
}
