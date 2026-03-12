package repository;

import configuration.TransactionSessionManager;
import domain.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CurrencyRepository {
    public final TransactionSessionManager manager;

    public void save(Currency currency) {
        manager.inTx(session -> session.persist(currency));
    }

    public Optional<Currency> findByCode(String code) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("""
                            from Currency c
                            where c.code = :code
                            """, Currency.class)
                    .setParameter("code", code)
                    .uniqueResult();
        }));
    }

}
