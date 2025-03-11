package de.aittr.g_52_shop.security.sec_config;

import de.aittr.g_52_shop.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Этот метод отключает защиту csrf
                .csrf(AbstractHttpConfigurer::disable)
                // Этот метод служит для настройки сессии
                // При текущей настройке мы отключили сессии,
                // т.е. сервер будет просить логин/пароль при каждом запросе
                .sessionManagement(x -> x
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Включаем базовую авторизацию (при помощи логина и пароля)
                .httpBasic(AbstractHttpConfigurer::disable)
                // При помощи этого метода мы конфигурируем доступ к разному функционалу
                // приложения для разных ролей пользователей
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                        .requestMatchers(HttpMethod.POST, "/files").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
