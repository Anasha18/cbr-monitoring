package service;

import domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public BigDecimal getLatestByCurrencyId(Long currencyId) {
        return exchangeRateRepository.getLatestByCurrencyId(currencyId)
                .map(ExchangeRate::getValue)
                .orElseThrow(() -> {
                    log.atError().addKeyValue("currencyId", currencyId).log("getLatestByCurrencyId failed");
                    return new RuntimeException("getLatestByCurrencyId failed");
                });
    }

    public void save(ExchangeRate exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
    }

    public Optional<ExchangeRate> getExchangeRateByCurrencyId(Long currencyId) {
        return exchangeRateRepository.findByCurrencyId(currencyId);
    }
}
