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
    public Client saveClient(Client client) {
        Optional<Client> existingClient = clientRepository.findByEmail(client.getEmail());
        return existingClient.orElseGet(() -> clientRepository.save(client));
    }
}
