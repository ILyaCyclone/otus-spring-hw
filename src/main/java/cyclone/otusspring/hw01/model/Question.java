package cyclone.otusspring.hw01.model;

import java.util.Arrays;
import java.util.List;

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
            throw new RuntimeException("Question \"" + text + "\" answer variants do not contain correct answer");
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
}
