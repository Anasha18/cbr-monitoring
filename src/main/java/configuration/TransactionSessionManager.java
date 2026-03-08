package configuration;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class TransactionSessionManager {
    private static final Logger log = LoggerFactory.getLogger(TransactionSessionManager.class);
    private final SessionFactory sessionFactory;

    public void inSession(Consumer<Session> func) {
        try (var session = sessionFactory.openSession()) {
            func.accept(session);
        } catch (Exception e) {
            log.error("Ошибка при работе с сессией БД: {}", e.getMessage());
        }
    }

    public void inTx(Consumer<Session> func) {
        try (var session = sessionFactory.openSession()) {
            session.inTransaction(tx -> func.accept(session));
        }
    }

    public <T> T inSession(Function<Session, T> func) {
        try (var session = sessionFactory.openSession()) {
            return func.apply(session);
        } catch (Exception e) {
            log.error("Ошибка при работе с сессией БД: {}", e.getMessage());
            throw new RuntimeException("Ошибка при работе с сессией БД: " + e.getMessage());
        }
    }
}
