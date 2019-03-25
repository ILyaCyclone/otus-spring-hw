package cyclone.otusspring.poll.config;

import cyclone.otusspring.poll.service.MessageService;
import cyclone.otusspring.poll.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {

    private final AppProperties appProperties;

    public AppConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public MessageService messageService() {
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasename("/i18n/messages");
        bundle.setDefaultEncoding(StandardCharsets.UTF_8.toString());

        MessageServiceImpl messageService = new MessageServiceImpl(bundle, appProperties);
        return messageService;
    }
}