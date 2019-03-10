package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestions();
}
