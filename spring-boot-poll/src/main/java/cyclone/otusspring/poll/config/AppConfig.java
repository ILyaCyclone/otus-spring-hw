package cyclone.otusspring.poll.config;

import cyclone.otusspring.poll.service.CsvQuestionService;
import cyclone.otusspring.poll.service.MessageService;
import cyclone.otusspring.poll.service.MessageServiceImpl;
import cyclone.otusspring.poll.service.QuestionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {

    @Bean
    QuestionService questionService(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language
            , @Value("${cyclone.otusspring.pollfile.separator:#{null}}") String csvSeparator
            , @Value("${cyclone.otusspring.pollfile.comment:#{null}}") String csvComment
    ) {
        return new CsvQuestionService(filenameBase, filenameExtension, language, csvSeparator, csvComment);
    }


//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Bean
    public MessageService messageService(@Value("${cyclone.otusspring.language}") String language) {
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasename("/i18n/messages");
        bundle.setDefaultEncoding(StandardCharsets.UTF_8.toString());

        MessageServiceImpl messageService = new MessageServiceImpl(bundle, language);
        return messageService;
    }
}