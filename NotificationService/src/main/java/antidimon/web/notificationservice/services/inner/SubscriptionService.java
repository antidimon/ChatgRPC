package antidimon.web.notificationservice.services.inner;

import antidimon.web.notificationservice.models.Subscription;
import antidimon.web.notificationservice.models.SubscriptionType;
import antidimon.web.notificationservice.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;

    @Transactional
    protected void saveSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void subscribe(long userId, String type) {
        Subscription existingSubscription = subscriptionRepository.findSubscriptionByUserIdAndSubType(userId, SubscriptionType.valueOf(type));

        if (existingSubscription != null) {
            existingSubscription.setActive(true);
            this.saveSubscription(existingSubscription);
        } else {
            Subscription newSubscription = Subscription.builder()
                    .userId(userId)
                    .subType(SubscriptionType.valueOf(type))
                    .isActive(true)
                    .build();
            this.saveSubscription(newSubscription);
        }
    }

    @Transactional
    public void unSubscribe(long userId, String type) {
        Subscription subscription = subscriptionRepository.findSubscriptionByUserIdAndSubType(userId, SubscriptionType.valueOf(type));
        subscription.setActive(false);
        this.saveSubscription(subscription);
    }

    public Map<SubscriptionType, Boolean> getSubscriptionStatuses(long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByUserId(userId);
        var map = new HashMap<SubscriptionType, Boolean>();
        subscriptions.forEach(subscription -> map.put(subscription.getSubType(), subscription.isActive()));
        return map;
    }
}
