package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.UserRepository;
import com.smallbusiness.crm.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Конструктор для внедрения зависимостей
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Обязательно, так как у нас есть ленивая загрузка ролей (Lazy)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя в базе
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // ВАЖНО: Возвращаем наш класс CustomUserDetails, а не org.springframework.security.core.userdetails.User
        return new CustomUserDetails(user);
    }
}