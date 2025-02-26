package antidimon.web.notificationservice.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "sub_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionType subType;

    @Column(name = "active")
    private boolean isActive;
}
