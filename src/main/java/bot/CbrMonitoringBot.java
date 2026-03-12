package bot;

import bot.exception.CommandNotFoundException;
import configuration.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import service.UserService;

@Slf4j
@RequiredArgsConstructor
public class CbrMonitoringBot extends TelegramLongPollingBot {
    private final AppConfig config;
    private final UserService userService;
    private final CommandResolver commandResolver;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText() || update.getMessage().getFrom() == null) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        User from = update.getMessage().getFrom();

        userService.getOrCreateUserByTelegramId(from.getUserName(), chatId);

        try {
            CommandResolver.CommandResult result = commandResolver.resolve(text);
            String response = result.command().execute(chatId, result.args());

            sendMessage(chatId, response);
        } catch (CommandNotFoundException e) {
            sendMessage(chatId, e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process message. chatId={}", chatId, e);
            sendMessage(chatId, "Произошла ошибка при обработке команды.");
        }
    }

    @Override
    public String getBotUsername() {
        return config.getTelegramConfig().telegramBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getTelegramConfig().telegramBotToken();
    }

    public void sendMessage(
            Long chatId,
            String text
    ) {
        try {
            execute(buildMessage(chatId, text));
        } catch (Exception e) {
            log.error("Failed to send message. chatId={}", chatId, e);
        }

    }

    private SendMessage buildMessage(
            Long chatId,
            String text
    ) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}
