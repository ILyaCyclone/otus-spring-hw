package cyclone.otusspring.poll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("cyclone.otusspring.poll.csv")
public class CsvProperties {
    private String basename, locale, separator, comment;

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String propertiesAsString() {
        return "basename='" + basename + '\'' +
                ", locale='" + locale + '\'' +
                ", separator='" + separator + '\'' +
                ", comment='" + comment + '\'';
    }
}
