package antidimon.web.messageservice.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChatMessage> messages;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ChatParticipant> members;


    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", ownerId=" + ownerId +
                ", user1Id=" + user1Id +
                ", user2Id=" + user2Id +
                ", createdAt=" + createdAt +
                ", messages=" + messages.stream().map(ChatMessage::getId).toList() +
                ", members=" + members.stream().map(member -> member.getId().getUserId()).toList()
                + '}';
    }
}
