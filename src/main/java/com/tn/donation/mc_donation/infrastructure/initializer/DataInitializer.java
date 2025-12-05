package com.tn.donation.mc_donation.infrastructure.initializer;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.AdminUserRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.AdminUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final AdminUserRepository adminUserRepository;

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(PasswordEncoder encoder, AdminUserRepository adminUserRepository) {
        this.encoder = encoder;
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public void run(String... args) {
        if (adminUserRepository.findByUsername("admin").isEmpty()) {
            AdminUserEntity admin = new AdminUserEntity();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole("ADMIN");

            adminUserRepository.save(admin);

            log.info("🟢 Default ADMIN user created (admin)");
        }
    }
}

