package re1kur.app.dto.response;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        int itemCount,
        int total
) {
}
