package cyclone.otusspring.poll.shell;

import cyclone.otusspring.poll.config.AppProperties;
import cyclone.otusspring.poll.config.CsvProperties;
import cyclone.otusspring.poll.model.Question;
import cyclone.otusspring.poll.runner.PollRunner;
import cyclone.otusspring.poll.service.QuestionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.stream.Collectors;

@ShellComponent
public class PollCommands {

    private final PollRunner pollRunner;
    private final QuestionService questionService;
    private final AppProperties appProperties;
    private final CsvProperties csvProperties;

    public PollCommands(PollRunner pollRunner, QuestionService questionService, AppProperties appProperties, CsvProperties csvProperties) {
        this.pollRunner = pollRunner;
        this.questionService = questionService;
        this.appProperties = appProperties;
        this.csvProperties = csvProperties;
    }

    @ShellMethod(value = "Run the poll", group = "poll" )
    public void run() {
        pollRunner.run();
    }

    @ShellMethod(value = "Print questions", group = "poll" )
    public String questions() {
        return questionService.getQuestions().stream()
                .map(Question::getText)
                .collect(Collectors.joining(", "));
    }

    @ShellMethod(value = "Set properties: --locale, --csv-basename, --csv-locale, --csv-separator, --csv-comment"
        , group = "properties")
    public String set(
            @ShellOption(value = "--locale", defaultValue = ShellOption.NULL) String locale
            , @ShellOption(value = "--csv-basename", defaultValue = ShellOption.NULL) String csvBaseFilename
            , @ShellOption(value = "--csv-locale", defaultValue = ShellOption.NULL) String csvLocale
            , @ShellOption(value = "--csv-separator", defaultValue = ShellOption.NULL) String csvSeparator
            , @ShellOption(value = "--csv-comment", defaultValue = ShellOption.NULL) String csvComment
    ) {
        String reply = "";
        if(locale != null) {
            appProperties.setLocale(locale);
            reply += propertyIsSet("application locale", locale);
        }

        // csv properties
        if (csvBaseFilename != null) {
            csvProperties.setBasename(csvBaseFilename);
            reply += propertyIsSet("csv basename", csvBaseFilename);

        }
        if (csvLocale != null) {
            csvProperties.setLocale(csvLocale);
            reply += propertyIsSet("csv basename", csvBaseFilename);
        }
        if (csvSeparator != null) {
            csvProperties.setSeparator(csvSeparator);
            reply += propertyIsSet("csv separator", csvBaseFilename);
        }
        if (csvComment != null) {
            csvProperties.setComment(csvComment);
            reply += propertyIsSet("csv basename", csvBaseFilename);
        }
        return reply;
    }

    private String propertyIsSet(String property, String value) {
        return property + " is set to '" + value + '\'';
    }

    @ShellMethod(value = "Set application locale", group = "properties")
    public void setLocale(@ShellOption String locale) {
        appProperties.setLocale(locale);
    }

    @ShellMethod(value = "Set CSV file basename", group = "properties")
    public void setCsvBasename(@ShellOption String csvBaseFilename) {
        csvProperties.setBasename(csvBaseFilename);
    }

    @ShellMethod(value = "Set CSV file locale", group = "properties")
    public void setCsvLocale(@ShellOption(defaultValue = ShellOption.NULL) String csvLocale) {
        csvProperties.setLocale(csvLocale);
    }

    @ShellMethod(value = "Set CSV separator", group = "properties")
    public void setCsvSeparator(@ShellOption(defaultValue = ShellOption.NULL) String csvSeparator) {
        csvProperties.setSeparator(csvSeparator);
    }

    @ShellMethod(value = "Set CSV comment", group = "properties")
    public void setCsvComment(@ShellOption(defaultValue = ShellOption.NULL) String csvComment) {
        csvProperties.setComment(csvComment);
    }

    @ShellMethod(value = "print properties", group = "properties")
    public String echo(@ShellOption(defaultValue = "*") String propertyName) {
        switch (propertyName) {
            case "locale": return "application locale = '" + appProperties.getLocale() + '\'';
            case "csv-basename": return "csv basename = '" + csvProperties.getBasename() + '\'';
            case "csv-locale": return "csv locale = '" + csvProperties.getLocale() + '\'';
            case "csv-separator": return "csv separator = '" + csvProperties.getSeparator() + '\'';
            case "csv-comment": return "csv comment = '" + csvProperties.getComment() + '\'';
            default: return "application: "+appProperties.propertiesAsString()
                    +"\ncsv: "+csvProperties.propertiesAsString();
        }
    }


}
