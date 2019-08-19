package cyclone.otusspring.librarymigration;

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import cyclone.otusspring.library.model.Author;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

//    private final SpringLiquibase springLiquibase;

    @Bean
    public Job libraryMigrationJob(Step createLibrarySchema, Step migrateAuthors) {
        return jobBuilderFactory.get("mongoMigrateToJdbc")
                .incrementer(new RunIdIncrementer())
                .start(createLibrarySchema)
                .next(migrateAuthors)
//                .next(migrateBooks)
                .build();
    }


    @Bean
    public Step createLibrarySchema(DataSource dataSource) {
        return stepBuilderFactory.get("createLibrarySchema")
                .tasklet((stepContribution, chunkContext) -> {
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                            new JdbcConnection(dataSource.getConnection()));
//                    Liquibase liquibase = new liquibase.Liquibase(springLiquibase.getChangeLog()
                    Liquibase liquibase = new liquibase.Liquibase("db/changelog/db.changelog-master.yaml"
                            , new ClassLoaderResourceAccessor(), database);
                    liquibase.update(new Contexts(), new LabelExpression());
                return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step migrateAuthors(ItemReader<Author> authorReader
//            , ItemProcessor<Author> authorProcessor
            , ItemWriter authorWriter) {
        return stepBuilderFactory.get("migrateAuthors")
                .chunk(5)
                .reader(authorReader)
//                .processor(authorProcessor)
                .writer(authorWriter)
//                .listener(readListener)
//                .listener(processListener)
//                .listener(writerListener)
                .build();
    }


    @Bean
    public MongoItemReader<Author> authorReader() {
        MongoItemReader<Author> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(Author.class);
        reader.setQuery("{}");
        return reader;
    }

//    @Bean
//    public ItemProcessor<Author, BookAuthor> authorProcessor(MongoToJdbcService mapper) {
//        return mapper::toBookAuthor;
//    }

    @Bean
    public JdbcBatchItemWriter authorWriter(DataSource dataSource) {
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();
        writer.setDataSource(dataSource);
        writer.setSql("insert into author (firstname, lastname, homeland) " +
                "values (:firstname, :lastname, :homeland)");
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Author>());
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        return writer;
    }

}
