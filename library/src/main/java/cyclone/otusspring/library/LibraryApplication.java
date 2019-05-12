package cyclone.otusspring.library;

import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import cyclone.otusspring.library.repository.mongo.MongoGenreRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("cyclone.otusspring.library.repository.mongo")
public class LibraryApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(LibraryApplication.class, args);

        // H2 console web: http://localhost:8082
//        org.h2.tools.Console.main(args);

        ApplicationContext context = SpringApplication.run(LibraryApplication.class);
        MongoAuthorRepository authorRepository = context.getBean(MongoAuthorRepository.class);
        MongoBookRepository bookRepository = context.getBean(MongoBookRepository.class);
        MongoGenreRepository genreRepository = context.getBean(MongoGenreRepository.class);

        System.out.println("authors ---------------------------------");
        authorRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");

        System.out.println("genres ---------------------------------");
        genreRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");

        System.out.println("books ---------------------------------");
        bookRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");
    }

}
