package re1kur.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String room;

    private String senderId;

    private String senderName;

    private boolean guest;

    private String text;

    private Instant createdAt;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof ChatMessage message)) return false;
        if (id == null || message.id == null) return false;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : System.identityHashCode(this));
    }
}
