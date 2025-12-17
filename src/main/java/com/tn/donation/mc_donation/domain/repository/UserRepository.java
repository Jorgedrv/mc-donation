package com.tn.donation.mc_donation.domain.repository;

import com.tn.donation.mc_donation.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    void delete(User user);
}
