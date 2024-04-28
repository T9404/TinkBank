package com.academy.fintech.origination.integration;

import com.academy.fintech.origination.core.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.application.db.outbox.OutboxApplicationRepository;
import com.academy.fintech.origination.core.application.db.outbox.enums.OutboxApplicationStatus;
import com.academy.fintech.origination.core.users.db.UserRepository;
import com.academy.fintech.origination.integration.config.KafkaPostgresContainersTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.academy.fintech.origination.integration.factory.ApplicationFactory.buildApplication;
import static com.academy.fintech.origination.integration.factory.UserFactory.buildUser;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@KafkaPostgresContainersTest
class ExporterTest {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OutboxApplicationRepository outboxApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        outboxApplicationRepository.deleteAll();
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testExportApplication() {
        var user = buildUser();
        var savedUser = userRepository.save(user);

        var application = buildApplication(savedUser);
        var savedApplication = applicationService.saveApplication(application);
        assertThat(savedApplication).isNotNull();

        await()
                .atMost(1, SECONDS)
                .untilAsserted(() -> {
                    var outboxApplication = outboxApplicationRepository.findAllByStatus(OutboxApplicationStatus.PENDING.name());
                    assertThat(outboxApplication).isNotEmpty();
                });


        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> {
                    var outboxApplication = outboxApplicationRepository.findAllByStatus(OutboxApplicationStatus.SENT.name());
                    assertThat(outboxApplication).isNotEmpty();
                });
    }

}
