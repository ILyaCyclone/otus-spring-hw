package cyclone.otusspring.librarymigration;

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Genre;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import sun.nio.ch.IOUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final MongoTemplate mongoTemplate;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-master.yaml";
    private final String LIQUIBASE_CHANGELOG = "db/changelog/db.changelog-library-migration.yaml";

//    private final SpringLiquibase springLiquibase;

//    @Bean
//    public Job onlyCreateSchemaJob(Step createLibrarySchema) {
//        return jobBuilderFactory.get("onlyCreateSchema")
//                .incrementer(new RunIdIncrementer())
//                .start(createLibrarySchema)
//                .build();
//    }

    @Bean
    public Job libraryMigrationJob(Step createLibrarySchema
            , Step migrateAuthors
            , Step migrateGenres
    ) {
        return jobBuilderFactory.get("mongoMigrateToJdbc")
                .incrementer(new RunIdIncrementer())
                .start(createLibrarySchema)
                .next(migrateAuthors)
                .next(migrateGenres)
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
    public MongoItemReader<Author> authorReader() {
//        MongoItemReader<Author> reader = new MongoItemReader<>();
//        reader.setTemplate(mongoTemplate);
//        reader.setSort(new HashMap<String, Sort.Direction>() {{
//            put("_id", Sort.Direction.DESC);
//        }});
//        reader.setTargetType(Author.class);
//        reader.setQuery("{}");
//        return reader;
        return libraryItemReader(Author.class);
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        return libraryItemReader(Genre.class);
    }

    private <T> MongoItemReader<T> libraryItemReader(Class<T> clazz) {
        MongoItemReader<T> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
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
//        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
//        writer.setDataSource(dataSource);
//        writer.setSql("insert into author (firstname, lastname, homeland) " +
//                "values (:firstname, :lastname, :homeland)");
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//
//        // custom SqlParameterSourceProvider example:
////        writer.setItemSqlParameterSourceProvider(author ->
////            new MapSqlParameterSource()
////                .addValue("firstname", author.getFirstname())
////                .addValue("lastname", author.getLastname())
////                .addValue("homeland", author.getHomeland())
////        );
//        return writer;
        return libraryItemWriter(dataSource, Author.class, "insert into author (firstname, lastname, homeland) " +
                "values (:firstname, :lastname, :homeland)");
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter(DataSource dataSource) {
        return libraryItemWriter(dataSource, Genre.class, "insert into genre (name) " +
                "values (:name)");
    }

    private <T> JdbcBatchItemWriter<T> libraryItemWriter(DataSource dataSource, Class<T> clazz, String insertSql) {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(insertSql);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

        // custom SqlParameterSourceProvider example:
//        writer.setItemSqlParameterSourceProvider(author ->
//            new MapSqlParameterSource()
//                .addValue("firstname", author.getFirstname())
//                .addValue("lastname", author.getLastname())
//                .addValue("homeland", author.getHomeland())
//        );
        return writer;
    }

}
