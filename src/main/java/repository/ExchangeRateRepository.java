package repository;

import configuration.TransactionSessionManager;
import domain.Currency;
import domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ExchangeRateRepository {
    private final TransactionSessionManager manager;

    public Optional<ExchangeRate> getLatestByCurrencyId(Long currencyId) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("""
                            from ExchangeRate r
                            join fetch r.currency c
                            where c.id = :currencyId
                            order by r.rateDate desc
                            """, ExchangeRate.class)
                    .setParameter("currencyId", currencyId)
                    .uniqueResult();
        }));
    }

    public void save(ExchangeRate exchangeRate) {
        manager.inTx(session -> session.persist(exchangeRate));
    }

    public Optional<ExchangeRate> findByCurrencyId(Long currencyId) {
        return Optional.ofNullable(manager.inSession(session -> {
            return session.createQuery("""
                            from ExchangeRate r
                            join fetch r.currency c
                            where c.id = :currencyId
                            """, ExchangeRate.class)
                    .setParameter("currencyId", currencyId)
                    .uniqueResult();
        }));
    }
}
