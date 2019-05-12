package cyclone.otusspring.library;

import cyclone.otusspring.library.model.mongo.MongoAuthor;
import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
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
        MongoAuthorRepository repository = context.getBean(MongoAuthorRepository.class);

        repository.save(new MongoAuthor("new mongo author", "lastname", "home"));

        repository.findAll().forEach(System.out::println);


    }

}
