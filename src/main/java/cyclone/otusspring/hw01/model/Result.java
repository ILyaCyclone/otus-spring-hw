package cyclone.otusspring.hw01.model;

public class Result {
    private final int total;
    private final int correct;

    public Result(int total, int correct) {
        this.total = total;
        this.correct = correct;
    }

    public String asFraction() {
        return correct + "/" + total;
    }

    public int asPercent() {
        return new Double((double) correct / total * 100).intValue();
    }
}
