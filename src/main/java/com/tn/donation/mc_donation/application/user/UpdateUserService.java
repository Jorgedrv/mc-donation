package com.tn.donation.mc_donation.application.user;

import com.tn.donation.mc_donation.api.dto.UserRequest;
import com.tn.donation.mc_donation.common.exception.DonationNotFoundException;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService {

    private final UserRepository userRepository;

    public User update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException(id));
        user.setName(userRequest.getName());
        user.setLastname(userRequest.getLastname());
        user.setStatus(userRequest.getStatus());
        return userRepository.save(user);
    }
}
