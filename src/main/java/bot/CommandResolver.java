package bot;

import bot.command.*;
import bot.exception.CommandNotFoundException;
import service.CurrencyService;
import service.SubscriptionService;

import java.util.Map;

public class CommandResolver {
    private final Map<String, Command> commands;

    public CommandResolver(
            CurrencyService currencyService,
            SubscriptionService subscriptionService
    ) {
        this.commands = Map.of(
                "/start", new StarCommand(),
                "/currency", new CurrencyCommand(currencyService),
                "/subscribe", new SubscribeCommand(subscriptionService),
                "/unsubscribe", new UnsubscribeCommand(subscriptionService)
        );
    }

    public CommandResult resolve(String message) {
        String parsedMessage = message.trim();
        if (parsedMessage.isEmpty()) {
            throw new CommandNotFoundException();
        }

        String[] parts = parsedMessage.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

        Command command = commands.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException();
        }

        return new CommandResult(command, args);
    }

    public record CommandResult(Command command, String[] args) {
    }
}
