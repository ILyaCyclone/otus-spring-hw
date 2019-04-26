package cyclone.otusspring.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LibraryApplication.class, args);

        // H2 console web: http://localhost:8082
//        org.h2.tools.Console.main(args);
    }

}
