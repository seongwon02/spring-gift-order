package gift.dto.wish;

import jakarta.validation.constraints.NotNull;

public class WishRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private final Long productId;

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
