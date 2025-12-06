package com.tn.donation.mc_donation.infrastructure.initializer;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.RoleJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) {
        log.info("Initial User");

        RoleEntity adminRole = roleJpaRepository.findByName("ADMIN")
                .orElseGet(() -> roleJpaRepository.save(new RoleEntity(null, "ADMIN")));

        RoleEntity userRole = roleJpaRepository.findByName("USER")
                .orElseGet(() -> roleJpaRepository.save(new RoleEntity(null, "USER")));

        if (userJpaRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@local.com");
            admin.setPassword(encoder.encode("admin123"));

            admin.getRoles().add(adminRole);
            admin.getRoles().add(userRole);

            userJpaRepository.save(admin);

            log.info("🟢 Default ADMIN user created (admin)");
        }
    }
}

