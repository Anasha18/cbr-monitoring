package repository;

import configuration.TransactionSessionManager;
import domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SubscriptionRepository {
    public final TransactionSessionManager manager;

    public void save(Subscription subscription) {
        manager.inTx(session -> {
            session.persist(subscription);
        });
    }

    public Optional<Subscription> findById(Long id) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("""
                            from Subscription s
                            join fetch s.currency
                            join fetch s.user
                            where s.id = :id
                            """, Subscription.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }));
    }
}
