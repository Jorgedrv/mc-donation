package com.tn.donation.mc_donation.application.user;

import com.tn.donation.mc_donation.common.exception.UserNotFoundException;
import com.tn.donation.mc_donation.domain.model.User;
import com.tn.donation.mc_donation.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserService {

    private final UserRepository userRepository;

    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
