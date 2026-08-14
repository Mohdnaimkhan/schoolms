package com.naim.school.security;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherRepository teacherRepository;

    public List<User> getAll() {

        return userRepository.findAllByOrderByUsernameAsc();

    }

    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

    }

    public void save(User user, String rawPassword) {

        if (user.getId() == null) {

            if (rawPassword == null || rawPassword.isBlank()) {

                throw new RuntimeException("Password is required for a new user.");

            }

            if (userRepository.existsByUsername(user.getUsername())) {

                throw new RuntimeException("Username already exists.");

            }

            user.setPassword(passwordEncoder.encode(rawPassword));

        } else {

            User existing = getById(user.getId());

            if (!existing.getUsername().equalsIgnoreCase(user.getUsername())
                    && userRepository.existsByUsername(user.getUsername())) {

                throw new RuntimeException("Username already exists.");

            }

            if (rawPassword != null && !rawPassword.isBlank()) {

                user.setPassword(passwordEncoder.encode(rawPassword));

            } else {

                user.setPassword(existing.getPassword());

            }

        }

        if (user.getRole() != Role.TEACHER) {
            user.setTeacher(null);
        } else if (user.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(user.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found."));
            user.setTeacher(teacher);
        } else if (user.getTeacher() == null || user.getTeacher().getId() == null) {
            throw new RuntimeException("A Teacher account must be linked to a teacher profile.");
        }

        userRepository.save(user);

    }


    public boolean adminExists() {
        return userRepository.existsByRole(Role.ADMIN);
    }

    public void createInitialAdmin(String username, String password, String confirmPassword) {
        if (adminExists()) {
            throw new RuntimeException("Administrator setup is already complete.");
        }

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required.");
        }

        username = username.trim();
        if (!username.matches("[A-Za-z0-9._-]{3,50}")) {
            throw new RuntimeException("Username must be 3-50 characters and contain only letters, numbers, dot, underscore or hyphen.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists.");
        }

        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long.");
        }

        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match.");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFullName("Administrator");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
    }

    public void delete(Long id) {

        User user = getById(id);

        if (user.getRole() == Role.ADMIN
                && userRepository.findAllByOrderByUsernameAsc()
                        .stream()
                        .filter(u -> u.getRole() == Role.ADMIN)
                        .count() <= 1) {

            throw new RuntimeException("Cannot delete the only remaining admin account.");

        }

        userRepository.deleteById(id);

    }

}
