package re1kur.rentalservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re1kur.rentalservice.core.dto.user.RegistrationRequest;
import re1kur.rentalservice.core.dto.user.UserProfile;
import re1kur.rentalservice.core.exception.AuthenticationException;
import re1kur.rentalservice.core.exception.RegistrationException;
import re1kur.rentalservice.entity.Role;
import re1kur.rentalservice.entity.User;
import re1kur.rentalservice.repository.RoleRepository;
import re1kur.rentalservice.repository.UserRepository;
import re1kur.rentalservice.util.JwtUtil;
import re1kur.rentalservice.util.PasswordUtil;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    /**
     * Аутентификация пользователя
     */
    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!passwordUtil.verifyPassword(password, user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        // Получаем роли пользователя
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // Генерируем JWT токен
        return jwtUtil.generateToken(user.getEmail(), roleNames);
    }

    /**
     * Регистрация нового пользователя
     */
    public User registerUser(RegistrationRequest request) {
        // Проверяем существование пользователя
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Email already registered");
        }

        // Хешируем пароль
        String passwordHash = passwordUtil.hashPassword(request.getPassword());

        // Получаем роль USER по умолчанию
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> Role.builder()
                        .name("USER")
                        .build());

        // Создаем пользователя
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordHash)
                .roles(Set.of(userRole))
                .build();

        return userRepository.save(user);
    }

    /**
     * Обновление пароля пользователя
     */
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Проверяем старый пароль
        if (!passwordUtil.verifyPassword(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid old password");
        }

        // Хешируем новый пароль
        String newPasswordHash = passwordUtil.hashPassword(newPassword);
        user.setPassword(newPasswordHash);

        userRepository.save(user);
    }

    /**
     * Получение информации о текущем пользователе
     */
    public UserProfile getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        return UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }


    /**
     * Проверка, имеет ли пользователь указанную роль
     */
    public boolean hasRole(String email, String roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Проверка, имеет ли пользователь указанную привилегию
     * (предполагается, что роли имеют permissions, но в вашей модели пока нет)
     */
    public boolean hasPermission(String email, String permission) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        return true;
    }
}
