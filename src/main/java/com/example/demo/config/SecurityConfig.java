package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        /* кол-во раундов - сила - (от 4 до 31, по умолчанию 10)
        *  здесь соль встроена в хэш BCrypt и отдельно в базе не храниться
        *  более новый метод чем передовать соль в него отдельно
        */
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll()  // Разрешаем доступ ко всем URL
            // .httpBasic() базовая аутентификация нам не нужна
            .and()
            .headers().frameOptions().disable();  // Разрешаем отображение в iframe (для H2 консоли)
    }
} 