package antidimon.web.notificationservice.repositories;

import antidimon.web.notificationservice.models.Subscription;
import antidimon.web.notificationservice.models.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findSubscriptionByUserIdAndSubType(long userId, SubscriptionType subType);

    List<Subscription> findSubscriptionsByUserId(long userId);
}
