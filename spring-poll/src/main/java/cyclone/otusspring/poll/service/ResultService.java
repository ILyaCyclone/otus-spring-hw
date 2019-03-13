package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.model.Answer;
import cyclone.otusspring.poll.model.Result;

import java.util.List;

public interface ResultService {
    Result getResult(List<Answer> answers);
}
