package repository;

import configuration.TransactionSessionManager;
import domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SubscriptionRepository {
    public final TransactionSessionManager manager;

    public void save(Subscription subscription) {
        manager.inTx(session -> {
            session.merge(subscription);
        });
    }

    public Optional<Subscription> findByUserIdAndCurrencyId(Long userId, Long currencyId) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("""
                            from Subscription s
                            join fetch s.currency c
                            join fetch s.user u
                            where u.id = :userId and c.id = :currencyId
                            """, Subscription.class)
                    .setParameter("userId", userId)
                    .setParameter("currencyId", currencyId)
                    .uniqueResult();
        }));
    }

    public List<Subscription> findAllActiveNotifications(LocalDateTime time) {
        return manager.inSession(session -> {
            return session.createQuery("""
                                            from Subscription s
                                            join fetch s.currency
                                            join fetch s.user
                                            where s.isActive = true
                                            and (s.lastNotifiedAt is null or s.lastNotifiedAt < :time)
                            """, Subscription.class)
                    .setParameter("time", time)
                    .list();
        });
    }
}
