package com.academy.fintech.origination.core.service.application.db.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Users saveClient(Users user) {
        Optional<Users> existingClient = clientRepository.findByEmail(user.getEmail());
        return existingClient.orElseGet(() -> clientRepository.save(user));
    }
}
