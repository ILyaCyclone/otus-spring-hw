package cyclone.otusspring.library.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
//@EnableConfigurationProperties (if needed)
@ComponentScan({"cyclone.otusspring.library.repository"
        , "cyclone.otusspring.library.service"})
@EnableJpaRepositories(basePackages = "cyclone.otusspring.library.repository.datajpa")
@EntityScan({"cyclone.otusspring.library.model"})
public class TestServiceConfiguration {
}
