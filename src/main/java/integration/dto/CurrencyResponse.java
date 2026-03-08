package integration.dto;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyResponse {
    public record CBRCurrencyData(
            String ID,
            String NumCode,
            String CharCode,
            Integer Nominal,
            String Name,
            BigDecimal Value,
            BigDecimal Previous
    ) {
    }

    public record CBRResponse(
            String Date,
            String PreviousDate,
            String PreviousURL,
            String Timestamp,
            Map<String, CBRCurrencyData> Valute
    ) {
    }
}
