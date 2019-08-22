package cyclone.otusspring.librarymigration.batch;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
@RequiredArgsConstructor
public class StepsConfig {

    private final StepBuilderFactory stepBuilderFactory;

    //    private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-master.yaml";
        private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-library-migration.yaml";

    @Bean
    public Step migrateAuthors(ItemReader<Author> authorReader
            , ItemWriter<Author> authorWriter) {
        return stepBuilderFactory.get("migrateAuthors")
                .<Author, Author>chunk(5)
                .reader(authorReader)
                .writer(authorWriter)
                .build();
    }

    @Bean
    public Step createLibrarySchema(DataSource dataSource) {
        return stepBuilderFactory.get("createLibrarySchema")
                .tasklet((stepContribution, chunkContext) -> {
                    // https://dzone.com/articles/executing-liquibase-3-use-cases
                    Database database = DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
                    Liquibase liquibase = new Liquibase(LIQUIBASE_CHANGELOG, new ClassLoaderResourceAccessor(), database);
                    liquibase.update(new Contexts(), new LabelExpression());
//                    liquibase.update();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }



    @Bean
    public Step migrateGenres(ItemReader<Genre> reader
            , ItemWriter<Genre> writer) {
        return stepBuilderFactory.get("migrateGenres")
                .<Genre, Genre>chunk(5)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Step migrateBooks(ItemReader<Book> reader
            , CompositeItemWriter<Book> bookAndCommentsWriter) {
        return stepBuilderFactory.get("migrateBooks")
                .<Book, Book>chunk(5)
                .reader(reader)
                .writer(bookAndCommentsWriter)
                .build();
    }
}
