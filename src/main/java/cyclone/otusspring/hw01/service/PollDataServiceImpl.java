package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.dao.PollDao;
import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Question;
import cyclone.otusspring.hw01.model.Result;

import java.util.List;

public class PollDataServiceImpl implements PollDataService {

    private final PollDao pollDao;

    public PollDataServiceImpl(PollDao pollDao) {
        this.pollDao = pollDao;
    }

    @Override
    public List<Question> getQuestions() {
        return pollDao.getQuestions();
    }

    @Override
    public Result getResult(List<Answer> answers) {
        int total = answers.size();
        long correct = answers.stream()
                .filter(answer -> answer.getText().equals(answer.getQuestion().getCorrectAnswer()))
                .count();
        return new Result(total, Math.toIntExact(correct));
    }
}
