package com.academy.fintech.origination.core.service.application.db.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Users saveClient(Users user) {
        Optional<Users> existingClient = userRepository.findByEmail(user.getEmail());
        return existingClient.orElseGet(() -> userRepository.save(user));
    }
}
