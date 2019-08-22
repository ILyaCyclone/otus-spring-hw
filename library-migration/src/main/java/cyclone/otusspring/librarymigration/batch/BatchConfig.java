package cyclone.otusspring.librarymigration.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;


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


}
