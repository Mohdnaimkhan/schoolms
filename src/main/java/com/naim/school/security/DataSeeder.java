package com.naim.school.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Seeds a default ADMIN login on first run so the application is usable
 * out of the box. If NO users exist yet in the database, one is created:
 *
 *      username : admin
 *      password : admin123
 *
 * IMPORTANT: Log in with this account and change the password (or create
 * a new admin account and deactivate this one) right after first setup.
 * This seeder never touches the database again once at least one user
 * already exists.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {

            return;

        }

        User admin = new User();

        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Administrator");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);

        log.warn("==============================================================");
        log.warn(" No users found - a default admin login has been created:");
        log.warn("   username: admin");
        log.warn("   password: admin123");
        log.warn(" Please log in and change this password immediately.");
        log.warn("==============================================================");

    }

}
