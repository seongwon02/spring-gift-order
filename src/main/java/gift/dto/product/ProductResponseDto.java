package gift.dto.product;

public class ProductResponseDto {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean approved;
    private String description;

    public ProductResponseDto(Long id,
                              String name,
                              Long price,
                              String imageUrl,
                              Boolean approved,
                              String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.approved = approved;
        this.description = description;
    }

    public Long getId() {
        return id;
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

    public Boolean getApproved() { return approved; }

    public String getDescription() { return description; }
}
