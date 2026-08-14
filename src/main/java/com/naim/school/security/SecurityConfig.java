package com.naim.school.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/setup", "/error", "/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/uploads/**").authenticated()

                // System administration - ADMIN only
                .requestMatchers("/users/**", "/settings/**", "/academic-sessions/**", "/teacher-sessions/**").hasAuthority("ROLE_ADMIN")

                // Master/configuration modules - ADMIN only
                .requestMatchers("/subjects/**", "/classrooms/**", "/sections/**", "/fee-heads/**", "/fee-structures/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/teachers/**", "/student-sessions/**", "/teacher-sessions/**").hasAuthority("ROLE_ADMIN")

                .requestMatchers("/students/certificate/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                // Teacher: academic read access plus attendance/marks entry.
                // This must be evaluated before the broader STAFF rules below.
                .requestMatchers(HttpMethod.GET, "/students/**", "/notices/**",
                        "/attendance/**", "/results/**", "/timetable/**", "/exams/**", "/reports/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF", "ROLE_TEACHER")
                .requestMatchers(HttpMethod.POST, "/attendance/save", "/results/entry/save")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF", "ROLE_TEACHER")

                // Financial administration - ADMIN or STAFF
                .requestMatchers("/fees/**", "/expenses/**", "/reports/fees/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                // Staff operations. Teachers do not receive write access through these broad rules.
                .requestMatchers("/students/**", "/students/certificate/**", "/notices/**", "/attendance/**",
                        "/results/**", "/timetable/**", "/exams/**", "/reports/**")
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                // Global search is authenticated; controller filters the result set by role.
                .requestMatchers("/search/**").authenticated()
                .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true).failureUrl("/login?error").permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }
}
