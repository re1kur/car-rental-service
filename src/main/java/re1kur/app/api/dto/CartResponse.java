package re1kur.app.api.dto;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        int itemCount,
        int total
) {
}
