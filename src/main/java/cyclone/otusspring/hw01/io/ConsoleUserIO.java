package cyclone.otusspring.hw01.io;

import javax.annotation.PreDestroy;
import java.util.Scanner;

public class ConsoleUserIO implements UserIO {

    private final Scanner scanner;

    public ConsoleUserIO() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void println(String line) {
        System.out.println(line);
    }

    @Override
    public void emptyLine() {
        System.out.println();
    }

    @Override
    public int readInt(String question) {
        print(ensureEndsWithSpace(question));
        int scannedInt = scanner.nextInt();
        scanner.nextLine(); // this consumes new line character left after entering a number
        return scannedInt;
    }

    @Override
    public String readString(String question) {
        print(ensureEndsWithSpace(question));
        return scanner.nextLine();
    }



    private String ensureEndsWithSpace(String s) {
        if (!s.endsWith(" ")) {
            return s + " ";
        } else {
            return s;
        }
    }


    @PreDestroy
    private void preDestroy() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
