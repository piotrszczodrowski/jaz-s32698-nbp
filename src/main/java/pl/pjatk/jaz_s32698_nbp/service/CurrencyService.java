package pl.pjatk.jaz_s32698_nbp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pjatk.jaz_s32698_nbp.dto.NbpResponse;
import pl.pjatk.jaz_s32698_nbp.dto.Rate;
import pl.pjatk.jaz_s32698_nbp.model.NbpLogRecord;
import pl.pjatk.jaz_s32698_nbp.repository.NbpLogRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final NbpLogRepository repository;

    public CurrencyService(RestTemplate restTemplate, NbpLogRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public BigDecimal calculateAverageRate(String currency, LocalDate startDate, LocalDate endDate) {
        String url = "http://api.nbp.pl/api/exchangerates/rates/A/{currency}/{startDate}/{endDate}/?format=json";

        NbpResponse response = restTemplate.getForObject(url, NbpResponse.class, currency, startDate, endDate);

        if (response == null || response.getRates() == null || response.getRates().isEmpty()) {
            throw new IllegalArgumentException("Brak kursów do obliczenia średniej");
        }

        BigDecimal sum = response.getRates().stream()
                .map(Rate::getMid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(new BigDecimal(response.getRates().size()), 4, RoundingMode.HALF_UP);

        LocalDateTime now = LocalDateTime.now();
        NbpLogRecord log = new NbpLogRecord(currency, startDate, endDate, average, now.toLocalDate(), now.toLocalTime());
        repository.save(log);

        return average;
    }
}