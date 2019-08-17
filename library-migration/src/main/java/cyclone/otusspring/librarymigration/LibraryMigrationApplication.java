package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class LibraryMigrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LibraryMigrationApplication.class, args);
        BookRepository bookRepository = context.getBean(BookRepository.class);
        System.out.println("bookRepository.findAll().size(): "+bookRepository.findAll().size());

        MongoBookRepository mongoBookRepository = context.getBean(MongoBookRepository.class);
        System.out.println("mongoBookRepository.count() = " + mongoBookRepository.count());
    }

}
