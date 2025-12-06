package com.tn.donation.mc_donation.infrastructure.initializer;

import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.RoleEntity;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Profile("dev")
@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final UserJpaRepository userJpaRepository;

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(PasswordEncoder encoder, UserJpaRepository userJpaRepository) {
        this.encoder = encoder;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void run(String... args) {
        if (userJpaRepository.findByUsername("admin").isEmpty()) {
            RoleEntity roles = new RoleEntity(null, "ADMIN");

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRoles(Set.of(roles));

            userJpaRepository.save(admin);

            log.info("🟢 Default ADMIN user created (admin)");
        }
    }
}

