package cyclone.otusspring.library.controller;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

/**
 * https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/test-mockmvc.html
 */
public class CustomSecurityMockMvcRequestPostProcessors {

    public static RequestPostProcessor user1() {
        return user("user1");
        //.roles("ADMIN");
    }

}
