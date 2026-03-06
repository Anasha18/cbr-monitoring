package configuration;

public record TelegramConfig(
        String telegramBotToken,
        String telegramBotUsername
) {
}
