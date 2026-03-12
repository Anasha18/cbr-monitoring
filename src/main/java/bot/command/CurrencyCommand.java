package bot.command;

import integration.dto.CurrencyResponse;
import lombok.RequiredArgsConstructor;
import service.CurrencyService;

@RequiredArgsConstructor
public class CurrencyCommand implements Command {
    private final CurrencyService currencyService;

    @Override
    public String execute(Long telegramId, String... args) {
        String query = String.join(" ", args).trim();

        if (query.isEmpty()) {
            return """
                    Укажи название валюты:
                    /currency <код валюты>
                    """;
        }

        CurrencyResponse.CBRCurrencyData found = currencyService.getCurrencyByCodeFromApi(query);

        return String.format("Курс %s (%s): %.2f руб.",
                found.CharCode(),
                found.Name(),
                found.Value());
    }
}
