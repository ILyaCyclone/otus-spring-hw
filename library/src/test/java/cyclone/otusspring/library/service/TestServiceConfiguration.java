package cyclone.otusspring.library.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
//@EnableConfigurationProperties (if needed)
@ComponentScan({"cyclone.otusspring.library.repository.jpa", "cyclone.otusspring.library.service"})
@EntityScan({"cyclone.otusspring.library.model"})
public class TestServiceConfiguration {
}
