package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
            .antMatchers("/testing/**").permitAll()
            .antMatchers("/sa/users").permitAll()
            .antMatchers("/sa/quiz/questions").authenticated() // Требуется аутентификация
            .antMatchers("/sa/quiz/questions/{id}").hasRole("ADMIN") //Только для роли ADMIN
            .anyRequest().authenticated()
            .and()
            .httpBasic();
            // .headers().frameOptions().disable();  // Разрешаем отображение в iframe (для H2 консоли);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password(passwordEncoder().encode("qwerty"))
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password(passwordEncoder().encode("qwerty"))
            .roles("USER");
    }

} 