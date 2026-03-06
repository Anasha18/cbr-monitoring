package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.AppConfig;
import domain.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public record ApiService(
        HttpClient httpClient,
        ObjectMapper objectMapper
) {

    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    public List<Currency> getCurrenciesApi(AppConfig config) {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(
                        URI.create(config.getCbrApiConfig().cbrApiUrl())
                )
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper
                        .readValue(
                                response.body(),
                                objectMapper.getTypeFactory().constructCollectionType(List.class, Currency.class)
                        );
            } else {
                log.error("Ошибка http: {}", response.statusCode());
                throw new RuntimeException("Не получилось получить данные о валютах...");
            }


        } catch (Exception e) {
            log.error("Ошибка при запросе к api: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
