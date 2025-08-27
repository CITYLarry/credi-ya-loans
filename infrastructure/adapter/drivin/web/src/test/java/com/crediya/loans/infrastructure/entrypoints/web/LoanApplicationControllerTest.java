package com.crediya.loans.infrastructure.entrypoints.web;

import com.crediya.loans.application.exceptions.LoanTypeNotFoundException;
import com.crediya.loans.application.exceptions.StatusNotFoundException;
import com.crediya.loans.application.ports.in.CreateLoanApplicationPort;
import com.crediya.loans.domain.model.LoanApplication;
import com.crediya.loans.domain.model.LoanType;
import com.crediya.loans.domain.model.Status;
import com.crediya.loans.infrastructure.entrypoints.web.dto.LoanApplicationRequest;
import com.crediya.loans.infrastructure.entrypoints.web.dto.LoanApplicationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the LoanApplicationController.
 * @WebFluxTest loads only the web layer.
 */
@WebFluxTest(LoanApplicationController.class)
class LoanApplicationControllerTest {

    @SpringBootApplication
    @ComponentScan(basePackages = "com.crediya.loans.infrastructure.entrypoints.web")
    static class TestConfiguration {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreateLoanApplicationPort createLoanApplicationPort;

    @Test
    void shouldReturnCreatedWhenRequestIsValid() {

        var request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("5000"))
                .term(24)
                .customerEmail("larry.ramirez@outlook.com")
                .loanTypeId(1L)
                .build();

        var status = new Status(1L, "PENDIENTE_REVISION", "...");
        var loanType = new LoanType(1L, "Personal Express", BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, false);
        var domainResponse = new LoanApplication(101L, request.getAmount(), request.getTerm(), request.getCustomerEmail(), status, loanType);

        when(createLoanApplicationPort.createLoanApplication(any())).thenReturn(Mono.just(domainResponse));

        webTestClient.post().uri("/api/v1/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(LoanApplicationResponse.class)
                .value(response -> {
                    assertThat(response.getApplicationId()).isEqualTo(101L);
                    assertThat(response.getRequestedAmount()).isEqualTo(request.getAmount());
                    assertThat(response.getLoanTypeName()).isEqualTo("Personal Express");
                    assertThat(response.getStatus()).isEqualTo("PENDIENTE_REVISION");
                });
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() {

        var request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("-100"))
                .term(0)
                .customerEmail("not-an-email")
                .loanTypeId(null)
                .build();

        webTestClient.post().uri("/api/v1/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").isNotEmpty();
    }

    @Test
    void shouldReturnBadRequestWhenLoanTypeNotFound() {

        var request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("5000"))
                .term(24)
                .customerEmail("valid@email.com")
                .loanTypeId(99L)
                .build();

        when(createLoanApplicationPort.createLoanApplication(any()))
                .thenReturn(Mono.error(new LoanTypeNotFoundException("LoanType with ID 99 not found.")));

        webTestClient.post().uri("/api/v1/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("LoanType with ID 99 not found.");
    }

    @Test
    void shouldReturnInternalServerErrorWhenStatusIsNotFound() {

        var request = LoanApplicationRequest.builder()
                .amount(new BigDecimal("5000"))
                .term(24)
                .customerEmail("valid@email.com")
                .loanTypeId(1L)
                .build();

        when(createLoanApplicationPort.createLoanApplication(any()))
                .thenReturn(Mono.error(new StatusNotFoundException("Initial status not configured.")));


        webTestClient.post().uri("/api/v1/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(500)
                .jsonPath("$.message").isEqualTo("An internal configuration error occurred. Please contact support.");
    }
}
