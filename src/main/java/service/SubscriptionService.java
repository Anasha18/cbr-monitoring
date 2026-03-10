package service;

import configuration.TransactionSessionManager;
import domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repository.SubscriptionRepository;

@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository repo;

    public void save(Subscription subscription) {
        repo.save(subscription);
    }

    public Subscription findById(Long id) {
        return repo.findById(id).orElseThrow(() -> {
            log.atError().addKeyValue("id", id).log("Subscription with id not found");
            return new RuntimeException("Subscription with id " + id + " not found");
        });
    }

    public void updateStatusById(Long id, Boolean status) {
        var foundedSubscription = findById(id);

        if (foundedSubscription == null) {
            log.atError().addKeyValue("id", id).log("Subscription with id not found");
            throw new RuntimeException("Subscription with id " + id + " not found");
        }

        foundedSubscription.setIsActive(status);
    }
}
