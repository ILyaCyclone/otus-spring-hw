package cyclone.otusspring.library.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@ComponentScan({"cyclone.otusspring.library.dao", "cyclone.otusspring.library.service"})
@EnableAutoConfiguration
//@EnableConfigurationProperties (if needed)
public class TestServiceConfiguration {
}
