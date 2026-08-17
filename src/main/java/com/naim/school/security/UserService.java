package com.naim.school.security;

import com.naim.school.sms.BusinessException;

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
                .orElseThrow(() -> new BusinessException("User not found."));

    }

    public void save(User user, String rawPassword) {

        if (user.getId() == null) {

            if (rawPassword == null || rawPassword.isBlank()) {

                throw new BusinessException("Password is required for a new user.");

            }

            if (userRepository.existsByUsername(user.getUsername())) {

                throw new BusinessException("Username already exists.");

            }

            user.setPassword(passwordEncoder.encode(rawPassword));

        } else {

            User existing = getById(user.getId());

            if (!existing.getUsername().equalsIgnoreCase(user.getUsername())
                    && userRepository.existsByUsername(user.getUsername())) {

                throw new BusinessException("Username already exists.");

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
                    .orElseThrow(() -> new BusinessException("Teacher not found."));
            user.setTeacher(teacher);
        } else if (user.getTeacher() == null || user.getTeacher().getId() == null) {
            throw new BusinessException("A Teacher account must be linked to a teacher profile.");
        }

        userRepository.save(user);

    }

    public void delete(Long id) {

        User user = getById(id);

        if (user.getRole() == Role.ADMIN
                && userRepository.findAllByOrderByUsernameAsc()
                        .stream()
                        .filter(u -> u.getRole() == Role.ADMIN)
                        .count() <= 1) {

            throw new BusinessException("Cannot delete the only remaining admin account.");

        }

        userRepository.deleteById(id);

    }

}
