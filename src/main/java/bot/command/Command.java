package bot.command;

public interface Command {
    String execute(Long telegramId, String... args);
}
