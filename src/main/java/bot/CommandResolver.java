package bot;

import bot.command.Command;
import bot.command.StarCommand;
import bot.command.SubscriptionCommand;
import bot.exception.CommandNotFoundException;
import bot.command.CurrencyCommand;
import integration.CbrHttpClient;

import java.util.Map;

public class CommandResolver {
    private final Map<String, Command> commands;

    public CommandResolver(CbrHttpClient httpClient) {
        this.commands = Map.of(
                "/start", new StarCommand(),
                "/currency", new CurrencyCommand(httpClient),
                "/subscription", new SubscriptionCommand()
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
