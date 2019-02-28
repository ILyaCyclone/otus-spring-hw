package cyclone.otusspring.hw01.dao;

import cyclone.otusspring.hw01.model.Question;

import java.util.List;

public interface PollDao {
    List<Question> getQuestions();
}
