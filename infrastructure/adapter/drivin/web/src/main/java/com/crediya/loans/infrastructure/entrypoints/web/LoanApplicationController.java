package com.crediya.loans.infrastructure.entrypoints.web;

import com.crediya.loans.application.exceptions.LoanTypeNotFoundException;
import com.crediya.loans.application.exceptions.StatusNotFoundException;
import com.crediya.loans.application.ports.in.CreateLoanApplicationPort;
import com.crediya.loans.infrastructure.entrypoints.web.dto.ErrorResponse;
import com.crediya.loans.infrastructure.entrypoints.web.dto.LoanApplicationRequest;
import com.crediya.loans.infrastructure.entrypoints.web.dto.LoanApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Loan Applications", description = "APIs for creating and managing loan applications")
public class LoanApplicationController {

    private final CreateLoanApplicationPort createLoanApplicationPort;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Submit a new loan application",
            description = "Creates a new loan application with the provided details.")
    @ApiResponse(responseCode = "201", description = "Application created successfully.", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<LoanApplicationResponse> createApplication(@Valid @RequestBody LoanApplicationRequest request) {
        log.info("Received loan application request for email: {}", request.getCustomerEmail());
        return Mono.just(request)
                .map(LoanApplicationRequest::toCommand)
                .flatMap(createLoanApplicationPort::createLoanApplication)
                .map(LoanApplicationResponse::fromDomain)
                .doOnSuccess(response -> log.info("Successfully processed application ID: {}", response.getApplicationId()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidationException(WebExchangeBindException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        log.warn("Validation failed for request: {}", errors);
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler({IllegalArgumentException.class, LoanTypeNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        log.warn("Bad request exception: {}", ex.getMessage());
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(StatusNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleConfigurationException(StatusNotFoundException ex) {
        log.error("Internal server configuration error: {}", ex.getMessage());
        return Mono.just(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An internal configuration error occurred. Please contact support."));
    }
}