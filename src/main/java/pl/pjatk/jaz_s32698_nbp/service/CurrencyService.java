package pl.pjatk.jaz_s32698_nbp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pjatk.jaz_s32698_nbp.dto.NbpResponse;
import pl.pjatk.jaz_s32698_nbp.dto.Rate;
import pl.pjatk.jaz_s32698_nbp.model.NbpLogRecord;
import pl.pjatk.jaz_s32698_nbp.repository.NbpLogRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final NbpLogRepository repository;

    public CurrencyService(RestTemplate restTemplate, NbpLogRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public double calculateAverageRate(String currency, LocalDate startDate, LocalDate endDate) {
        // 1. Zbudowanie dokładnego URL do API NBP z przedziałem dat [cite: 3, 4]
        String url = "http://api.nbp.pl/api/exchangerates/rates/A/" + currency + "/" + startDate + "/" + endDate + "/?format=json";

        // 2. Wysłanie zapytania i zmapowanie JSONa na obiekty DTO
        NbpResponse response = restTemplate.getForObject(url, NbpResponse.class);

        // 3. Wyliczenie średniego kursu z pobranych dni [cite: 3]
        double sum = 0;
        for (Rate rate : response.getRates()) {
            sum += rate.getMid();
        }
        double average = sum / response.getRates().size();

        // 4. Stworzenie rekordu logu i zapis do bazy danych H2 
        NbpLogRecord log = new NbpLogRecord(currency, startDate, endDate, average, LocalDate.now(), LocalTime.now());
        repository.save(log);

        // 5. Zwrócenie wyniku do kontrolera [cite: 3]
        return average;
    }
}