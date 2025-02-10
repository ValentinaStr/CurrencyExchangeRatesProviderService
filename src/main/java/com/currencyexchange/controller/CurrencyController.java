package com.currencyexchange.controller;

import com.currencyexchange.business.CurrencyService;
import com.currencyexchange.dto.CurrencyListDto;
import com.currencyexchange.model.CurrencyEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
@Tag(
    name = "Currency API",
    description = "Handles operations for currency names stored in the database.")
public class CurrencyController {

  private final CurrencyService currencyService;

  /**
   * Handles GET requests to retrieve a list of all available currencies. This method logs the
   * request and response, then returns the list of all currencies
   *
   * @return a {@link ResponseEntity} containing a list of currency names
   */
  @Operation(
      summary = "Get all available currencies",
      description =
          "Retrieves a list of all available currencies from the database. "
              + "This endpoint returns all the currencies that are stored in the system, "
              + "which can be used to fetch exchange rates",
      security = @SecurityRequirement(name = "basicAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful Operation",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            implementation = CurrencyListDto.class,
                            example = "{\"currencies\": [\"USD\", \"EUR\", \"JPY\"]}",
                            description = "List of currency codes available in the database"))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request - This response is not applicable for this endpoint.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Unauthorized\", "
                                    + "\"message\": \"Authentication required\"}",
                            description =
                                "Returned when the user is not authenticated "
                                    + "or credentials are invalid."))),
        @ApiResponse(
            responseCode = "404",
            description = "Not found - This response is not applicable for this endpoint.",
            content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error ",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Unauthorized\", "
                                    + "\"message\": \"Internal server error\"}",
                            description = "Error message when server encounters an issue")))
      })
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/")
  public CurrencyListDto getAllCurrencies() {
    log.info("Received request to get all currencies.");
    Set<String> currencies = currencyService.getAllCurrencies();
    log.info("Returning list of currencies: {}", currencies);
    return new CurrencyListDto(currencies);
  }

  /**
   * Handles POST requests to add a new currency to the system. Validates the provided currency code
   * and stores it in the database.
   *
   * @param currency The {@link CurrencyEntity} object containing the currency code to add.
   * @return A {@link ResponseEntity} with the result message and corresponding HTTP status.
   */
  @Operation(
      summary = "Add a new currency to the system",
      description =
          "Validates and adds a new currency to the system. "
              + "If the currency already exists, it will be skipped. "
              + "If validation fails, a bad request response is returned. "
              + "This operation is available only to users with the 'ADMIN' role.",
      security = @SecurityRequirement(name = "basicAuth"),
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Currency processed successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example = "{\"currency\": \"EUR\"}",
                            description =
                                "Returned when a currency is successfully processed and added."))),
        @ApiResponse(
            responseCode = "400",
            description = "Validation errors found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example = "{\"currency\": \"Currency must be 3 uppercase letters\"}",
                            description = "Error message when the currency validation fails"))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Unauthorized\","
                                    + "\"message\": \"Authentication required\"}",
                            description =
                                "Returned when the user is not authenticated"
                                    + " or credentials are invalid."))),
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found - The user does not have access.",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Resource not found\","
                                    + " \"message\": \"Resource not found\"}",
                            description = "Returned when access is denied."))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content =
                @Content(
                    mediaType = "text/plain",
                    schema =
                        @Schema(
                            type = "string",
                            example =
                                "{\"error\": \"Unauthorized\", "
                                    + "\"message\": \"Internal server error\"}",
                            description = "Error message when server encounters an issue")))
      })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/")
  public CurrencyEntity addCurrency(@Valid @RequestBody CurrencyEntity currency) {
    log.info("Received request to add currency: {}", currency.getCurrency());
    CurrencyEntity savedCurrency = currencyService.addCurrency(currency);
    log.info("Currency processed successfully: {}", currency.getCurrency());
    return savedCurrency;
  }
}
