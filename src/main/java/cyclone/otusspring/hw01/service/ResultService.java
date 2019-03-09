package cyclone.otusspring.hw01.service;

import cyclone.otusspring.hw01.model.Answer;
import cyclone.otusspring.hw01.model.Result;

import java.util.List;

public interface ResultService {
    Result getResult(List<Answer> answers);
}
