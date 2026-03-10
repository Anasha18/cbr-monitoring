package bot.command;

import integration.CbrHttpClient;
import integration.dto.CurrencyResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurrencyCommand implements Command {
    private final CbrHttpClient httpClient;

    @Override
    public String execute(String... args) {
        String query = String.join(" ", args).trim();

        if (query.isEmpty()) {
            return """
                    Укажи название валюты:
                    /currency <код валюты>
                    """;
        }

        CurrencyResponse response = httpClient.getCurrencies();

        var currencies = response.Valute().values();

       var found = currencies.stream()
                .filter(c -> c.CharCode().equalsIgnoreCase(query)
                        || c.Name().toLowerCase().contains(query.toLowerCase()))
                .findFirst();


        return found.map(c -> String.format(
                "Курс %s (%s): %.4f руб.",
                c.CharCode(),
                c.Name(),
                c.Value()
        )).orElse("Валюта не найдена. Попробуй указать код (USD, EUR) или часть названия.");
    }


}
