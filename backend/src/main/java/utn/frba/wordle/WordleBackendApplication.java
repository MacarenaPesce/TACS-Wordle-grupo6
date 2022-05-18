package utn.frba.wordle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import utn.frba.wordle.security.JWTAuthorizationFilter;

import java.util.Collections;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@SpringBootApplication
public class WordleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordleBackendApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    static
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public CorsFilter corsFilter() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            final CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.unmodifiableList(Collections.singletonList(ALL)));
            config.addAllowedHeader("*");
            config.addAllowedMethod("OPTIONS");
            config.addAllowedMethod("HEAD");
            config.addAllowedMethod("GET");
            config.addAllowedMethod("PUT");
            config.addAllowedMethod("POST");
            config.addAllowedMethod("DELETE");
            config.addAllowedMethod("PATCH");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
                    .cors().and().csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/api/**").permitAll()
//                    .antMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup").permitAll()
                    .antMatchers(HttpMethod.GET, "/", "/csrf",
                            "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png").permitAll()
                    .anyRequest().authenticated();
        }
    }
}
