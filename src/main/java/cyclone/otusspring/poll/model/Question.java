package cyclone.otusspring.poll.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Question {
    private final String text;
    private final String correctAnswer;
    private final List<String> variants;

    public Question(String text, String correctAnswer, String... variants) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.variants = Arrays.asList(variants);

        assertVariantsContainCorrectAnswer();
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getVariants() {
        return variants;
    }


    private void assertVariantsContainCorrectAnswer() {
        if (variants.stream().noneMatch(variant -> variant.equals(correctAnswer))) {
            throw new IllegalArgumentException("Question \"" + text + "\" answer variants do not contain correct answer");
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", variants=" + variants +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(text, question.text) &&
                Objects.equals(correctAnswer, question.correctAnswer) &&
                Objects.equals(variants, question.variants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, correctAnswer, variants);
    }
}
