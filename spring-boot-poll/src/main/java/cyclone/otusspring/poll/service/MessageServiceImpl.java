package cyclone.otusspring.poll.service;

import cyclone.otusspring.poll.config.AppProperties;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;
    private final AppProperties appProperties;

    public MessageServiceImpl(MessageSource messageSource, AppProperties appProperties) {
        this.messageSource = messageSource;
        this.appProperties = appProperties;
    }

    @Override
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, getLocale());
    }

    @Override
    public String getMessage(String key, String[] parameters) {
        return messageSource.getMessage(key, parameters, getLocale());
    }

    Locale getLocale() {
        return new Locale(appProperties.getLocale());
    }
}
