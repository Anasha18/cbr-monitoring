package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.AppConfig;
import integration.dto.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
public class CbrHttpClient {
    public final ObjectMapper objectMapper;
    private final AppConfig appConfig;

    public CurrencyResponse getCurrencies() {
        try (var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build()) {

            var httpRequest = HttpRequest.newBuilder()
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .uri(URI.create(appConfig.getCbrApiConfig().cbrApiUrl()))
                    .build();

            try {
                var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

                int statusCode = response.statusCode();

                if (statusCode < 200 || statusCode >= 300) {
                    log.warn("Запрос не выполнен статус код: {}, тело запроса: {}", statusCode, response.body());
                    return null;
                }

                return objectMapper.readValue(response.body(), CurrencyResponse.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
