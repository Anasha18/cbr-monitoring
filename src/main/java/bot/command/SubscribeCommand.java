package bot.command;

import lombok.RequiredArgsConstructor;
import service.SubscriptionService;

@RequiredArgsConstructor
public class SubscribeCommand implements Command {
    private final SubscriptionService subscriptionService;

    @Override
    public String execute(Long telegramId, String... args) {
        if (args.length == 0) {
            return "Укажите код валюты: /subscribe USD";
        }
        String currencyCode = args[0];
        return subscriptionService.subscribe(telegramId, currencyCode);
    }
}
