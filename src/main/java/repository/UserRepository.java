package repository;

import configuration.TransactionSessionManager;
import domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserRepository {
    private final TransactionSessionManager manager;

    public void save(User user) {
        manager.inTx(session -> session.persist(user));
    }

    public Optional<User> findByTelegramId(Long telegramId) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("from User where telegramId = :telegramId", User.class)
                    .setParameter("telegramId", telegramId)
                    .uniqueResult();
        }));
    }
}
