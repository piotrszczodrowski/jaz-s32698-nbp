package pl.pjatk.jaz_s32698_nbp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class NbpLogRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal calculatedRate;

    private LocalDate queryDate;

    private LocalTime queryTime;

    public NbpLogRecord() {}

    public NbpLogRecord(String currency, LocalDate startDate, LocalDate endDate, BigDecimal calculatedRate, LocalDate queryDate, LocalTime queryTime) {
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.calculatedRate = calculatedRate;
        this.queryDate = queryDate;
        this.queryTime = queryTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getCalculatedRate() { return calculatedRate; }
    public void setCalculatedRate(BigDecimal calculatedRate) { this.calculatedRate = calculatedRate; }
    public LocalDate getQueryDate() { return queryDate; }
    public void setQueryDate(LocalDate queryDate) { this.queryDate = queryDate; }
    public LocalTime getQueryTime() { return queryTime; }
    public void setQueryTime(LocalTime queryTime) { this.queryTime = queryTime; }
}