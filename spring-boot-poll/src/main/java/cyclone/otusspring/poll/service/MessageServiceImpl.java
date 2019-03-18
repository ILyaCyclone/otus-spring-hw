package cyclone.otusspring.poll.service;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;
    private final Locale locale;

    public MessageServiceImpl(MessageSource messageSource, String locale) {
        this.messageSource = messageSource;
        this.locale = new Locale(locale);
    }

    @Override
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public String getMessage(String key, String[] parameters) {
        return messageSource.getMessage(key, parameters, locale);
    }
}
