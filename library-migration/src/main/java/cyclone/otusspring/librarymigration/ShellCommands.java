package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.mongo.MongoAuthorRepository;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.UUID;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final JobLauncher jobLauncher;
    @Qualifier("libraryMigrationJob")
    private final Job libraryMigrationJob;
    @Qualifier("onlyCreateSchemaJob")
    private final Job onlyCreateSchemaJob;

    private final MongoBookRepository mongoBookRepository;
    private final MongoAuthorRepository mongoAuthorRepository;

    private final JdbcTemplate jdbcTemplate;


    @ShellMethod(value = "Create schema", key = "create-schema")
    public void createSchema() {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", UUID.randomUUID().toString())
                .toJobParameters();
        try {
            jobLauncher.run(onlyCreateSchemaJob, params);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }

    @ShellMethod(value = "Migrate Library from source DB to target DB", key = "migrate")
    public void migrate() {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", UUID.randomUUID().toString())
                .toJobParameters();
        try {
            jobLauncher.run(libraryMigrationJob, params);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }


    @ShellMethod(value = "Count authors in source DB")
    public long countAuthorsSource() {
        return mongoAuthorRepository.count();
    }

    @ShellMethod(value = "Count authors in target DB")
    public long countAuthorsTarget() {
        return countInTargetDB("author");
    }

    private long countInTargetDB(String tableName) {
        String safeTableName = tableName.split("\\s")[0]; // better safe than sorry
        return jdbcTemplate.queryForObject("select count(*) from "+safeTableName+";", Long.class);
    }
}
