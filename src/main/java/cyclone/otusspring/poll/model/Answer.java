package cyclone.otusspring.poll.model;

public class Answer {
    private final Question question;
    private final String text;

    public Answer(Question question, String text) {
        this.question = question;
        this.text = text;
    }

    public Question getQuestion() {
        return question;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question=" + question +
                ", text='" + text + '\'' +
                '}';
    }
}
