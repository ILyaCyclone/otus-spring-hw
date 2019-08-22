package cyclone.otusspring.librarymigration;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;
import java.util.UUID;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final JobLauncher jobLauncher;

    @Qualifier("libraryMigrationJob")
    private final Job libraryMigrationJob;

    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;


    @ShellMethod(value = "Migrate Library from source DB to target DB", key = "migrate")
    public String migrate() {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", UUID.randomUUID().toString())
                .toJobParameters();
        try {
            jobLauncher.run(libraryMigrationJob, params);
            return "-------------------------"
                    + "\nMigration is completed"
                    + "\n"
                    + "\nPost migration statistics:"
                    + "\n-------------------------\n"
                    + statistics();
        } catch (JobExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @ShellMethod(value = "Source and target databases statistics", key = {"statistics", "stats"})
    public String statistics() {
        return "Source DB"
                + "\n---------------"
                + "\nAuthors: \t" + countInSourceDB(Author.class)
                + "\nGenres: \t" + countInSourceDB(Genre.class)
                + "\nBooks: \t\t" + countInSourceDB(Book.class)
                + "\nComments: \t" + countCommentsInSourceDB()
                + "\n"
                + "\nTarget DB"
                + "\n---------------"
                + "\nAuthors: \t" + countInTargetDB("author")
                + "\nGenres: \t" + countInTargetDB("genre")
                + "\nBooks: \t\t" + countInTargetDB("book")
                + "\nComments: \t" + countInTargetDB("comment")
                ;
    }

    private long countInSourceDB(Class clazz) {
        return mongoTemplate.count(new Query(), clazz);
    }

    private String countInTargetDB(String tableName) {
        String safeTableName = tableName.split("\\s")[0]; // better safe than sorry
        try {
            return String.valueOf(jdbcTemplate.queryForObject("select count(*) from " + safeTableName + ";", Long.class));
        } catch (BadSqlGrammarException e) {
            if (e.getMessage().contains("relation \"" + tableName + "\" does not exist")) {
                return "table doesn't exist";
            }
            throw e;
        }
    }

    private String countCommentsInSourceDB() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("comments"),
                Aggregation.group().count().as("count"));

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Book.class, Map.class);
        return String.valueOf(results.getMappedResults().get(0).get("count"));
    }
}
