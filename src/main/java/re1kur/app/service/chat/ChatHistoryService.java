package re1kur.app.service.chat;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import re1kur.app.entity.ChatMessage;
import re1kur.app.repository.chat.ChatMessageRepository;
import re1kur.app.dto.websocket.ChatPrincipal;
import re1kur.app.dto.websocket.ChatRoom;
import re1kur.app.dto.websocket.ChatMessageDto;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private static final int MAX_HISTORY = 50;

    private final ChatMessageRepository repository;
    private final Map<String, Deque<ChatMessageDto>> cache = new ConcurrentHashMap<>();

    @PostConstruct
    void warmUp() {
        for (ChatRoom room : ChatRoom.values()) {
            List<ChatMessage> latest = repository.findByRoomOrderByCreatedAtDesc(
                    room.id(), PageRequest.of(0, MAX_HISTORY));
            Deque<ChatMessageDto> deque = new ArrayDeque<>();
            for (int i = latest.size() - 1; i >= 0; i--) {
                deque.addLast(toDto(latest.get(i)));
            }
            cache.put(room.id(), deque);
        }
    }

    public ChatMessageDto save(String roomId, ChatPrincipal sender, String text) {
        ChatMessage saved = repository.save(ChatMessage.builder()
                .room(roomId)
                .senderId(sender.userId())
                .senderName(sender.displayName())
                .guest(sender.guest())
                .text(text)
                .createdAt(Instant.now())
                .build());

        ChatMessageDto dto = toDto(saved);
        Deque<ChatMessageDto> deque = cache.computeIfAbsent(roomId, key -> new ArrayDeque<>());
        synchronized (deque) {
            deque.addLast(dto);
            while (deque.size() > MAX_HISTORY) {
                deque.removeFirst();
            }
        }
        return dto;
    }

    public List<ChatMessageDto> recent(String roomId) {
        Deque<ChatMessageDto> deque = cache.get(roomId);
        if (deque == null) {
            return List.of();
        }
        synchronized (deque) {
            return new ArrayList<>(deque);
        }
    }

    private ChatMessageDto toDto(ChatMessage message) {
        return new ChatMessageDto(
                message.getId(),
                message.getRoom(),
                new ChatPrincipal(message.getSenderId(), message.getSenderName(), message.isGuest()),
                message.getText(),
                message.getCreatedAt().toString()
        );
    }
}
