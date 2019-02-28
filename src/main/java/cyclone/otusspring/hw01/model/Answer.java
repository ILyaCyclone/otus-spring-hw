package cyclone.otusspring.hw01.model;

public class Answer {
    private Question question;
    private String text;

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
