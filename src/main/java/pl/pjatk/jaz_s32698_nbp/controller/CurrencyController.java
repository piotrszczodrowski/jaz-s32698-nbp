package pl.pjatk.jaz_s32698_nbp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.jaz_s32698_nbp.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/exchange")
@Tag(name = "Zaliczenie JAZ - Kalkulator NBP", description = "Endpointy zaliczeniowe do obliczania średnich kursów walut na podstawie wskazanego przedziału dat")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(
            summary = "Oblicz średni kurs waluty z przedziału dat",
            description = "Pobiera z API NBP kursy średnie (mid) dla wskazanej waluty między datą początkową a końcową, wylicza średnią arytmetyczną i zapisuje pełny log (w tym zakres dat i czas zapytania) do bazy danych H2."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pomyślnie obliczono średni kurs i zalogowano operację w bazie H2",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class, example = "4.0253"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Błędne żądanie - zły format daty (wymagany YYYY-MM-DD) lub przekroczenie limitu 367 dni w przedziale",
                    content = @Content(mediaType = "text/plain", schema = @Schema(example = "400: NBP odrzucił zapytanie (np. limit maksymalnie 367 dni w przedziale)."))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Brak notowań dla danej waluty w podanym przedziale (np. brak danych w bazie NBP lub przedział obejmuje wyłącznie weekendy)",
                    content = @Content(mediaType = "text/plain", schema = @Schema(example = "404: Brak danych (waluta nie istnieje lub brak kursów w podanym przedziale)."))
            )
    })
    @GetMapping("/{currency}")
    public ResponseEntity<BigDecimal> getAverageRate(
            @Parameter(
                    name = "currency",
                    description = "Trzyliterowy kod waluty zgodny ze standardem ISO 4217",
                    required = true,
                    example = "EUR"
            )
            @PathVariable String currency,

            @Parameter(
                    name = "startDate",
                    description = "Data początkowa przedziału wyszukiwania w formacie YYYY-MM-DD",
                    required = true,
                    example = "2026-06-01"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(
                    name = "endDate",
                    description = "Data końcowa przedziału wyszukiwania w formacie YYYY-MM-DD",
                    required = true,
                    example = "2026-06-15"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        BigDecimal avgRate = currencyService.calculateAverageRate(currency, startDate, endDate);
        return ResponseEntity.ok(avgRate);
    }
}