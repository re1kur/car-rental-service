package re1kur.app.repository.push;

import org.springframework.data.jpa.repository.JpaRepository;
import re1kur.app.entity.PushSubscription;

import java.util.Optional;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    Optional<PushSubscription> findByToken(String token);

    void deleteByToken(String token);
}
