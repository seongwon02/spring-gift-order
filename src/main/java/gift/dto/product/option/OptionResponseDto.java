package gift.dto.product.option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final Long quantity;

    public OptionResponseDto(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}
