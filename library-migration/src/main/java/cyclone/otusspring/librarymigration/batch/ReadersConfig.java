package cyclone.otusspring.librarymigration.batch;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;

@Configuration
public class ReadersConfig {
    @Autowired
    MongoTemplate mongoTemplate;

    @Bean
    public MongoItemReader<Author> authorReader() {
        return standardMongoItemReader(Author.class);
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        return standardMongoItemReader(Genre.class);
    }

    @Bean
    public MongoItemReader<Book> bookReader() {
        return standardMongoItemReader(Book.class);
    }

    private <T> MongoItemReader<T> standardMongoItemReader(Class<T> clazz) {
        MongoItemReader<T> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.ASC);
        }});
        reader.setTargetType(clazz);
        reader.setQuery("{}");
        return reader;
    }
}
