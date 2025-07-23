package gift.dto.wish;

public class WishResponseDto {

    private final Long wishId;
    private final Long productId;
    private final String name;
    private final Long price;
    private final String imageUrl;

    public WishResponseDto(Long wishId, Long productId, String name, Long price, String imageUrl) {
        this.wishId = wishId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
