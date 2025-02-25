package antidimon.web.messageservice.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_participants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatParticipant {

    @EmbeddedId
    private ChatParticipantPK id;

    @ManyToOne
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;
}
