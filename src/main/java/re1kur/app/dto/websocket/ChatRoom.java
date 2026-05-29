package re1kur.app.dto.websocket;

import java.util.Arrays;
import java.util.Optional;

public enum ChatRoom {
    GENERAL("general"),
    SUPPORT("support"),
    CARS("cars"),
    RANDOM("random");

    private final String id;

    ChatRoom(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static boolean exists(String id) {
        return find(id).isPresent();
    }

    public static Optional<ChatRoom> find(String id) {
        return Arrays.stream(values()).filter(r -> r.id.equals(id)).findFirst();
    }
}
