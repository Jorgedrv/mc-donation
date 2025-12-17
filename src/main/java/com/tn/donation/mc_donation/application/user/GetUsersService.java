package com.tn.donation.mc_donation.application.user;

import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUsersService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException(id));
    }
}
