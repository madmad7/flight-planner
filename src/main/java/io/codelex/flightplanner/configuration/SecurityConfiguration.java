package io.codelex.flightplanner.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/testing-api/**", "/api/**", "/error/**").permitAll()
                        .anyRequest().authenticated()
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}

