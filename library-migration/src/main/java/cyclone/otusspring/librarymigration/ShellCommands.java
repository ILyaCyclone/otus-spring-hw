package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.repository.BookRepository;
import cyclone.otusspring.library.repository.mongo.MongoBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.UUID;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final JobLauncher jobLauncher;
    @Qualifier("libraryMigrationJob")
    private final Job libraryMigrationJob;

    private final MongoBookRepository mongoBookRepository;

    @ShellMethod(value = "Count books")
    public long countBooks() {
        return mongoBookRepository.count();
    }

    @ShellMethod(value = "Migrate Library from Mongo to Postgresql", key = "migrate")
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
}
