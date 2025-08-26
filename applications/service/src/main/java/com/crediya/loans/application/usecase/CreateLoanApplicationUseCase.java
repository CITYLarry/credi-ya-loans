package com.crediya.loans.application.usecase;

import com.crediya.loans.application.exceptions.LoanTypeNotFoundException;
import com.crediya.loans.application.exceptions.StatusNotFoundException;
import com.crediya.loans.application.ports.in.CreateLoanApplicationCommand;
import com.crediya.loans.application.ports.in.CreateLoanApplicationPort;
import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.domain.model.Status;
import com.crediya.loans.domain.ports.out.LoanApplicationRepositoryPort;
import com.crediya.loans.domain.ports.out.LoanTypeRepositoryPort;
import com.crediya.loans.domain.ports.out.StatusRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

/**
 * Implements the use case for creating a loan application.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateLoanApplicationUseCase implements CreateLoanApplicationPort {

    private final LoanTypeRepositoryPort loanTypeRepositoryPort;
    private final LoanApplicationRepositoryPort loanApplicationRepositoryPort;
    private final StatusRepositoryPort statusRepositoryPort;

    private static final String INITIAL_STATUS_NAME = "PENDIENTE_REVISION";

    @Override
    @Transactional
    public Mono<LoanApplication> createLoanApplication(CreateLoanApplicationCommand command) {
        log.info("Starting loan application process for email: {}", command.customerEmail());

        Mono<Status> statusMono = statusRepositoryPort.findByName(INITIAL_STATUS_NAME)
                .switchIfEmpty(Mono.error(new StatusNotFoundException("Initial status '" + INITIAL_STATUS_NAME + "' not configured in the system.")));

        Mono<LoanType> loanTypeMono = loanTypeRepositoryPort.findById(command.loanTypeId())
                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException("LoanType with ID " + command.loanTypeId() + " not found.")));

        return Mono.zip(statusMono, loanTypeMono)
                .flatMap(tuple -> validateAndCreate(command, tuple))
                .doOnSuccess(savedApplication -> log.info("Successfully created and saved loan application with ID: {}", savedApplication.getId()))
                .doOnError(error -> log.error("Error during loan application creation for email {}: {}", command.customerEmail(), error.getMessage()));
    }

    private Mono<LoanApplication> validateAndCreate(CreateLoanApplicationCommand command, Tuple2<Status, LoanType> tuple) {
        Status initialStatus = tuple.getT1();
        LoanType loanType = tuple.getT2();
        BigDecimal requestedAmount = command.amount();

        if (requestedAmount.compareTo(loanType.getMinAmount()) < 0 || requestedAmount.compareTo(loanType.getMaxAmount()) > 0) {
            String errorMessage = String.format("Requested amount %s is not within the allowed range [%s, %s] for the selected loan type.",
                    requestedAmount, loanType.getMinAmount(), loanType.getMaxAmount());
            log.warn(errorMessage);
            return Mono.error(new IllegalArgumentException(errorMessage));
        }

        log.trace("Validation successful. Creating application for email {}", command.customerEmail());
        LoanApplication newApplication = new LoanApplication(
                null,
                command.amount(),
                command.term(),
                command.customerEmail(),
                initialStatus.getId(),
                loanType.getId()
        );

        return loanApplicationRepositoryPort.save(newApplication);
    }
}
