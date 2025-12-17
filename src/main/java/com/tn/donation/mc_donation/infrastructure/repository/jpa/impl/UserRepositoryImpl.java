package com.tn.donation.mc_donation.infrastructure.repository.jpa.impl;

import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.domain.repository.UserRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.UserJpaRepository;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLastname(user.getLastname());
        entity.setPassword(user.getPassword());
        entity.setRoles(user.getRoles());
        entity.setStatus(user.getStatus());
        UserEntity saved = userJpaRepository.save(entity);
        return new User(
                saved.getId(),
                saved.getName(),
                saved.getLastname(),
                saved.getEmail(),
                saved.isEnabled(),
                saved.getStatus(),
                saved.getRoles()
        );
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(entity -> new User(
                        entity.getId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getEmail(),
                        entity.isEnabled(),
                        entity.getStatus(),
                        entity.getRoles()
                ))
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getEmail(),
                        entity.isEnabled(),
                        entity.getStatus(),
                        entity.getRoles()
                ));
    }

    @Override
    public void delete(User user) {
        userJpaRepository.deleteById(user.getId());
    }
}
