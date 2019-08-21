package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final MongoTemplate mongoTemplate;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-master.yaml";
    private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-library-migration.yaml";

    @Bean
    public Job libraryMigrationJob(Step createLibrarySchema
            , Step migrateAuthors
            , Step migrateGenres
            , Step migrateBooks
    ) {
        return jobBuilderFactory.get("mongoMigrateToJdbc")
                .incrementer(new RunIdIncrementer())
                .start(createLibrarySchema)
                .next(migrateAuthors)
                .next(migrateGenres)
                .next(migrateBooks)
                .build();
    }


    @Bean
    public Step createLibrarySchema(DataSource dataSource) {
        return stepBuilderFactory.get("createLibrarySchema")
                .tasklet((stepContribution, chunkContext) -> {
                    InputStream stream = BatchConfig.class.getClassLoader().getResourceAsStream("library-schema.sql");
                    String librarySchemaSql = readFromInputStream(stream);
                    stream.close();
                    new JdbcTemplate(dataSource).execute(librarySchemaSql);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    // works fine locally, doesn't do anything in Docker
//    @Bean
//    public Step createLibrarySchema(DataSource dataSource) {
//        return stepBuilderFactory.get("createLibrarySchema")
//                .tasklet((stepContribution, chunkContext) -> {
//                    // https://dzone.com/articles/executing-liquibase-3-use-cases
//                    Database database = DatabaseFactory.getInstance()
//                            .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
//                    Liquibase liquibase = new Liquibase(LIQUIBASE_CHANGELOG, new ClassLoaderResourceAccessor(), database);
//                    liquibase.update(new Contexts(), new LabelExpression());
////                    liquibase.update();
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }


    @Bean
    public Step migrateAuthors(ItemReader<Author> authorReader
//            , ItemProcessor<Author> authorProcessor
            , ItemWriter<Author>  authorWriter) {
        return stepBuilderFactory.get("migrateAuthors")
                .<Author, Author>chunk(5)
                .reader(authorReader)
//                .processor(authorProcessor)
                .writer(authorWriter)
//                .listener(readListener)
//                .listener(processListener)
//                .listener(writerListener)
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

//    @Bean
//    public Step migrateBookComments(ItemReader<Book> reader
//            , ItemWriter<CommentWithBookId> writer
//    ) {
//        return stepBuilderFactory.get("migrateBooks")
//                .<Book, List<CommentWithBookId>>chunk(5)
//                .reader(reader)
//                .processor((Function<? super Book, ? extends List<CommentWithBookId>>)
//                        book -> book.getComments().stream()
//                                .map(comment -> new CommentWithBookId(book.getId(), comment))
//                                .collect(Collectors.toList())
//                )
//                .writer(new ListUnpackingItemWriter<>(writer))
//                .build();
//    }




    @Bean
    public MongoItemReader<Author> authorReader() {
        return libraryItemReader(Author.class);
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        return libraryItemReader(Genre.class);
    }

    @Bean
    public MongoItemReader<Book> bookReader() {
        return libraryItemReader(Book.class);
    }

    private <T> MongoItemReader<T> libraryItemReader(Class<T> clazz) {
        MongoItemReader<T> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.ASC);
        }});
        reader.setTargetType(clazz);
        reader.setQuery("{}");
        return reader;
    }

//    @Bean
//    public ItemProcessor<Author, BookAuthor> authorProcessor(MongoToJdbcService mapper) {
//        return mapper::toBookAuthor;
//    }

    @Bean
    public JdbcBatchItemWriter<Author> authorWriter(DataSource dataSource) {
        return libraryItemWriter(dataSource, Author.class, "insert into author (id, firstname, lastname, homeland) " +
                "values (:id, :firstname, :lastname, :homeland)");
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter(DataSource dataSource) {
        return libraryItemWriter(dataSource, Genre.class, "insert into genre (id, name) " +
                "values (:id, :name)");
    }

    @Bean
    public CompositeItemWriter<Book> bookAndCommentsWriter(
             JdbcBatchItemWriter<Book> bookWriter
            , JdbcBatchItemWriter<CommentWithBookId> commentWriter

    ) {
        return new CompositeItemWriterBuilder<Book>()
                .delegates(Arrays.asList(
                        bookWriter,
                        books -> {
                            List<CommentWithBookId> comments = books.stream()
                                    .flatMap(book -> book.getComments().stream()
                                            .map(comment -> new CommentWithBookId(book.getId(), comment))
                                    )
                                    .collect(Collectors.toList());
                            commentWriter.write(comments);
                        }
                )).build();
    }

    @Bean
    public JdbcBatchItemWriter<Book> bookWriter(DataSource dataSource) {
        String insertSql = "insert into book (id, title, year, author_id, genre_id) " +
                "values (:id, :title, :year, :author_id, :genre_id)";

        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(insertSql);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        // custom SqlParameterSourceProvider example:
        writer.setItemSqlParameterSourceProvider(book ->
            new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("year", book.getYear())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId())
        );
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<CommentWithBookId> commentWriter(DataSource dataSource) {
        return libraryItemWriter(dataSource, CommentWithBookId.class
                , "insert into comment (book_id, text, commentator, date) " +
                "values (:bookId, :text, :commentator, :date)");
    }


    private <T> JdbcBatchItemWriter<T> libraryItemWriter(DataSource dataSource, Class<T> clazz, String insertSql) {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(insertSql);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }


}
