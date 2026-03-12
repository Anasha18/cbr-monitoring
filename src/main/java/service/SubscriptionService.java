package service;

import domain.Currency;
import domain.Subscription;
import domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final int NOTIFICATION_INTERVAL = 1;

    private final SubscriptionRepository subscriptionRepo;
    private final UserService userService;
    private final CurrencyService currencyService;

    public void save(Subscription subscription) {
        subscriptionRepo.save(subscription);
    }

    public String subscribe(Long telegramId, String currencyCode) {
        User foundedUser = userService.findByTelegramId(telegramId);

        Currency foundedCurrency = currencyService.getOrCreateCurrencyByCode(currencyCode);

        Optional<Subscription> foundedSubscription = subscriptionRepo.findByUserIdAndCurrencyId(foundedUser.getId(), foundedCurrency.getId());

        if (foundedSubscription.isPresent()) {
            var subscription = foundedSubscription.get();

            if (subscription.getIsActive()) {
                return "Вы уже подписаны на: " + currencyCode;
            } else {
                subscription.setIsActive(true);
                subscription.setLastNotifiedAt(null);
                save(subscription);

                return "Подписка на: " + currencyCode + " активна.";
            }
        } else {

            Subscription subscription = Subscription.builder()
                    .isActive(true)
                    .lastNotifiedAt(null)
                    .user(foundedUser)
                    .currency(foundedCurrency)
                    .build();

            save(subscription);
            return "Вы подписались на " + currencyCode + ". Уведомления будут приходить каждую минуту.";
        }
    }

    public List<Subscription> getAllSubscriptionsToNotify() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(NOTIFICATION_INTERVAL);
        return subscriptionRepo.findAllActiveNotifications(time);
    }

    public void updateLastNotifiedAt(Subscription subscription) {
        subscription.setLastNotifiedAt(LocalDateTime.now());
        save(subscription);
    }

    public String unsubscribe(Long telegramId, String currencyCode) {
        User foundedUser = userService.findByTelegramId(telegramId);

        Currency foundedCurrency = currencyService.getCurrencyByCodeFromDb(currencyCode);

        Optional<Subscription> foundedSubscription = subscriptionRepo.findByUserIdAndCurrencyId(foundedUser.getId(), foundedCurrency.getId());

        if (foundedSubscription.isEmpty()) {
            return "У вас нет подписки на валюту " + currencyCode;
        }

        Subscription subscription = foundedSubscription.get();
        if (!subscription.getIsActive()) {
            return "Подписка на " + currencyCode + " уже неактивна";
        }

        subscription.setIsActive(false);
        updateLastNotifiedAt(subscription);

        return "Подписка на " + currencyCode + " отключена.";

    }
}
