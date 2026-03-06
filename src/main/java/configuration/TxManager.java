package configuration;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@AllArgsConstructor
public class TxManager {
    private static final Logger log = LoggerFactory.getLogger(TxManager.class);
    private final AppConfig config;

    public void inTx(Consumer<Session> func) {
        try (var session = HibernateSessionFactoryProvider
                .build(config).openSession()
        ) {
            session.beginTransaction();
            try {
                func.accept(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                log.error("Error while trying to save transaction", e);
                throw new RuntimeException("Произошла ошибка при работе с транзакцией: ", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при работе с базой данных...", e);
        }
    }
}
