package bot;

import configuration.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import service.UserService;

@RequiredArgsConstructor
@Slf4j
public class CbrMonitoringBot extends TelegramLongPollingBot {
    private final AppConfig config;
    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        Long telegramId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        User from = update.getMessage().getFrom();

        if (!update.hasMessage() || !update.getMessage().hasText() || update.getMessage().getFrom() == null) {
            return;
        }

        userService.getOrCreateUserByTelegramId(from.getUserName(), telegramId);
    }

    @Override
    public String getBotUsername() {
        return config.getTelegramConfig().telegramBotUsername();
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(buildMessage(chatId, text));
        } catch (Exception e) {
            log.error("Failed to send message. chatId={}", chatId, e);
        }

    }

    private SendMessage buildMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}
