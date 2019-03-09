package cyclone.otusspring.hw01;

import cyclone.otusspring.hw01.runner.PollRunner;
import cyclone.otusspring.hw01.service.CsvQuestionService;
import cyclone.otusspring.hw01.service.MessageService;
import cyclone.otusspring.hw01.service.MessageServiceImpl;
import cyclone.otusspring.hw01.service.QuestionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Application.class);
        context.refresh();

        PollRunner runner = context.getBean(PollRunner.class);
        runner.run();
    }


    @Bean
    QuestionService questionService(@Value("${cyclone.otusspring.pollfile.base}") String filenameBase
            , @Value("${cyclone.otusspring.pollfile.ext}") String filenameExtension
            , @Value("${cyclone.otusspring.language}") String language) {
        return new CsvQuestionService(filenameBase, filenameExtension, language);
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