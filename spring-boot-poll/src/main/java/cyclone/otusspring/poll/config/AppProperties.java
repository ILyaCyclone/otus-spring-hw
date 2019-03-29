package cyclone.otusspring.poll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("cyclone.otusspring.poll")
public class AppProperties {
    private String locale;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String propertiesAsString() {
        return "locale='" + locale + '\'';
    }
}
