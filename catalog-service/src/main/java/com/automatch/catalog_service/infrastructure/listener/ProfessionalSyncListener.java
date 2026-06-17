package com.automatch.catalog_service.infrastructure.listener;

import com.automatch.catalog_service.domain.event.ProfessionalCreatedEvent;
import com.automatch.catalog_service.domain.repository.ProfessionalReadModelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfessionalSyncListener {
    private final ProfessionalReadModelPort professionalReadModelPort;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProfessionalCreated(ProfessionalCreatedEvent event) {
        log.info("Transaction committed. Syncing Professional Read Model (Redis) for: {}", event.getProfessional().getId());
        professionalReadModelPort.saveReadModel(event.getProfessional());
    }
}
