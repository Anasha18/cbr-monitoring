package repository;

import configuration.TxManager;
import domain.Currency;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CurrencyRepository {
    public final TxManager txManager;

    public void save(Currency currency) {
        txManager.inTx(session -> session.persist(currency));
    }
}
