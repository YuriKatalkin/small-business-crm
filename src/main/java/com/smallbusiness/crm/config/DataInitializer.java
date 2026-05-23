package com.smallbusiness.crm.config;

import com.smallbusiness.crm.entity.Role;
import com.smallbusiness.crm.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, пуста ли таблица ролей, чтобы не создавать дубликаты
        if (roleRepository.count() == 0) {

            // Роль USER
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Стандартный пользователь CRM");
            roleRepository.save(userRole);

            // Роль ADMIN
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Администратор системы");
            roleRepository.save(adminRole);

            // Роль MANAGER (добавлено для корректной работы AuthController)
            Role managerRole = new Role();
            managerRole.setName("MANAGER");
            managerRole.setDescription("Менеджер с доступом к CRM");
            roleRepository.save(managerRole);

            System.out.println(">>> DataInitializer: Базовые роли успешно созданы!");
        }
    }
}