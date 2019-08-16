package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.repository.BookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("cyclone.otusspring.library")
//@EnableMongoRepositories(basePackages = "cyclone.otusspring.library.repository")
public class LibraryMigrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LibraryMigrationApplication.class, args);
        BookRepository bookRepository = context.getBean(BookRepository.class);
        System.out.println("bookRepository.findAll().size(): "+bookRepository.findAll().size());
    }

}
