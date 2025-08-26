package com.crediya.loans.application.usecase;

import com.crediya.loans.application.exceptions.LoanTypeNotFoundException;
import com.crediya.loans.application.exceptions.StatusNotFoundException;
import com.crediya.loans.application.ports.in.CreateLoanApplicationCommand;
import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.domain.model.Status;
import com.crediya.loans.domain.ports.out.LoanApplicationRepositoryPort;
import com.crediya.loans.domain.ports.out.LoanTypeRepositoryPort;
import com.crediya.loans.domain.ports.out.StatusRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the CreateLoanApplicationUseCase.
 * @ExtendWith(MockitoExtension.class) enables Mockito annotations.
 */
@ExtendWith(MockitoExtension.class)
class CreateLoanApplicationUseCaseTest {

    @Mock
    private LoanTypeRepositoryPort loanTypeRepositoryPort;
    @Mock
    private LoanApplicationRepositoryPort loanApplicationRepositoryPort;
    @Mock
    private StatusRepositoryPort statusRepositoryPort;

    @InjectMocks
    private CreateLoanApplicationUseCase createLoanApplicationUseCase;

    @Test
    void shouldCreateLoanApplicationSuccessfullyWhenAllValidationsPass() {

        var command = new CreateLoanApplicationCommand(new BigDecimal("5000"), 12, "test@example.com", 1L);
        var loanType = new LoanType(1L, "Personal", new BigDecimal("1000"), new BigDecimal("10000"), new BigDecimal("0.1"), false);
        var initialStatus = new Status(1L, "PENDIENTE_REVISION", "Pending review");
        var expectedApplicationToSave = new LoanApplication(null, command.amount(), command.term(), command.customerEmail(), initialStatus.getId(), loanType.getId());
        var savedApplication = new LoanApplication(100L, command.amount(), command.term(), command.customerEmail(), initialStatus.getId(), loanType.getId());

        when(statusRepositoryPort.findByName("PENDIENTE_REVISION")).thenReturn(Mono.just(initialStatus));
        when(loanTypeRepositoryPort.findById(1L)).thenReturn(Mono.just(loanType));
        when(loanApplicationRepositoryPort.save(any(LoanApplication.class))).thenReturn(Mono.just(savedApplication));

        Mono<LoanApplication> resultMono = createLoanApplicationUseCase.createLoanApplication(command);

        StepVerifier.create(resultMono)
                .expectNext(savedApplication)
                .verifyComplete();

        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanApplicationRepositoryPort).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expectedApplicationToSave);
    }

    @Test
    void shouldReturnErrorWhenLoanTypeIsNotFound() {

        var command = new CreateLoanApplicationCommand(new BigDecimal("5000"), 12, "test@example.com", 99L);
        var initialStatus = new Status(1L, "PENDIENTE_REVISION", "Pending review");

        when(statusRepositoryPort.findByName("PENDIENTE_REVISION")).thenReturn(Mono.just(initialStatus));
        when(loanTypeRepositoryPort.findById(99L)).thenReturn(Mono.empty());

        Mono<LoanApplication> resultMono = createLoanApplicationUseCase.createLoanApplication(command);

        StepVerifier.create(resultMono)
                .expectError(LoanTypeNotFoundException.class)
                .verify();

        verify(loanApplicationRepositoryPort, never()).save(any());
    }

    @Test
    void shouldReturnErrorWhenInitialStatusIsNotFound() {
        var command = new CreateLoanApplicationCommand(new BigDecimal("5000"), 12, "test@example.com", 1L);
        var loanType = new LoanType(1L, "Personal", new BigDecimal("1000"), new BigDecimal("10000"), new BigDecimal("0.1"), false);

        when(statusRepositoryPort.findByName("PENDIENTE_REVISION")).thenReturn(Mono.empty());
        when(loanTypeRepositoryPort.findById(1L)).thenReturn(Mono.just(loanType));

        Mono<LoanApplication> resultMono = createLoanApplicationUseCase.createLoanApplication(command);

        StepVerifier.create(resultMono)
                .expectError(StatusNotFoundException.class)
                .verify();

        verify(loanApplicationRepositoryPort, never()).save(any());
    }

    @Test
    void shouldReturnErrorWhenAmountIsBelowMinimum() {
        var command = new CreateLoanApplicationCommand(new BigDecimal("500"), 12, "test@example.com", 1L);
        var loanType = new LoanType(1L, "Personal", new BigDecimal("1000"), new BigDecimal("10000"), new BigDecimal("0.1"), false);
        var initialStatus = new Status(1L, "PENDIENTE_REVISION", "Pending review");

        when(statusRepositoryPort.findByName("PENDIENTE_REVISION")).thenReturn(Mono.just(initialStatus));
        when(loanTypeRepositoryPort.findById(1L)).thenReturn(Mono.just(loanType));

        Mono<LoanApplication> resultMono = createLoanApplicationUseCase.createLoanApplication(command);

        StepVerifier.create(resultMono)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("not within the allowed range"))
                .verify();

        verify(loanApplicationRepositoryPort, never()).save(any());
    }
}
