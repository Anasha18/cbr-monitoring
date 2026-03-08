package repository;

import configuration.TransactionSessionManager;
import domain.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CurrencyRepository {
    public final TransactionSessionManager manager;

    public void save(Currency currency) {
        manager.inTx(session -> session.persist(currency));
    }
}
