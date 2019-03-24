package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Answer;
import cyclone.otusspring.poll.model.Result;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Override
    public Result getResult(List<Answer> answers) {
        int total = answers.size();
        long correct = answers.stream()
                .filter(answer -> answer.getText().equals(answer.getQuestion().getCorrectAnswer()))
                .count();
        return new Result(total, Math.toIntExact(correct));
    }
}
