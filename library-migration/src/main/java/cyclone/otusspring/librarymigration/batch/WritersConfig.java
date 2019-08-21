package cyclone.otusspring.librarymigration.batch;

import cyclone.otusspring.library.model.Author;
import cyclone.otusspring.library.model.Book;
import cyclone.otusspring.library.model.Genre;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WritersConfig {

    @Bean
    public JdbcBatchItemWriter<Author> authorWriter(DataSource dataSource) {
        return standardJdbcBatchWriter(dataSource, Author.class, "insert into author (id, firstname, lastname, homeland) " +
                "values (:id, :firstname, :lastname, :homeland)");
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter(DataSource dataSource) {
        return standardJdbcBatchWriter(dataSource, Genre.class, "insert into genre (id, name) " +
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
        return standardJdbcBatchWriter(dataSource, CommentWithBookId.class
                , "insert into comment (book_id, text, commentator, date) " +
                        "values (:bookId, :text, :commentator, :date)");
    }


    private <T> JdbcBatchItemWriter<T> standardJdbcBatchWriter(DataSource dataSource, Class<T> clazz, String insertSql) {
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql(insertSql);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
}
