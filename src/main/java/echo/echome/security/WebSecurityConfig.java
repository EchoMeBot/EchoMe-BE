package echo.echome.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST={
            "/users/**",
            "/",
            "/**"
    };
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(c->c.disable());
        httpSecurity.authorizeHttpRequests((authz)-> authz
                .requestMatchers(WHITE_LIST).permitAll()
//                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .anyRequest().authenticated());

//        http.headers(h->h.frameOptions(f->f.disable()).disable());
        return httpSecurity.build();
    }
}
