package com.crediya.loans.infrastructure.driven.persistence;

import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.domain.model.Status;
import com.crediya.loans.infrastructure.driven.persistence.mapper.LoanApplicationMapperImpl;
import com.crediya.loans.infrastructure.driven.persistence.mapper.LoanTypeMapperImpl;
import com.crediya.loans.infrastructure.driven.persistence.mapper.StatusMapperImpl;
import com.crediya.loans.infrastructure.driven.persistence.repository.LoanApplicationDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@DataR2dbcTest
@Import({
        StatusRepositoryAdapter.class, LoanTypeRepositoryAdapter.class, LoanApplicationRepositoryAdapter.class,
        StatusMapperImpl.class, LoanTypeMapperImpl.class, LoanApplicationMapperImpl.class
})
class PersistenceAdapterIntegrationTest {

    @SpringBootApplication
    static class TestConfiguration {}

    @Autowired
    private StatusRepositoryAdapter statusRepositoryAdapter;
    @Autowired
    private LoanTypeRepositoryAdapter loanTypeRepositoryAdapter;
    @Autowired
    private LoanApplicationRepositoryAdapter loanApplicationRepositoryAdapter;
    @Autowired
    private LoanApplicationDataRepository loanApplicationDataRepository;

    private Status initialStatus;
    private LoanType defaultLoanType;

    @BeforeEach
    void setUp() {
        loanApplicationDataRepository.deleteAll().block();
        initialStatus = statusRepositoryAdapter.findByName("PENDIENTE_REVISION").block();
        defaultLoanType = loanTypeRepositoryAdapter.findById(1L).block();
    }

    @Test
    void statusRepositoryAdapter_shouldFindStatusByName() {
        StepVerifier.create(statusRepositoryAdapter.findByName("PENDIENTE_REVISION"))
                .expectNextMatches(status ->
                        status.getId() != null && status.getName().equals("PENDIENTE_REVISION")
                )
                .verifyComplete();
    }

    @Test
    void statusRepositoryAdapter_shouldReturnEmptyWhenNameNotFound() {
        var resultMono = statusRepositoryAdapter.findByName("NON_EXISTENT_STATUS");
        StepVerifier.create(resultMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void loanTypeRepositoryAdapter_shouldFindLoanTypeById() {
        StepVerifier.create(loanTypeRepositoryAdapter.findById(1L))
                .expectNextMatches(loanType ->
                        loanType.getId().equals(1L) && loanType.getName().equals("Personal Express")
                )
                .verifyComplete();
    }

    @Test
    void loanTypeRepositoryAdapter_shouldReturnEmptyWhenIdNotFound() {
        long nonExistentId = 999L;
        var resultMono = loanTypeRepositoryAdapter.findById(nonExistentId);
        StepVerifier.create(resultMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void loanApplicationRepositoryAdapter_shouldSaveApplicationSuccessfully() {
        var applicationToSave = new LoanApplication(
                null, new BigDecimal("7500.00"), 36, "integration.test@example.com",
                initialStatus, defaultLoanType
        );
        var savedApplicationMono = loanApplicationRepositoryAdapter.save(applicationToSave);

        StepVerifier.create(savedApplicationMono)
                .expectNextMatches(savedApp ->
                        savedApp.getId() != null &&
                                savedApp.getAmount().compareTo(new BigDecimal("7500.00")) == 0 &&
                                savedApp.getStatus().getName().equals("PENDIENTE_REVISION") &&
                                savedApp.getLoanType().getName().equals("Personal Express")
                )
                .verifyComplete();
    }

    @Test
    void loanApplicationRepositoryAdapter_shouldFailWhenSavingWithInvalidForeignKey() {
        var invalidStatus = new Status(999L, "INVALID", "Invalid Status");
        var applicationWithInvalidFk = new LoanApplication(
                null, new BigDecimal("1000.00"), 12, "fk.test@example.com",
                invalidStatus, defaultLoanType
        );

        var resultMono = loanApplicationRepositoryAdapter.save(applicationWithInvalidFk);

        StepVerifier.create(resultMono)
                .expectError(DataIntegrityViolationException.class)
                .verify();
    }
}
