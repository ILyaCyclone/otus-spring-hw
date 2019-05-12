package cyclone.otusspring.library;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
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

        authorRepository.save(new Author("new mongo author", "lastname", "home"));
        authorRepository.save(new Author("new mongo author 2", "lastname 2", "home 2"));

        System.out.println("---------------------------------");
        authorRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");

        genreRepository.save(new Genre("mongo genre 1"));
        genreRepository.save(new Genre("mongo genre 2"));


        System.out.println("---------------------------------");
        genreRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");

        bookRepository.save(new Book("mongo book 1", 2000, authorRepository.findAll().get(0), genreRepository.findAll().get(0)));
        bookRepository.save(new Book("mongo book 2", 2001, authorRepository.findAll().get(1), genreRepository.findAll().get(1)));

        System.out.println("---------------------------------");
        bookRepository.findAll().forEach(System.out::println);
        System.out.println("---------------------------------");
    }

}
