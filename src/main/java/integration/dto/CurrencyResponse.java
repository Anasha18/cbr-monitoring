package integration.dto;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyResponse(
        String Date,
        String PreviousDate,
        String PreviousURL,
        String Timestamp,
        Map<String, CBRCurrencyData> Valute
){
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
}

