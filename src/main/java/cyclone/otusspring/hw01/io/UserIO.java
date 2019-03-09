package cyclone.otusspring.hw01.io;

public interface UserIO {
    void print(String line);

    void println(String line);

    void emptyLine();

    int readInt(String question);

    String readString(String question);
}
