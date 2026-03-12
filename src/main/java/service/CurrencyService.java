package service;

import domain.Currency;
import domain.ExchangeRate;
import integration.CbrHttpClient;
import integration.dto.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.CurrencyRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateService exchangeRateService;
    private final CbrHttpClient httpClient;

    public void save(Currency currency) {
        currencyRepository.save(currency);
    }

    public CurrencyResponse.CBRCurrencyData getCurrencyByCodeFromApi(String code) {
        CurrencyResponse response = httpClient.getCurrencies();

        var currencies = response.Valute().values();

        return currencies.stream()
                .filter(c -> c.CharCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency code with: " + code + " not found"));
    }

    public Currency getOrCreateCurrencyByCode(String code) {
        Optional<Currency> foundedCurrency = currencyRepository.findByCode(code.toUpperCase());

        if (foundedCurrency.isPresent()) {
            return foundedCurrency.get();
        }

        CurrencyResponse.CBRCurrencyData currency = getCurrencyByCodeFromApi(code);
        Currency newCurrency = Currency.builder()
                .name(currency.Name())
                .code(currency.CharCode())
                .value(currency.Value())
                .build();
        save(newCurrency);

        Optional<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRateByCurrencyId(newCurrency.getId());
        if (exchangeRate.isPresent()) {
            var rate = exchangeRate.get();
            rate.setValue(currency.Value());
            exchangeRateService.save(rate);
        } else {
            ExchangeRate newRate = ExchangeRate.builder()
                    .currency(newCurrency)
                    .value(currency.Value())
                    .rateDate(LocalDateTime.now())
                    .build();
            exchangeRateService.save(newRate);
        }

        return newCurrency;
    }

    public Currency getCurrencyByCodeFromDb(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.atError().addKeyValue("code", code).log("no currency with code: " + code);
                    return new RuntimeException("Currency code with: " + code + " not found");
                });
    }


}
