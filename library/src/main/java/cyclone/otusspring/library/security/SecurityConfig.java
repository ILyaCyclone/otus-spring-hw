package cyclone.otusspring.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // По умолчанию SecurityContext хранится в сессии
                // Это необходимо, чтобы он нигде не хранился
                // и данные приходили каждый раз с запросом
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
//                .authorizeRequests().antMatchers("/public").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/authenticated", "/success").authenticated()
//                .and()

//                .authorizeRequests().antMatchers("/api/**").authenticated()
//                .and().httpBasic()
//                .and()

                .authorizeRequests().anyRequest().authenticated()
                .and()
                // Включает Form-based аутентификацию
                .formLogin()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // for in-memory users
//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
//                .and()
//                .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
//                .and()
//                .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
//    }

}
